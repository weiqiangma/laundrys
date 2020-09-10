package com.mawkun.core.base.entity;

import java.util.Date;
import lombok.Data;
                                            import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (User)实体类
 *
 * @author mawkun
 * @since 2020-09-10 11:33:36
 */
@Data 
public class User {
    
    private Long id;
    /**
    * 微信用户唯一ID
    */
    private String openId;
    /**
    * 用户昵称
    */
    private String userName;
    /**
    * 真实姓名
    */
    private String realName;
    /**
    * 联系方式
    */
    private String mobile;
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