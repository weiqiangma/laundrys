package com.mawkun.admin.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.mawkun.core.base.entity.PayFlow;
import com.mawkun.core.base.service.PayFlowService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/adm/payFlow")
public class PayFlowController extends BaseController {

    @Autowired
    private PayFlowService payFlowService;

    @GetMapping("/get")
    public JsonResult getById(Long id) {
        PayFlow payFlow = payFlowService.getById(id);
        return sendSuccess(payFlow);
    }

    @GetMapping("/getByEntity")
    public JsonResult getByEntity(PayFlow payFlow) {
        PayFlow result = payFlowService.getByEntity(payFlow);
        return sendSuccess(result);
    }

    @GetMapping("/list")
    public JsonResult list(PayFlow payFlow) {
        List<PayFlow> payFlowList = payFlowService.listByEntity(payFlow);
        return sendSuccess(payFlowList);
    }

    @PostMapping("/insert")
    public JsonResult insert(PayFlow payFlow){
        payFlowService.insert(payFlow);
        return sendSuccess(payFlow);
    }

    @PutMapping("/update")
    public JsonResult update(PayFlow payFlow){
        int result = payFlowService.update(payFlow);
        return sendSuccess(result);
    }

    @DeleteMapping("/delete")
    public JsonResult deleteOne(Long id){
        int result = payFlowService.deleteById(id);
        return sendSuccess(result);
    }

}