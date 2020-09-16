package com.mawkun.core.service;

import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.UserQuery;
import com.mawkun.core.base.data.vo.ShopUserVo;
import com.mawkun.core.base.data.vo.UserVo;
import com.mawkun.core.base.entity.UserAddress;
import com.mawkun.core.base.service.UserAddressService;
import com.mawkun.core.base.service.UserService;
import com.mawkun.core.dao.ShopUserDaoExt;
import com.mawkun.core.dao.UserAddressDaoExt;
import com.mawkun.core.dao.UserDaoExt;
import com.mawkun.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserAddressServiceExt extends UserAddressService {

    @Autowired
    private UserAddressDaoExt userAddressDaoExt;

    public UserAddress getByIdAndUserId(Long id, Long userId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(id);
        userAddress.setUserId(userId);
        return userAddressDaoExt.getByEntity(userAddress);
    }


}