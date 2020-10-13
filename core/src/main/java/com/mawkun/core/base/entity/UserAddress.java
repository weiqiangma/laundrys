package com.mawkun.core.base.entity;

import java.util.Date;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (UserAddress)实体类
 *
 * @author mawkun
 * @date 2020-10-13 22:05:58
 */
@Data
public class UserAddress {

    private Long id;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 区域
     */
    private String area;
    /**
     * 微信定位地址
     */
    private String name;
    /**
     * 微信定位地址
     */
    private String address;
    /**
     * 详细地址
     */
    private String detail;
    /**
     * 全地址
     */
    private String exactAddress;
    /**
     * 联系人
     */
    private String linkMan;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 联系人电话
     */
    private String linkMobile1;

    private String linkMobile2;
    /**
     * 地理编码
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
     * 坐标对
     */
    private String location;
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