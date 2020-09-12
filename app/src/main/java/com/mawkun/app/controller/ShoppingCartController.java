package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.entity.ShoppingCart;
import com.mawkun.core.base.service.ShoppingCartService;
import com.mawkun.core.service.ShoppingCartServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.xiaoleilu.hutool.convert.Convert;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Validation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags={"购物车操作接口"})
@RequestMapping("/api/shoppingCart")
public class ShoppingCartController extends BaseController {
    
    @Autowired
    private ShoppingCartServiceExt shoppingCartServiceExt;

    @GetMapping("/get")
    public JsonResult getById(Long id) {
        ShoppingCart shoppingCart = shoppingCartServiceExt.getById(id);
        return sendSuccess(shoppingCart);
    }

    @GetMapping("/getByEntity")
    public JsonResult getByEntity(ShoppingCart shoppingCart) {
        ShoppingCart cart = shoppingCartServiceExt.getByEntity(shoppingCart);
        return sendSuccess(cart);
    }

    @GetMapping("/list")
    public JsonResult list(ShoppingCart shoppingCart) {
        List<ShoppingCart> shoppingCartList = shoppingCartServiceExt.listByEntity(shoppingCart);
        List<ShoppingCart> resutlList = shoppingCartList.stream().filter(a -> a.getStatus() == Constant.GOODS_UNDERCARRIAGE).collect(Collectors.toList());
        return sendSuccess(resutlList);
    }

    @PostMapping("/insert")
    public JsonResult insert(@LoginedAuth UserSession session, Long goodsId, Integer type){
        Assert.notNull(goodsId);
        shoppingCartServiceExt.save(session, goodsId, type);
        return sendSuccess("ok");
    }

    @PutMapping("/update")
    public JsonResult update(ShoppingCart shoppingCart){
        int result = shoppingCartServiceExt.update(shoppingCart);
        return sendSuccess(result);
    }

    @DeleteMapping("/delete")
    public JsonResult deleteOne(Long id){
        int result = shoppingCartServiceExt.deleteById(id);
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
        if (idList.size()>0) result = shoppingCartServiceExt.deleteByIds(idList);
        return sendSuccess(result);
    }

}