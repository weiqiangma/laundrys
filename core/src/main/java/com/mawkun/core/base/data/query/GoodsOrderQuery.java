package com.mawkun.core.base.data.query;

import com.mawkun.core.base.entity.GoodsOrder;
import com.mawkun.core.base.entity.OrderClothes;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 */
@Data
public class GoodsOrderQuery extends GoodsOrder {
    private Integer pageNo;
    private Integer pageSize;
    private String shopName;
    private Integer useIntegral;
    private Integer sumOfMoney;
    private Date createTime;
    private Date createTimeStart;
    private Date createTimeEnd;

    private Integer type;   //配送员界面1:待接单，2：已结单
    private Integer orderType;  //订单类型
    private Integer timeType;
    private Long amount;
    private String distributorMobile;
    private String linkMobile1;
    private String linkMobile2;
    private Integer integral;
    private List<Long> shopIdList;

    public void init() {
        if(pageNo == null) {
            pageNo = 1;
        }
        if(pageSize == null) {
            pageSize = 5;
        }
    }
}
