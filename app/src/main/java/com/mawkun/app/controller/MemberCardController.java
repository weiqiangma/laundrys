package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.mawkun.core.base.entity.MemberCard;
import com.mawkun.core.base.service.MemberCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 22:45:50
 */
@RestController
@Api(tags={"会员卡操作接口"})
@RequestMapping("/api/memberCard")
public class MemberCardController extends BaseController {
    
    @Autowired
    private MemberCardService memberCardService;

    @GetMapping("/get")
    @ApiOperation(value="会员卡详情", notes="会员卡详情")
    public JsonResult getById(Long id) {
        MemberCard memberCard = memberCardService.getById(id);
        return sendSuccess(memberCard);
    }

    @GetMapping("/getByEntity")
    @ApiOperation(value="会员卡详情", notes="会员卡详情")
    public JsonResult getByEntity(MemberCard memberCard) {
        MemberCard resultCart = memberCardService.getByEntity(memberCard);
        return sendSuccess(resultCart);
    }

    @GetMapping("/list")
    @ApiOperation(value="会员卡列表", notes="会员卡列表")
    public JsonResult list(MemberCard memberCard) {
        List<MemberCard> memberCardList = memberCardService.listByEntity(memberCard);
        return sendSuccess(memberCardList);
    }

    @PutMapping("/update")
    @ApiOperation(value="会员卡编辑", notes="会员卡编辑")
    public JsonResult update(MemberCard memberCard){
        int result = memberCardService.update(memberCard);
        return sendSuccess(result);
    }
}