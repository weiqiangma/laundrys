package com.mawkun.core.base.data.query;

import com.mawkun.core.base.entity.GoodsOrder;
import lombok.Data;

@Data
public class OrderLogQuery extends GoodsOrder {
    private Long shopId;
    private String userName;
    private Integer pageNo;
    private Integer pageSize;

    public void init() {
        if(pageNo == null) pageNo = 1;
        if(pageSize == null) pageSize = 20;
    }
}
