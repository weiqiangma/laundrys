package com.mawkun.app.controller;

import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.controller.BaseController;
import com.mawkun.core.base.data.JsonResult;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.PayFlowQuery;
import com.mawkun.core.base.entity.PayFlow;
import com.mawkun.core.base.service.PayFlowService;
import com.mawkun.core.service.PayFlowServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/payFlow")
public class PayFlowController extends BaseController {
    
    @Autowired
    private PayFlowServiceExt payFlowServiceExt;

    @GetMapping("/get")
    public JsonResult getById(Long id) {
        PayFlow payFlow = payFlowServiceExt.getById(id);
        return sendSuccess(payFlow);
    }

    @GetMapping("/getByEntity")
    public JsonResult getByEntity(PayFlow payFlow) {
        PayFlow result = payFlowServiceExt.getByEntity(payFlow);
        return sendSuccess(result);
    }

    @GetMapping("/list")
    public JsonResult list(@LoginedAuth UserSession session, PayFlow payFlow) {
        if(session.getId() > 0) {
            payFlow.setUserId(session.getId());
        }
        List<PayFlow> payFlowList = payFlowServiceExt.listByEntity(payFlow);
        return sendSuccess(payFlowList);
    }

    @GetMapping("/pageList")
    public JsonResult pageList(@LoginedAuth UserSession session, PayFlowQuery query) {
        if(session.getId() > 0) {
            query.setUserId(session.getId());
        }
        PageInfo page = payFlowServiceExt.pageList(query);
        return sendSuccess(page);
    }

    @PostMapping("/insert")
    public JsonResult insert(PayFlow payFlow){
        payFlowServiceExt.insert(payFlow);
        return sendSuccess(payFlow);
    }

    @PutMapping("/update")
    public JsonResult update(PayFlow payFlow){
        int result = payFlowServiceExt.update(payFlow);
        return sendSuccess(result);
    }

    @DeleteMapping("/delete")
    public JsonResult deleteOne(Long id){
        int result = payFlowServiceExt.deleteById(id);
        return sendSuccess(result);
    }
}