package com.mawkun.core.service;

import cn.pertech.common.http.HttpResult;
import cn.pertech.common.http.HttpUtils;
import com.alibaba.fastjson.JSONObject;
import com.mawkun.core.base.data.WxLoginResultData;
import com.mawkun.core.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public WxLoginResultData getOpenIdByCode(String code) {
        WxLoginResultData loginResult = new WxLoginResultData();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + AppId + "&secret=" + AppSecret +"&js_code=" + code +"&grant_type=authorization_code";
        try {
            HttpResult result = HttpUtils.get(url);
            JSONObject object = result.asJSON();
            if(0 != object.getInteger("errCode")) log.info("查询异常");
            String openId = object.getString("openId");
            String sessionKey = object.getString("session_key");
            if(!StringUtils.isEmpty(openId)) loginResult.setOpenId(openId);
            if(!StringUtils.isEmpty(sessionKey)) loginResult.setSessionKey(sessionKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginResult;
    }

}
