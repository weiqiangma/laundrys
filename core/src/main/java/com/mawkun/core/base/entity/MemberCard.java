package com.mawkun.core.base.entity;

import java.util.Date;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (MemberCard)实体类
 *
 * @author mawkun
 * @since 2020-09-21 09:24:41
 */
@Data
public class MemberCard {

    private Long id;
    /**
     * 卡名称
     */
    private String modelName;
    /**
     * 充值金额
     */
    private Long modelAmount;
    /**
     * 赠送金额
     */
    private Long modelGift;
    /**
     * 折扣
     */
    private String discount;
    /**
     * 描述
     */
    private String description;
    /**
     * 是否热销
     */
    private Integer ishot;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

}