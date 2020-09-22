package com.mawkun.core.base.entity;

import java.util.Date;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (OperateOrderLog)实体类
 *
 * @author mawkun
 * @date 2020-09-02 21:13:38
 */
@Data
public class OperateOrderLog {

    private Long id;

    private Long userId;

    private Long orderFormId;

    private Integer userKind;

    private String operate;

    private String description;

    private Integer status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}