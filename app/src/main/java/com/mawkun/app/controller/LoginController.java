package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.CryptUtils;
import cn.pertech.common.utils.NumberUtils;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.WxLoginResultData;
import com.mawkun.core.base.data.query.UserQuery;
import com.mawkun.core.base.data.vo.UserVo;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.base.service.UserCacheService;
import com.mawkun.core.service.UserServiceExt;
import com.mawkun.core.service.WxApiServiceExt;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Date 2020/9/8 16:23
 * @Author mawkun
 */
@RestController
@Api(tags = {"登录操作操作接口"})
public class LoginController extends BaseController {

    @Autowired
    private WxApiServiceExt wxApiServiceExt;
    @Autowired
    private UserServiceExt userServiceExt;
    @Autowired
    private UserCacheService userCacheService;

    @PostMapping(value = "/login")
    public JsonResult login(String code) {
        WxLoginResultData resultData = wxApiServiceExt.getOpenIdByCode(code);
        resultData.setKind(Constant.USER_TYPE_CUSTOMER);
        //根据openID查询数据库中是否存在该用户，没有则添加
        UserQuery query = new UserQuery();
        query.setOpenId(resultData.getOpenId());
        List<UserVo> list = userServiceExt.listByEntity(query);
        if (list.size() == 0) {
            User user = new User();
            user.setOpenId(resultData.getOpenId());
            int userId = userServiceExt.insert(user);
            resultData.setUserId((long) userId);
        }
        //生成token,保存session
        String token = CryptUtils.md5Safe(resultData.getOpenId()  + resultData.getSessionKey() + System.currentTimeMillis());
        UserSession session = new UserSession(token, resultData);
        userCacheService.putUserSession(token, session);
        return sendSuccess("ok", token);
    }
}
