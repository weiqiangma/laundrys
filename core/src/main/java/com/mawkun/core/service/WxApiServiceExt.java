package com.mawkun.core.service;

import cn.pertech.common.http.HttpResult;
import cn.pertech.common.http.HttpUtils;
import cn.pertech.common.utils.RandomUtils;
import cn.pertech.common.utils.XmlUtils;
import com.alibaba.fastjson.JSONObject;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.WxLoginResultData;
import com.mawkun.core.base.entity.*;
import com.mawkun.core.base.service.CacheService;
import com.mawkun.core.base.service.UserAddressService;
import com.mawkun.core.utils.StringUtils;
import com.mawkun.core.utils.WechatDecryptDataUtil;
import com.xiaoleilu.hutool.crypto.SecureUtil;
import com.xiaoleilu.hutool.lang.Base64;
import com.xiaoleilu.hutool.lang.Validator;
import com.xiaoleilu.hutool.util.XmlUtil;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.JDKMessageDigest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.InetAddress;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.MessageDigest;
import java.security.Security;
import java.util.*;
import java.util.stream.Collectors;

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
    private String macId;
    @Value("${wx.macKey}")
    private String macKey;
    private final String trade_type = "JSAPI";

    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserAddressServiceExt userAddressServiceExt;
    @Autowired
    private OrderClothesServiceExt orderClothesServiceExt;

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
        JSONObject object = JSONObject.parseObject(result);
        result = object.getString("phoneNumber");
        return result;
    }

    /**
     * 统一下单接口(生成预支付ID)
     */
    public JSONObject unifyOrder(String openId, String orderNo, String totalFee, String body, String detail, String notifyUrl) {
        String msg = "";
        JSONObject object = new JSONObject();
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String param = createParam(openId, orderNo, "1", body, detail, notifyUrl);
        try {
            HttpResult result = HttpUtils.post(url, param, "UTF-8");
            String message = result.getHtml();
            Map<String, String> map = XmlUtils.xmlStr2Map(message);
            String resultCode = map.get("result_code");
            if(StringUtils.equals("SUCCESS", resultCode)) {
                String prepayId = map.get("prepay_id");
                String nonceStr = map.get("nonce_str");
                msg = createSecondParam(prepayId, nonceStr, "MD5");
                Map<String, String> resultMap = XmlUtils.xmlStr2Map(msg);
                for(Map.Entry<String, String> entry : resultMap.entrySet()) {
                    String key = entry.getKey();
                    if(StringUtils.equals("sign", key)) {
                        key = "paySign";
                    }
                    String value = entry.getValue();
                    object.put(key, value);
                }
            } else{
                throw new Exception("调用微信接口下单失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 查询订单状态
     * @param outTradeNo
     * @return
     */
    public JSONObject getOrderStatus(String outTradeNo) {
        JSONObject object = new JSONObject();
        String url = "https://api.mch.weixin.qq.com/pay/orderquery";
        String param= createQueryParam(outTradeNo);
        try {
            HttpResult result = HttpUtils.post(url, param, "UTF-8");
            String message = result.getHtml();
            Map<String, String> map = XmlUtils.xmlStr2Map(message);
            String resultCode = map.get("result_code");
            for(Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                object.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }



    public String createQueryParam(String outTradeNo) {
        String randomStr = StringUtils.createRandomStr(30);
        SortedMap<String, String> param = new TreeMap<>();
        param.put("appid", AppId);
        param.put("mch_id", macId);
        //生成32位一下的随机字符串
        param.put("nonce_str", randomStr);
        param.put("out_trade_no", outTradeNo);
        param.put("sign", createSign(param));
        return XmlUtils.map2Xml(param);
    }

    /**
     * 生成统一下单所需要的参数
     * @param openId
     * @param orderNo
     * @param totalFee
     * @param body
     * @param detail
     * @return
     */
    public String createParam(String openId, String orderNo, String totalFee, String body,  String detail, String notifyUrl) {
        String randomStr = StringUtils.createRandomStr(30);
        String spbillCreateIp = getHostIp();
        SortedMap<String, String> param = new TreeMap<>();
        param.put("appid", AppId);
        param.put("mch_id", macId);
        //生成32位一下的随机字符串
        param.put("nonce_str", randomStr);
        //商品名称
        param.put("body", body);
        //商品详情
        param.put("detail", detail);
        //订单序列号
        param.put("out_trade_no", orderNo);
        //总费用
        param.put("total_fee", totalFee);
        //回调地址
        param.put("notify_url", notifyUrl);
        //支付类型
        param.put("trade_type", trade_type);
        //用户openId
        param.put("openid", openId);
        param.put("spbill_create_ip", spbillCreateIp);
        param.put("sign", createSign(param));
        return XmlUtils.map2Xml(param);
    }


    /**
     * 生成二次签名所需参数供前端支付使用
     * @param prepayId
     * @param nonceStr
     * @param signType
     * @return
     */
    public String createSecondParam(String prepayId, String nonceStr, String signType) {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        SortedMap<String, String> param = new TreeMap<>();
        param.put("appId", AppId);
        param.put("nonceStr", nonceStr);
        param.put("package", "prepay_id=" + prepayId);
        param.put("signType", signType);
        param.put("timeStamp", timeStamp);
        param.put("sign", createSign(param));
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
                    sb.append("&key=" + macKey);
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

    public String getHostIp() {
        String ip = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 发送通知给用户
     *
     * @param accessToken 接口调用凭证
     * @param toUser    openId
     * @param data      五个参数
     * @param page      小程序模板跳转页面
     * @param miniprogramState 跳转小程序类型
     */
    public void sendMessage(String accessToken, String toUser, String templateId, JSONObject data, String page, String miniprogramState) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken;
        try {
            JSONObject object = new JSONObject(new LinkedHashMap<>());
            object.put("touser", toUser);
            object.put("template_id", templateId);
            object.put("data", data);
            String param = object.toJSONString();
            HttpResult result = HttpUtils.post(url, param, "UTF-8");
            JSONObject jsonObject = result.asJSON();
            if(jsonObject.containsKey("errcode")) {
                //accessToken超时或无效
                String errcode = jsonObject.getString("errcode");
                if(StringUtils.equals(errcode, "42001")) {
                    String newToken = getNewAccessToken();
                    sendMessage(newToken, toUser, templateId, data, page, miniprogramState);
                }
            }
            System.out.println(jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取小程序调用接口凭据
     * @return
     */
    public String getAccessToken() {
        String accessToken = cacheService.get("accessToken");
        if(StringUtils.isNotEmpty(accessToken)) {
            return accessToken;
        }
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + AppId + "&secret=" + AppSecret;
        try {
            HttpResult result = HttpUtils.get(url);
            JSONObject object = result.asJSON();
            accessToken = object.getString("access_token");
            int expiresIn = object.getIntValue("expires_in");
            cacheService.put("accessToken", accessToken, expiresIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public String getNewAccessToken() {
        String accessToken = "";
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + AppId + "&secret=" + AppSecret;
        try {
            HttpResult result = HttpUtils.get(url);
            JSONObject object = result.asJSON();
            accessToken = object.getString("access_token");
            int expiresIn = object.getIntValue("expires_in");
            cacheService.put("accessToken", accessToken, expiresIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }

    public void sendPaySuccessMsg(String openId, String templateId, JSONObject data) {
        String accessToken = getAccessToken();
        sendMessage(accessToken, openId, templateId, data, null, null);
    }

    /**
     * 用户充值成功发送的通知
     * @param user
     * @param investOrder
     * @param timeEnd
     */
    public void sendInvestSuccessNotice(User user, InvestOrder investOrder, String timeEnd) {
        JSONObject object = new JSONObject(new LinkedHashMap<>());
        JSONObject name1Object = new JSONObject();
        name1Object.put("value", user.getUserName());
        object.put("name1",name1Object);
        JSONObject time2Object = new JSONObject();
        time2Object.put("value", timeEnd);
        object.put("time2",time2Object);
        JSONObject amount3Object = new JSONObject();
        amount3Object.put("value", investOrder.getAmountMoney()/100);
        object.put("amount3", amount3Object);
        JSONObject amount4Object = new JSONObject();
        amount4Object.put("value", investOrder.getInvestMoney()/100);
        object.put("amount4", amount4Object);
        JSONObject amount5Object = new JSONObject();
        amount5Object.put("value", investOrder.getResidueMoney()/100);
        object.put("amount5", amount5Object);
        sendPaySuccessMsg(user.getOpenId(), Constant.INVEST_SUCCESS_NOTICE, object);
    }

    public void sendOrderPaySuccessNotice(User user, GoodsOrder goodsOrder, String timeEnd, String openId) {
        List<OrderClothes> orderClothes = orderClothesServiceExt.getByOrderId(goodsOrder.getId());
        List<String> clothesList = orderClothes.stream().map(OrderClothes::getGoodsName).collect(Collectors.toList());
        String goodsName = String.join(",", clothesList);

        JSONObject object = new JSONObject(new LinkedHashMap<>());
        JSONObject object1 = new JSONObject();
        object1.put("value", user.getMobile());
        object.put("phone_number13", object1);
        JSONObject object2 = new JSONObject();
        object2.put("value", goodsOrder.getOrderNo());
        object.put("character_string2", object2);
        JSONObject object3 = new JSONObject();
        object3.put("value", goodsOrder.getTotalAmount()/100);
        object.put("amount3", object3);
        JSONObject object4 = new JSONObject();
        object4.put("value", timeEnd);
        object.put("date4", object4);
        JSONObject object5 = new JSONObject();
        object5.put("value", goodsName);
        object.put("thing6", object5);
        sendPaySuccessMsg(openId, Constant.ORDER_PAY_SUCCESS_NOTICE, object);
    }

    public void sendDistributorOrderTakeNotice(User user, GoodsOrder goodsOrder, String timeEnd, List<String> list) {
        List<OrderClothes> orderClothes = orderClothesServiceExt.getByOrderId(goodsOrder.getId());
        List<String> clothesList = orderClothes.stream().map(OrderClothes::getGoodsName).collect(Collectors.toList());
        String goodsName = String.join(",", clothesList);

        UserAddress address = userAddressServiceExt.getById(goodsOrder.getAddressId());

        JSONObject object = new JSONObject(new LinkedHashMap<>());
        JSONObject object1 = new JSONObject();
        object1.put("value", goodsOrder.getOrderNo());
        object.put("character_string1", object1);
        JSONObject object2 = new JSONObject();
        object2.put("value", goodsName);
        object.put("date2", object2);
        JSONObject object3 = new JSONObject();
        object3.put("value", goodsOrder.getShopName());
        object.put("thing3", object3);
        JSONObject object4 = new JSONObject();
        object4.put("value", address.getDetail());
        object.put("thing7", object4);
        JSONObject object5 = new JSONObject();
        object5.put("value", address.getLinkMobile1());
        object.put("phone_number9", object5);
        for(String openId : list) {
            sendPaySuccessMsg(openId, Constant.DISTRIBUTOR_ORDER_TAKE, object);
        }
    }
}