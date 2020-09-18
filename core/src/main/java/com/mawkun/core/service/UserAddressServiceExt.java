package com.mawkun.core.service;

import cn.pertech.common.spring.JsonResult;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.entity.UserAddress;
import com.mawkun.core.base.service.UserAddressService;
import com.mawkun.core.dao.UserAddressDaoExt;
import com.mawkun.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserAddressServiceExt extends UserAddressService {

    @Autowired
    private UserAddressDaoExt userAddressDaoExt;
    @Autowired
    private GaoDeApiServiceExt gaoDeApiServiceExt;

    public UserAddress getByIdAndUserId(Long id, Long userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(id);
        userAddress.setUserId(userId);
        return userAddressDaoExt.getByEntity(userAddress);
    }

    public String getDetailAddressById(Long id) {
        UserAddress userAddress = userAddressDaoExt.getById(id);
        String detailAddress = userAddress.getCity() + userAddress.getArea() + userAddress.getStreet() + userAddress.getDetail();
        return detailAddress;
    }


    public JsonResult insertUserAddress(UserAddress address) {
        String province = (address.getProvince() != null) ? address.getProvince() : "";
        String city = (address.getCity() != null) ? address.getCity() : "";
        String region = (address.getArea() != null) ? address.getArea() : "";
        String street = (address.getStreet() != null) ? address.getStreet() : "";
        String detail = (address.getDetail() != null) ? address.getDetail() : "";
        String detailAddress = province + city + region + street + detail;
        String location = gaoDeApiServiceExt.getLalByAddress(detailAddress);
        if(StringUtils.isEmpty(location)) return new JsonResult().error("收货地址解析失败,请输入正确地址");
        String[] lal = location.split(",");
        String longitude = lal[0];
        String latidute = lal[1];
        address.setExactAddress(detailAddress);
        address.setLongitude(longitude);
        address.setLatidute(latidute);
        address.setLocation(location);
        int result = userAddressDaoExt.insert(address);
        if(result < 1) return new JsonResult().error("收货地址添加失败");
        return new JsonResult().success("地址添加成功");
    }

    @Transactional
    public int updateUserAddress(UserAddress address) {
        if(address.getStatus() != null) {
            userAddressDaoExt.setAddressUnUsed(address.getUserId());
        }
        return userAddressDaoExt.update(address);
    }


}