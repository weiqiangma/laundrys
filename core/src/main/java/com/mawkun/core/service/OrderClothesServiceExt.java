package com.mawkun.core.service;

import com.mawkun.core.base.entity.Goods;
import com.mawkun.core.base.entity.OrderClothes;
import com.mawkun.core.base.entity.ShoppingCart;
import com.mawkun.core.base.service.OrderClothesService;
import com.mawkun.core.dao.GoodsDaoExt;
import com.mawkun.core.dao.OrderClothesDaoExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderClothesServiceExt extends OrderClothesService {
    @Autowired
    private GoodsDaoExt goodsDaoExt;
    @Autowired
    private OrderClothesDaoExt orderClothesDaoExt;

    public int addByShoppingCarts(List<ShoppingCart> list) {
        //订单中的商品加入订单商品表方便后续查询
        List<OrderClothes> orderClothesList = new ArrayList<>();
        for(ShoppingCart cart : list) {
            Goods goods = goodsDaoExt.getById(cart.getGoodsId());
            OrderClothes orderClothes = new OrderClothes();
            orderClothes.setGoodsId(goods.getId());
            orderClothes.setGoodsName(goods.getGoodsName());
            orderClothes.setGoodsNum(cart.getGoodsNum());
            orderClothes.setGoodsPicture(goods.getPicture());
            orderClothesList.add(orderClothes);
        }
        int num = orderClothesDaoExt.insertBatch(orderClothesList);
        if(list.size() == num) return 1;
        return 0;
    }

    public List<OrderClothes> getByOrderId(Long orderId) {
        OrderClothes clothes = new OrderClothes();
        clothes.setOrderFormId(orderId);
        return orderClothesDaoExt.listByEntity(clothes);
    }

}