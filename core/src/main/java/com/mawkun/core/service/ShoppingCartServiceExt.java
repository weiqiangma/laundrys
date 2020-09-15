package com.mawkun.core.service;

import cn.pertech.common.utils.NumberUtils;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.dao.ShoppingCartDao;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.GoodsQuery;
import com.mawkun.core.base.entity.Goods;
import com.mawkun.core.base.entity.ShoppingCart;
import com.mawkun.core.base.entity.SysParam;
import com.mawkun.core.base.service.ShoppingCartService;
import com.mawkun.core.dao.GoodsDaoExt;
import com.mawkun.core.dao.ShoppingCartDaoExt;
import com.mawkun.core.dao.SysParamDaoExt;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.lang.Validator;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    private GoodsDaoExt goodsDaoExt;
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

    /**
     * 计算购物车商品金额
     * @param goodsIds
     */
    public void countCartTotal(UserSession session, String goodsIds) {
        Validate.notEmpty(goodsIds, "请至少选择一项购物车内商品");
        List<String> idArray = Arrays.asList(goodsIds.split(","));
        List idList = new ArrayList<>();
        idList = CollectionUtils.transform(idArray, new Transformer() {
            @Override
            public Object transform(Object o) {
                return Convert.toInt(o, 0);
            }
        });
    }


    /**
     * 计算运费
     * @param address
     */
    public String countTransportFee(Integer goodsAmount, String address, String shopLocation) {
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
                String minGoodsAmount = paramList.get(i+1).getSysValue();
                fee = paramList.get(i+1).getSysValue();
            }
            if(distance < min) {
                fee = paramList.get(i).getSysValue();
            }
            if(distance >= max) log.info("配送距离超过范围");
        }
        return fee;
    }
}
