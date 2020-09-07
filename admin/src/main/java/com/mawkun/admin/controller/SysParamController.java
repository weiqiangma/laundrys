package com.mawkun.admin.controller;

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
@RequestMapping("/adm/sysParam")
public class SysParamController extends BaseController {
    
    @Autowired
    private SysParamServiceExt sysParamServiceExt;

    @GetMapping("/get")
    public JsonResult getById(Long id) {
        SysParam sysParam = sysParamServiceExt.getById(id);
        return sendSuccess(sysParam);
    }

    @GetMapping("/list")
    public JsonResult list(SysParam sysParam) {
        List<SysParam> sysParamList = sysParamServiceExt.listByEntity(sysParam);
        return sendSuccess(sysParamList);
    }

    @PostMapping("/insert")
    public JsonResult insert(SysParam sysParam){
        sysParamServiceExt.insert(sysParam);
        return sendSuccess(sysParam);
    }

    @PutMapping("/update")
    public JsonResult update(SysParam sysParam, @RequestParam(value = "files", required = false)MultipartFile[] files){
        int result = sysParamServiceExt.updateWithPic(sysParam, files);
        return sendSuccess(result);
    }

    @DeleteMapping("/delete")
    public JsonResult deleteOne(Long id){
        int result = sysParamServiceExt.deleteById(id);
        return sendSuccess(result);
    }

    @DeleteMapping("/deleteBatch")
    public int deleteBatch(@RequestBody List<Long> ids){
        int result = 0;
        if (ids!=null&&ids.size()>0) result = sysParamServiceExt.deleteByIds(ids);
        return result;
    }

}