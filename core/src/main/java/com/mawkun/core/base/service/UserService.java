package com.mawkun.core.base.service;

import cn.pertech.common.utils.NumberUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.dao.ShopUserDao;
import com.mawkun.core.base.dao.UserDao;
import com.mawkun.core.base.data.query.UserQuery;
import com.mawkun.core.base.entity.ShopUser;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.dao.ShopUserDaoExt;
import com.mawkun.core.utils.StringUtils;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author mawkun
 * @date 2020-08-19 20:42:58
 */
@Service
public class UserService {

    @Resource
    private UserDao userDao;
    @Resource
    private ShopUserDaoExt shopUserDaoExt;

    public UserDao getUserDao() {
        return userDao;
    }

    public User getById(Long id) {
        return userDao.getById(id);
    }

    public User getByEntity(User user) {
        return userDao.getByEntity(user);
    }

    public List<User> listByEntity(User user) {
        if(!StringUtils.isEmpty(user.getUserName())) {
            user.setUserName("%" + user.getUserName() + "%");
        }
        if(!StringUtils.isEmpty(user.getMobile())) {
            user.setMobile("%" + user.getMobile() + "%");
        }
        return userDao.listByEntity(user);
    }

    public List<User> listByIds(List<Long> ids) {
        return userDao.listByIds(ids);
    }

    public int insert(User user) {
        Date date = new Date();
        user.setCreateTime(date);
        user.setUpdateTime(date);
        return userDao.insert(user);
    }

    public int insertBatch(List<User> list) {
        return userDao.insertBatch(list);
    }

    public int update(User user, String shopIds) {
        if(StringUtils.isNotEmpty(shopIds)) {
            List<String> shopIdList = Arrays.asList(shopIds.split(","));
            shopUserDaoExt.deleteByUserId(user.getId());
            for(String shopId : shopIdList) {
                Long sId = NumberUtils.str2Long(shopId);
                ShopUser shopUser = new ShopUser();
                shopUser.setUserId(user.getId());
                shopUser.setShopId(sId);
                shopUserDaoExt.insert(shopUser);
            }
        }
        user.setUpdateTime(new Date());
        return userDao.update(user);
    }

    public int updateBatch(List<User> list) {
        return userDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return userDao.deleteById(id);
    }

    public int deleteByEntity(User user) {
        return userDao.deleteByEntity(user);
    }
  
    public int deleteByIds(List<Long> list) {
        return userDao.deleteByIds(list);
    }

    public int countAll() {
        return userDao.countAll();
    }
    
    public int countByEntity(User user) {
        return userDao.countByEntity(user);
    }

}