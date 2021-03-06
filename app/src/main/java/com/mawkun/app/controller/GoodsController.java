package com.mawkun.app.controller;

import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.controller.BaseController;
import com.mawkun.core.base.data.JsonResult;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.GoodsQuery;
import com.mawkun.core.base.data.vo.GoodsVo;
import com.mawkun.core.base.entity.Goods;
import com.mawkun.core.base.entity.SysParam;
import com.mawkun.core.service.GoodsServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.xiaoleilu.hutool.convert.Convert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mawkun
 * @date 2020-08-19 21:43:02
 */
@RestController
@RequestMapping("/api/goods")
@Api(tags={"商品操作接口"})
public class GoodsController extends BaseController {
    
    @Autowired
    private GoodsServiceExt goodsServiceExt;

    @GetMapping("/get")
    @ApiOperation(value="商品详情", notes="商品详情")
    @ApiImplicitParam(name = "id", value = "商品ID", dataType = "Long", paramType = "header")
    public JsonResult getById(Long id) {
        Goods goods = goodsServiceExt.getById(id);
        return sendSuccess(goods);
    }

    @GetMapping("/getByEntity")
    @ApiOperation(value="商品详情", notes="商品详情")
    public JsonResult getByEntity(Goods goods) {
        Goods resultGoods =  goodsServiceExt.getByEntity(goods);
        return sendSuccess(resultGoods);
    }

    @GetMapping("/list")
    @ApiOperation(value="商品列表", notes="商品列表")
    public JsonResult list(GoodsQuery query) {
        //List<Goods> goodsList = goodsServiceExt.listByEntity(goods);
        List<GoodsVo> goodsList =  goodsServiceExt.selectList(query);
        List<Goods> resutlList = goodsList.stream().filter(a -> a.getStatus() == Constant.GOODS_GROUNDING).collect(Collectors.toList());
        List<Goods> sortList = resutlList.stream().sorted(Comparator.comparingInt(Goods::getSort).reversed()).collect(Collectors.toList());
        return sendSuccess(sortList);
    }

    @GetMapping("/pageList")
    @ApiOperation(value="商品列表分页", notes="商品列表分业务")
    public JsonResult pageList(GoodsQuery query) {
        PageInfo page = goodsServiceExt.pageByEntity(query);
        return sendSuccess(page);
    }
}