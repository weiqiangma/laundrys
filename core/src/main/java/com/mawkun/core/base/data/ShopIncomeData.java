package com.mawkun.core.base.data;

import lombok.Data;

@Data
public class ShopIncomeData {
    //shop_id
    private Long id;
    //店铺名
    private String shopName;
    //店铺收入
    private Double amount;
}
