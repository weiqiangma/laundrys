package com.mawkun.app;

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
    void createOrder() {
        for(int i = 0; i < 30; i++) {
            Random random = new Random();
            int result = random.nextInt(6);
            OrderForm orderForm = new OrderForm();
            orderForm.setTransportFee(Double.valueOf(5));
            orderForm.setUserName("沈文蓬");
            orderForm.setUserAddress("浙江省宁波市首南街道和顺家园");
            orderForm.setAddressId(Long.valueOf(1));
            orderForm.setCreateTime(new Date());
            orderForm.setTotalAmount(Double.valueOf(180));
            orderForm.setRealAmount(Double.valueOf(150));
            orderForm.setOrderSerial("4s5df4s5d4f" + i);
            orderForm.setPayKind(1);
            orderForm.setRemark("洗干净" + i);
            orderForm.setShopId(Long.valueOf(3));
            orderForm.setStatus(result);
            orderFormServiceExt.insert(orderForm);
        }
    }

    @Test
    void getMobile() {
        String encry = "sDXrUftQyBSyY7M4Ro4Xvjl8amI2agPjUWkcJXTJx6qsqaeetGdE7bp7P7V95aU7TS6O6l9cSznKhw5dhvkjSE+2vH19SntKxvfNtWUJigVKgzB28ACPjROC/4tidR4mMjJO81HX9pMCjusMVdYy0YzlYvvtCgTUNIOHNByMSB5fJ+vXIMnw==";
        String ivpa = "Yi1SGcsMg==";
        String sessionKey = "sDXrUftQyBSyY7M4Ro4Xvjl8amI2agPjUWkcJXTJx6qsqaeetGdE7bp7P7V95aU7TS6O6l9cSznKhw5dhvkjSE+2vH19SntKxvfNtWUJigVKgzB28ACPjROC/4tidR4mMjJO81HX9pMCjusMVdYy0YzlYvvtCgTUNIOHNByMSB5fJ+vXIMnw==";
        String mobile = wxApiServiceExt.getPhoneNumber(encry, sessionKey, ivpa);
        System.out.println(mobile);
    }

}
