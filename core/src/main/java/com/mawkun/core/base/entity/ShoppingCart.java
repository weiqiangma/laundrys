package com.mawkun.core.base.entity;

import java.util.Date;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (ShoppingCart)实体类
 *
 * @author mawkun
 * @since 2020-09-21 09:54:57
 */
@Data
public class ShoppingCart {

    private Long id;

    private Long goodsId;

    private Long kindId;

    private Long userId;
    /**
     * 购买数量
     */
    private Integer goodsNum;
    /**
     * 商品图片
     */
    private String goodsPic;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品价格
     */
    private Long goodsPrice;
    /**
     * 用户名
     */
    private String userName;

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