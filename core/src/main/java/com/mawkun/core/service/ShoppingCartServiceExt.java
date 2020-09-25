package com.mawkun.core.service;

import cn.pertech.common.utils.NumberUtils;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.GoodsQuery;
import com.mawkun.core.base.entity.*;
import com.mawkun.core.base.service.ShoppingCartService;
import com.mawkun.core.dao.GoodsDaoExt;
import com.mawkun.core.dao.ShopDaoExt;
import com.mawkun.core.dao.ShoppingCartDaoExt;
import com.mawkun.core.dao.SysParamDaoExt;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Date 2020/9/11 14:42
 * @Author mawkun
 */
@Slf4j
@Service
public class ShoppingCartServiceExt extends ShoppingCartService {
    @Resource
    private ShoppingCartDaoExt shoppingCartDaoExt;
    @Resource
    private SysParamDaoExt sysParamDaoExt;
    @Resource
    private GoodsDaoExt goodsDaoExt;
    @Resource
    private GaoDeApiServiceExt gaoDeApiServiceExt;

    /**
     * 编辑购物车信息
     * @param session
     * @param goodsId
     * @param goodsNum
     */
    public void save(UserSession session, Long goodsId, Integer goodsNum) {
        /**
         * 1.判断shopId不为空
         * 2.根据shopId查询数据库中是否存在该商品
         * 3.根据userId和shopId查询该商品是否已添加至该用户购物车
         * 如果该用户购物车中已存在，根据type判断商品数量+1或-1，如果不存在，添加商品至购物车
         */
        Validate.notNull(goodsId, "shopId不能为空");
        Validate.notNull(goodsNum, "商品数量不能为空");
        GoodsQuery query = new GoodsQuery();
        query.setId(goodsId);
        query.setStatus(Constant.GOODS_GROUNDING);
        Goods goods = goodsDaoExt.selectByTerms(query);
        Validate.notNull(goods, "数据库中未查询到该商品信息");
        ShoppingCart resultCart = shoppingCartDaoExt.selectByUserId(session.getId(), goodsId, null);
        if (resultCart != null) {
            if(goodsNum < 1) {
                shoppingCartDaoExt.deleteByEntity(resultCart);
            } else {
                resultCart.setGoodsNum(goodsNum);
                shoppingCartDaoExt.update(resultCart);
            }
        } else {
            ShoppingCart insertCart = new ShoppingCart();
            insertCart.setGoodsId(goods.getId());
            insertCart.setUserId(session.getId());
            insertCart.setKindId(goods.getKindId());
            insertCart.setGoodsNum(goodsNum);
            insertCart.setGoodsName(goods.getGoodsName());
            insertCart.setGoodsPic(goods.getPicture());
            insertCart.setGoodsPrice(goods.getPrice());
            insertCart.setUserName(session.getUserName());
            insertCart.setUpdateTime(new Date());
            insertCart.setCreateTime(new Date());
            shoppingCartDaoExt.insert(insertCart);
        }
    }

    public int deleteByUserId(Long userId) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(userId);
        return shoppingCartDaoExt.deleteByEntity(cart);
    }

    public List<ShoppingCart> findByUserId(Long userId) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(userId);
        return shoppingCartDaoExt.listByEntity(cart);
    }

    public Double getAmountByUserId(Long userId) {
        ShoppingCart cart = new ShoppingCart();
        cart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartDaoExt.listByEntity(cart);
        double result = 0;
        for(ShoppingCart resultCart : list) {
            Integer goodsNum = resultCart.getGoodsNum();
            Long goodsPrice = resultCart.getGoodsPrice();
            Long goodsAmount = goodsNum * goodsPrice;
            result = goodsAmount + result;
        }
        return result;
    }

    public Long countTransportFee(Shop shop, UserAddress userAddress, Long amount) {
        String location = userAddress.getLocation();
        String distanceStr = gaoDeApiServiceExt.getDistanceWithUserAndShop(location, shop.getLocation());
        Integer distance = NumberUtils.str2Int(distanceStr);
        List<SysParam> paramList = sysParamDaoExt.selectTransportFee();
        List<SysParam> sortList = paramList.stream().sorted(Comparator.comparingInt(SysParam::getDistance)).collect(Collectors.toList());
        long fee = 0;
        for(int i = 0; i < sortList.size() - 1; i++) {
            int front = sortList.get(i).getDistance() * 1000;
            int next = sortList.get(i+1).getDistance() * 1000;
            int max = sortList.get(paramList.size() -1).getDistance() * 1000;
            if(distance >= max) return (long) -1;
            if(distance >= front && distance < next) {
                long lowAmount = sortList.get(i).getLowAmount();
                if(amount >= lowAmount) {
                    return fee;
                }
                String feeStr = sortList.get(i).getSysValue();
                fee = NumberUtils.str2Int(feeStr);
                return fee;
            }
        }
        return (long) -1;
    }
}
