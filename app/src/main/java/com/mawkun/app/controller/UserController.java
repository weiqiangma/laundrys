package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.XmlUtils;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.WxLoginResultData;
import com.mawkun.core.base.data.query.UserQuery;
import com.mawkun.core.base.entity.MemberCard;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.base.service.UserCacheService;
import com.mawkun.core.service.InvestLogServiceExt;
import com.mawkun.core.service.MemberCardServiceExt;
import com.mawkun.core.service.UserServiceExt;
import com.mawkun.core.service.WxApiServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * @author mawkun
 * @date 2020-08-19 21:44:11
 */
@RestController
@RequestMapping("/api/user")
@Api(tags={"用户操作接口"})
public class UserController extends BaseController {

    @Value("${wx.AppId}")
    private String AppId;
    @Value("${wx.macId}")
    private String macId;
    
    @Autowired
    private UserServiceExt userServiceExt;
    @Autowired
    private InvestLogServiceExt investLogServiceExt;
    @Autowired
    private UserCacheService userCacheService;
    @Autowired
    private WxApiServiceExt wxApiServiceExt;
    @Autowired
    private MemberCardServiceExt MemberCardServiceExt;

    @GetMapping("/get")
    @ApiOperation(value="用户详情", notes="用户详情")
    public JsonResult getById(Long id) {
        User user = userServiceExt.getById(id);
        return sendSuccess(user);
    }

    @GetMapping("/getByOpenId")
    @ApiOperation(value="用户详情", notes="用户详情")
    public JsonResult getByOpenId(String openId) {
        User user = userServiceExt.getByOpenId(openId);
        return sendSuccess(user);
    }

    @GetMapping("/getByToken")
    @ApiOperation(value="用户详情", notes="用户详情")
    public JsonResult getByToken(@LoginedAuth UserSession session){
        Validate.notNull(session.getId());
        User user = userServiceExt.getById(session.getId());
        return sendSuccess(user);
    }

    @GetMapping("/getByEntity")
    @ApiOperation(value="用户详情", notes="用户详情")
    public JsonResult getByEntity(@LoginedAuth UserSession session, User user) {
        User resultUser = userServiceExt.getByEntity(user);
        return sendSuccess(resultUser);
    }

    @GetMapping("/list")
    @ApiOperation(value="用户列表", notes="用户列表")
    public JsonResult list(User user) {
        List<User> userList = userServiceExt.listByEntity(user);
        return sendSuccess(userList);
    }

    @GetMapping("/pageList")
    public JsonResult pageList(UserQuery userQuery) {
        PageInfo page = userServiceExt.pageByEntity(userQuery);
        return sendSuccess(page);
    }

    @PostMapping("/updateUserInfo")
    @ApiOperation(value="编辑用户", notes="编辑用户")
    public JsonResult update(@LoginedAuth UserSession session, User user){
        user.setId(session.getId());
        user.setStatus(Constant.STATUS_YES);
        int result = userServiceExt.update(user, null);
        return sendSuccess(result);
    }

    @PostMapping("/getUserMobile")
    @ApiOperation(value="获取微信用户手机号", notes="获取微信用户手机号")
    public JsonResult getUserMobile(@LoginedAuth UserSession session, String encryptedData, String iv, String code) {
        User user = userServiceExt.getById(session.getId());
        if(user == null) return sendArgsError("未查询到该用户信息");
        WxLoginResultData data = wxApiServiceExt.getOpenIdByCode(code);
        session.setSessionKey(data.getSessionKey());
        String mobile = wxApiServiceExt.getPhoneNumber(encryptedData, session.getSessionKey(), iv);
        user.setMobile(mobile);
        userServiceExt.update(user, null);
        return sendSuccess("ok", mobile);
    }

    @PostMapping("/rechargetMoney")
    @ApiOperation(value = "充值接口", notes = "充值接口")
    public JsonResult rechargetMoney(@LoginedAuth UserSession session,Integer type, Long cardId, Long money) {
        if(type == null) return sendArgsError("请选择充值方式");
        User user = userServiceExt.getById(session.getId());
        if(user == null) return sendArgsError("未查询到用户信息,请联系客服人员处理");
        if(type == Constant.RECHARGE_WITH_CARD) {
            if(cardId == null) return sendArgsError("请选择充值卡券");
            MemberCard cart = MemberCardServiceExt.findByIdAndStatus(cardId, Constant.MEMBER_CART_ON);
            if(cart == null) return sendArgsError("未查询到充值卡信息，请重新选择");
            userServiceExt.rechargeMoney(user, cart);
        }
        if(type == Constant.RECHARGE_WITH_MONEY) {
            userServiceExt.rechargeMoney(user, money);
        }
        return sendSuccess("充值成功");
    }

    @PostMapping("/rechargeCallBack")
    public JsonResult rechargeCallBack(HttpServletRequest request, HttpServletResponse response) {
        String resXml = "";
        String result = "";
        try {
            InputStream is = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resXml = sb.toString();
            logger.info("微信小程序支付回调报文：" + resXml);
            Map<String, String> map = XmlUtils.xmlStr2Map(resXml);
            if (map.get("return_code").equals("SUCCESS") && map.get("appid").equals(AppId) && map.get("mch_id").equals(macId)) {
                String orderNo = map.get("out_trade_no");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "<xml><return_code>FAIL</return_code><return_msg>接口请求错误</return_msg></xml>";
        }
        return sendSuccess("ok", result);
    }
}