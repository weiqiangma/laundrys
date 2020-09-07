package com.mawkun.admin.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.mawkun.core.base.entity.OrderClothes;
import com.mawkun.core.service.OrderClothesServiceExt;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-19 21:43:30
 */
@RestController
@RequestMapping("/adm/orderClothes")
@Api(value="订单商品关联controller",tags={"订单商品关联操作接口"})
public class OrderClothesController extends BaseController {
    
    @Autowired
    private OrderClothesServiceExt orderClothesServiceExt;

    @GetMapping("/get")
    public JsonResult getById(Long id) {
        OrderClothes orderClothes = orderClothesServiceExt.getById(id);
        return sendSuccess(orderClothes);
    }

    @GetMapping("/getByEntity")
    public JsonResult getByEntity(OrderClothes orderClothes) {
        OrderClothes resultOrderClothes = orderClothesServiceExt.getByEntity(orderClothes);
        return sendSuccess(resultOrderClothes);
    }

    @GetMapping("/list")
    public JsonResult list(OrderClothes orderClothes) {
        List<OrderClothes> orderClothesList = orderClothesServiceExt.listByEntity(orderClothes);
        return sendSuccess(orderClothesList);
    }

    @PostMapping("/insert")
    public JsonResult insert(OrderClothes orderClothes){
        orderClothesServiceExt.insert(orderClothes);
        return sendSuccess(orderClothes);
    }

    @PutMapping("/update")
    public JsonResult update(@RequestBody OrderClothes orderClothes){
        int result = orderClothesServiceExt.update(orderClothes);
        return sendSuccess(result);
    }

    @DeleteMapping("/delete/{id}")
    public JsonResult deleteOne(@PathVariable Long id){
        int result = orderClothesServiceExt.deleteById(id);
        return sendSuccess(result);
    }

    @DeleteMapping("/delete")
    public JsonResult deleteBatch(@RequestBody List<Long> ids){
        int result = 0;
        if (ids!=null&&ids.size()>0) result = orderClothesServiceExt.deleteByIds(ids);
        return sendSuccess(result);
    }

}