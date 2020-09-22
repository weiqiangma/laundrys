package com.mawkun.admin.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.entity.MemberCard;
import com.mawkun.core.base.service.MemberCardService;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.xiaoleilu.hutool.convert.Convert;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 22:44:11
 */
@RestController
@RequestMapping("/MemberCard")
public class MemberCardController extends BaseController {
    
    @Autowired
    private MemberCardService memberCardService;

    @GetMapping("/get")
    public JsonResult getById(@LoginedAuth UserSession session, Long id) {
        MemberCard MemberCard = memberCardService.getById(id);
        return sendSuccess(MemberCard);
    }

    @GetMapping("/getByEntity")
    public JsonResult getByEntity(@LoginedAuth UserSession session, MemberCard MemberCard) {
        MemberCard resultCart = memberCardService.getByEntity(MemberCard);
        return sendSuccess(resultCart);
    }

    @GetMapping("/list")
    public JsonResult list(@LoginedAuth UserSession session, MemberCard MemberCard) {
        List<MemberCard> MemberCardList = memberCardService.listByEntity(MemberCard);
        return sendSuccess(MemberCardList);
    }

    @PostMapping("/insert")
    public JsonResult insert(@LoginedAuth UserSession session, MemberCard MemberCard){
        if(session.getShopId() > 0) return sendArgsError("子管理员无权删除商品分类");
        memberCardService.insert(MemberCard);
        return sendSuccess(MemberCard);
    }

    @PutMapping("/update")
    public JsonResult update(@LoginedAuth UserSession session, MemberCard MemberCard){
        if(session.getShopId() > 0) return sendArgsError("子管理员无权删除商品分类");
        int result = memberCardService.update(MemberCard);
        return sendSuccess(result);
    }

    @DeleteMapping("/delete")
    public JsonResult deleteOne(@LoginedAuth UserSession session, Long id){
        if(session.getShopId() > 0) return sendArgsError("子管理员无权删除商品分类");
        int result = memberCardService.deleteById(id);
        return sendSuccess(result);
    }

    @DeleteMapping("/deleteBatch")
    public JsonResult deleteBatch(@LoginedAuth UserSession session, String ids){
        int result = 0;
        if(session.getShopId() > 0) return sendArgsError("子管理员无权删除商品分类");
        List<String> idArray = Arrays.asList(ids.split(","));
        List idList = new ArrayList<>();
        idList = CollectionUtils.transform(idArray, new Transformer() {
            @Override
            public Object transform(Object o) {
                return Convert.toInt(o, 0);
            }
        });
        if (idList.size()>0) result = memberCardService.deleteByIds(idList);
        return sendSuccess(result);
    }

}