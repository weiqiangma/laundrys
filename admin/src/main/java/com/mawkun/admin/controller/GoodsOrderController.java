package com.mawkun.admin.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.GoodsOrderQuery;
import com.mawkun.core.base.data.vo.GoodsOrderVo;
import com.mawkun.core.base.entity.GoodsOrder;
import com.mawkun.core.service.GoodsOrderServiceExt;
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
public class GoodsOrderController extends BaseController {
    
    @Autowired
    private GoodsOrderServiceExt GoodsOrderServiceExt;

    @GetMapping("/get")
    @ApiOperation(value="根据id获取订单", notes="根据id获取订单")
    public JsonResult getById(Long id) {
        GoodsOrderVo GoodsOrderVo = GoodsOrderServiceExt.getDetail(id);
        return sendSuccess(GoodsOrderVo);
    }

    @GetMapping("/getByEntity")
    @ApiOperation(value="根据entity获取订单", notes="根据entity获取订单")
    public JsonResult getByEntity(GoodsOrder GoodsOrder) {
        GoodsOrder resultForm = GoodsOrderServiceExt.getByEntity(GoodsOrder);
        return sendSuccess(resultForm);
    }

    @GetMapping("/list")
    @ApiOperation(value="获取订单列表", notes="获取订单列表")
    public JsonResult list(GoodsOrder GoodsOrder) {
        List<GoodsOrder> GoodsOrderList = GoodsOrderServiceExt.listByEntity(GoodsOrder);
        return sendSuccess(GoodsOrderList);
    }

    @GetMapping("/pageList")
    @ApiOperation(value="订单列表分页", notes="订单列表分页")
    public JsonResult pageList(@LoginedAuth @ApiIgnore UserSession session, GoodsOrderQuery query) {
        if(session.getShopId() > 0) query.setShopId(session.getShopId());
        PageInfo page = GoodsOrderServiceExt.pageByEntity(query);
        return sendSuccess(page);
    }

    @PostMapping("/insert")
    @ApiOperation(value="添加订单", notes="添加订单")
    public JsonResult insert(GoodsOrder GoodsOrder){
        GoodsOrderServiceExt.insert(GoodsOrder);
        return sendSuccess(GoodsOrder);
    }

    @PutMapping("/update")
    @ApiOperation(value="编辑订单", notes="编辑订单")
    public JsonResult update(@LoginedAuth UserSession session, GoodsOrder GoodsOrder){
        return GoodsOrderServiceExt.update(session, GoodsOrder);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value="删除订单", notes="删除订单")
    public JsonResult deleteOne(Long id){
        int result = GoodsOrderServiceExt.deleteById(id);
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
        if (idList.size()>0) result = GoodsOrderServiceExt.deleteByIds(idList);
        return sendSuccess(result);
    }
}