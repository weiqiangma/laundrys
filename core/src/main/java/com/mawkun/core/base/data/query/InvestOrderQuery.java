package com.mawkun.core.base.data.query;

import com.mawkun.core.base.entity.InvestOrder;
import lombok.Data;

import java.util.Date;

@Data
public class InvestOrderQuery extends InvestOrder {
    private String mobile;
    private Integer pageNo;
    private Integer pageSize;
    private Integer timeType;

    private Date createTimeStart;
    private Date createTimeEnd;
    private Date startTime;
    private Date endTime;

    public void init() {
        if(pageNo == null) pageNo = 1;
        if(pageSize == null) pageSize = 20;
    }
}
