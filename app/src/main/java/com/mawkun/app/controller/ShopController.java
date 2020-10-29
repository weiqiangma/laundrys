package com.mawkun.app.controller;

import cn.pertech.common.utils.RequestUtils;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.controller.BaseController;
import com.mawkun.core.base.data.JsonResult;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.ShopQuery;
import com.mawkun.core.base.data.query.StateQuery;
import com.mawkun.core.base.data.vo.ShopVo;
import com.mawkun.core.base.entity.Shop;
import com.mawkun.core.service.ShopServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.xiaoleilu.hutool.convert.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mawkun
 * @date 2020-08-19 21:43:58
 */
@Slf4j
@RestController
@RequestMapping("/api/shop")
@Api(tags={"门店操作接口"})
public class ShopController extends BaseController {
    
    @Autowired
    private ShopServiceExt shopServiceExt;

    @GetMapping("/get")
    @ApiOperation(value="门店详情", notes="门店详情")
    public JsonResult getById(Long id) {
        Shop shop = shopServiceExt.getById(id);
        return sendSuccess(shop);
    }

    @GetMapping("/getByEntity")
    @ApiOperation(value="门店详情", notes="门店详情")
    public JsonResult getByEntity(Shop shop) {
        Shop resultShop = shopServiceExt.getByEntity(shop);
        return sendSuccess(resultShop);
    }


    @GetMapping("/list")
    @ApiOperation(value="门店列表分业", notes="门店列表分业")
    public JsonResult pageList(ShopQuery shopQuery) {
        PageInfo<ShopVo> page = shopServiceExt.pageByEntity(shopQuery);
        List<ShopVo> list = page.getList();
        list = list.stream().limit(6).collect(Collectors.toList());
        List<ShopVo> sortShopList = list.stream().sorted(Comparator.comparingInt(ShopVo::getLength)).collect(Collectors.toList());
        return sendSuccess(sortShopList);
    }

}