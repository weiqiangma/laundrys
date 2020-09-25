package com.mawkun.core.base.entity;

import java.util.Date;
import lombok.Data;
                                    import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (OrderLog)实体类
 *
 * @author mawkun
 * @since 2020-09-25 14:22:13
 */
@Data 
public class OrderLog {
    
    private Long id;
    /**
    * 用户id
    */
    private Long userId;
    /**
    * 订单id
    */
    private Long orderId;
    /**
    * 用户类别
    */
    private Integer userKind;
    /**
    * 用户名称
    */
    private String userName;
    /**
    * 操作
    */
    private String operate;
    /**
    * 描述
    */
    private String description;
    /**
    * 状态
    */
    private Integer status;
    /**
    * 创建时间
    */
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date createTime;

}