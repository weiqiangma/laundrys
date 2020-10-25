package com.mawkun.core.base.data.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mawkun.core.base.entity.GoodsOrder;
import com.mawkun.core.base.entity.OrderClothes;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class GoodsOrderVo {
    @ExcelIgnore
    private Long id;
    /**
     * 用户id
     */
    @ExcelIgnore
    private Long userId;
    /**[
     * 店铺id
     */
    @ExcelIgnore
    private Long shopId;
    /**
     * 配送员id
     */
    @ExcelIgnore
    private Long distributorId;
    /**
     * 地址id
     */
    @ExcelIgnore
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
     * 配送员名
     */
    @ExcelProperty("配送员姓名")
    private String distributorName;     //配送员姓名
    @ExcelProperty("微信电话")
    private String mobile;
    @ExcelProperty("联系电话")
    private String linkMobile1;
    @ExcelProperty("备用联系电话")
    private String linkMobile2;
    @ExcelProperty("收货联系人")
    private String linkMan;
    @ExcelProperty("配送员手机号")
    private String distributorMobile;
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
     * 运费
     */
    @ExcelProperty("运费")
    private Long transportFee;
    /**
     * 用户地址
     */
    @ExcelProperty("收货地址")
    private String userAddress;
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

    @ExcelIgnore
    private String shopLocation;
    @ExcelIgnore
    private String shopAddress;



    /**
     * 是否新订单
     */
    @ExcelIgnore
    private Integer isnew;
    /**
     * 线上线下订单
     */
    @ExcelIgnore
    private Integer orderType;
    @ExcelIgnore
    private String customerName;        //客户姓名
    @ExcelIgnore
    private String location;            //订单收货地址
    @ExcelIgnore
    private String address;
    @ExcelIgnore
    private String name;
    @ExcelIgnore
    private String detail;
    @ExcelIgnore
    private String gender;
    @ExcelIgnore
    private Double amount;
    @ExcelIgnore
    private List<OrderClothes> list;         //订单下商品
    @ExcelIgnore
    private String type;
}
