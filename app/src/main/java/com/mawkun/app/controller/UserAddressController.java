package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.entity.UserAddress;
import com.mawkun.core.base.service.UserAddressService;
import com.mawkun.core.spring.annotation.LoginedAuth;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-12 22:26:18
 */
@RestController
@RequestMapping("/api/userAddress")
public class UserAddressController extends BaseController {
    
    @Autowired
    private UserAddressService userAddressService;

    @GetMapping("/get")
    public JsonResult getByEntity(UserAddress userAddress) {
        UserAddress address = userAddressService.getByEntity(userAddress);
        return sendSuccess(address);
    }

    @GetMapping("/list")
    public JsonResult list(@LoginedAuth UserSession session, UserAddress userAddress) {
        if(session.getId() > 0) userAddress.setUserId(session.getId());
        List<UserAddress> userAddressList = userAddressService.listByEntity(userAddress);
        return sendSuccess(userAddressList);
    }

    @PostMapping("/insert")
    public JsonResult insert(UserSession session, UserAddress userAddress){
        if(session.getId() > 0) userAddress.setUserId(session.getId());
        userAddressService.insert(userAddress);
        return sendSuccess();
    }

    @PutMapping("/update")
    public int update(@RequestBody UserAddress userAddress){
        return userAddressService.update(userAddress);
    }

    @DeleteMapping("/delete/{id}")
    public int deleteOne(@PathVariable Long id){
        return userAddressService.deleteById(id);
    }

    @DeleteMapping("/delete")
    public int deleteBatch(@RequestBody List<Long> ids){
        int result = 0;
        if (ids!=null&&ids.size()>0) result = userAddressService.deleteByIds(ids);
        return result;
    }

}