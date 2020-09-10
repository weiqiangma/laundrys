package com.mawkun.core.base.data;

import lombok.Data;

/**
 * @Date 2020/9/10 11:13
 * @Author mawkun
 */
@Data
public class WxLoginResultData {
    private String openId;
    private String sessionKey;
}
