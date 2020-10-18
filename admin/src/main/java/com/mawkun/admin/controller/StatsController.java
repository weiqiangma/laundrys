package com.mawkun.admin.controller;

import cn.pertech.common.utils.DateUtils;
import com.github.pagehelper.PageHelper;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.controller.BaseController;
import com.mawkun.core.base.data.JsonResult;
import com.mawkun.core.base.data.PageInfo;
import com.mawkun.core.base.data.ShopOrderData;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.GoodsOrderQuery;
import com.mawkun.core.base.data.query.InvestOrderQuery;
import com.mawkun.core.base.data.query.StateQuery;
import com.mawkun.core.base.data.vo.GoodsOrderVo;
import com.mawkun.core.base.data.vo.InvestOrderStatsVo;
import com.mawkun.core.base.entity.GoodsOrder;
import com.mawkun.core.base.entity.InvestOrder;
import com.mawkun.core.service.GoodsOrderServiceExt;
import com.mawkun.core.service.InvestOrderServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.mawkun.core.utils.TimeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/adm/stats")
public class StatsController extends BaseController {

    @Resource
    private GoodsOrderServiceExt goodsOrderServiceExt;
    @Resource
    private InvestOrderServiceExt investOrderServiceExt;

    @RequestMapping("/statsOrder")
    public JsonResult statsOrder(@LoginedAuth UserSession session, StateQuery query) {
        if(session.getLevel() > 0) query.setShopId(session.getShopId());
        if(query.getOrderType() == Constant.ORDER_TYPE_GOODS) {
            GoodsOrderQuery goodsOrderQuery = new GoodsOrderQuery();
            goodsOrderQuery.setStatus(Constant.DELIVERY_ORDER_SURE_FINISH);
            if(query.getShopId() != null) goodsOrderQuery.setShopId(query.getShopId());
            if(query.getDistributorId() != null) goodsOrderQuery.setDistributorId(query.getDistributorId());
            if(query.getPayKind() != null) goodsOrderQuery.setPayKind(query.getPayKind());
            if(query.getPageNum() == null || query.getPageSize() == null) {
                query.setPageNum(1);
                query.setPageSize(10);
            }
            if(query.getTimeType()!= null) {
                Date sTime = new Date();
                Date eTime = new Date();
                if(Constant.TIME_TYPE_DAY == query.getTimeType()) {
                    sTime = DateUtils.timeNow();
                    eTime = DateUtils.dateEndDate(sTime);
                }
                if(Constant.TIME_TYPE_WEEK == query.getTimeType()) {
                    sTime = TimeUtils.getWeekStart();
                    eTime = TimeUtils.getWeekEnd();
                }
                if(Constant.TIME_TYPE_MONTH == query.getTimeType()) {
                    sTime = TimeUtils.getMonthStart();
                    eTime = TimeUtils.getMonthEnd();
                }
                if(Constant.TIME_TYPE_YEAR == query.getTimeType()) {
                    sTime = TimeUtils.getCurrentYearStartTime();
                    eTime = TimeUtils.getCurrentYearEndTime();
                }
                if(Constant.TIME_TYPE_RANDOM == query.getTimeType()) {
                    sTime = query.getCreateTimeStart();
                    eTime = query.getCreateTimeEnd();
                }
                query.setCreateTimeStart(sTime);
                query.setCreateTimeEnd(eTime);
            }
            PageHelper.startPage(query.getPageNum(), query.getPageSize());
            List<GoodsOrder> goodsOrderList = goodsOrderServiceExt.getGoodsOrderList(goodsOrderQuery);
            PageInfo pageInfo = new PageInfo<GoodsOrder>(goodsOrderList);
            ShopOrderData shopOrderData = goodsOrderServiceExt.statsGoodsOrder(goodsOrderQuery);
            pageInfo.setTotalAmount(shopOrderData.getTotalAmount());
            pageInfo.setTotalTransportFee(shopOrderData.getTotalTransportFee());
            return sendSuccess(pageInfo);
        }
        if(query.getOrderType() == Constant.ORDER_TYPE_INVEST) {
            if(query.getOrderType() == Constant.ORDER_TYPE_INVEST) {
                InvestOrderQuery investOrderQuery = new InvestOrderQuery();
                investOrderQuery.setStatus(Constant.ORDER_TYPE_INVEST);
                if(query.getPageNum() == null || query.getPageSize() == null) {
                    query.setPageNum(1);
                    query.setPageSize(10);
                }
                if(query.getTimeType()!= null) {
                    Date sTime = new Date();
                    Date eTime = new Date();
                    if(Constant.TIME_TYPE_DAY == query.getTimeType()) {
                        sTime = DateUtils.timeNow();
                        eTime = DateUtils.dateEndDate(sTime);
                    }
                    if(Constant.TIME_TYPE_WEEK == query.getTimeType()) {
                        sTime = TimeUtils.getWeekStart();
                        eTime = TimeUtils.getWeekEnd();
                    }
                    if(Constant.TIME_TYPE_MONTH == query.getTimeType()) {
                        sTime = TimeUtils.getMonthStart();
                        eTime = TimeUtils.getMonthEnd();
                    }
                    if(Constant.TIME_TYPE_YEAR == query.getTimeType()) {
                        sTime = TimeUtils.getCurrentYearStartTime();
                        eTime = TimeUtils.getCurrentYearEndTime();
                    }
                    if(Constant.TIME_TYPE_RANDOM == query.getTimeType()) {
                        sTime = query.getCreateTimeStart();
                        eTime = query.getCreateTimeEnd();
                    }
                    investOrderQuery.setStartTime(sTime);
                    investOrderQuery.setEndTime(eTime);
                }
                PageInfo<InvestOrder> pageInfo = investOrderServiceExt.pageList(investOrderQuery);
                InvestOrderStatsVo investOrderStatsVo = investOrderServiceExt.statsInvestOrder(investOrderQuery);
                pageInfo.setInvestMoney(investOrderStatsVo.getInvestMoney());
                pageInfo.setGiftMoney(investOrderStatsVo.getGiftMoney());
                pageInfo.setAmountMoney(investOrderStatsVo.getAmountMoney());
            }

        }
        return sendSuccess();
    }
}
