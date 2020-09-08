package com.mawkun.core.service;

import cn.pertech.common.http.HttpUtils;
import org.springframework.stereotype.Service;

/**
 * @Date 2020/9/8 16:42
 * @Author mawkun
 */
@Service
public class WxApiServiceExt {
    private String appId;
    private String appSecret;

    public void getOpenIdByCode(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + appSecret +"&js_code=" + code +"&grant_type=authorization_code";
        try {
            HttpUtils.get(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
