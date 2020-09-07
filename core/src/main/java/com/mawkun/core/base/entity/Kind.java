package com.mawkun.core.base.entity;

import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * (Kind)实体类
 *
 * @author mawkun
 * @date 2020-08-22 15:32:21
 */
@Data
@ApiModel(value = "kind实体", description = "kind实体")
public class Kind {

    @ApiModelProperty(value = "商品分类ID", name = "id")
    private Long id;

    @ApiModelProperty(value = "商品分类名称", name = "kindName")
    private String kindName;

    @ApiModelProperty(value = "商品分类图标", name = "iconName")
    private String iconName;

    @ApiModelProperty(value = "商品分类描述", name = "description")
    private String description;

    @ApiModelProperty(value = "是否展示", name = "showStatus")
    private Object showStatus;

    @ApiModelProperty(value = "是否导航栏展示", name = "navStatus")
    private Object navStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

}