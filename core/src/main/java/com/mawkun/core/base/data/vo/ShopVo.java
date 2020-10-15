package com.mawkun.core.base.data.vo;

import com.mawkun.core.base.entity.Admin;
import com.mawkun.core.base.entity.Shop;
import lombok.Data;

import java.util.List;

@Data
public class ShopVo extends Shop {
    private String distributorIds;
    private List<Admin> adminList;
    private Integer length;
    private String distance;
}
