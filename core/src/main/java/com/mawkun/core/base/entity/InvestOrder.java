package com.mawkun.core.base.entity;

import java.util.Date;
import lombok.Data;
                                                    import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (InvestOrder)实体类
 *
 * @author mawkun
 * @since 2020-09-24 15:28:43
 */
@Data 
public class InvestOrder {
    
    private Long id;
    
    private Long userId;
    
    private Long cartId;
    /**
    * 订单号
    */
    private String orderNo;
    /**
    * 用户名
    */
    private String userName;
    /**
    * 卡名称
    */
    private String cartName;
    
    private Long cartNum;
    /**
    * 充值金额
    */
    private Long investMoney;
    /**
    * 赠送金额
    */
    private Long giftMoney;
    /**
    * 到账金额
    */
    private Long amountMoney;
    /**
    * 账户余额
    */
    private Long residueMoney;
    /**
    * 状态
    */
    private Integer status;
    /**
    * 支付时间
    */
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date payTime;
    /**
    * 更新时间
    */
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date updateTime;
    /**
    * 创建时间
    */
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date createTime;

}