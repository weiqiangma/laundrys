package com.mawkun.core.base.data.query;

import com.mawkun.core.base.entity.PayFlow;
import lombok.Data;

import java.util.Date;

/**
 * @Date 2020/9/24 17:07
 * @Author mawkun
 */
@Data
public class PayFlowQuery extends PayFlow {
    private Integer pageNo;
    private Integer pageSize;
    private Date createTimeStart;
    private Date createTimeEnd;

    public void init() {
        if(pageNo == null) pageNo = 1;
        if(pageSize == null) pageSize = 20;
    }
}
