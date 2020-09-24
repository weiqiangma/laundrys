package com.mawkun.admin.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.InvestOrderQuery;
import com.mawkun.core.base.entity.InvestOrder;
import com.mawkun.core.service.InvestOrderServiceExt;
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
    private InvestOrderServiceExt investOrderServiceExt;

    @GetMapping("/get")
    @ApiOperation(value="充值日志详情", notes="充值日志详情")
    public JsonResult getById(Long id) {
        InvestOrder investOrder = investOrderServiceExt.getById(id);
        return sendSuccess(investOrder);
    }

    @GetMapping("/getByEntity")
    @ApiOperation(value="充值日志详情", notes="充值日志详情")
    public JsonResult getByEntity(InvestOrder investLog) {
        InvestOrder resultLog = investOrderServiceExt.getByEntity(investLog);
        return sendSuccess(resultLog);
    }

    @GetMapping("/list")
    @ApiOperation(value="充值日志列表", notes="充值日志列表")
    public JsonResult list(InvestOrder investLog) {
        List<InvestOrder> investOrderList = investOrderServiceExt.listByEntity(investLog);
        return sendSuccess(investOrderList);
    }

    @GetMapping("/pageList")
    @ApiOperation(value="充值日志列表分页", notes="充值日志列表分页")
    public JsonResult pageList(@LoginedAuth UserSession session, InvestOrderQuery query) {
        if(session.getId() > 0) query.setUserId(session.getId());
        PageInfo<InvestOrder> pageInfo = investOrderServiceExt.pageList(query);
        return sendSuccess(pageInfo);
    }

    @PostMapping("/insert")
    @ApiOperation(value="充值日志添加", notes="充值日志添加")
    public JsonResult insert(InvestOrder investLog){
        investOrderServiceExt.insert(investLog);
        return sendSuccess(investLog);
    }

    @PutMapping("/update")
    @ApiOperation(value="充值日志更新", notes="充值日志更新")
    public JsonResult update(InvestOrder investLog){
        int result =  investOrderServiceExt.update(investLog);
        return sendSuccess(result);
    }
}