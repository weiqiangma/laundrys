package com.mawkun.core.base.data.vo;

import com.mawkun.core.base.entity.GoodsOrder;
import com.mawkun.core.base.entity.OrderClothes;
import lombok.Data;

import java.util.List;

@Data
public class GoodsOrderVo extends GoodsOrder {

    private String customerName;        //客户姓名
    private String mobile;
    private String distributorName;     //配送员姓名
    private String shopName;
    private Double amount;
    private List<OrderClothes> list;         //订单下商品
    private String type;
}
