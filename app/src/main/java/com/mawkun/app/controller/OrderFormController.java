package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.DateUtils;
import cn.pertech.common.utils.NumberUtils;
import cn.pertech.common.utils.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.OrderFormQuery;
import com.mawkun.core.base.data.vo.OrderFormVo;
import com.mawkun.core.base.data.vo.ShopVo;
import com.mawkun.core.base.entity.OperateOrderLog;
import com.mawkun.core.base.entity.OrderForm;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.base.service.UserService;
import com.mawkun.core.dao.ShopUserDaoExt;
import com.mawkun.core.service.OperateOrderLogServiceExt;
import com.mawkun.core.service.OrderFormServiceExt;
import com.mawkun.core.service.UserServiceExt;
import com.mawkun.core.service.WxApiServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.xiaoleilu.hutool.convert.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mawkun
 * @date 2020-08-19 21:43:45
 */
@RestController
@RequestMapping("/api/orderForm")
@Api(tags={"订单操作接口"})
public class OrderFormController extends BaseController {
    
    @Autowired
    private OrderFormServiceExt orderFormServiceExt;
    @Autowired
    private WxApiServiceExt wxApiServiceExt;
    @Autowired
    private UserServiceExt userServiceExt;
    @Autowired
    private ShopUserDaoExt shopUserDaoExt;
    @Autowired
    private OperateOrderLogServiceExt operateOrderLogServiceExt;

    @GetMapping("/get")
    @ApiOperation(value="根据id获取订单", notes="根据id获取订单")
    public JsonResult getById(Long id) {
        OrderFormVo orderFormVo = orderFormServiceExt.getDetail(id);
        return sendSuccess(orderFormVo);
    }

    @GetMapping("/getByEntity")
    @ApiOperation(value="根据entity获取订单", notes="根据entity获取订单")
    public JsonResult getByEntity(OrderForm orderForm) {
        OrderForm resultForm = orderFormServiceExt.getByEntity(orderForm);
        return sendSuccess(resultForm);
    }

    @GetMapping("/list")
    @ApiOperation(value="获取订单列表", notes="获取订单列表")
    public JsonResult list(@LoginedAuth UserSession session, OrderForm orderForm) {
        if(session.getId() > 0) {
            orderForm.setUserId(session.getId());
        }
        List<OrderForm> orderFormList = orderFormServiceExt.listByEntity(orderForm);
        return sendSuccess(orderFormList);
    }

    @GetMapping("/pageList")
    @ApiOperation(value="订单列表分页", notes="订单列表分页")
    public JsonResult pageList(@LoginedAuth UserSession session, OrderFormQuery query) {
        if(session.getId() > 0) {
            query.setUserId(session.getId());
        }
        PageInfo page = orderFormServiceExt.pageByEntity(query);
        return sendSuccess(page);
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

    @PostMapping("/insert")
    @ApiOperation(value="添加订单", notes="添加订单")
    public JsonResult insert(OrderForm orderForm){
        orderFormServiceExt.insert(orderForm);
        return sendSuccess(orderForm);
    }

    @PutMapping("/update")
    @ApiOperation(value="编辑订单", notes="编辑订单")
    public JsonResult update(@LoginedAuth UserSession session, OrderForm orderForm){
        return orderFormServiceExt.update(session, orderForm);
    }

    @PostMapping("/cancel")
    @ApiOperation(value="取消订单", notes="取消订单")
    public JsonResult cancel(@LoginedAuth UserSession session, Long orderId) {
        if(orderId == null) {
            return sendArgsError("请选择一个订单");
        }
        OrderForm order = new OrderForm();
        order.setId(orderId);
        order.setUserId(session.getId());
        order.setStatus(Constant.ORDER_STATUS_WAITING_PAY);
        OrderForm resultOrder = orderFormServiceExt.getByEntity(order);
        if(resultOrder == null) return sendArgsError("数据库中未查询到该订单");;
        orderFormServiceExt.cancel(session, resultOrder);
        return sendSuccess("ok", "取消成功");
    }

    @PostMapping("/delete")
    @ApiOperation(value="删除订单", notes="删除订单")
    public JsonResult deleteOne(Long orderId){
        int result = orderFormServiceExt.deleteById(orderId);
        return sendSuccess(result);
    }

    @PostMapping("/deleteBatch")
    @ApiOperation(value="批量删除订单", notes="批量删除订单")
    public JsonResult deleteBatch(String ids){
        int result = 0;
        List<String> idArray = Arrays.asList(ids.split(","));
        List idList = new ArrayList<>();
        idList = CollectionUtils.transform(idArray, new Transformer() {
            @Override
            public Object transform(Object o) {
                return Convert.toInt(o, 0);
            }
        });
        if (idList.size()>0) {
            result = orderFormServiceExt.deleteByIds(idList);
        }
        return sendSuccess(result);
    }

    @GetMapping("/getOrderLog")
    @ApiOperation(value="获取订单操作日志", notes="获取订单操作日志")
    public JsonResult getOrderLog(Long orderId) {
        JSONArray array = new JSONArray();
        OperateOrderLog orderLog = new OperateOrderLog();
        orderLog.setOrderFormId(orderId);
        List<OperateOrderLog> list = operateOrderLogServiceExt.listByEntity(orderLog);
        List<OperateOrderLog> sortList = list.stream().sorted(Comparator.comparingInt(OperateOrderLog::getStatus)).collect(Collectors.toList());
        for(OperateOrderLog log : sortList) {
            JSONObject object = new JSONObject();
            String createTime = DateUtils.format("yyyy-MM-dd HH:mm:ss", log.getCreateTime());
            object.put("status", log.getStatus());
            object.put("createTime", createTime);
            array.add(object);
        }
        return sendSuccess(array);
    }

    @GetMapping("/getDistributorOrder")
    @ApiOperation(value="配送员订单列表分页", notes="配送员订单列表分页")
    public JsonResult getDistributorOrder(@LoginedAuth UserSession session, OrderFormQuery query) {
        if(!session.isDistributor()) {
            return sendArgsError("非配送员无权查看");
        }
        PageInfo page = orderFormServiceExt.getDistributorOrder(session.getId(), query);
        return sendSuccess(page);
    }

    @GetMapping("/orderTaking")
    @ApiOperation(value="配送员确认订单", notes="配送员确认订单")
    public JsonResult orderTaking(@LoginedAuth UserSession session, Long orderId, String description) {
        if(!session.isDistributor()) {
            return sendArgsError("非配送员无权操作");
        }
        OrderForm orderForm = orderFormServiceExt.getById(orderId);
        if(orderForm == null) {
            return sendArgsError("未查询到该订单");
        }
        boolean flag = orderFormServiceExt.checkOrderIsDistributor(session.getId(), orderId);
        if(!flag) {
            return sendArgsError("订单不属于配送员关联门店,无权操作");
        }
        int result = orderFormServiceExt.orderTaking(session, orderForm, description);
        return sendSuccess(result);
    }
}