package com.mawkun.admin.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.GoodsQuery;
import com.mawkun.core.base.entity.Goods;
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
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-19 21:43:02
 */
@RestController
@RequestMapping("/adm/goods")
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
    public JsonResult list(Goods goods) {
        List<Goods> goodsList = goodsServiceExt.listByEntity(goods);
        return sendSuccess(goodsList);
    }

    @GetMapping("/pageList")
    @ApiOperation(value="商品列表分页", notes="商品列表分业务")
    public JsonResult pageList(GoodsQuery query) {
        PageInfo page = goodsServiceExt.pageByEntity(query);
        return sendSuccess(page);
    }

    @PostMapping("/insert")
    @ApiOperation(value="添加商品", notes="添加商品")
    public JsonResult insert(@LoginedAuth @ApiIgnore UserSession session, Goods goods, MultipartFile file){
        if(session.getShopId() > 0) return sendArgsError("子管理员无权添加商品");
        if(file.isEmpty() || goods.getGoodsName() == null) return sendError("缺少参数");
        List<Goods> goodsList = goodsServiceExt.getByName(goods.getGoodsName());
        if(!goodsList.isEmpty()) return sendError("该商品名称已存在，请勿重复添加");
        goodsServiceExt.insertWithPic(goods, file);
        return sendSuccess(goods);
    }

    @PutMapping("/update")
    @ApiOperation(value="编辑商品", notes="编辑商品")
    public JsonResult update(@LoginedAuth @ApiIgnore UserSession session,Goods goods, MultipartFile file){
        if(session.getShopId() > 0) return sendArgsError("子管理员无权编辑商品");
        List<Goods> goodsList = goodsServiceExt.getByName(goods.getGoodsName());
        if(goodsList.size() > 1) return sendError("该商品名称已存在，请勿重复添加");
        int result = goodsServiceExt.updateWithPic(goods, file);
        return sendSuccess(result);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value="删除商品", notes="删除商品")
    public JsonResult deleteOne(@LoginedAuth @ApiIgnore UserSession session,Long id){
        if(session.getShopId() > 0) return sendArgsError("子管理员无权删除商品");
        int result = goodsServiceExt.deleteById(id);
        return sendSuccess(result);
    }

    @DeleteMapping("/deleteBatch")
    @ApiOperation(value="批量删除商品", notes="批量删除商品")
    public JsonResult deleteBatch(@LoginedAuth @ApiIgnore UserSession session,String ids){
        int result = 0;
        if(session.getShopId() > 0) return sendArgsError("子管理员无权删除商品");
        List<String> idArray = Arrays.asList(ids.split(","));
        List idList = new ArrayList<>();
        idList = CollectionUtils.transform(idArray, new Transformer() {
            @Override
            public Object transform(Object o) {
                return Convert.toInt(o, 0);
            }
        });
        if (idList.size() > 0) result = goodsServiceExt.deleteByIds(idList);
        return sendSuccess(result);
    }
}