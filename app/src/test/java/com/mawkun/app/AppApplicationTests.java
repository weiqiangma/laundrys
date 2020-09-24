package com.mawkun.app;

import cn.pertech.common.http.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mawkun.core.base.entity.OrderForm;
import com.mawkun.core.service.OrderFormServiceExt;
import com.mawkun.core.service.WxApiServiceExt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Random;

@SpringBootTest
class AppApplicationTests {

    @Autowired
    private OrderFormServiceExt orderFormServiceExt;
    @Autowired
    private WxApiServiceExt wxApiServiceExt;

    @Test
    void contextLoads() {
    }

    @Test
    void getMobile() {
        String encry = "sDXrUftQyBSyY7M4Ro4Xvjl8amI2agPjUWkcJXTJx6qsqaeetGdE7bp7P7V95aU7TS6O6l9cSznKhw5dhvkjSE+2vH19SntKxvfNtWUJigVKgzB28ACPjROC/4tidR4mMjJO81HX9pMCjusMVdYy0YzlYvvtCgTUNIOHNByMSB5fJ+vXIMnw==";
        String ivpa = "Yi1SGcsMg==";
        String sessionKey = "sDXrUftQyBSyY7M4Ro4Xvjl8amI2agPjUWkcJXTJx6qsqaeetGdE7bp7P7V95aU7TS6O6l9cSznKhw5dhvkjSE+2vH19SntKxvfNtWUJigVKgzB28ACPjROC/4tidR4mMjJO81HX9pMCjusMVdYy0YzlYvvtCgTUNIOHNByMSB5fJ+vXIMnw==";
        String mobile = wxApiServiceExt.getPhoneNumber(encry, sessionKey, ivpa);
        System.out.println(mobile);
    }

    @Test
    void createOrder() {
        String accessToken = wxApiServiceExt.getAccessToken();
        JSONObject object = new JSONObject();
        object.put("thing1","军大衣");
        object.put("character_string2","234826423672846");
        object.put("thing3","2020年4月4日 20:00");
        object.put("thing4","大双洗衣店");
        object.put("thing5","您的衣服已经清洗完成请及时领取");
        //wxApiServiceExt.sendMessageToUser(accessToken, "owrdq5Cdbl9sWklPO4tMwoRqkP68", object.toJSONString(), null, null);
    }

    @Test
    void getOrderStatus() {
        String outTradeNo="20200923114266";
        JSONObject object = wxApiServiceExt.getOrderStatus(outTradeNo);
        System.out.println(object);
    }

}
