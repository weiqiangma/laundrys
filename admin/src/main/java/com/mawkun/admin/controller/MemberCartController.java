package com.mawkun.admin.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.entity.MemberCart;
import com.mawkun.core.base.service.MemberCartService;
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
@RequestMapping("/memberCart")
public class MemberCartController extends BaseController {
    
    @Autowired
    private MemberCartService memberCartService;

    @GetMapping("/get")
    public JsonResult getById(@LoginedAuth UserSession session, Long id) {
        MemberCart memberCart = memberCartService.getById(id);
        return sendSuccess(memberCart);
    }

    @GetMapping("/getByEntity")
    public JsonResult getByEntity(@LoginedAuth UserSession session, MemberCart memberCart) {
        MemberCart resultCart = memberCartService.getByEntity(memberCart);
        return sendSuccess(resultCart);
    }

    @GetMapping("/list")
    public JsonResult list(@LoginedAuth UserSession session, MemberCart memberCart) {
        List<MemberCart> memberCartList = memberCartService.listByEntity(memberCart);
        return sendSuccess(memberCartList);
    }

    @PostMapping("/insert")
    public JsonResult insert(@LoginedAuth UserSession session, MemberCart memberCart){
        if(session.getShopId() > 0) return sendArgsError("子管理员无权删除商品分类");
        memberCartService.insert(memberCart);
        return sendSuccess(memberCart);
    }

    @PutMapping("/update")
    public JsonResult update(@LoginedAuth UserSession session, MemberCart memberCart){
        if(session.getShopId() > 0) return sendArgsError("子管理员无权删除商品分类");
        int result = memberCartService.update(memberCart);
        return sendSuccess(result);
    }

    @DeleteMapping("/delete")
    public JsonResult deleteOne(@LoginedAuth UserSession session, Long id){
        if(session.getShopId() > 0) return sendArgsError("子管理员无权删除商品分类");
        int result = memberCartService.deleteById(id);
        return sendSuccess(result);
    }

    @DeleteMapping("/delete")
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
        if (idList.size()>0) result = memberCartService.deleteByIds(idList);
        return sendSuccess(result);
    }

}