package com.mawkun.core.base.entity;

import java.util.Date;
import lombok.Data;
                                import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (OrderClothes)实体类
 *
 * @author mawkun
 * @date 2020-09-23 20:29:53
 */
@Data 
public class OrderClothes {
    
        private Long id;
    
        private Long orderFormId;
    
        private Long goodsId;
    
        private String goodsName;
    
        private String goodsPicture;
    
        private Integer goodsNum;
    
        private Long goodsPrice;
    
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

}