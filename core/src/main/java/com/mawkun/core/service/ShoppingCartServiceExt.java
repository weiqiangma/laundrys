package com.mawkun.core.service;

import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.dao.ShoppingCartDao;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.GoodsQuery;
import com.mawkun.core.base.entity.Goods;
import com.mawkun.core.base.entity.ShoppingCart;
import com.mawkun.core.base.service.ShoppingCartService;
import com.mawkun.core.dao.GoodsDaoExt;
import com.mawkun.core.dao.ShoppingCartDaoExt;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.lang.Validator;
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
@Service
public class ShoppingCartServiceExt extends ShoppingCartService {
    @Autowired
    private ShoppingCartDaoExt shoppingCartDaoExt;
    @Autowired
    private GoodsDaoExt goodsDaoExt;

    /**
     * 编辑购物车信息
     * @param session
     * @param cart
     * @param type
     */
    public void insert(UserSession session, ShoppingCart cart, Integer type) {
        /**
         * 1.判断shopId不为空
         * 2.根据shopId查询数据库中是否存在该商品
         * 3.根据userId和shopId查询该商品是否已添加至该用户购物车
         * 如果该用户购物车中已存在，根据type判断商品数量+1或-1，如果不存在，添加商品至购物车
         */
        Validate.notNull(cart.getGoodsId(), "shopId不能为空");
        GoodsQuery query = new GoodsQuery();
        query.setId(cart.getGoodsId());
        query.setStatus(Constant.GOODS_GROUNDING);
        Goods goods = goodsDaoExt.selectByTerms(query);
        Validate.notNull(goods, "数据库中未查询到该商品信息");
        ShoppingCart resultCart = shoppingCartDaoExt.selectByUserId(session.getId(), cart.getGoodsId());
        if (resultCart != null) {
            if(type == 1) resultCart.setGoodsNum(resultCart.getGoodsNum() + 1);
            if(type == 0) resultCart.setGoodsNum(resultCart.getGoodsNum() - 1);
        } else {
            ShoppingCart insertCart = ShoppingCart.builder().goodsId(goods.getId())
                    .userId(session.getId()).kindId(goods.getKindId()).goodsNum(1)
                    .goodsName(goods.getGoodsName()).goodsPic(goods.getPicture()).goodsPrice(goods.getPrice())
                    .userName(session.getUserName()).updateTime(new Date()).createTime(new Date()).build();
            shoppingCartDaoExt.insert(insertCart);
        }
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

}
