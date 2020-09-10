package com.mawkun.app.controller;

import com.mawkun.core.base.common.result.JsonResult;
import io.swagger.annotations.Api;
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

    @PostMapping(value = "/login")
    public JsonResult login(String code) {
        return null;
    }
}
