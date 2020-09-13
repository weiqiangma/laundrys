package com.mawkun.app.controller;

import cn.pertech.common.abs.BaseController;
import cn.pertech.common.spring.JsonResult;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.entity.UserAddress;
import com.mawkun.core.base.service.UserAddressService;
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
    public JsonResult insert(@LoginedAuth UserSession session, UserAddress userAddress){
        if(session.getId() > 0) userAddress.setUserId(session.getId());
        int result = userAddressService.insert(userAddress);
        return sendSuccess(result);
    }

    @PostMapping("/update")
    public JsonResult update(UserAddress userAddress){
        int result = userAddressService.update(userAddress);
        return sendSuccess(result);
    }

    @PostMapping("/delete")
    public JsonResult deleteOne(Long id){
        int result = userAddressService.deleteById(id);
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
        if (idList.size()>0) result = userAddressService.deleteByIds(idList);
        return sendSuccess(result);
    }

}