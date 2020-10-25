package com.mawkun.core.base.entity;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (InvestOrder)实体类
 *
 * @author mawkun
 * @date 2020-10-16 00:24:53
 */
@Data
public class InvestOrder {

    @ExcelIgnore
    private Long id;
    @ExcelProperty("用户ID")
    private Long userId;
    @ExcelProperty("充值卡ID")
    private Long cartId;
    /**
     * 订单号
     */
    @ExcelProperty("充值单号")
    private String orderNo;
    /**
     * 用户名
     */
    @ExcelProperty("用户名")
    private String userName;
    /**
     * 卡名称
     */
    @ExcelProperty("充值卡名")
    private String cartName;
    /**
     * 充值金额
     */
    @ExcelProperty("充值金额")
    private Long investMoney;
    /**
     * 赠送金额
     */
    @ExcelProperty("赠送金额")
    private Long giftMoney;
    /**
     * 总金额
     */
    @ExcelProperty("到账金额")
    private Long amountMoney;
    /**
     * 用户余额
     */
    @ExcelProperty("用户余额")
    private Long residueMoney;
    /**
     * 状态
     */
    @ExcelIgnore
    private Integer status;
    /**
     * 支付时间
     */
    @ExcelProperty("支付时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;
    /**
     * 更新时间
     */
    @ExcelProperty("更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}