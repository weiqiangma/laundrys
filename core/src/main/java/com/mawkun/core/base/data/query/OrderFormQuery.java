package com.mawkun.core.base.data.query;

import com.mawkun.core.base.entity.OrderForm;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderFormQuery extends OrderForm {
    private Integer pageNo;
    private Integer pageSize;
    private String shopName;
    private Integer useIntegral;
    private Date createTime;
    private Date createTimeStart;
    private Date createTimeEnd;

    private Long amount;
    private Integer integral;
    private List<Long> shopIdList;

    public void init() {
        if(pageNo == null) pageNo = 1;
        if(pageSize == null) pageSize = 20;
    }
}
