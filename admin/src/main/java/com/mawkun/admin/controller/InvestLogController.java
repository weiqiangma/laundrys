package com.mawkun.admin.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.DateUtils;
import com.alibaba.excel.EasyExcel;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.PageInfo;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.InvestOrderQuery;
import com.mawkun.core.base.data.vo.InvestOrderStatsVo;
import com.mawkun.core.base.entity.InvestOrder;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.service.InvestOrderServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.mawkun.core.utils.TimeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 23:07:35
 */
@Controller
@Api(tags={"充值日志操作接口"})
@RequestMapping("/adm/investLog")
public class InvestLogController extends BaseController {
    
    @Autowired
    private InvestOrderServiceExt investOrderServiceExt;

    @ResponseBody
    @GetMapping("/get")
    @ApiOperation(value="充值日志详情", notes="充值日志详情")
    public JsonResult getById(Long id) {
        InvestOrder investOrder = investOrderServiceExt.getById(id);
        return sendSuccess(investOrder);
    }

    @ResponseBody
    @GetMapping("/getByEntity")
    @ApiOperation(value="充值日志详情", notes="充值日志详情")
    public JsonResult getByEntity(InvestOrder investLog) {
        InvestOrder resultLog = investOrderServiceExt.getByEntity(investLog);
        return sendSuccess(resultLog);
    }

    @ResponseBody
    @GetMapping("/list")
    @ApiOperation(value="充值日志列表", notes="充值日志列表")
    public JsonResult list(InvestOrder investLog) {
        List<InvestOrder> investOrderList = investOrderServiceExt.listByEntity(investLog);
        return sendSuccess(investOrderList);
    }

    @ResponseBody
    @GetMapping("/pageList")
    @ApiOperation(value="充值日志列表分页", notes="充值日志列表分页")
    public JsonResult pageList(@LoginedAuth UserSession session, InvestOrderQuery query) {
        if(session.getId() > 0) query.setUserId(session.getId());
        query.setStatus(Constant.ORDER_TYPE_INVEST);
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
            query.setStartTime(sTime);
            query.setEndTime(eTime);
        }
        PageInfo pageInfo = investOrderServiceExt.pageList(query);
        InvestOrderStatsVo investOrderStatsVo = investOrderServiceExt.statsInvestOrder(query);
        pageInfo.setInvestMoney(investOrderStatsVo.getInvestMoney());
        pageInfo.setGiftMoney(investOrderStatsVo.getGiftMoney());
        pageInfo.setAmountMoney(investOrderStatsVo.getAmountMoney());
        return sendSuccess(pageInfo);
    }

    @ResponseBody
    @PostMapping("/insert")
    @ApiOperation(value="充值日志添加", notes="充值日志添加")
    public JsonResult insert(InvestOrder investLog){
        investOrderServiceExt.insert(investLog);
        return sendSuccess(investLog);
    }

    @ResponseBody
    @PutMapping("/update")
    @ApiOperation(value="充值日志更新", notes="充值日志更新")
    public JsonResult update(InvestOrder investLog){
        int result =  investOrderServiceExt.update(investLog);
        return sendSuccess(result);
    }

    @GetMapping("/export")
    @ApiOperation(value="充值记录导出excel", notes="充值记录导出excel")
    public void export(InvestOrder investOrder, HttpServletResponse response) {
        List<InvestOrder> list = investOrderServiceExt.listByEntity(investOrder);
        List<InvestOrder> resultList = new ArrayList<>();
        for(InvestOrder order : list) {
            Long amountMoney = order.getAmountMoney() / 100;
            Long residueMoney = order.getResidueMoney() / 100;
            Long investMoney = order.getInvestMoney() / 100;
            Long giftMoney =order.getGiftMoney() / 100;
            order.setAmountMoney(amountMoney);
            order.setResidueMoney(residueMoney);
            order.setInvestMoney(investMoney);
            order.setGiftMoney(giftMoney);
            resultList.add(order);
        }
        try(OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("用户充值记录统计.xlsx", "UTF-8"));
            EasyExcel.write(outputStream, InvestOrder.class).sheet("用户充值记录统计").doWrite(resultList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}