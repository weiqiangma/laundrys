package com.mawkun.core.base.data.query;

import lombok.Data;

import java.util.Date;

@Data
public class StateQuery {
    private Long shopId;
    private Long distributorId;
    private Date startTime;
    private Date endTime;
    private String formatCode;
    private Integer type;
    private Integer status;
    private Integer transportWay;
    private int dateCount;
}
