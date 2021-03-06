package com.mawkun.core.base.data.query;

import com.mawkun.core.base.entity.Kind;
import lombok.Data;

import java.util.Date;

@Data
public class KindQuery extends Kind {

    private Integer pageNo;
    private Integer pageSize;
    private Date createTimeStart;
    private Date createTimeEnd;

    public void init() {
        if(pageNo == null) pageNo = 1;
        if(pageSize == null) pageSize = 20;
    }
}
