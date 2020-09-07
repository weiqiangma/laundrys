package com.mawkun.core.base.data.query;

import lombok.Data;

import java.util.Date;

@Data
public class ShopIncomeQuery {
    private Long shopId;
    private Date startTime;
    private Date endTime;
}
