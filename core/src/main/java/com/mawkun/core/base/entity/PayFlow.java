package com.mawkun.core.base.entity;

import java.util.Date;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (PayFlow)实体类
 *
 * @author mawkun
 * @since 2020-09-24 16:25:21
 */
@Data
public class PayFlow {

    private Long id;

    private Long userId;

    private String openId;

    private Long orderId;

    private String userName;

    private String orderNo;

    private Integer orderType;

    private Long totalFee;

    private String tradeState;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date payTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

}