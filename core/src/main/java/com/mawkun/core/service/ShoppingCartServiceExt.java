package com.mawkun.core.service;

import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.NumberUtils;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.dao.ShoppingCartDao;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.GoodsQuery;
import com.mawkun.core.base.data.query.OrderFormQuery;
import com.mawkun.core.base.entity.*;
import com.mawkun.core.base.service.ShoppingCartService;
import com.mawkun.core.base.service.UserService;
import com.mawkun.core.dao.GoodsDaoExt;
import com.mawkun.core.dao.ShopDaoExt;
import com.mawkun.core.dao.ShoppingCartDaoExt;
import com.mawkun.core.dao.SysParamDaoExt;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.lang.Validator;
import com.xiaoleilu.hutool.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Validation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Date 2020/9/11 14:42
 * @Author mawkun
 */
@Slf4j
@Service
public class ShoppingCartServiceExt extends ShoppingCartService {
    @Autowired
    private ShoppingCartDaoExt shoppingCartDaoExt;
    @Autowired
    private SysParamDaoExt sysParamDaoExt;
    @Autowired
    private ShopDaoExt shopDaoExt;
    @Autowired
    private GoodsDaoExt goodsDaoExt;
    @Autowired
    private UserServiceExt userServiceExt;
    @Autowired
    private UserAddressServiceExt userAddressServiceExt;
    @Autowired
    private OrderFormServiceExt orderFormServiceExt;
    @Autowired
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

    public Long countTransportFee(Long shopId, Long addressId, Long amount) {
        UserAddress userAddress = userAddressServiceExt.getById(addressId);
        Shop shop = shopDaoExt.getById(shopId);
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

    @Transactional
    public JsonResult countOrderForm(UserSession session, OrderFormQuery query) {
        User user = userServiceExt.getById(session.getId());
        if(user == null) return new JsonResult().error("数据库中未查询到该用户信息,请联系管理员");
        List<ShoppingCart> cartList = findByUserId(user.getId());
        if(cartList.isEmpty()) return new JsonResult().error("购物车内无商品信息,请先添加");
        //计算购物车内商品总价格
        long resultAmount = 0;
        for(ShoppingCart cart : cartList) {
            //商品单价，商品数量计算该商品总价
            Long price = cart.getGoodsPrice();
            Integer goodsNum = cart.getGoodsNum();
            Long goodsAmount = price * goodsNum;
            resultAmount = goodsAmount + resultAmount;
        }
        if(resultAmount != query.getAmount()) return new JsonResult().error("所选商品价格和购物车内商品价格不一致,请重新添加");
        //判断用户是否使用余额支付，如果是减去对应积分
        if(query.getIntegral() != null && query.getIntegral() > 0) {
            Integer integral = user.getIntegral();
            //resultAmount = resultAmount - NumberUtil.div(integral, 100, 1);
        }
        if(query.getTransportFee() != null && query.getTransportFee() > 0) {
            resultAmount = resultAmount + query.getTransportFee();
        }
        //获取用户收货地址
        UserAddress address = userAddressServiceExt.getByIdAndUserId(query.getAddressId(), user.getId());
        if(address == null) return new JsonResult().error("未查询到该收获地址,请选择其它地址或重新添加");
        //生成待支付订单
        orderFormServiceExt.generateOrderForm(user, query, address, resultAmount);
        return new JsonResult().success("下单成功");
    }
}
