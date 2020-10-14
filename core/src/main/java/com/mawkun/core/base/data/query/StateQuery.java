package com.mawkun.core.base.data.query;

import lombok.Data;

import java.util.Date;

@Data
public class StateQuery {
    private Long shopId;
    private Long distributorId;
    private Integer orderType;  //订单类型
    private Integer timeType;   //时间类型
    private Integer transportWay;   //配送类型
    private Integer payKind;        //支付类型
    private Date startTime;
    private Date endTime;
    private String formatCode;
    private Integer type;
    private Integer status;
    private int dateCount;

    private Date createTimeStart;
    private Date createTimeEnd;

    private Integer pageNum;
    private Integer pageSize;
}
