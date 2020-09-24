package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.DateUtils;
import cn.pertech.common.utils.NumberUtils;
import cn.pertech.common.utils.StringUtils;
import cn.pertech.common.utils.XmlUtils;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Date 2020/9/23 9:39
 * @Author mawkun
 */
@Controller
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
    private InvestOrderServiceExt investOrderServiceExt;
    @Autowired
    private OrderFormServiceExt orderFormServiceExt;
    @Autowired
    private MemberCardServiceExt memberCardServiceExt;
    @Autowired
    private OrderClothesServiceExt orderClothesServiceExt;

    @ResponseBody
    @PostMapping("/api/wxApi/wxPay")
    @ApiOperation(value="小程序下单支付", notes="小程序下单支付")
    public JsonResult wxPay(@LoginedAuth UserSession session, Long orderId) {
        User user = userServiceExt.getByIdAndStatus(session.getId(), Constant.USER_STATUS_ACTIVE);
        if(user == null) {
            return sendArgsError("未查询到该用户");
        }
        OrderForm orderForm = orderFormServiceExt.getByUserIdAndId(orderId, user.getId());
        if(orderForm == null) {
            return sendArgsError("未查询到该订单");
        }
        if(orderForm.getStatus() != Constant.ORDER_STATUS_WAITING_PAY) {
            return sendArgsError("非待支付订单,无法支付");
        }
        List<OrderClothes> clothesList = orderClothesServiceExt.getByOrderId(orderId);
        String body = clothesList.stream().map(OrderClothes::getGoodsName).collect(Collectors.joining());
        JSONObject object = wxApiServiceExt.unifyOrder(user.getOpenId(), orderForm.getOrderSerial(), orderForm.getTotalAmount().toString(), body, body);
        return sendSuccess(object);
    }

    @PostMapping("/wxPayCallBack")
    @ApiOperation(value="小程序下单支付回调", notes="小程序下单支付回调")
    public String wxPayCallBack(HttpServletRequest request) {
        Map<String, String> map = getResultFromWx(request);
        String returnCode = map.get("return_code");
        String appId = map.get("appid");
        String mchid = map.get("mchid");
        if (StringUtils.equals(returnCode, Constant.WX_RETURN_SUCCESS) && StringUtils.equals(appId,AppId) && StringUtils.equals(mchid, macId)) {
            /**
             * 1.判断支付方式，如果是余额支付，减去用户对应余额并更新
             * 如果直接微信支付，直接更新用户状态
             */
            String orderNo = map.get("out_trade_no");
            String timeEnd = map.get("time_end");
            String totalFee = map.get("total_fee");
            Date payTime = new Date();
            try {
                payTime = DateUtils.parse("yyyy-MM-dd HH:mm:ss", timeEnd);
            } catch (Exception e) {
                e.printStackTrace();
            }
            OrderForm orderForm = orderFormServiceExt.getByOrderSerialAndStatus(orderNo, Constant.ORDER_STATUS_WAITING_PAY);
            //如果是余额支付,减去支付费用并更新
            if(orderForm.getPayKind() == Constant.PAY_WITH_REMAINDER) {
                User user = userServiceExt.getById(orderForm.getUserId());
                user.setSumOfMoney(user.getSumOfMoney() - NumberUtils.str2Long(totalFee));
                userServiceExt.update(user, null);
            }
            //更新订单
            orderForm.setStatus(Constant.ORDER_STATUS_WAITING_REAP);
            orderForm.setPayTime(payTime);
            orderForm.setUpdateTime(new Date());
            orderForm.setRealAmount(NumberUtils.str2Long(totalFee));
            orderFormServiceExt.update(null, orderForm);
            //发送通知
            String accessToken = wxApiServiceExt.getAccessToken();
            return "<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>";
        } else {
            return "<xml><return_code>FAIL</return_code><return_msg>接口请求错误/return_msg></xml>";
        }
    }

    /**
     * 检查付款成功的订单状态
     * @param session
     * @param orderId
     * @return
     */
    @RequestMapping("/checkOrderPayStatus")
    @ApiOperation(value="检查付款成功的订单状态", notes="检查付款成功的订单状态")
    public JsonResult checkOrderPayStatus(@LoginedAuth UserSession session, Long orderId) {
        /**
         * 1.检查当前登录用户是否为下单用户
         * 2.用户付款成功，成功进入回调订单状态应为待收货，若未正常进入
         *  回调函数，未能及时更新订单状态，此处要检查并及时更新订单状态并展示给用户付款成功的界面
         */
        User user = userServiceExt.getById(session.getId());
        if(user == null) {
            return sendArgsError("未查询到该用户");
        }
        OrderForm orderForm = orderFormServiceExt.getById(orderId);
        if(!orderForm.getUserId().equals(session.getId())) {
            return sendArgsError("非下单用户无权编辑");
        }
        JSONObject object = wxApiServiceExt.getOrderStatus(orderForm.getOrderSerial());
        String resultCode = object.getString("result_code");
        if(StringUtils.equals(Constant.WX_RETURN_SUCCESS, resultCode)) {
            String tradeState = object.getString("trade_state");
            String openId = object.getString("openid");
            String timeEnd = object.getString("time_end");
            String totalFee = object.getString("total_fee");
            Date payTime = new Date();
            try {
                payTime = DateUtils.parse("yyyy-MM-dd HH:mm:ss", timeEnd);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //用户已经支付成功，订单状态却未及时更新
            if(StringUtils.equals(Constant.PAY_STATU_SUCCESS, tradeState) && StringUtils.equals(user.getOpenId(), openId)) {
                //如果是余额支付,用户余额减去支付费用并更新
                if(orderForm.getPayKind() == Constant.PAY_WITH_REMAINDER) {
                    user.setSumOfMoney(user.getSumOfMoney() - NumberUtils.str2Long(totalFee));
                    userServiceExt.update(user, null);
                }
                //如果订单状态还是待支付
                if(orderForm.getStatus() == Constant.ORDER_STATUS_WAITING_PAY) {
                    //更新订单
                    orderForm.setStatus(Constant.ORDER_STATUS_WAITING_REAP);
                    orderForm.setPayTime(payTime);
                    orderForm.setUpdateTime(new Date());
                    orderForm.setRealAmount(NumberUtils.str2Long(totalFee));
                    orderFormServiceExt.update(null, orderForm);
                }
            }
        } else {
            return sendArgsError("调用微信接口查询订单异常");
        }
        OrderForm resultOrder = orderFormServiceExt.getById(orderId);
        return sendSuccess(resultOrder);
    }

    @ResponseBody
    @PostMapping("/api/wxApi/getUserMobile")
    @ApiOperation(value="获取微信用户手机号", notes="获取微信用户手机号")
    public JsonResult getUserMobile(@LoginedAuth UserSession session, String encryptedData, String iv, String code) {
        User user = userServiceExt.getById(session.getId());
        if(user == null) {
            return sendArgsError("未查询到该用户信息");
        }
        WxLoginResultData data = wxApiServiceExt.getOpenIdByCode(code);
        session.setSessionKey(data.getSessionKey());
        String mobile = wxApiServiceExt.getPhoneNumber(encryptedData, session.getSessionKey(), iv);
        user.setMobile(mobile);
        userServiceExt.update(user, null);
        return sendSuccess("ok", mobile);
    }

    @ResponseBody
    @PostMapping("/api/wxApi/rechargetMoney")
    @ApiOperation(value = "小程序充值接口", notes = "小程序充值接口")
    public JsonResult rechargetMoney(@LoginedAuth UserSession session,Integer type, Long cardId, Long money) {
        JSONObject object = new JSONObject();
        if(type == null) {
            return sendArgsError("请选择充值方式");
        }
        User user = userServiceExt.getById(session.getId());
        if(user == null) {
            return sendArgsError("未查询到用户信息,请联系客服人员处理");
        }
        if(type == Constant.RECHARGE_WITH_CARD) {
            if(cardId == null) {
                return sendArgsError("请选择充值卡券");
            }
            MemberCard cart = memberCardServiceExt.findByIdAndStatus(cardId, Constant.MEMBER_CART_ON);
            if(cart == null) {
                return sendArgsError("未查询到充值卡信息，请重新选择");
            }
            object = userServiceExt.rechargeMoney(user, cart);
        }
        if(type == Constant.RECHARGE_WITH_MONEY) {
            object = userServiceExt.rechargeMoney(user, money);
        }
        return sendSuccess(object);
    }

    @PostMapping("/rechargeCallBack")
    @ApiOperation(value = "小程序充值回调", notes = "小程序充值回调")
    public String rechargeCallBack(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = getResultFromWx(request);
        String returnCode = map.get("return_code");
        String appId = map.get("appid");
        String mchid = map.get("mchid");
        if (StringUtils.equals(returnCode, Constant.WX_RETURN_SUCCESS) && StringUtils.equals(appId, AppId) && StringUtils.equals(mchid, macId)) {
            //更新用户充值订单状态
            String orderNo = map.get("out_trade_no");
            InvestOrder investOrder = investOrderServiceExt.getByOrderNo(orderNo);
            investOrder.setStatus(Constant.ORDER_STATUS_SURE_TAKE);
            investOrderServiceExt.update(investOrder);
            //更新用户余额
            User user = userServiceExt.getById(investOrder.getUserId());
            Long sumOfMoney = user.getSumOfMoney() +investOrder.getAmountMoney();
            user.setSumOfMoney(sumOfMoney);
            userServiceExt.update(user, null);
            return "<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>";
        } else {
            return "<xml><return_code>FAIL</return_code><return_msg>接口请求错误/return_msg></xml>";
        }
    }

    @PostMapping("/api/wxApi/checkRechargeOrderStatus")
    @ApiOperation(value = "检查充值订单状态", notes = "检查充值订单状态")
    public JsonResult checkRechargeOrderStatus(@LoginedAuth UserSession session, String orderNo) {
        User user = userServiceExt.getById(session.getId());
        if(user == null) {
            return sendArgsError("用户不存在");
        }
        InvestOrder investOrder = investOrderServiceExt.getByOrderNoAndStatus(orderNo, Constant.ORDER_STATUS_WAITING_PAY);
        if(investOrder == null) {
            return sendArgsError("未查询到该充值订单");
        }
        if(user.getId().equals(investOrder.getUserId())) {
            return sendArgsError("非下单用户无权编辑");
        }
        JSONObject object = wxApiServiceExt.getOrderStatus(investOrder.getOrderNo());
        String resultCode = object.getString("result_code");
        if(StringUtils.equals(Constant.WX_RETURN_SUCCESS, resultCode)) {
            String tradeState = object.getString("trade_state");
            String openId = object.getString("openid");
            String timeEnd = object.getString("time_end");
            String totalFee = object.getString("total_fee");
            Date payTime = new Date();
            try {
                payTime = DateUtils.parse("yyyy-MM-dd HH:mm:ss", timeEnd);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //用户已经支付成功，订单状态却未及时更新
            if(StringUtils.equals(Constant.PAY_STATU_SUCCESS, tradeState) && StringUtils.equals(user.getOpenId(), openId)) {
                investOrder.setInvestMoney(NumberUtils.str2Long(totalFee));
                investOrder.setPayTime(payTime);
                investOrder.setUpdateTime(new Date());
                investOrder.setStatus(Constant.ORDER_STATUS_SURE_TAKE);
                investOrderServiceExt.update(investOrder);
            }
        } else {
            return sendArgsError("调用微信接口查询订单异常");
        }
        InvestOrder order = investOrderServiceExt.getByOrderNo(orderNo);
        return sendSuccess(order);
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
