package com.mawkun.admin.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.InvestLogQuery;
import com.mawkun.core.base.entity.InvestLog;
import com.mawkun.core.base.service.InvestLogService;
import com.mawkun.core.service.InvestLogServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 23:07:35
 */
@RestController
@Api(tags={"充值日志操作接口"})
@RequestMapping("/adm/investLog")
public class InvestLogController extends BaseController {
    
    @Autowired
    private InvestLogServiceExt investLogServiceExt;

    @GetMapping("/get")
    @ApiOperation(value="充值日志详情", notes="充值日志详情")
    public JsonResult getById(Long id) {
        InvestLog investLog = investLogServiceExt.getById(id);
        return sendSuccess(investLog);
    }

    @GetMapping("/getByEntity")
    @ApiOperation(value="充值日志详情", notes="充值日志详情")
    public JsonResult getByEntity(InvestLog investLog) {
        InvestLog resultLog = investLogServiceExt.getByEntity(investLog);
        return sendSuccess(resultLog);
    }

    @GetMapping("/list")
    @ApiOperation(value="充值日志列表", notes="充值日志列表")
    public JsonResult list(InvestLog investLog) {
        List<InvestLog> investLogList = investLogServiceExt.listByEntity(investLog);
        return sendSuccess(investLogList);
    }

    @GetMapping("/pageList")
    @ApiOperation(value="充值日志列表分页", notes="充值日志列表分页")
    public JsonResult pageList(@LoginedAuth UserSession session, InvestLogQuery query) {
        if(session.getId() > 0) query.setUserId(session.getId());
        PageInfo<InvestLog> pageInfo = investLogServiceExt.pageList(query);
        return sendSuccess(pageInfo);
    }

    @PostMapping("/insert")
    @ApiOperation(value="充值日志添加", notes="充值日志添加")
    public JsonResult insert(InvestLog investLog){
        investLogServiceExt.insert(investLog);
        return sendSuccess(investLog);
    }

    @PutMapping("/update")
    @ApiOperation(value="充值日志更新", notes="充值日志更新")
    public JsonResult update(InvestLog investLog){
        int result =  investLogServiceExt.update(investLog);
        return sendSuccess(result);
    }
}