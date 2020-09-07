package com.mawkun.core.base.entity;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (Goods)实体类
 *
 * @author mawkun
 * @date 2020-08-30 20:49:25
 */
@Data
public class Goods {

    @ExcelProperty("商品ID")
    private Long id;
    @ExcelProperty("商品种类ID")
    private Long kindId;        //kindId=0为套餐
    @ExcelProperty("商品名")
    private String goodsName;
    @ExcelProperty("商品价格")
    private Double price;
    @ExcelProperty("商品描述")
    private String description;
    @ExcelProperty("商品图片")
    private String picture;
    @ExcelProperty("商品状态")
    private Object status;
    @ExcelProperty(value = "商品更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    @ExcelProperty("商品创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}