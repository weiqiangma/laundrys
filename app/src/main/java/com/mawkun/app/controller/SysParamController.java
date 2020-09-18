package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.mawkun.core.base.entity.SysParam;
import com.mawkun.core.service.SysParamServiceExt;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-02 20:40:05
 */
@RestController
@Api(tags={"系统参数操作接口"})
@RequestMapping("/api/sysParam")
public class SysParamController extends BaseController {
    
    @Autowired
    private SysParamServiceExt sysParamServiceExt;

    @GetMapping("/getMainPicture")
    public JsonResult getMainPicture() {
        String result = sysParamServiceExt.getMainPicture();
        return sendSuccess("ok", result);
    }

    @GetMapping("/getMainAdvice")
    public JsonResult getMainAdvice() {
        String result = sysParamServiceExt.getMainAdvice();
        return sendSuccess("ok", result);
    }

    @GetMapping("/list")
    public JsonResult list(SysParam sysParam) {
        List<SysParam> sysParamList = sysParamServiceExt.listByEntity(sysParam);
        return sendSuccess(sysParamList);
    }
}