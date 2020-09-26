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
import com.mawkun.core.base.data.query.GoodsOrderQuery;
import com.mawkun.core.base.data.vo.GoodsOrderVo;
import com.mawkun.core.base.entity.GoodsOrder;
import com.mawkun.core.base.entity.OrderLog;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.dao.ShopUserDaoExt;
import com.mawkun.core.service.OrderLogServiceExt;
import com.mawkun.core.service.GoodsOrderServiceExt;
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

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mawkun
 * @date 2020-08-19 21:43:45
 */
@RestController
@RequestMapping("/api/goodsOrder")
@Api(tags={"订单操作接口"})
public class GoodsOrderController extends BaseController {
    
    @Resource
    private GoodsOrderServiceExt goodsOrderServiceExt;
    @Resource
    private WxApiServiceExt wxApiServiceExt;
    @Resource
    private UserServiceExt userServiceExt;
    @Resource
    private OrderLogServiceExt orderLogServiceExt;

    @GetMapping("/get")
    @ApiOperation(value="根据id获取订单", notes="根据id获取订单")
    public JsonResult getById(Long id) {
        GoodsOrderVo goodsOrder = goodsOrderServiceExt.getDetail(id);
        return sendSuccess(goodsOrder);
    }

    @GetMapping("/getByEntity")
    @ApiOperation(value="根据entity获取订单", notes="根据entity获取订单")
    public JsonResult getByEntity(GoodsOrder goodsOrder) {
        GoodsOrder resultForm = goodsOrderServiceExt.getByEntity(goodsOrder);
        return sendSuccess(resultForm);
    }

    @GetMapping("/list")
    @ApiOperation(value="获取订单列表", notes="获取订单列表")
    public JsonResult list(@LoginedAuth UserSession session, GoodsOrder goodsOrder) {
        if(session.getId() > 0) {
            goodsOrder.setUserId(session.getId());
        }
        List<GoodsOrder> goodsOrderList = goodsOrderServiceExt.listByEntity(goodsOrder);
        return sendSuccess(goodsOrderList);
    }

    @GetMapping("/pageList")
    @ApiOperation(value="订单列表分页", notes="订单列表分页")
    public JsonResult pageList(@LoginedAuth UserSession session, GoodsOrderQuery query) {
        if(session.getId() > 0) {
            query.setUserId(session.getId());
        }
        PageInfo page = goodsOrderServiceExt.pageByEntity(query);
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
        GoodsOrder GoodsOrder = goodsOrderServiceExt.getById(orderId);
        if(!GoodsOrder.getUserId().equals(session.getId())) {
            return sendArgsError("非下单用户无权编辑");
        }
        JSONObject object = wxApiServiceExt.getOrderStatus(GoodsOrder.getOrderNo());
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
                if(GoodsOrder.getPayKind() == Constant.PAY_WITH_REMAINDER) {
                    user.setSumOfMoney(user.getSumOfMoney() - NumberUtils.str2Long(totalFee));
                    userServiceExt.update(user, null);
                }
                //如果订单状态还是待支付
                if(GoodsOrder.getStatus() == Constant.ORDER_STATUS_WAITING_PAY) {
                    //更新订单
                    GoodsOrder.setStatus(Constant.ORDER_STATUS_WAITING_REAP);
                    GoodsOrder.setPayTime(payTime);
                    GoodsOrder.setUpdateTime(new Date());
                    GoodsOrder.setRealAmount(NumberUtils.str2Long(totalFee));
                    goodsOrderServiceExt.update(null, GoodsOrder);
                }
            }
        } else {
            return sendArgsError("调用微信接口查询订单异常");
        }
        GoodsOrder resultOrder = goodsOrderServiceExt.getById(orderId);
        return sendSuccess(resultOrder);
    }

    @PostMapping("/insert")
    @ApiOperation(value="添加订单", notes="添加订单")
    public JsonResult insert(GoodsOrder goodsOrder){
        goodsOrderServiceExt.insert(goodsOrder);
        return sendSuccess(goodsOrder);
    }

    @PutMapping("/update")
    @ApiOperation(value="编辑订单", notes="编辑订单")
    public JsonResult update(@LoginedAuth UserSession session, GoodsOrder goodsOrder){
        return goodsOrderServiceExt.update(session, goodsOrder);
    }

    @PostMapping("/cancel")
    @ApiOperation(value="取消订单", notes="取消订单")
    public JsonResult cancel(@LoginedAuth UserSession session, Long orderId) {
        if(orderId == null) {
            return sendArgsError("请选择一个订单");
        }
        GoodsOrder order = new GoodsOrder();
        order.setId(orderId);
        order.setUserId(session.getId());
        order.setStatus(Constant.ORDER_STATUS_WAITING_PAY);
        GoodsOrder resultOrder = goodsOrderServiceExt.getByEntity(order);
        if(resultOrder == null) return sendArgsError("数据库中未查询到该订单");;
        goodsOrderServiceExt.cancel(session, resultOrder);
        return sendSuccess("ok", "取消成功");
    }

    @PostMapping("/delete")
    @ApiOperation(value="删除订单", notes="删除订单")
    public JsonResult deleteOne(Long orderId){
        int result = goodsOrderServiceExt.deleteById(orderId);
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
            result = goodsOrderServiceExt.deleteByIds(idList);
        }
        return sendSuccess(result);
    }

    @GetMapping("/getOrderLog")
    @ApiOperation(value="获取订单操作日志", notes="获取订单操作日志")
    public JsonResult getOrderLog(Long orderId) {
        JSONArray array = new JSONArray();
        OrderLog orderLog = new OrderLog();
        orderLog.setOrderId(orderId);
        List<OrderLog> list = orderLogServiceExt.listByEntity(orderLog);
        List<OrderLog> sortList = list.stream().sorted(Comparator.comparingInt(OrderLog::getStatus)).collect(Collectors.toList());
        for(OrderLog log : sortList) {
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
    public JsonResult getDistributorOrder(@LoginedAuth UserSession session, GoodsOrderQuery query) {
        if(!session.isDistributor()) {
            return sendArgsError("非配送员无权查看");
        }
        PageInfo page = goodsOrderServiceExt.getDistributorOrder(session.getId(), query);
        return sendSuccess(page);
    }

    @PostMapping("/orderTaking")
    @ApiOperation(value="配送员确认订单", notes="配送员确认订单")
    public JsonResult orderTaking(@LoginedAuth UserSession session, Long orderId, String description) {
        if(!session.isDistributor()) {
            return sendArgsError("非配送员无权操作");
        }
        GoodsOrder GoodsOrder = goodsOrderServiceExt.getById(orderId);
        if(GoodsOrder == null) {
            return sendArgsError("未查询到该订单");
        }
        boolean flag = goodsOrderServiceExt.checkOrderIsDistributor(session.getId(), orderId);
        if(!flag) {
            return sendArgsError("订单不属于配送员关联门店,无权操作");
        }
        int result = goodsOrderServiceExt.orderTaking(session, GoodsOrder, description);
        return sendSuccess(result);
    }
}