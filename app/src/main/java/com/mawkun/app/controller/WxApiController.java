package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.entity.OrderForm;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.service.OrderFormServiceExt;
import com.mawkun.core.service.UserServiceExt;
import com.mawkun.core.service.WxApiServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date 2020/9/23 9:39
 * @Author mawkun
 */
@RestController
@RequestMapping("/api/wxApi")
@Api(tags={"微信api操作接口"})
public class WxApiController extends BaseController {
    @Autowired
    private WxApiServiceExt wxApiServiceExt;
    @Autowired
    private UserServiceExt userServiceExt;
    @Autowired
    private OrderFormServiceExt orderFormServiceExt;

    @RequestMapping("/wxPay")
    public JsonResult wxPay(@LoginedAuth UserSession session, Long orderId) {
        User user = userServiceExt.getByIdAndStatus(session.getId(), Constant.USER_STATUS_ACTIVE);
        if(user == null) return sendArgsError("未查询到该用户");
        OrderForm orderForm = orderFormServiceExt.getByUserIdAndId(orderId, user.getId());
        if(orderForm == null) return sendArgsError("未查询到该订单");
        //wxApiServiceExt.unifyOrder(user.getOpenId(), orderForm.getOrderSerial(), orderForm.getTotalAmount(), orderForm.get);
        return sendSuccess();
    }
}
