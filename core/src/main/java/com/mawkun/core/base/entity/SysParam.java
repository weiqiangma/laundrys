package com.mawkun.core.base.entity;

import lombok.Data;

/**
 * (SysParam)实体类
 *
 * @author mawkun
 * @date 2020-10-06 22:09:10
 */
@Data
public class SysParam {

    private Long id;
    /**
     * 别名
     */
    private String aliasName;
    /**
     * 真名
     */
    private String realName;
    /**
     * 系统值
     */
    private String sysValue;
    /**
     * 距离
     */
    private Integer distance;
    /**
     * 最低消费
     */
    private Integer lowAmount;
    /**
     * 轮播图跳转地址
     */
    private String path;
    /**
     * 状态
     */
    private Integer status;

    private Integer type;

}