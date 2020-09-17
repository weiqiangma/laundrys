package com.mawkun.core.base.entity;

import java.util.Date;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (InvestLog)实体类
 *
 * @author mawkun
 * @date 2020-09-17 23:04:26
 */
@Data
public class InvestLog {

    private Long id;

    private Long userId;

    private Long cartId;

    private String userName;

    private String cartName;

    private Long cartNum;

    private Double investMoney;

    private Double giftMoney;

    private Double residueMoney;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}