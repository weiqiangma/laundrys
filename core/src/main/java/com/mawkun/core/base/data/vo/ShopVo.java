package com.mawkun.core.base.data.vo;

import com.mawkun.core.base.entity.Shop;
import lombok.Data;

@Data
public class ShopVo extends Shop {
    private String distributorIds;
    private Integer length;
    private String distance;
}
