package com.mawkun.core.base.entity;

import java.util.Date;
import lombok.Data;
                                import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (OrderClothes)实体类
 *
 * @author mawkun
 * @date 2020-09-26 15:32:04
 */
@Data 
public class OrderClothes {
    
        private Long id;
    /**
    * 订单id
    */
        private Long orderId;
    /**
    * 商品id
    */
        private Long goodsId;
    /**
    * 商品名称
    */
        private String goodsName;
    /**
    * 商品图片
    */
        private String goodsPicture;
    /**
    * 商品数量
    */
        private Integer goodsNum;
    /**
    * 商品单价
    */
        private Long goodsPrice;
    /**
    * 创建时间
    */
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}