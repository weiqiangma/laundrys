package com.mawkun.admin.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.OperateOrderLogQuery;
import com.mawkun.core.base.entity.OperateOrderLog;
import com.mawkun.core.service.OperateOrderLogServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.xiaoleilu.hutool.convert.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-29 14:40:07
 */
@RestController
@Api(tags={"订单操作日志接口"})
@RequestMapping("/adm/operateOrderLog")
public class OperateOrderLogController extends BaseController {
    
    @Autowired
    private OperateOrderLogServiceExt operateOrderLogServiceExt;

    @GetMapping("/get")
    @ApiOperation(value="订单日志详情", notes="订单日志详情")
    public JsonResult getById(Long id) {
        OperateOrderLog operateOrderLog = operateOrderLogServiceExt.getById(id);
        return sendSuccess(operateOrderLog);
    }

    @GetMapping("/getByEntity")
    @ApiOperation(value="订单日志详情", notes="订单日志详情")
    public JsonResult getByEntity(OperateOrderLog operateOrderLog) {
        OperateOrderLog resultLog = operateOrderLogServiceExt.getByEntity(operateOrderLog);
        return sendSuccess(resultLog);
    }

    @GetMapping("/list")
    @ApiOperation(value="订单日志列表", notes="订单日志列表")
    public JsonResult list(OperateOrderLog operateOrderLog) {
        List<OperateOrderLog> operateOrderLogList = operateOrderLogServiceExt.listByEntity(operateOrderLog);
        return sendSuccess(operateOrderLogList);
    }

    @GetMapping("/pageList")
    @ApiOperation(value="订单日志列表分页", notes="订单日志列表分页")
    public JsonResult pageList(@LoginedAuth @ApiIgnore UserSession session, OperateOrderLogQuery query) {
        if(session.getShopId() > 0) query.setShopId(session.getShopId());
        PageInfo<OperateOrderLog> page = operateOrderLogServiceExt.pageList(query);
        return sendSuccess(page);
    }

    @PostMapping("/insert")
    public JsonResult insert(@RequestBody OperateOrderLog operateOrderLog){
        operateOrderLogServiceExt.insert(operateOrderLog);
        return sendSuccess(operateOrderLog);
    }

    @PutMapping("/update")
    public JsonResult update(@RequestBody OperateOrderLog operateOrderLog){
        int result = operateOrderLogServiceExt.update(operateOrderLog);
        return sendSuccess(result);
    }

    @DeleteMapping("/delete")
    public JsonResult deleteOne(Long id){
        int result = operateOrderLogServiceExt.deleteById(id);
        return sendSuccess(result);
    }

    @DeleteMapping("/deleteBatch")
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
        if (idList.size()>0) result = operateOrderLogServiceExt.deleteByIds(idList);
        return sendSuccess(result);
    }

}