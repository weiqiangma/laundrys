package com.mawkun.core.service;

import cn.pertech.common.http.HttpResult;
import cn.pertech.common.http.HttpUtils;
import cn.pertech.common.utils.RandomUtils;
import cn.pertech.common.utils.XmlUtils;
import com.alibaba.fastjson.JSONObject;
import com.mawkun.core.base.data.WxLoginResultData;
import com.mawkun.core.utils.StringUtils;
import com.xiaoleilu.hutool.lang.Base64;
import com.xiaoleilu.hutool.lang.Validator;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
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
    private String machId;
    private String key;

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
     * @param code
     * @param iv
     * @return
     */
    public String getPhoneNumber(String encryptedData, String code, String iv) {
        //传入code后然后获取openid和session_key的，把他们封装到json里面
        WxLoginResultData resultData = getOpenIdByCode(code);
        String result = "";
        String session_key = "";
        if (resultData != null) {
            session_key = resultData.getSessionKey();
            // 被加密的数据
            byte[] dataByte = com.xiaoleilu.hutool.lang.Base64.decode(encryptedData);
            // 加密秘钥
            byte[] keyByte = com.xiaoleilu.hutool.lang.Base64.decode(session_key);
            // 偏移量
            byte[] ivByte = Base64.decode(iv);
            try {
                // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
                int base = 16;
                if (keyByte.length % base != 0) {
                    int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                    byte[] temp = new byte[groups * base];
                    Arrays.fill(temp, (byte) 0);
                    System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                    keyByte = temp;
                }
                // 初始化
                Security.addProvider(new BouncyCastleProvider());
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
                AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
                parameters.init(new IvParameterSpec(ivByte));
                cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
                byte[] resultByte = cipher.doFinal(dataByte);
                if (null != resultByte && resultByte.length > 0) {
                    result = new String(resultByte, "UTF-8");
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 统一下单接口
     */
    public void unifyOrder(String openId, String orderNo, String totalFee, String body, String detail, String notifyUrl, String tradeType) {
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String param = createParam(openId, orderNo, totalFee, body, detail, notifyUrl, tradeType);
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
    public String createParam(String openId, String orderNo, String totalFee, String body, String detail, String notifyUrl, String tradeType) {
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
        param.put("trade_type", tradeType);         //支付类型
        param.put("openid", openId);                //用户openId
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
        for (Map.Entry<String, String> e : es) {
            String k = e.getKey();
            String v = e.getValue();
            if(StringUtils.isNotEmpty(v) && !StringUtils.equals("sign", k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append(key);
        return sb.toString();
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