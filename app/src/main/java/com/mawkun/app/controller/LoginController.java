package com.mawkun.app.controller;

import com.mawkun.core.base.common.result.JsonResult;
import com.mawkun.core.base.data.WxLoginResultData;
import com.mawkun.core.base.data.query.UserQuery;
import com.mawkun.core.service.UserServiceExt;
import com.mawkun.core.service.WxApiServiceExt;
import com.mawkun.core.utils.StringUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date 2020/9/8 16:23
 * @Author mawkun
 */
@RestController
@Api(tags={"登录操作操作接口"})
public class LoginController {

    @Autowired
    private WxApiServiceExt wxApiServiceExt;
    @Autowired
    private UserServiceExt userServiceExt;

    @PostMapping(value = "/login")
    public JsonResult login(String code) {
        WxLoginResultData resultData = wxApiServiceExt.getOpenIdByCode(code);
        if(StringUtils.isNotEmpty(resultData.getOpenId())) {
        }
        return null;
    }
}
