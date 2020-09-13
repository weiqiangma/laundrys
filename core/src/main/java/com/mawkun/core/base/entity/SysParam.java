package com.mawkun.core.base.entity;

import lombok.Data;
                        /**
 * (SysParam)实体类
 *
 * @author mawkun
 * @date 2020-09-13 15:55:06
 */
@Data 
public class SysParam {
    
        private Long id;
    
        private String aliasName;
    
        private String realName;
    
        private String sysValue;
    
        private Integer distance;
    
        private Object status;

}