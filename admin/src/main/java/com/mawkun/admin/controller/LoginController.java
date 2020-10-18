package com.mawkun.admin.controller;

import cn.pertech.common.utils.CryptUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.controller.BaseController;
import com.mawkun.core.base.data.JsonResult;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.entity.Admin;
import com.mawkun.core.base.entity.Goods;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.base.service.UserCacheService;
import com.mawkun.core.service.AdminServiceExt;
import com.mawkun.core.service.GoodsServiceExt;
import com.mawkun.core.service.UserServiceExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@Controller
@Api(tags={"登录操作接口"})
public class LoginController extends BaseController {

    @Autowired
    AdminServiceExt adminServiceExt;
    @Autowired
    UserCacheService userCacheService;
    @Autowired
    UserServiceExt userServiceExt;
    @Autowired
    GoodsServiceExt goodsServiceExt;

    @ResponseBody
    @PostMapping(value = "/login")
    @ApiOperation(value="登录接口", notes="登录接口")
    public JsonResult login(String userName, String password) {
        if (userName == null || password == null) return sendArgsError();
        password = CryptUtils.md5Safe(password);
        Admin queryAdmin = new Admin();
        queryAdmin.setUserName(userName);
        queryAdmin.setPassword(password);
        Admin admin = adminServiceExt.getByEntity(queryAdmin);
        if (admin == null){
            super.addAdminLog("管理员登录:帐号密码错误");
            return sendError("帐号密码错误");
        }
        if(admin.getStatus() == Constant.USER_STATUS_LOCK) {
            return sendArgsError("该用户已被封锁，如需解锁请联系主管理员");
        }
        String token = CryptUtils.md5(String.valueOf(System.currentTimeMillis()) + admin.getId() + admin.getUserName() + System.nanoTime());
        JSONObject item = new JSONObject();
        item.put("id", admin.getId());
        item.put("shopId", admin.getShopId());
        item.put("mobile", admin.getMobile());
        item.put("realName", admin.getRealName());
        item.put("level", admin.getLevel());
        item.put("token", token);
        UserSession session = new UserSession(token, admin);
        userCacheService.putAdminSession(token, session);
        return sendSuccess(item);
    }

    @GetMapping("/export")
    @ApiOperation(value="用户信息导出excel", notes="用户信息导出excel")
    public void export(User user, HttpServletResponse response) {
        List<Goods> list = goodsServiceExt.listByEntity(new Goods());
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("导出", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), Goods.class)
                    .sheet("sheet0")
                    .doWrite(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
