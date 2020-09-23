package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.StringUtils;
import cn.pertech.common.utils.XmlUtils;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.WxLoginResultData;
import com.mawkun.core.base.entity.*;
import com.mawkun.core.service.*;
import com.mawkun.core.spring.annotation.LoginedAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Date 2020/9/23 9:39
 * @Author mawkun
 */
@RestController
@RequestMapping("/api/wxApi")
@Api(tags={"微信api操作接口"})
public class WxApiController extends BaseController {
    @Value("${wx.AppId}")
    private String AppId;
    @Value("${wx.macId}")
    private String macId;

    @Autowired
    private WxApiServiceExt wxApiServiceExt;
    @Autowired
    private UserServiceExt userServiceExt;
    @Autowired
    private InvestLogServiceExt investLogServiceExt;
    @Autowired
    private OrderFormServiceExt orderFormServiceExt;
    @Autowired
    private MemberCardServiceExt memberCardServiceExt;
    @Autowired
    private OrderClothesServiceExt orderClothesServiceExt;

    @PostMapping("/wxPay")
    @ApiOperation(value="小程序下单支付", notes="小程序下单支付")
    public JsonResult wxPay(@LoginedAuth UserSession session, Long orderId) {
        User user = userServiceExt.getByIdAndStatus(session.getId(), Constant.USER_STATUS_ACTIVE);
        if(user == null) return sendArgsError("未查询到该用户");
        OrderForm orderForm = orderFormServiceExt.getByUserIdAndId(orderId, user.getId());
        if(orderForm == null) return sendArgsError("未查询到该订单");
        List<OrderClothes> clothesList = orderClothesServiceExt.getByOrderId(orderId);
        String body = clothesList.stream().map(OrderClothes::getGoodsName).collect(Collectors.joining());
        String msg = wxApiServiceExt.unifyOrder(user.getOpenId(), orderForm.getOrderSerial(), orderForm.getTotalAmount().toString(), body, body);
        if(StringUtils.isNotEmpty(msg)) return sendSuccess("ok",msg);
        return sendArgsError("下单失败");
    }

    @PostMapping("/wxPayCallBack")
    @ApiOperation(value="小程序下单支付回调", notes="小程序下单支付回调")
    public JsonResult wxPayCallBack(HttpServletRequest request) {
        String result = "";
        Map<String, String> map = getResultFromWx(request);
        if (map.get("return_code").equals("SUCCESS") && map.get("appid").equals(AppId) && map.get("mch_id").equals(macId)) {
            //更新用户充值订单状态
            String orderNo = map.get("out_trade_no");
            InvestLog investLog = investLogServiceExt.getByOrderNo(orderNo);
            investLog.setStatus(Constant.ORDER_STATUS_SURE_TAKE);
            investLogServiceExt.update(investLog);
            //更新用户余额
            User user = userServiceExt.getById(investLog.getUserId());
            Long sumOfMoney = user.getSumOfMoney() +investLog.getAmountMoney();
            user.setSumOfMoney(sumOfMoney);
            userServiceExt.update(user, null);
        }
        return sendSuccess("ok", result);
    }

    @PostMapping("/getUserMobile")
    @ApiOperation(value="获取微信用户手机号", notes="获取微信用户手机号")
    public JsonResult getUserMobile(@LoginedAuth UserSession session, String encryptedData, String iv, String code) {
        User user = userServiceExt.getById(session.getId());
        if(user == null) return sendArgsError("未查询到该用户信息");
        WxLoginResultData data = wxApiServiceExt.getOpenIdByCode(code);
        session.setSessionKey(data.getSessionKey());
        String mobile = wxApiServiceExt.getPhoneNumber(encryptedData, session.getSessionKey(), iv);
        user.setMobile(mobile);
        userServiceExt.update(user, null);
        return sendSuccess("ok", mobile);
    }

    @PostMapping("/rechargetMoney")
    @ApiOperation(value = "小程序充值接口", notes = "小程序充值接口")
    public JsonResult rechargetMoney(@LoginedAuth UserSession session,Integer type, Long cardId, Long money) {
        String payParam="";
        if(type == null) return sendArgsError("请选择充值方式");
        User user = userServiceExt.getById(session.getId());
        if(user == null) return sendArgsError("未查询到用户信息,请联系客服人员处理");
        if(type == Constant.RECHARGE_WITH_CARD) {
            if(cardId == null) return sendArgsError("请选择充值卡券");
            MemberCard cart = memberCardServiceExt.findByIdAndStatus(cardId, Constant.MEMBER_CART_ON);
            if(cart == null) return sendArgsError("未查询到充值卡信息，请重新选择");
            payParam = userServiceExt.rechargeMoney(user, cart);
        }
        if(type == Constant.RECHARGE_WITH_MONEY) {
            payParam = userServiceExt.rechargeMoney(user, money);
        }
        return sendSuccess("支付参数", payParam);
    }

    @PostMapping("/rechargeCallBack")
    @ApiOperation(value = "小程序充值回调", notes = "小程序充值回调")
    public JsonResult rechargeCallBack(HttpServletRequest request, HttpServletResponse response) {
        String result = "";
        Map<String, String> map = getResultFromWx(request);
        if (map.get("return_code").equals("SUCCESS") && map.get("appid").equals(AppId) && map.get("mch_id").equals(macId)) {
            //更新用户充值订单状态
            String orderNo = map.get("out_trade_no");
            InvestLog investLog = investLogServiceExt.getByOrderNo(orderNo);
            investLog.setStatus(Constant.ORDER_STATUS_SURE_TAKE);
            investLogServiceExt.update(investLog);
            //更新用户余额
            User user = userServiceExt.getById(investLog.getUserId());
            Long sumOfMoney = user.getSumOfMoney() +investLog.getAmountMoney();
            user.setSumOfMoney(sumOfMoney);
            userServiceExt.update(user, null);
        }
        return sendSuccess("ok", result);
    }

    public Map<String, String> getResultFromWx(HttpServletRequest request) {
        String resXml = "";
        Map<String, String> map = new HashMap<>();
        try {
            InputStream is = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            logger.info("微信小程序支付回调报文：" + resXml);
            map = XmlUtils.xmlStr2Map(resXml);
            if (map.get("return_code").equals("SUCCESS") && map.get("appid").equals(AppId) && map.get("mch_id").equals(macId)) {
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
