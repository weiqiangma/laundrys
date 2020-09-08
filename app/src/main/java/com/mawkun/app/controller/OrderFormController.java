package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.OrderFormQuery;
import com.mawkun.core.base.data.vo.OrderFormVo;
import com.mawkun.core.base.entity.OrderForm;
import com.mawkun.core.service.OrderFormServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.xiaoleilu.hutool.convert.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-19 21:43:45
 */
@RestController
@RequestMapping("/adm/orderForm")
@Api(tags={"订单操作接口"})
public class OrderFormController extends BaseController {
    
    @Autowired
    private OrderFormServiceExt orderFormServiceExt;

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
    public JsonResult list(OrderForm orderForm) {
        List<OrderForm> orderFormList = orderFormServiceExt.listByEntity(orderForm);
        return sendSuccess(orderFormList);
    }

    @GetMapping("pageList")
    @ApiOperation(value="订单列表分页", notes="订单列表分页")
    public JsonResult pageList(@LoginedAuth @ApiIgnore UserSession session, OrderFormQuery query) {
        if(session.getShopId() > 0) query.setShopId(session.getShopId());
        PageInfo page = orderFormServiceExt.pageByEntity(query);
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

    @DeleteMapping("/delete")
    @ApiOperation(value="删除订单", notes="删除订单")
    public JsonResult deleteOne(Long id){
        int result = orderFormServiceExt.deleteById(id);
        return sendSuccess(result);
    }

    @DeleteMapping("/deleteBatch")
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
}