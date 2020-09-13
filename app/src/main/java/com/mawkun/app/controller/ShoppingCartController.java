package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.NumberUtils;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.entity.ShoppingCart;
import com.mawkun.core.base.entity.SysParam;
import com.mawkun.core.base.service.ShoppingCartService;
import com.mawkun.core.dao.SysParamDaoExt;
import com.mawkun.core.service.GaoDeApiServiceExt;
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
    @Autowired
    private GaoDeApiServiceExt gaoDeApiServiceExt;
    @Autowired
    private SysParamDaoExt sysParamDaoExt;

    @GetMapping("/get")
    public JsonResult getById(Long id) {
        ShoppingCart shoppingCart = shoppingCartServiceExt.getById(id);
        return sendSuccess(shoppingCart);
    }

    @GetMapping("/getByEntity")
    public JsonResult getByEntity(@LoginedAuth UserSession session, ShoppingCart shoppingCart) {
        if(session.getId() > 0) shoppingCart.setUserId(session.getId());
        ShoppingCart cart = shoppingCartServiceExt.getByEntity(shoppingCart);
        return sendSuccess(cart);
    }

    @GetMapping("/list")
    public JsonResult list(@LoginedAuth UserSession session, ShoppingCart shoppingCart) {
        if(session.getId() > 0) shoppingCart.setUserId(session.getId());
        List<ShoppingCart> shoppingCartList = shoppingCartServiceExt.listByEntity(shoppingCart);
        //List<ShoppingCart> resutlList = shoppingCartList.stream().filter(a -> a.getStatus() == Constant.GOODS_UNDERCARRIAGE).collect(Collectors.toList());
        return sendSuccess(shoppingCartList);
    }

    @PostMapping("/insert")
    public JsonResult insert(@LoginedAuth UserSession session, Long goodsId, Integer goodsNum){
        Assert.notNull(goodsId);
        shoppingCartServiceExt.save(session, goodsId, goodsNum);
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

    /**
     * 计算运费
     * @param address
     */
    @GetMapping("/countTransportFee")
    public String countTransportFee(String address, String shopLocation) {
        /**
         * 1.根据用户收货地址及门店坐标计算距离
         * 2.根据距离计算运费
         */
        String fee = "";
        String location = gaoDeApiServiceExt.getLalByAddress(address);
        String distanceStr = gaoDeApiServiceExt.getDistanceWithUserAndShop(location, shopLocation);
        Integer distance = NumberUtils.str2Int(distanceStr);
        List<SysParam> paramList = sysParamDaoExt.selectTransportFee();
        for(int i = 0; i < paramList.size(); i++) {
            int min = paramList.get(i).getDistance();
            int max = paramList.get(i+1).getDistance();
            if(distance >= min && distance < max) {
                fee = paramList.get(i).getSysValue();
            }
            if(distance >= max) sendSuccess("ok", "所选地址附近洗衣店正在建设中，请耐心等待");
        }
        return fee;
    }

}