package com.mawkun.core.base.entity;

import java.util.Date;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (MemberCart)实体类
 *
 * @author mawkun
 * @date 2020-09-17 22:42:31
 */
@Data
public class MemberCart {

    private Long id;
    /**
     * 卡名称
     */
    private String modelName;
    /**
     * 充值金额
     */
    private Double modelAmount;
    /**
     * 赠送金额
     */
    private Double modelGift;
    /**
     * 折扣
     */
    private String discount;
    /**
     * 描述
     */
    private String description;
    /**
     * 状态
     */
    private Object status;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}