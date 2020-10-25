package com.mawkun.admin.controller;

import cn.pertech.common.utils.DateUtils;
import cn.pertech.common.utils.RequestUtils;
import com.alibaba.excel.EasyExcel;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.controller.BaseController;
import com.mawkun.core.base.data.JsonResult;
import com.mawkun.core.base.data.PageInfo;
import com.mawkun.core.base.data.ShopOrderData;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.GoodsOrderQuery;
import com.mawkun.core.base.data.query.StateQuery;
import com.mawkun.core.base.data.vo.GoodsOrderVo;
import com.mawkun.core.base.entity.GoodsOrder;
import com.mawkun.core.base.entity.InvestOrder;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.dao.ShopUserDaoExt;
import com.mawkun.core.service.GoodsOrderServiceExt;
import com.mawkun.core.service.UserServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.mawkun.core.utils.StringUtils;
import com.mawkun.core.utils.TimeUtils;
import com.xiaoleilu.hutool.convert.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-19 21:43:45
 */
@Slf4j
@Controller
@RequestMapping("/adm/goodsOrder")
@Api(tags={"订单操作接口"})
public class GoodsOrderController extends BaseController {
    
    @Resource
    GoodsOrderServiceExt goodsOrderServiceExt;
    @Resource
    ShopUserDaoExt shopUserDaoExt;
    @Resource
    UserServiceExt userServiceExt;

    @ResponseBody
    @GetMapping("/get")
    @ApiOperation(value="根据id获取订单", notes="根据id获取订单")
    public JsonResult getById(Long id) {
        GoodsOrderVo goodsOrderVo = goodsOrderServiceExt.getDetail(id);
        return sendSuccess(goodsOrderVo);
    }
    @ResponseBody
    @GetMapping("/getByEntity")
    @ApiOperation(value="根据entity获取订单", notes="根据entity获取订单")
    public JsonResult getByEntity(GoodsOrder goodsOrderVo) {
        GoodsOrder resultForm = goodsOrderServiceExt.getByEntity(goodsOrderVo);
        return sendSuccess(resultForm);
    }
    @ResponseBody
    @GetMapping("/list")
    @ApiOperation(value="获取订单列表", notes="获取订单列表")
    public JsonResult list(GoodsOrder goodsOrderVo) {
        List<GoodsOrder> goodsOrderList = goodsOrderServiceExt.listByEntity(goodsOrderVo);
        return sendSuccess(goodsOrderList);
    }
    @ResponseBody
    @GetMapping("/pageList")
    @ApiOperation(value="订单列表分页", notes="订单列表分页")
    public JsonResult pageList(@LoginedAuth @ApiIgnore UserSession session, GoodsOrderQuery query) {
        if(session.getLevel() == Constant.ADMIN_TYPE_COMMON) {
            query.setShopId(session.getShopId());
        }
        if(session.isDistributor()) {
            User user = userServiceExt.getDistributorByMobile(session.getMobile());
            if(user == null) return sendArgsError("未查询到该配送员信息");
            query.setDistributorId(user.getId());
        }
        Date sTime = new Date();
        Date eTime = new Date();
        if(query.getTimeType() != null) {
            if (Constant.TIME_TYPE_DAY == query.getTimeType()) {
                sTime = DateUtils.timeNow();
                eTime = DateUtils.dateEndDate(sTime);
            }
            if (Constant.TIME_TYPE_WEEK == query.getTimeType()) {
                sTime = TimeUtils.getWeekStart();
                eTime = TimeUtils.getWeekEnd();
                sTime = TimeUtils.getBeginDayOfWeek();
                eTime = TimeUtils.getEndDayOfWeek();
            }
            if (Constant.TIME_TYPE_MONTH == query.getTimeType()) {
                sTime = TimeUtils.getMonthStart();
                eTime = TimeUtils.getMonthEnd();
            }
            if (Constant.TIME_TYPE_YEAR == query.getTimeType()) {
                sTime = TimeUtils.getCurrentYearStartTime();
                eTime = TimeUtils.getCurrentYearEndTime();
            }
            if (Constant.TIME_TYPE_RANDOM == query.getTimeType()) {
                sTime = query.getCreateTimeStart();
                eTime = query.getCreateTimeEnd();
            }
            query.setCreateTimeStart(sTime);
            query.setCreateTimeEnd(eTime);
        }
        PageInfo<GoodsOrderVo> page = goodsOrderServiceExt.pageByEntity(query);
        List<GoodsOrderVo> list = page.getList();
        if(list.size() > 0) {
            list.forEach(item -> item.setIsnew(Constant.ORDER_OLD));
            goodsOrderServiceExt.setOrderIsOld(list);
        }
        ShopOrderData shopOrderData = goodsOrderServiceExt.statsGoodsOrder(query);
        if(shopOrderData != null) {
            page.setTotalAmount(shopOrderData.getTotalAmount());
            page.setTotalTransportFee(shopOrderData.getTotalTransportFee());
            page.setOrderSize(shopOrderData.getOrderSize());
            if(shopOrderData.getTotalTransportFee() == null) {
                page.setTotalTransportFee(0);
            }
            if(shopOrderData.getTotalAmount() == null) {
                page.setTotalAmount(0);
            }

        } else{
            page.setTotalAmount(0);
            page.setTotalTransportFee(0);
            page.setOrderSize(0);
        }
        return sendSuccess(page);
    }
    @ResponseBody
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
    @ResponseBody
    @RequestMapping("/update")
    @ApiOperation(value="编辑订单", notes="编辑订单")
    public JsonResult update(@LoginedAuth UserSession session, GoodsOrder goodsOrder){
        return goodsOrderServiceExt.update(session, goodsOrder);
    }
    @ResponseBody
    @RequestMapping("/delete")
    @ApiOperation(value="删除订单", notes="删除订单")
    public JsonResult deleteOne(Long id){
        int result = goodsOrderServiceExt.deleteById(id);
        return sendSuccess(result);
    }
    @ResponseBody
    @RequestMapping("/deleteBatch")
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
    @ResponseBody
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
        JsonResult result1 = sendSuccess(result);
        return sendSuccess(result);
    }

    @GetMapping("/export")
    @ApiOperation(value="订单导出", notes="订单导出")
    public void export(@LoginedAuth @ApiIgnore UserSession session, GoodsOrderQuery query, HttpServletResponse response) {
        if(session.getLevel() > 0) {
            query.setShopId(session.getShopId());
        }
        Date sTime = new Date();
        Date eTime = new Date();
        if(query.getTimeType() != null) {
            if (Constant.TIME_TYPE_DAY == query.getTimeType()) {
                sTime = DateUtils.timeNow();
                eTime = DateUtils.dateEndDate(sTime);
            }
            if (Constant.TIME_TYPE_WEEK == query.getTimeType()) {
                sTime = TimeUtils.getWeekStart();
                eTime = TimeUtils.getWeekEnd();
                sTime = TimeUtils.getBeginDayOfWeek();
                eTime = TimeUtils.getEndDayOfWeek();
            }
            if (Constant.TIME_TYPE_MONTH == query.getTimeType()) {
                sTime = TimeUtils.getMonthStart();
                eTime = TimeUtils.getMonthEnd();
            }
            if (Constant.TIME_TYPE_YEAR == query.getTimeType()) {
                sTime = TimeUtils.getCurrentYearStartTime();
                eTime = TimeUtils.getCurrentYearEndTime();
            }
            if (Constant.TIME_TYPE_RANDOM == query.getTimeType()) {
                sTime = query.getCreateTimeStart();
                eTime = query.getCreateTimeEnd();
            }
            query.setCreateTimeStart(sTime);
            query.setCreateTimeEnd(eTime);
        }
        List<GoodsOrderVo> list = goodsOrderServiceExt.listByEntity(query);

        List<GoodsOrderVo> resultList = new ArrayList<>();
        for(GoodsOrderVo goodsOrderVo : list) {
            long realAmount = 0;
            long totalAmount = 0;
            long transportFee = 0;
            if(goodsOrderVo.getRealAmount() != null) {
                realAmount = goodsOrderVo.getRealAmount() / 100;
            }
            if(goodsOrderVo.getTotalAmount() != null) {
                totalAmount = goodsOrderVo.getTotalAmount() / 100;
            }
            if(goodsOrderVo.getTransportFee() != null) {
                transportFee = goodsOrderVo.getTransportFee() / 100;
            }
            goodsOrderVo.setRealAmount(realAmount);
            goodsOrderVo.setTotalAmount(totalAmount);
            goodsOrderVo.setTransportFee(transportFee);
            resultList.add(goodsOrderVo);
        }
        try(OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("订单统计.xlsx", "UTF-8"));
            EasyExcel.write(outputStream, GoodsOrderVo.class).sheet("订单统计").doWrite(resultList);
        } catch (Exception e) {
            e.printStackTrace();
        }
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