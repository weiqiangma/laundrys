package com.mawkun.admin.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.RequestUtils;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.GoodsOrderQuery;
import com.mawkun.core.base.data.query.StateQuery;
import com.mawkun.core.base.data.vo.GoodsOrderVo;
import com.mawkun.core.base.entity.GoodsOrder;
import com.mawkun.core.service.GoodsOrderServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.mawkun.core.utils.StringUtils;
import com.xiaoleilu.hutool.convert.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-19 21:43:45
 */
@Slf4j
@RestController
@RequestMapping("/adm/goodsOrder")
@Api(tags={"订单操作接口"})
public class GoodsOrderController extends BaseController {
    
    @Resource
    GoodsOrderServiceExt goodsOrderServiceExt;

    @GetMapping("/get")
    @ApiOperation(value="根据id获取订单", notes="根据id获取订单")
    public JsonResult getById(Long id) {
        GoodsOrderVo goodsOrderVo = goodsOrderServiceExt.getDetail(id);
        return sendSuccess(goodsOrderVo);
    }

    @GetMapping("/getByEntity")
    @ApiOperation(value="根据entity获取订单", notes="根据entity获取订单")
    public JsonResult getByEntity(GoodsOrder goodsOrderVo) {
        GoodsOrder resultForm = goodsOrderServiceExt.getByEntity(goodsOrderVo);
        return sendSuccess(resultForm);
    }

    @GetMapping("/list")
    @ApiOperation(value="获取订单列表", notes="获取订单列表")
    public JsonResult list(GoodsOrder goodsOrderVo) {
        List<GoodsOrder> goodsOrderList = goodsOrderServiceExt.listByEntity(goodsOrderVo);
        return sendSuccess(goodsOrderList);
    }

    @GetMapping("/pageList")
    @ApiOperation(value="订单列表分页", notes="订单列表分页")
    public JsonResult pageList(@LoginedAuth @ApiIgnore UserSession session, GoodsOrderQuery query) {
        if(session.getLevel() > 0) {
            query.setShopId(session.getShopId());
        }
        PageInfo page = goodsOrderServiceExt.pageByEntity(query);
        List<GoodsOrderVo> list = page.getList();
        if(list.size() > 0) {
            list.forEach(item -> item.setIsnew(Constant.ORDER_OLD));
            goodsOrderServiceExt.setOrderIsOld(list);
        }
        return sendSuccess(page);
    }

    @PostMapping("/insert")
    @ApiOperation(value="添加订单", notes="添加订单")
    public JsonResult insert(GoodsOrder goodsOrder){
        goodsOrder.setOrderType(Constant.ORDER_OFFLINE);
        goodsOrder.setStatus(Constant.DELIVERY_ORDER_SURE_FINISH);
        goodsOrderServiceExt.insert(goodsOrder);
        String orderNo = StringUtils.createOrderFormNo(goodsOrder.getId().toString());
        goodsOrder.setOrderNo(orderNo);
        goodsOrderServiceExt.update(goodsOrder);
        return sendSuccess(goodsOrder);
    }

    @PutMapping("/update")
    @ApiOperation(value="编辑订单", notes="编辑订单")
    public JsonResult update(@LoginedAuth UserSession session, GoodsOrder goodsOrder){
        return goodsOrderServiceExt.update(session, goodsOrder);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value="删除订单", notes="删除订单")
    public JsonResult deleteOne(Long id){
        int result = goodsOrderServiceExt.deleteById(id);
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
        if (idList.size()>0) result = goodsOrderServiceExt.deleteByIds(idList);
        return sendSuccess(result);
    }

    /**
     * 获取新订单
     * @param session
     * @return
     */
    @GetMapping("/getNewOrder")
    public JsonResult getNewOrder(@LoginedAuth UserSession session) {
        int result = 0;
        List<GoodsOrderVo> resultList = new ArrayList<>();
        GoodsOrderQuery query = new GoodsOrderQuery();
        if(session.getLevel() > 0) {
            query.setShopId(session.getShopId());
            resultList = goodsOrderServiceExt.getShopNewOrder(query);
        } else {
            resultList = goodsOrderServiceExt.getShopNewOrder(query);
        }
        if(resultList.size() > 0) result = 1;
        return sendSuccess(result);
    }

    @GetMapping("/export")
    @ApiOperation(value="订单列表分页", notes="订单列表分页")
    public JsonResult export(@LoginedAuth @ApiIgnore UserSession session, GoodsOrderQuery query) {
        if(session.getLevel() > 0) {
            query.setShopId(session.getShopId());
        }
        PageInfo page = goodsOrderServiceExt.pageByEntity(query);
        List<GoodsOrderVo> list = page.getList();
        if(list.size() > 0) {
            list.forEach(item -> item.setIsnew(Constant.ORDER_OLD));
            goodsOrderServiceExt.setOrderIsOld(list);
        }
        return sendSuccess(page);
    }

    //查询统计对象(首页统计)
    private StateQuery createQueryStateVo(){
        int type = getIntPar("type",1);
        HttpServletRequest request = getRequest();
        Date start = null;
        Date end = null;
        Integer shopId = RequestUtils.getIntPar(request, "shopId", 0);
        try{
            start = RequestUtils.getDatePar(request,"createTimeStart","yyyy-MM-dd");
            end = RequestUtils.getDatePar(request,"createTimeEnd","yyyy-MM-dd");
        }catch (Exception e){
            log.error("开始和结束错误时间格式错误");
        }
        StateQuery queryVO = new StateQuery();
        queryVO.setShopId(shopId.longValue());
        if(start!=null && end!=null){
            type = 4 ;
            queryVO.setStartTime(start);
            queryVO.setEndTime(end);
        }
        queryVO.setType(type);
        return queryVO;
    }

}