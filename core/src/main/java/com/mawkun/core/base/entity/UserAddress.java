package com.mawkun.core.base.entity;

import lombok.Data;

/**
 * (UserAddress)实体类
 *
 * @author mawkun
 * @since 2020-09-18 14:20:43
 */
@Data
public class UserAddress {

    private Long id;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 省份
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String area;
    /**
     * 街道
     */
    private String street;
    /**
     * 详细地址
     */
    private String detail;
    /**
     * 精确地址
     */
    private String exactAddress;
    /**
     * 联系人
     */
    private String linkMan;
    /**
     * 联系手机号1
     */
    private String linkMobile1;
    /**
     * 联系手机号2
     */
    private String linkMobile2;
    /**
     * 地址编码
     */
    private String code;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 维度
     */
    private String latidute;
    /**
     * 经纬度坐标
     */
    private String location;
    /**
     * 1：默认使用
     */
    private Integer status;

}