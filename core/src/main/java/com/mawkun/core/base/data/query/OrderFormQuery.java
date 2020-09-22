package com.mawkun.core.base.data.query;

import com.mawkun.core.base.entity.OrderForm;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderFormQuery extends OrderForm {
    private Integer pageNo;
    private Integer pageSize;
    private String shopName;        //店名
    private Integer useIntegral;    //用户积分
    private Date createTime;        //创建时间
    private Date createTimeStart;   //开始时间
    private Date createTimeEnd;     //结束时间

    private Long amount;            //购物车商品总金额
    private Integer integral;       //积分
    private List<Long> shopIdList;  //店铺ids

    public void init() {
        if(pageNo == null) pageNo = 1;
        if(pageSize == null) pageSize = 20;
    }
}
