package com.mawkun.core.base.data.query;

import com.mawkun.core.base.entity.Shop;
import lombok.Data;

import java.util.Date;

@Data
public class ShopQuery extends Shop {
    private Integer pageNo;
    private Integer pageSize;
    private String address;
    private String lal;
    private Date createTimeStart;
    private Date createTimeEnd;

    public void init() {
        if(pageNo == null) pageNo = 1;
        if(pageSize == null) pageSize = 20;
    }
}
