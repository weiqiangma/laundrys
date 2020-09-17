package com.mawkun.admin.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.mawkun.core.base.data.query.InvestLogQuery;
import com.mawkun.core.base.entity.InvestLog;
import com.mawkun.core.base.service.InvestLogService;
import com.mawkun.core.service.InvestLogServiceExt;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 23:07:35
 */
@RestController
@RequestMapping("/investLog")
public class InvestLogController extends BaseController {
    
    @Autowired
    private InvestLogServiceExt investLogServiceExt;

    @GetMapping("/get")
    public JsonResult getById(Long id) {
        InvestLog investLog = investLogServiceExt.getById(id);
        return sendSuccess(investLog);
    }

    @GetMapping("/getByEntity")
    public JsonResult getByEntity(InvestLog investLog) {
        InvestLog resultLog = investLogServiceExt.getByEntity(investLog);
        return sendSuccess(resultLog);
    }

    @GetMapping("/list")
    public JsonResult list(InvestLog investLog) {
        List<InvestLog> investLogList = investLogServiceExt.listByEntity(investLog);
        return sendSuccess(investLogList);
    }

    @PostMapping("/insert")
    public JsonResult insert(InvestLog investLog){
        investLogServiceExt.insert(investLog);
        return sendSuccess(investLog);
    }

    @PutMapping("/update")
    public JsonResult update(InvestLog investLog){
        int result =  investLogServiceExt.update(investLog);
        return sendSuccess(result);
    }
}