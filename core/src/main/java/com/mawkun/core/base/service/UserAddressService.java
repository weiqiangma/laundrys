package com.mawkun.core.base.service;

import com.mawkun.core.base.dao.UserAddressDao;
import com.mawkun.core.base.entity.UserAddress;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-12 22:21:58
 */
@Service
public class UserAddressService {

    @Resource(type = UserAddressDao.class)
    private UserAddressDao userAddressDao;

    public UserAddressDao getUserAddressDao() {
        return userAddressDao;
    }

    public UserAddress getById(Long id) {
        return userAddressDao.getById(id);
    }

    public UserAddress getByEntity(UserAddress userAddress) {
        return userAddressDao.getByEntity(userAddress);
    }

    public List<UserAddress> listByEntity(UserAddress userAddress) {
        return userAddressDao.listByEntity(userAddress);
    }

    public List<UserAddress> listByIds(List<Long> ids) {
        return userAddressDao.listByIds(ids);
    }

    public int insert(UserAddress userAddress) {
        return userAddressDao.insert(userAddress);
    }

    public int insertBatch(List<UserAddress> list) {
        return userAddressDao.insertBatch(list);
    }

    public int update(UserAddress userAddress) {
        return userAddressDao.update(userAddress);
    }

    public int updateBatch(List<UserAddress> list) {
        return userAddressDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return userAddressDao.deleteById(id);
    }

    public int deleteByEntity(UserAddress userAddress) {
        return userAddressDao.deleteByEntity(userAddress);
    }
  
    public int deleteByIds(List<Long> list) {
        return userAddressDao.deleteByIds(list);
    }

    public int countAll() {
        return userAddressDao.countAll();
    }
    
    public int countByEntity(UserAddress userAddress) {
        return userAddressDao.countByEntity(userAddress);
    }

}