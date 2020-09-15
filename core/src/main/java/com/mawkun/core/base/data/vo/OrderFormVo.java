package com.mawkun.core.base.data.vo;

import com.mawkun.core.base.entity.OrderForm;
import lombok.Data;

import java.util.List;

@Data
public class OrderFormVo extends OrderForm {

    private String customerName;        //客户姓名
    private String distributorName;     //配送员姓名
    private String shopName;
    private Double amount;
    private List<GoodsVo> list;         //订单下商品
    private String type;
}
