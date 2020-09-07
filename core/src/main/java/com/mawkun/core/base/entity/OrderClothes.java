package com.mawkun.core.base.entity;

import lombok.Data;

/**
* (OrderClothes)实体类
*
* @author mawkun
* @date 2020-08-29 14:55:16
*/
@Data 
public class OrderClothes {

private Long id;

private Long orderFormId;

private Long goodsId;

private Integer goodsNum;

}