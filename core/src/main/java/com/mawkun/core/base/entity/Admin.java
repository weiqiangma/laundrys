package com.mawkun.core.base.entity;

import java.util.Date;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (Admin)实体类
 *
 * @author mawkun
 * @date 2020-09-02 20:31:19
 */
@Data
public class Admin {

    private Long id;

    private Long shopId;

    private String userName;

    private String realName;

    private String password;

    private String phone;

    private Integer level;

    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}