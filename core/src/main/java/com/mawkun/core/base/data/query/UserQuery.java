package com.mawkun.core.base.data.query;

import com.mawkun.core.base.entity.User;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
public class UserQuery extends User {
    private Integer pageNo;
    private Integer pageSize;
    private Date createTimeStart;
    private Date createTimeEnd;

    public void init() {
        if(pageNo == null) pageNo = 1;
        if(pageSize == null) pageSize = 20;
    }
}
