package com.mawkun.core.base.data.query;

import com.mawkun.core.base.entity.ShoppingCart;
import lombok.Data;

/**
 * @Date 2020/9/16 17:24
 * @Author mawkun
 */
@Data
public class ShoppingCartQuery extends ShoppingCart {

    private Integer integral;   //用户积分
    private Double transportFee;//运费
    private Double amount;      //购物车总金额
    private Long shopId;




}
