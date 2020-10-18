package com.mawkun.core.base.entity;

import java.util.Date;
import lombok.Data;
                                                import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (Shop)实体类
 *
 * @author mawkun
 * @date 2020-10-17 15:54:37
 */
@Data 
public class Shop {
    
        private Long id;
    /**
    * 店铺名称
    */
        private String shopName;
    /**
    * 店铺地址
    */
        private String address;
    /**
    * 店铺图片
    */
        private String picture;
    /**
    * 经度
    */
        private String longitude;
    /**
    * 维度
    */
        private String latitude;
    /**
    * 坐标
    */
        private String location;
    /**
    * 等级
    */
        private Integer level;
    /**
    * 状态
    */
        private Integer status;
    /**
    * 经营者姓名
    */
        private String bossName;
    /**
    * 经营者联系方式
    */
        private String bossMobile;
    /**
    * 更新时间
    */
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;
    /**
    * 创建时间
    */
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

}