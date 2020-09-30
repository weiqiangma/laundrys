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
import com.mawkun.core.utils.TimeUtils;
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
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
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
    @Value("${wx.pay.goods.notifyUrl}")
    private String goodsNotifyUrl;
    @Value("${wx.pay.invest.notifyUrl}")
    private String investNotifyUrl;

    @Autowired
    private WxApiServiceExt wxApiServiceExt;
    @Autowired
    private UserServiceExt userServiceExt;
    @Autowired
    private PayFlowServiceExt payFlowServiceExt;
    @Autowired
    private InvestOrderServiceExt investOrderServiceExt;
    @Autowired
    private GoodsOrderServiceExt goodsOrderServiceExt;
    @Autowired
    private MemberCardServiceExt memberCardServiceExt;
    @Autowired
    private OrderClothesServiceExt orderClothesServiceExt;

    @ResponseBody
    @PostMapping("/api/wxApi/wxPay")
    @ApiOperation(value="小程序下单支付", notes="小程序下单支付")
    public JsonResult wxPay(@LoginedAuth UserSession session, Long orderId, String orderNo) {
        User user = userServiceExt.getByIdAndStatus(session.getId(), Constant.USER_STATUS_ACTIVE);
        if(user == null) {
            return sendArgsError("未查询到该用户");
        }
        GoodsOrder orderForm = goodsOrderServiceExt.getByUserIdAndId(orderId, user.getId());
        if(orderForm == null) {
            return sendArgsError("未查询到该订单");
        }
        if(orderForm.getStatus() != Constant.ORDER_STATUS_WAITING_PAY) {
            return sendArgsError("非待支付订单,无法支付");
        }
        List<OrderClothes> clothesList = orderClothesServiceExt.getByOrderId(orderId);
        String body = clothesList.stream().map(OrderClothes::getGoodsName).collect(Collectors.joining());
        JSONObject object = wxApiServiceExt.unifyOrder(user.getOpenId(), orderForm.getOrderNo(), orderForm.getTotalAmount().toString(), body, body, goodsNotifyUrl);
        return sendSuccess(object);
    }


    @PostMapping("/wxPayCallBack")
    @ApiOperation(value = "小程序下单支付回调", notes = "小程序下单支付回调")
    public void wxPayCallBack(HttpServletRequest request, HttpServletResponse response) {
        String result = "<xml><return_code>FAIL</return_code><return_msg>接口请求错误/return_msg></xml>";
        Map<String, String> map = getResultFromWx(request);
        String returnCode = map.get("return_code");
        String appId = map.get("appid");
        String mchid = map.get("mch_id");
        try {
            OutputStream outputStream = response.getOutputStream();
            if (StringUtils.equals(returnCode, Constant.WX_RETURN_SUCCESS) && StringUtils.equals(appId, AppId) && StringUtils.equals(mchid, macId)) {
                /**
                 * 1.判断支付方式，如果是余额支付，减去用户对应余额并更新
                 * 如果直接微信支付，直接更新用户状态
                 */
                String orderNo = map.get("out_trade_no");
                String timeEnd = map.get("time_end");
                String totalFee = map.get("total_fee");
                Date payTime = TimeUtils.convertWeiXinTime(timeEnd);
                GoodsOrder orderForm = goodsOrderServiceExt.getByOrderNo(orderNo);
                orderForm.setIsnew(Constant.ORDER_NEW);
                goodsOrderServiceExt.update(orderForm);
                //如果订单状态还是待支付，更新订单状态
                if (orderForm.getStatus() == Constant.ORDER_STATUS_WAITING_PAY) {
                    User user = userServiceExt.getById(orderForm.getUserId());
                    //如果是余额支付,减去支付费用并更新
                    if (orderForm.getPayKind() == Constant.PAY_WITH_REMAINDER) {
                        user.setSumOfMoney(user.getSumOfMoney() - NumberUtils.str2Long(totalFee));
                        userServiceExt.update(user, null);
                    }
                    //判断订单配送方式
                    //如果配送员配送下一状态 待收货
                    if(orderForm.getTransportWay() == Constant.ORDER_DELIVERY_GET) {
                        orderForm.setStatus(Constant.DELIVERY_ORDER_WAITING_REAP);
                    }
                    //如果客户送至门店下一状态 待送达门店
                    if(orderForm.getTransportWay() == Constant.ORDER_DELIVERY_SEND) {
                        orderForm.setStatus(Constant.SELF_ORDER_WAITING_SEND);
                    }
                    //更新订单
                    orderForm.setPayTime(payTime);
                    orderForm.setUpdateTime(new Date());
                    orderForm.setRealAmount(NumberUtils.str2Long(totalFee));
                    UserSession session = new UserSession(user);
                    goodsOrderServiceExt.update(session, orderForm);
                    //生成支付流水
                    payFlowServiceExt.createPayFlow(user, orderForm, Constant.ORDER_TYPE_GOODS);
                    //发送通知
                    String accessToken = wxApiServiceExt.getAccessToken();
                }
                result = "<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>";
            } else {
                result = "<xml><return_code>FAIL</return_code><return_msg>接口请求错误/return_msg></xml>";
            }
            outputStream.write(result.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查付款成功的订单状态
     * @param session
     * @param orderId
     * @return
     */
    @ResponseBody
    @RequestMapping("/api/wxApi/checkOrderPayStatus")
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
        GoodsOrder orderForm = goodsOrderServiceExt.getById(orderId);
        if(!orderForm.getUserId().equals(session.getId())) {
            return sendArgsError("非下单用户无权编辑");
        }
        orderForm.setIsnew(Constant.ORDER_NEW);
        goodsOrderServiceExt.update(orderForm);
        JSONObject object = wxApiServiceExt.getOrderStatus(orderForm.getOrderNo());
        String resultCode = object.getString("result_code");
        if(StringUtils.equals(Constant.WX_RETURN_SUCCESS, resultCode)) {
            String tradeState = object.getString("trade_state");
            String openId = object.getString("openid");
            String timeEnd = object.getString("time_end");
            String totalFee = object.getString("total_fee");
            Date payTime = new Date();
            try {
                payTime = TimeUtils.convertWeiXinTime(timeEnd);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //微信返回订单支付成功
            if(StringUtils.equals(Constant.PAY_STATU_SUCCESS, tradeState) && StringUtils.equals(user.getOpenId(), openId)) {
                //数据库订单状态还是待支付
                if(orderForm.getStatus() == Constant.ORDER_STATUS_WAITING_PAY) {
                    //如果是余额支付,用户余额减去支付费用并更新
                    if(orderForm.getPayKind() == Constant.PAY_WITH_REMAINDER) {
                        user.setSumOfMoney(user.getSumOfMoney() - NumberUtils.str2Long(totalFee));
                        userServiceExt.update(user, null);
                    }
                    //判断订单配送方式
                    //如果配送员配送下一状态 待收货
                    if(orderForm.getTransportWay() == Constant.ORDER_DELIVERY_GET) {
                        orderForm.setStatus(Constant.DELIVERY_ORDER_WAITING_REAP);
                    }
                    //如果客户送至门店下一状态 待送达门店
                    if(orderForm.getTransportWay() == Constant.ORDER_DELIVERY_SEND) {
                        orderForm.setStatus(Constant.SELF_ORDER_WAITING_SEND);
                    }
                    //更新订单
                    orderForm.setPayTime(payTime);
                    orderForm.setUpdateTime(new Date());
                    orderForm.setRealAmount(NumberUtils.str2Long(totalFee));
                    goodsOrderServiceExt.update(null, orderForm);
                    //生成支付流水
                    payFlowServiceExt.createPayFlow(user, orderForm, Constant.ORDER_TYPE_GOODS);
                    //发送通知

                }
            }
        } else {
            return sendArgsError("调用微信接口查询订单异常");
        }
        GoodsOrder resultOrder = goodsOrderServiceExt.getById(orderId);
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
    @PostMapping("/api/wxApi/rechargeMoney")
    @ApiOperation(value = "小程序充值接口", notes = "小程序充值接口")
    public JsonResult rechargeMoney(@LoginedAuth UserSession session,Integer type, Long cardId, Long money) {
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
            object = userServiceExt.rechargeMoney(user, cart, investNotifyUrl);
        }
        if(type == Constant.RECHARGE_WITH_MONEY) {
            object = userServiceExt.rechargeMoney(user, money, investNotifyUrl);
        }
        return sendSuccess(object);
    }

    @PostMapping("/rechargeCallBack")
    @ApiOperation(value = "小程序充值回调", notes = "小程序充值回调")
    public void rechargeCallBack(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = getResultFromWx(request);
        String result = "<xml><return_code>FAIL</return_code><return_msg>接口请求错误/return_msg></xml>";
        String returnCode = map.get("return_code");
        String appId = map.get("appid");
        String mchid = map.get("mch_id");
        String timeEnd = map.get("time_end");
        String openId = map.get("openid");
        try {
            OutputStream outputStream = response.getOutputStream();
            Date payTime = TimeUtils.convertWeiXinTime(timeEnd);
            if (StringUtils.equals(returnCode, Constant.WX_RETURN_SUCCESS) && StringUtils.equals(appId, AppId) && StringUtils.equals(mchid, macId)) {
                String orderNo = map.get("out_trade_no");
                InvestOrder order = investOrderServiceExt.getByOrderNo(orderNo);
                //如果订单还是待支付状态就更新订单状态
                if(order.getStatus() == Constant.INVEST_ORDER_WAITING_WAY) {
                    //更新用户充值订单状态
                    InvestOrder investOrder = investOrderServiceExt.getByOrderNo(orderNo);
                    investOrder.setStatus(Constant.INVEST_ORDER_FINISH);
                    investOrder.setPayTime(payTime);
                    investOrderServiceExt.update(investOrder);
                    //更新用户余额
                    User user = userServiceExt.getById(investOrder.getUserId());
                    Long sumOfMoney = user.getSumOfMoney() + investOrder.getAmountMoney();
                    user.setSumOfMoney(sumOfMoney);
                    userServiceExt.update(user, null);
                    //生成支付流水
                    payFlowServiceExt.createPayFlow(user, investOrder, Constant.ORDER_TYPE_INVEST);
                    //发送通知
                    JSONObject object = new JSONObject();
                    object.put("name1",user.getUserName());
                    object.put("time2",payTime);
                    object.put("amount3", investOrder.getAmountMoney());
                    object.put("amount4", investOrder.getInvestMoney());
                    object.put("amount5", investOrder.getResiduemoney());
                    wxApiServiceExt.sendPaySuccessMsg(user.getOpenId(), object.toJSONString());
                    result = "<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>";
                } else {
                    result = "<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>";
                }
            }
            outputStream.write(result.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ResponseBody
    @PostMapping("/api/wxApi/checkRechargeOrderStatus")
    @ApiOperation(value = "检查充值订单状态", notes = "检查充值订单状态")
    public JsonResult checkRechargeOrderStatus(@LoginedAuth UserSession session, String orderNo) {
        User user = userServiceExt.getById(session.getId());
        if(user == null) {
            return sendArgsError("用户不存在");
        }
        InvestOrder investOrder = investOrderServiceExt.getByOrderNoAndStatus(orderNo, Constant.ORDER_STATUS_WAITING_PAY);
        if(investOrder == null) {
            return sendSuccess(investOrder);
        }
        if(!user.getId().equals(investOrder.getUserId())) {
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
                payTime = TimeUtils.convertWeiXinTime(timeEnd);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //微信返回已经支付成功，数据库订单状态却未及时更新（还是待支付状态）
            if(StringUtils.equals(Constant.PAY_STATU_SUCCESS, tradeState) && StringUtils.equals(user.getOpenId(), openId)) {
                //如果数据库订单状态还是待支付更新状态
                if(investOrder.getStatus() == Constant.INVEST_ORDER_WAITING_WAY) {
                    investOrder.setInvestMoney(NumberUtils.str2Long(totalFee));
                    investOrder.setPayTime(payTime);
                    investOrder.setUpdateTime(new Date());
                    investOrder.setStatus(Constant.INVEST_ORDER_FINISH);
                    investOrderServiceExt.update(investOrder);
                    //生成支付流水
                    payFlowServiceExt.createPayFlow(user, investOrder, Constant.ORDER_TYPE_INVEST);
                }
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
            String returnCode = map.get("return_code");
            String appId = map.get("appid");
            String mchid = map.get("mchid");
            if (StringUtils.equals(returnCode, Constant.WX_RETURN_SUCCESS) && StringUtils.equals(appId, AppId) && StringUtils.equals(mchid, macId)) {
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
