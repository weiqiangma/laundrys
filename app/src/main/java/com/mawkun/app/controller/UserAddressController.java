package com.mawkun.app.controller;

import com.mawkun.core.base.controller.BaseController;
import com.mawkun.core.base.data.JsonResult;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.entity.UserAddress;
import com.mawkun.core.base.service.UserAddressService;
import com.mawkun.core.service.UserAddressServiceExt;
import com.mawkun.core.spring.annotation.LoginedAuth;
import com.xiaoleilu.hutool.convert.Convert;
import net.sf.cglib.core.CollectionUtils;
import net.sf.cglib.core.Transformer;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-12 22:26:18
 */
@RestController
@RequestMapping("/api/userAddress")
public class UserAddressController extends BaseController {
    
    @Autowired
    private UserAddressServiceExt userAddressServiceExt;

    @GetMapping("/get")
    public JsonResult getByEntity(UserAddress userAddress) {
        UserAddress address = userAddressServiceExt.getByEntity(userAddress);
        return sendSuccess(address);
    }

    @GetMapping("/list")
    public JsonResult list(@LoginedAuth UserSession session, UserAddress userAddress) {
        if(session.getId() > 0) userAddress.setUserId(session.getId());
        List<UserAddress> userAddressList = userAddressServiceExt.listByEntity(userAddress);
        return sendSuccess(userAddressList);
    }

    @PostMapping("/insert")
    public JsonResult insert(@LoginedAuth UserSession session, UserAddress address){
        if(session.getId() > 0) address.setUserId(session.getId());
        if(address.getAddress() == null || address.getDetail() == null)
            return sendArgsError("请填写完整格式地址");
        return userAddressServiceExt.insertUserAddress(session.getId(), address);
    }

    @PostMapping("/update")
    public JsonResult update(@LoginedAuth UserSession session, UserAddress userAddress){
        if(session.getId() > 0) userAddress.setUserId(session.getId());
        return userAddressServiceExt.updateUserAddress(userAddress);
    }

    @PostMapping("/delete")
    public JsonResult deleteOne(Long id){
        int result = userAddressServiceExt.deleteById(id);
        return sendSuccess(result);
    }

    @PostMapping("/deleteBatch")
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
        if (idList.size()>0) result = userAddressServiceExt.deleteByIds(idList);
        return sendSuccess(result);
    }

}