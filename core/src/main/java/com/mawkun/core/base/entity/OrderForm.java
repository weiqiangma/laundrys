package com.mawkun.core.base.entity;

import java.util.Date;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (OrderForm)实体类
 *
 * @author mawkun
 * @date 2020-08-22 15:34:11
 */
@Data
public class OrderForm {

    private Long id;

    private Long userId;

    private Long shopId;

    private Long distributorId;

    private String orderSerial;

    private Double price;

    private String remark;

    private Integer status;

    private Double realAmount;

    private Double totalAmount;

    private Integer transportWay;

    private Integer integral;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}