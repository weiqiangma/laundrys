package com.mawkun.core.base.entity;

import java.util.Date;
import lombok.Data;
                                import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (Goods)实体类
 *
 * @author mawkun
 * @since 2020-09-21 09:23:54
 */
@Data 
public class Goods {
    
    private Long id;
    
    private Long kindId;
    
    private String goodsName;
    
    private Long price;
    
    private String description;
    
    private String picture;
    
    private Integer status;
    
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date updateTime;
    
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date createTime;

}