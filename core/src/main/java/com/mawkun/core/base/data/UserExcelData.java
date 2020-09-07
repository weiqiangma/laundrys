package com.mawkun.core.base.data;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class UserExcelData {

    @ExcelProperty(index = 0)
    private Long id;

    @ExcelProperty(index = 1)
    private String userName;

    @ExcelProperty(index = 2)
    private String realName;

    @ExcelProperty(index = 3)
    private String mobile;

    @ExcelProperty(index = 4)
    private String address;

    @ExcelProperty(index = 5)
    private Integer kind;

    @ExcelProperty(index = 6)
    private Double sumOfMoney;

    @ExcelProperty(index = 7)
    private Integer integral;

    @ExcelProperty(index = 8)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @ExcelProperty(index = 9)
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
