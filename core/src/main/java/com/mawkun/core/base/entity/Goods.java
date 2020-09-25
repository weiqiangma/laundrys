package com.mawkun.core.base.entity;

import java.util.Date;
import lombok.Data;
                                    import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (Goods)实体类
 *
 * @author mawkun
 * @since 2020-09-25 14:58:45
 */
@Data 
public class Goods {
    
    private Long id;
    /**
    * 种类id
    */
    private Long kindId;
    /**
    * 商品名称
    */
    private String goodsName;
    /**
    * 商品价格
    */
    private Long price;
    /**
    * 商品描述
    */
    private String description;
    /**
    * 图片
    */
    private String picture;
    /**
    * 状态
    */
    private Integer status;
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