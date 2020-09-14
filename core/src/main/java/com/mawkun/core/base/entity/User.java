package com.mawkun.core.base.entity;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (User)实体类
 *
 * @author mawkun
 * @date 2020-09-12 14:04:31
 */
@Data
public class User {
    @ExcelProperty("ID")
    private Long id;
    /**
     * 微信用户唯一ID
     */
    @ExcelProperty("openId")
    private String openId;
    /**
     * 用户昵称
     */
    @ExcelProperty("用户名")
    private String userName;
    /**
     * 真实姓名
     */
    @ExcelProperty("realName")
    private String realName;
    /**
     * 头像
     */
    @ExcelProperty("avatartUrl")
    private String avatartUrl;
    /**
     * 联系方式
     */
    private String mobile;
    /**
     * 性别 0：未知 1：男 2：女
     */
    private Object gender;
    /**
     * 地址
     */
    private String address;
    /**
     * 类型
     */
    private Integer kind;
    /**
     * 余额
     */
    private Double sumOfMoney;
    /**
     * 积分
     */
    private Integer integral;
    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
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