package com.mawkun.core.base.entity;

import lombok.Data;

/**
 * (UserAddress)实体类
 *
 * @author mawkun
 * @date 2020-09-13 18:34:09
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
     * 联系人
     */
    private String linkMan;
    /**
     * 联系手机号
     */
    private String linkMobile1;

    private String linkMobile2;
    /**
     * 地址编码
     */
    private String code;
    /**
     * 1：默认使用
     */
    private Object status;

}