package com.mawkun.core.base.entity;

import lombok.Data;
                            /**
 * (SysParam)实体类
 *
 * @author mawkun
 * @date 2020-09-15 23:28:57
 */
@Data 
public class SysParam {
    
        private Long id;
    
        private String aliasName;
    
        private String realName;
    
        private String sysValue;
    
        private Integer distance;
    
        private Integer lowAmount;
    
        private Object status;

}