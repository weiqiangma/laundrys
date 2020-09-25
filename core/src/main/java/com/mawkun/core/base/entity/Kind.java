package com.mawkun.core.base.entity;

import java.util.Date;
import lombok.Data;
                                import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (Kind)实体类
 *
 * @author mawkun
 * @since 2020-09-25 14:58:19
 */
@Data 
public class Kind {
    
    private Long id;
    /**
    * 分类名称
    */
    private String kindName;
    /**
    * 图表名称
    */
    private String iconName;
    /**
    * 分类描述
    */
    private String description;
    /**
    * 是否展示
    */
    private Integer showStatus;
    
    private Integer navStatus;
    /**
    * 排序
    */
    private Integer sort;
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