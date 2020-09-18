package com.mawkun.admin.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.RequestUtils;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.WxLoginResultData;
import com.mawkun.core.base.data.query.StateQuery;
import com.mawkun.core.base.data.query.UserQuery;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.service.UserServiceExt;
import com.mawkun.core.service.WxApiServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.xiaoleilu.hutool.convert.Convert;
import com.xiaoleilu.hutool.lang.Base64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-19 21:44:11
 */
@Slf4j
@Controller
@RequestMapping("/adm/user")
@Api(tags={"用户操作接口"})
public class UserController extends BaseController {
    
    @Autowired
    private UserServiceExt userServiceExt;
    @Autowired
    private WxApiServiceExt wxApiServiceExt;

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
    @PostMapping("/insert")
    @ApiOperation(value="添加用户", notes="添加用户")
    public JsonResult insert(User user){
        int result = userServiceExt.insert(user);
        return sendSuccess(result);
    }

    @ResponseBody
    @PutMapping("/update")
    @ApiOperation(value="编辑用户", notes="编辑用户")
    public JsonResult update(@LoginedAuth @ApiIgnore UserSession session, User user, String shopIds){
        if(session.getShopId() > 0) return sendArgsError("子管理员无权编辑用户");
        int result = userServiceExt.update(user, shopIds);
        return sendSuccess(result);
    }

    @ResponseBody
    @DeleteMapping("/delete")
    @ApiOperation(value="删除用户", notes="删除用户")
    public JsonResult deleteOne(Long id){
        int result = userServiceExt.deleteById(id);
        return sendSuccess(result);
    }

    @ResponseBody
    @DeleteMapping("/deleteBatch")
    @ApiOperation(value="批量删除用户", notes="批量删除用户")
    public JsonResult deleteBatch(String ids){
        int result = 0;
        List<String> idArray = Arrays.asList(ids.split(","));
        List idList = new ArrayList<>();
        idList = CollectionUtils.transform(idArray, new Transformer() {
            @Override
            public Object transform(Object o) {
                return Convert.toInt(o, 0);
            }
        });
        if (idList.size()>0) result = userServiceExt.deleteByIds(idList);
        return sendSuccess(result);
    }

    @ResponseBody
    @GetMapping("/statsIncreaseMember")
    @ApiOperation(value = "统计新增会员",notes = "统计新增会员")
    public JsonResult statsIncreaseMember(@LoginedAuth UserSession session) {
        return null;
    }

    @GetMapping("/export")
    @ApiOperation(value="用户信息导出excel", notes="用户信息导出excel")
    public void export(User user, HttpServletResponse response) {
        List<User> list = userServiceExt.listByEntity(user);
        try(OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("教职工考勤统计.xlsx", "UTF-8"));
            EasyExcel.write(outputStream, User.class).sheet("用户统计").doWrite(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/getUserMobile")
    @ApiOperation(value="获取微信用户手机号", notes="获取微信用户手机号")
    public JsonResult getUserMobile(@LoginedAuth UserSession session, String encryptedData, String code, String iv) {
        User user = userServiceExt.getById(session.getId());
        if(user == null) return sendArgsError("未查询到该用户信息");
        String mobile = wxApiServiceExt.getPhoneNumber(encryptedData, code, iv);
        user.setMobile(mobile);
        userServiceExt.update(user, null);
        return sendSuccess(user);
    }


    //查询统计对象(首页统计)
    private StateQuery createQueryStateVo(){
        int type = getIntPar("type",1);
        HttpServletRequest request = getRequest();
        Date start = null;
        Date end = null;
        Integer shopId = RequestUtils.getIntPar(request, "shopId", 0);
        try{
            start = RequestUtils.getDatePar(request,"createTimeStart","yyyy-MM-dd");
            end = RequestUtils.getDatePar(request,"createTimeEnd","yyyy-MM-dd");
        }catch (Exception e){
            log.error("开始和结束错误时间格式错误");
        }
        StateQuery queryVO = new StateQuery();
        queryVO.setShopId(shopId.longValue());
        if(start!=null && end!=null){
            type = 4 ;
            queryVO.setStartTime(start);
            queryVO.setEndTime(end);
        }
        queryVO.setType(type);
        return queryVO;
    }

}