package com.mawkun.core.base.entity;

import java.util.Date;
import lombok.Data;
                                                                import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (OrderForm)实体类
 *
 * @author mawkun
 * @since 2020-09-24 13:47:46
 */
@Data 
public class OrderForm {
    
    private Long id;
    
    private Long userId;
    
    private Long shopId;
    
    private Long distributorId;
    
    private Long addressId;
    
    private String userName;
    
    private String orderSerial;
    
    private String remark;
    
    private Integer status;
    
    private Long realAmount;
    
    private Long totalAmount;
    /**
    * 用户地址
    */
    private String userAddress;
    /**
    * 运费
    */
    private Long transportFee;
    /**
    * 配送方式
    */
    private Integer transportWay;
    /**
    * 支付类型
    */
    private Integer payKind;
    
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date payTime;
    
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date updateTime;
    
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date createTime;

}