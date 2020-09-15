package com.mawkun.core.base.entity;

import java.util.Date;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (OrderForm)实体类
 *
 * @author mawkun
 * @date 2020-09-15 22:32:46
 */
@Data
public class OrderForm {

    private Long id;

    private Long userId;

    private Long shopId;

    private Long distributorId;

    private String userName;

    private String orderSerial;

    private Double price;

    private String remark;

    private Integer status;

    private Double realAmount;

    private Double totalAmount;
    /**
     * 用户地址
     */
    private String userAddress;
    /**
     * 配送方式
     */
    private Object transportWay;
    /**
     * 支付类型
     */
    private Object payKind;

    private Integer integral;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}