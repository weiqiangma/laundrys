package com.mawkun.core.base.entity;

import java.util.Date;
import lombok.Data;
                                            import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
/**
 * (InvestLog)实体类
 *
 * @author mawkun
 * @since 2020-09-18 10:28:43
 */
@Data 
public class InvestLog {
    
    private Long id;
    
    private Long userId;
    
    private Long cartId;
    
    private String userName;
    
    private String cartName;
    
    private Integer cartNum;
    
    private Double investMoney;
    
    private Double giftMoney;
    
    private Double amountMoney;
    
    private Double residueMoney;
    
        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
private Date createTime;

}