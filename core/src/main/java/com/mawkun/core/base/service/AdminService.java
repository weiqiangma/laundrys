package com.mawkun.core.base.service;

import cn.pertech.common.utils.CryptUtils;
import com.mawkun.core.base.dao.AdminDao;
import com.mawkun.core.base.entity.Admin;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-22 15:25:45
 */
@Service
public class AdminService {

    @Resource(type = AdminDao.class)
    private AdminDao adminDao;

    public AdminDao getAdminDao() {
        return adminDao;
    }

    public Admin getById(Long id) {
        return adminDao.getById(id);
    }

    public Admin getByEntity(Admin admin) {
        return adminDao.getByEntity(admin);
    }

    public List<Admin> listByEntity(Admin admin) {
        return adminDao.listByEntity(admin);
    }

    public List<Admin> listByIds(List<Long> ids) {
        return adminDao.listByIds(ids);
    }

    public int insert(Admin admin) {
        Date date = new Date();
        admin.setCreateTime(date);
        admin.setUpdateTime(date);
        admin.setPassword(CryptUtils.md5Safe(admin.getPassword()));
        return adminDao.insert(admin);
    }

    public int insertBatch(List<Admin> list) {
        return adminDao.insertBatch(list);
    }

    public int update(Admin admin) {
        admin.setUpdateTime(new Date());
        if(admin.getPassword() != null) admin.setPassword(CryptUtils.md5Safe(admin.getPassword()));
        return adminDao.update(admin);
    }

    public int updateBatch(List<Admin> list) {
        return adminDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return adminDao.deleteById(id);
    }

    public int deleteByEntity(Admin admin) {
        return adminDao.deleteByEntity(admin);
    }
  
    public int deleteByIds(List<Long> list) {
        return adminDao.deleteByIds(list);
    }

    public int countAll() {
        return adminDao.countAll();
    }
    
    public int countByEntity(Admin admin) {
        return adminDao.countByEntity(admin);
    }

    public static void main(String[] args) {
        String password = CryptUtils.md5Safe("123456");
        System.out.println(password);
    }

}