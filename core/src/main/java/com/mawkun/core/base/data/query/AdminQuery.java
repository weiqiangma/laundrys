package com.mawkun.core.base.data.query;

import com.mawkun.core.base.entity.Admin;
import lombok.Data;

import java.util.Date;

@Data
public class AdminQuery extends Admin {
    private Integer pageNo;
    private Integer pageSize;
    private Date createTimeStart;
    private Date createTimeEnd;

    public void init() {
        if(pageNo == null) pageNo = 1;
        if(pageSize == null) pageSize = 20;
    }
}
