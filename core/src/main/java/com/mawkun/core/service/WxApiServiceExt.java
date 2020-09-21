package com.mawkun.core.service;

import cn.pertech.common.http.HttpResult;
import cn.pertech.common.http.HttpUtils;
import cn.pertech.common.utils.RandomUtils;
import cn.pertech.common.utils.XmlUtils;
import com.alibaba.fastjson.JSONObject;
import com.mawkun.core.base.data.WxLoginResultData;
import com.mawkun.core.utils.StringUtils;
import com.mawkun.core.utils.WechatDecryptDataUtil;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.lang.Base64;
import com.xiaoleilu.hutool.lang.Validator;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.JDKMessageDigest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.MessageDigest;
import java.security.Security;
import java.util.*;

/**
 * @Date 2020/9/8 16:42
 * @Author mawkun
 */
@Slf4j
@Service
public class WxApiServiceExt {
    @Value("${wx.AppId}")
    private String AppId;
    @Value("${wx.AppSecret}")
    private String AppSecret;
    @Value("${wx.macId}")
    private String machId;

    private static final String KEY_ALGORITHM = "AES";
    private static final String ALGORITHM_STR = "AES/CBC/PKCS7Padding";
    private static Key key;
    private static Cipher cipher;

    /**
     * 根据code获取用户openId
     *
     * @param code
     * @return
     */
    public WxLoginResultData getOpenIdByCode(String code) {
        WxLoginResultData loginResult = new WxLoginResultData();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + AppId + "&secret=" + AppSecret + "&js_code=" + code + "&grant_type=authorization_code";
        try {
            HttpResult result = HttpUtils.get(url);
            JSONObject object = result.asJSON();
            String openId = object.getString("openid");
            String sessionKey = object.getString("session_key");
            Validator.isNotEmpty(openId);
            Validator.isNotEmpty(sessionKey);
            loginResult.setOpenId(openId);
            loginResult.setSessionKey(sessionKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginResult;
    }

    /**
     * 解密用户手机号
     * @param encryptedData
     * @param sessionKey
     * @param iv
     * @return
     */
    public String getPhoneNumber(String encryptedData, String sessionKey, String iv) {
        String result = "";
        result = WechatDecryptDataUtil.decrypt(AppId,encryptedData,sessionKey,iv);
        return result;
    }

    /**
     * 统一下单接口
     */
    public void unifyOrder(String openId, String orderNo, String totalFee, String body, String detail, String notifyUrl, String tradeType, String spbillCreateIp) {
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String param = createParam(openId, orderNo, totalFee, body, detail, notifyUrl, tradeType, spbillCreateIp);
        try {
            HttpResult result = HttpUtils.post(url, param, "UTF-8");
            String message = result.getMessage();
            System.out.println(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成统一下单所需要的参数
     * @param openId
     * @param orderNo
     * @param totalFee
     * @param body
     * @param detail
     * @param notifyUrl
     * @param tradeType
     * @return
     */
    public String createParam(String openId, String orderNo, String totalFee, String body, String detail, String notifyUrl, String tradeType, String spbillCreateIp) {
        String randomStr = StringUtils.createRandomStr(30);
        SortedMap<String, String> param = new TreeMap<>();
        param.put("appid", AppId);
        param.put("mch_id", machId);
        param.put("nonce_str", randomStr);          //生成32位一下的随机字符串
        param.put("body", body);                    //商品名称
        param.put("detail", detail);                //商品详情
        param.put("out_trade_no", orderNo);         //订单序列号
        param.put("total_fee", totalFee);           //总费用
        param.put("notify_url", notifyUrl);         //回调地址
        param.put("trade_type", "JSAPI");         //支付类型
        param.put("openid", openId);                //用户openId
        param.put("spbill_create_ip", spbillCreateIp);
        param.put("sign", createSign(param));       //签名
        return XmlUtils.map2Xml(param);
    }

    /**
     * 生成签名
     * @param parameters
     * @return
     */
    public String createSign(SortedMap<String, String> parameters) {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, String>> es = parameters.entrySet();
        Iterator<Map.Entry<String, String>> iterator = es.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String k = entry.getKey();
            String v = entry.getValue();
            if(cn.pertech.common.utils.StringUtils.isNotEmpty(v) && !cn.pertech.common.utils.StringUtils.equals("sign", k)) {
                sb.append(k + "=" + v);
                if(iterator.hasNext()) {
                    sb.append("&");
                } else {
                    sb.append("&key=" + machId);
                }
            }
        }
        String sign = sb.toString();
        try {
            sign = SecureUtil.md5(sign).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, String> paraFilter(Map<String, String> sArray) {

        Map<String, String> result = new HashMap<String, String>();

        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }

        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }
}