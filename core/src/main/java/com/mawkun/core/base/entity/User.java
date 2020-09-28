package com.mawkun.core.base.entity;

import java.util.Date;
import lombok.Data;
                                                                import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (User)实体类
 *
 * @author mawkun
 * @date 2020-09-28 22:38:47
 */
@Data 
public class User {
    
        private Long id;
    /**
    * openId
    */
        private String openId;
    /**
    * 用户名
    */
        private String userName;
    /**
    * 真名
    */
        private String realName;
    /**
    * 头像
    */
        private String avatarUrl;
    /**
    * 手机
    */
        private String mobile;
    /**
    * 性别
    */
        private Integer gender;
    /**
    * 地址
    */
        private String address;
    /**
    * 种类
    */
        private Integer kind;
    /**
    * 余额
    */
        private Long sumOfMoney;
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
        private Integer status;
    /**
    * 更新时间
    */
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
    * 创建时间
    */
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}