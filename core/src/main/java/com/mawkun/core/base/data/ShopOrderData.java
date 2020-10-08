package com.mawkun.core.base.data;

import lombok.Data;

/**
 * @Date 2020/9/14 16:10
 * @Author mawkun
 */
@Data
public class ShopOrderData {
    private Long id;
    private Integer amount;
    private Integer fee;
    private String shopName;
    private String type;
}
