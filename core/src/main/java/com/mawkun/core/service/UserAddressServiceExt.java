package com.mawkun.core.service;

import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.JsonResult;
import com.mawkun.core.base.entity.UserAddress;
import com.mawkun.core.base.service.UserAddressService;
import com.mawkun.core.dao.UserAddressDaoExt;
import com.mawkun.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author js
 */
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

    public void setUserAddressUnused(Long userId) {
        UserAddress query = new UserAddress();
        query.setUserId(userId);
        List<UserAddress> list = userAddressDaoExt.listByEntity(query);
        if(list != null && list.size() > 0) {
        list.forEach(item -> item.setStatus(Constant.USER_ADDRESS_UNUSED));
        userAddressDaoExt.updateBatch(list);
        }
    }


    public JsonResult insertUserAddress(Long userId, UserAddress userAddress) {
        String name = (userAddress.getName() != null) ? userAddress.getName() : "";
        String address = (userAddress.getAddress() != null) ? userAddress.getAddress() : "";
        String detail = (userAddress.getDetail() != null) ? userAddress.getDetail() : "";
        String detailAddress = address + name + detail;
//        if(StringUtils.isEmpty(userAddress.getLongitude()) || StringUtils.isEmpty(userAddress.getLatidute())) {
//            return new JsonResult().error("地址解析失败，请重新选择");
//        }
        String location = gaoDeApiServiceExt.getLalByAddress(detailAddress);
        if(StringUtils.isEmpty(location)) return new JsonResult().error("收货地址解析失败,请输入正确地址");
        if(userAddress.getStatus() != null && userAddress.getStatus() == Constant.USER_ADDRESS_USED) {
            setUserAddressUnused(userId);
        }
        String[] lal = location.split(",");
        String longitude = lal[0];
        String latidute = lal[1];
        //String longitude = userAddress.getLongitude();
        //String latidute = userAddress.getLatidute();
        //String location = longitude + "," + latidute;
        userAddress.setExactAddress(detailAddress);
        userAddress.setLongitude(longitude);
        userAddress.setLatidute(latidute);
        userAddress.setLocation(location);
        int result = userAddressDaoExt.insert(userAddress);
        if(result < 1) return new JsonResult().error("收货地址添加失败");
        return new JsonResult().success("地址添加成功");
    }

    public JsonResult updateUserAddress(UserAddress userAddress) {
//        if(StringUtils.isEmpty(userAddress.getLongitude()) || StringUtils.isEmpty(userAddress.getLatidute())) {
//            if(userAddress.getStatus() == null) {
//                return new JsonResult().error("地址解析失败，请重新选择");
//            }
//        }
        if(userAddress.getStatus() != null) {
            userAddressDaoExt.setAddressUnUsed(userAddress.getUserId());
        }
        String name = (userAddress.getName() != null) ? userAddress.getName() : "";
        String address = (userAddress.getAddress() != null) ? userAddress.getAddress() : "";
        String detail = (userAddress.getDetail() != null) ? userAddress.getDetail() : "";
        String detailAddress = address + name + detail;
        if(StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(address) && StringUtils.isNotEmpty(detail)) {
            String location = gaoDeApiServiceExt.getLalByAddress(detailAddress);
            if (StringUtils.isEmpty(location)) return new JsonResult().error("地址解析失败，请重新选择");
            String[] lal = location.split(",");
            String longitude = lal[0];
            String latidute = lal[1];
            //String longitude = userAddress.getLongitude();
            //String latidute = userAddress.getLatidute();
            //String location = longitude + "," + latidute;
            userAddress.setExactAddress(detailAddress);
            userAddress.setLongitude(longitude);
            userAddress.setLatidute(latidute);
            userAddress.setLocation(location);
        }
        int result =  userAddressDaoExt.update(userAddress);
        if(result > 0) return new JsonResult().success("地址编辑成功");
        return new JsonResult().error("地址编辑失败");
    }


}