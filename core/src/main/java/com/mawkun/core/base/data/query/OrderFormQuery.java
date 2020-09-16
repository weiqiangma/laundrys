package com.mawkun.core.base.data.query;

import com.mawkun.core.base.entity.OrderForm;
import lombok.Data;

import java.util.Date;

@Data
public class OrderFormQuery extends OrderForm {
    private Integer pageNo;
    private Integer pageSize;
    private String shopName;
    private Date createTimeStart;
    private Date createTimeEnd;

    private Double amount;
    private Integer integral;

    public void init() {
        if(pageNo == null) pageNo = 1;
        if(pageSize == null) pageSize = 20;
    }
}
