package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.DateUtils;
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
        if(session.getId() > 0) orderForm.setUserId(session.getId());
        List<OrderForm> orderFormList = orderFormServiceExt.listByEntity(orderForm);
        return sendSuccess(orderFormList);
    }

    @GetMapping("/pageList")
    @ApiOperation(value="订单列表分页", notes="订单列表分页")
    public JsonResult pageList(@LoginedAuth UserSession session, OrderFormQuery query) {
        if(session.isCustomer()) query.setUserId(session.getId());
        if(session.isDistributor()) query.setShopIdList(session.getShopIdList());
        PageInfo page = orderFormServiceExt.pageByEntity(query);
        return sendSuccess(page);
    }

    @GetMapping("/getDistributorOrder")
    @ApiOperation(value="配送员订单列表分页", notes="配送员订单列表分页")
    public JsonResult getDistributorOrder(@LoginedAuth UserSession session, OrderFormQuery query) {
        PageInfo page = orderFormServiceExt.getDistributorOrder(session.getId(), query);
        return sendSuccess(page);
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
        if(orderId == null) return sendArgsError("请选择一个订单");
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
        if (idList.size()>0) result = orderFormServiceExt.deleteByIds(idList);
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
}