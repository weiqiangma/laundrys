package com.mawkun.core.base.entity;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (GoodsOrder)实体类
 *
 * @author mawkun
 * @date 2020-10-07 16:30:45
 */
@Data
public class GoodsOrder {

    @ExcelProperty("ID")
    private Long id;
    /**
     * 用户id
     */
    @ExcelProperty("用户ID")
    private Long userId;
    /**[
     * 店铺id
     */
    @ExcelProperty("店铺ID")
    private Long shopId;
    /**
     * 配送员id
     */
    @ExcelProperty("配送员ID")
    private Long distributorId;
    /**
     * 地址id
     */
    @ExcelProperty("地址ID")
    private Long addressId;
    /**
     * 用户名
     */
    @ExcelProperty("用户名")
    private String userName;
    /**
     * 店铺名
     */
    @ExcelProperty("店铺名")
    private String shopName;
    /**
     * 订单号
     */
    @ExcelProperty("订单号")
    private String orderNo;
    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;
    /**
     * 状态
     */
    @ExcelProperty("订单状态")
    private Integer status;
    /**
     * 实付金额
     */
    @ExcelProperty("实付金额")
    private Long realAmount;
    /**
     * 总金额
     */
    @ExcelProperty("总金额")
    private Long totalAmount;
    /**
     * 用户地址
     */
    @ExcelProperty("收货地址")
    private String userAddress;
    /**
     * 运费
     */
    @ExcelProperty("运费")
    private Long transportFee;
    /**
     * 配送方式
     */
    @ExcelProperty("配送方式")
    private Integer transportWay;
    /**
     * 支付类型
     */
    @ExcelProperty("支付类型")
    private Integer payKind;
    /**
     * 支付时间
     */
    @ExcelProperty("支付时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date payTime;
    /**
     * 是否新订单
     */
    @ExcelProperty("是否新订单")
    private Integer isnew;
    /**
     * 线上线下订单
     */
    @ExcelProperty("线上线下订单")
    private Integer orderType;
    /**
     * 线下订单手机号
     */
    @ExcelProperty("线下订单手机号")
    private String offlineMobile;
    /**
     * 更新时间
     */
    @ExcelProperty("更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;
    /**
     * 创建时间
     */
    @ExcelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

}