package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.NumberUtils;
import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.UserQuery;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.service.UserServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.lang.Validator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-19 21:44:11
 */
@Controller
@RequestMapping("/api/user")
@Api(tags={"用户操作接口"})
public class UserController extends BaseController {
    
    @Autowired
    private UserServiceExt userServiceExt;

    @GetMapping("/get")
    @ResponseBody
    @ApiOperation(value="用户详情", notes="用户详情")
    public JsonResult getById(Long id) {
        User user = userServiceExt.getById(id);
        return sendSuccess(user);
    }

    @ResponseBody
    @GetMapping("/getByEntity")
    @ApiOperation(value="用户详情", notes="用户详情")
    public JsonResult getByEntity(@LoginedAuth UserSession session, User user) {
        User resultUser = userServiceExt.getByEntity(user);
        return sendSuccess(resultUser);
    }

    @ResponseBody
    @GetMapping("/list")
    @ApiOperation(value="用户列表", notes="用户列表")
    public JsonResult list(User user) {
        List<User> userList = userServiceExt.listByEntity(user);
        return sendSuccess(userList);
    }

    @ResponseBody
    @GetMapping("/pageList")
    public JsonResult pageList(UserQuery userQuery) {
        PageInfo page = userServiceExt.pageByEntity(userQuery);
        return sendSuccess(page);
    }

    @ResponseBody
    @PostMapping("/updateUserInfo")
    @ApiOperation(value="编辑用户", notes="编辑用户")
    public JsonResult update(User user){
        int result = userServiceExt.update(user, null);
        return sendSuccess(result);
    }

    @ResponseBody
    @PostMapping("/rechargetMoney")
    @ApiOperation(value = "充值接口", notes = "充值接口")
    public JsonResult rechargetMoney(@LoginedAuth UserSession session, String money) {
        Validator.isMoney(money);
        User user = userServiceExt.getById(session.getId());
        int result = userServiceExt.rechargeMoney(user, NumberUtils.str2MoneyUp(money));
        if(result > 0) return sendSuccess("充值成功");
        return sendArgsError("充值失败");
    }
}