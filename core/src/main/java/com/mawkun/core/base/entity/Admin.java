package com.mawkun.core.base.entity;

import java.util.Date;
import lombok.Data;
                                    import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (Admin)实体类
 *
 * @author mawkun
 * @date 2020-09-25 22:03:40
 */
@Data 
public class Admin {
    
        private Long id;
    
        private Long shopId;
    /**
    * 用户名
    */
        private String userName;
    /**
    * 真实姓名
    */
        private String realName;
    /**
    * 密码
    */
        private String password;
    /**
    * 手机号
    */
        private String mobile;
    /**
    * 等级
    */
        private Integer level;
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