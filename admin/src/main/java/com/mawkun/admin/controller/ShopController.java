package com.mawkun.admin.controller;

import cn.pertech.common.utils.RequestUtils;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.controller.BaseController;
import com.mawkun.core.base.data.JsonResult;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.ShopQuery;
import com.mawkun.core.base.data.query.StateQuery;
import com.mawkun.core.base.data.vo.ShopUserVo;
import com.mawkun.core.base.data.vo.ShopVo;
import com.mawkun.core.base.entity.Admin;
import com.mawkun.core.base.entity.Shop;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.dao.ShopUserDaoExt;
import com.mawkun.core.service.AdminServiceExt;
import com.mawkun.core.service.GoodsOrderServiceExt;
import com.mawkun.core.service.ShopServiceExt;
import com.mawkun.core.service.UserServiceExt;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mawkun
 * @date 2020-08-19 21:43:58
 */
@Slf4j
@RestController
@RequestMapping("/adm/shop")
@Api(tags={"门店操作接口"})
public class ShopController extends BaseController {
    
    @Autowired
    private ShopServiceExt shopServiceExt;
    @Autowired
    private ShopUserDaoExt shopUserDaoExt;
    @Resource
    private UserServiceExt userServiceExt;
    @Resource
    private AdminServiceExt adminServiceExt;
    @Autowired
    private GoodsOrderServiceExt goodsOrderServiceExt;

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
    @ApiOperation(value="门店列表", notes="门店列表")
    public JsonResult list(Shop shop) {
        List<Shop> shopList = shopServiceExt.listByEntity(shop);
        return sendSuccess(shopList);
    }

    @GetMapping("/pageList")
    @ApiOperation(value="门店列表分业", notes="门店列表分业")
    public JsonResult pageList(@LoginedAuth @ApiIgnore UserSession session, ShopQuery shopQuery) {
        if(session.getLevel() == Constant.ADMIN_TYPE_COMMON) shopQuery.setId(session.getShopId());
        if(session.getLevel() == Constant.ADMIN_TYPE_DISTRIBUTOR) {

            User user = userServiceExt.getByMobile(session.getMobile());
            if(user == null) return sendArgsError("未查询到该配送员信息");
            List<ShopUserVo> list = shopUserDaoExt.selectShopNameByUserId(user.getId());
            List<Long> shopIdList = list.stream().map(ShopUserVo::getShopId).collect(Collectors.toList());
            shopQuery.setShopIdList(shopIdList);
        }
        PageInfo<ShopVo> page = shopServiceExt.pageByEntity(shopQuery);
        return sendSuccess(page);
    }

    @PostMapping("/insert")
    @ApiOperation(value="添加门店", notes="添加门店")
    public JsonResult insert(@LoginedAuth @ApiIgnore UserSession session,Shop shop, MultipartFile[] files){
        if(session.getLevel() > 0) return sendArgsError("子管理员无权添加门店");
        if(files.length == 0) return sendError("请上传门店图片");
        List shopList = shopServiceExt.getByName(shop.getShopName());
        if(!shopList.isEmpty()) return sendError("该门店名称已存在，不能重复添加");
        //Shop resultShop = shopServiceExt.getFirstLevelShop();
        if(shop.getLevel() != null && shop.getLevel() == Constant.SHOP_LEVEL_FIRST) {
            List<Shop> resultShop = shopServiceExt.getShopByStatus(shop.getStatus());
            if(resultShop != null && resultShop.size() > 0) return sendArgsError("总店已存在，请勿重复添加");
        }
        int result = shopServiceExt.insertWithPic(shop, files);
        return sendSuccess(result);
    }

    @PutMapping("/update")
    @ApiOperation(value="编辑门店", notes="编辑门店")
    public JsonResult update(@LoginedAuth @ApiIgnore UserSession session, Shop shop, MultipartFile[] files){
        if(session.getLevel() > 0) return sendArgsError("子管理员无权编辑门店");
        List shopList = shopServiceExt.getByName(shop.getShopName());
        if(shopList.size() > 1) return sendError("该门店名称已存在不能重复添加");
        if(shop.getLevel() != null && shop.getLevel() == 0) {
            Shop resultShop = shopServiceExt.getById(shop.getId());
            if(resultShop.getLevel() == Constant.SHOP_LEVEL_SECOND && shop.getLevel() == Constant.SHOP_LEVEL_FIRST) {
                return sendArgsError("总店已存在，请勿重复添加");
            }
        }
        int result = shopServiceExt.updateWithPic(shop, files);
        return sendSuccess(result);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value="删除门店", notes="删除门店")
    public JsonResult deleteOne(@LoginedAuth @ApiIgnore UserSession session,Long id){
        if(session.getLevel() > 0) return sendArgsError("子管理员无权删除门店");
        int result = shopServiceExt.deleteById(id);
        return sendSuccess(result);
    }

    @DeleteMapping("/deleteBatch")
    @ApiOperation(value="批量删除门店", notes="批量删除门店")
    public JsonResult deleteBatch(@LoginedAuth @ApiIgnore UserSession session, String ids){
        int result = 0;
        if(session.getLevel() > 0) return sendArgsError("子管理员无权删除门店");
        List<String> idArray = Arrays.asList(ids.split(","));
        List idList = new ArrayList<>();
        idList = CollectionUtils.transform(idArray, new Transformer() {
            @Override
            public Object transform(Object o) {
                return Convert.toInt(o, 0);
            }
        });
        if (idList.size()>0) result = shopServiceExt.deleteByIds(idList);
        return sendSuccess(result);
    }

    /**
     * 统计门店收入
     * @return
     */
    @GetMapping("/statsShopIncome")
    @ApiOperation(value="统计门店收入", notes="统计门店收入")
    public JsonResult statsShopIncome(@LoginedAuth UserSession session) {
        StateQuery query = this.createQueryStateVo();
        //query.setStatus(Constant.SELF_ORDER_SURE_TAKE);
        if(session.getLevel() > 0) query.setShopId(session.getShopId());
        JSONArray array = shopServiceExt.statsShopIncome(query);
        return sendSuccess(array);
    }

    /**
     * 统计门店订单
     * @param session
     * @return
     */
    @GetMapping("/statsShopOrder")
    public JsonResult statsShopOrder(@LoginedAuth UserSession session) {
        StateQuery query = this.createQueryStateVo();
        if(session.getLevel() > 0) query.setShopId(session.getShopId());
        //query.setStatus(Constant.SELF_ORDER_SURE_TAKE);
        JSONArray array = shopServiceExt.statsShopOrder(query);
        return sendSuccess(array);
    }

    @GetMapping("/statsDistributorOrder")
    @ApiOperation(value="配送员订单统计", notes="配送员订单统计")
    public JsonResult statsDistributorOrder(@LoginedAuth UserSession session) {
        StateQuery query = this.createQueryStateVo();
        Admin admin = adminServiceExt.getById(session.getId());
        if(admin == null || admin.getMobile() == null) return sendArgsError("查询不到该配送员信息");
        User user = userServiceExt.getByMobile(admin.getMobile());
        query.setDistributorId(user.getId());
        //query.setStatus(Constant.SELF_ORDER_SURE_TAKE);
        //query.setTransportWay(Constant.ORDER_DELIVERY_GET);
        JSONArray array = goodsOrderServiceExt.statsDistributorOrder(query);
        return sendSuccess(array);
    }

    //查询统计对象(首页统计)
    private StateQuery createQueryStateVo(){
        int type = getIntPar("type",1);
        HttpServletRequest request = getRequest();
        Date start = null;
        Date end = null;
        Integer shopId = RequestUtils.getIntPar(request, "shopId", 0);
        try{
            start = RequestUtils.getDatePar(request,"createTimeStart","yyyy-MM-dd");
            end = RequestUtils.getDatePar(request,"createTimeEnd","yyyy-MM-dd");
        }catch (Exception e){
            log.error("开始和结束错误时间格式错误");
        }
        StateQuery queryVO = new StateQuery();
        queryVO.setShopId(shopId.longValue());
        if(start!=null && end!=null){
            type = 4 ;
            queryVO.setStartTime(start);
            queryVO.setEndTime(end);
        }
        queryVO.setType(type);
        return queryVO;
    }

}