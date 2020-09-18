package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.mawkun.core.base.entity.MemberCart;
import com.mawkun.core.base.service.MemberCartService;
import com.xiaoleilu.hutool.convert.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 22:45:50
 */
@RestController
@Api(tags={"会员卡操作接口"})
@RequestMapping("/memberCart")
public class MemberCartController extends BaseController {
    
    @Autowired
    private MemberCartService memberCartService;

    @GetMapping("/get")
    @ApiOperation(value="会员卡详情", notes="会员卡详情")
    public JsonResult getById(Long id) {
        MemberCart memberCart = memberCartService.getById(id);
        return sendSuccess(memberCart);
    }

    @GetMapping("/getByEntity")
    @ApiOperation(value="会员卡详情", notes="会员卡详情")
    public JsonResult getByEntity(MemberCart memberCart) {
        MemberCart resultCart = memberCartService.getByEntity(memberCart);
        return sendSuccess(resultCart);
    }

    @GetMapping("/list")
    @ApiOperation(value="会员卡列表", notes="会员卡列表")
    public JsonResult list(MemberCart memberCart) {
        List<MemberCart> memberCartList = memberCartService.listByEntity(memberCart);
        return sendSuccess(memberCartList);
    }

    @PutMapping("/update")
    @ApiOperation(value="会员卡编辑", notes="会员卡编辑")
    public JsonResult update(MemberCart memberCart){
        int result = memberCartService.update(memberCart);
        return sendSuccess(result);
    }
}