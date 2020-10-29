package com.mawkun.core.base.data.query;

import com.mawkun.core.base.entity.Shop;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ShopQuery extends Shop {
    private Integer pageNo;
    private Integer pageSize;
    private String userAddress;
    private Long addressId;
    private String lal;
    private List<Long> shopIdList;
    private String wxLongitude;
    private String wxLatitude;
    private Date createTimeStart;
    private Date createTimeEnd;

    public void init() {
        if(pageNo == null) pageNo = 1;
        if(pageSize == null) pageSize = 20;
    }
}
