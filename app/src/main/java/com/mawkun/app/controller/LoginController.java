package com.mawkun.app.controller;

import cn.pertech.common.utils.CryptUtils;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.controller.BaseController;
import com.mawkun.core.base.data.JsonResult;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.WxLoginResultData;
import com.mawkun.core.base.data.query.UserQuery;
import com.mawkun.core.base.entity.ShopUser;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.base.service.UserCacheService;
import com.mawkun.core.dao.ShopUserDaoExt;
import com.mawkun.core.service.UserServiceExt;
import com.mawkun.core.service.WxApiServiceExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ShopUserDaoExt shopUserDaoExt;

    @PostMapping(value = "/login")
    @ApiOperation(value="小程序登录", notes="小程序登录")
    public JsonResult login(String code) {
        
        WxLoginResultData resultData = wxApiServiceExt.getOpenIdByCode(code);
        //根据openID查询数据库中是否存在该用户，没有则添加
        UserQuery query = new UserQuery();
        query.setOpenId(resultData.getOpenId());
        User user = userServiceExt.getByEntity(query);
        if (user == null) {
            User addUser = new User();
            addUser.setOpenId(resultData.getOpenId());
            addUser.setKind(Constant.USER_TYPE_CUSTOMER);
            userServiceExt.insert(addUser);
            resultData.setKind(Constant.USER_TYPE_CUSTOMER);
            resultData.setUserId(addUser.getId());
        } else {
            resultData.setKind(user.getKind());
            resultData.setUserId(user.getId());
            //如果是配送员，将其关联的店铺存入session
            if(user.getKind() != null) {
                if(user.getKind() == Constant.USER_TYPE_DISTRIBUTOR) {
                    ShopUser shopUser = new ShopUser();
                    shopUser.setUserId(user.getId());
                    List<ShopUser> list = shopUserDaoExt.listByEntity(shopUser);
                    List<Long> shopIdList = list.stream().map(ShopUser::getShopId).collect(Collectors.toList());
                    resultData.setShopIdList(shopIdList);
                }
            }
        }
        //生成token,保存session
        String token = CryptUtils.md5Safe(resultData.getOpenId()  + resultData.getSessionKey() + System.currentTimeMillis());
        UserSession session = new UserSession(token, resultData);
        userCacheService.putUserSession(token, session);
        return sendSuccess("ok", token);
    }
}
