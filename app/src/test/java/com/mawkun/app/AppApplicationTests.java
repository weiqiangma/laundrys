package com.mawkun.app;

import com.alibaba.fastjson.JSONObject;
import com.mawkun.core.base.entity.GoodsOrder;
import com.mawkun.core.service.GoodsOrderServiceExt;
import com.mawkun.core.service.WxApiServiceExt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class AppApplicationTests {

    @Autowired
    private GoodsOrderServiceExt orderFormServiceExt;
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
        List<Long> ids = new ArrayList<>();
        ids.add(111111111L);
        List<GoodsOrder> list = orderFormServiceExt.listByIds(ids);
        if(list != null) {
            System.out.println("hello");
        } else {
            System.out.println("world");
        }
    }

    @Test
    void getOrderStatus() {
        String outTradeNo="20200923114266";
        JSONObject object = wxApiServiceExt.getOrderStatus(outTradeNo);
        System.out.println(object);
    }

}
