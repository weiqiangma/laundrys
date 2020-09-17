package com.mawkun.app;

import com.mawkun.core.base.entity.OrderForm;
import com.mawkun.core.service.OrderFormServiceExt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.Random;

@SpringBootTest
class AppApplicationTests {

    @Autowired
    private OrderFormServiceExt orderFormServiceExt;

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

}
