package com.mawkun.app;

import cn.pertech.common.http.HttpUtils;
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
        String openId = "owrdq5NOMPpRLdTI7MJ-ihHU0iVQ";
        String orderNo = "20150806120341";
        String totalFee = "1";
        String body = "短袖";
        String detail = "夏日清凉短袖";
        String notifyUrl = "http://www.baidu.com";
        String tradeType = "JSAPI";
        String ip = "125.111.199.42";

        String msg = wxApiServiceExt.unifyOrder(openId, orderNo, totalFee, body, detail, notifyUrl, tradeType, ip);
        System.out.println(msg);
    }

}
