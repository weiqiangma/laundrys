package com.mawkun.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.query.AdminQuery;
import com.mawkun.core.base.entity.Admin;
import com.mawkun.core.base.service.AdminService;
import com.mawkun.core.dao.AdminDaoExt;
import com.mawkun.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminServiceExt extends AdminService {

    @Resource
    AdminDaoExt adminDaoExt;

    public PageInfo pageByEntity(AdminQuery query) {
        query.init();
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        if(StringUtils.isNotEmpty(query.getUserName())) query.setUserName("%" + query.getUserName() + "%");
        if(StringUtils.isNotEmpty(query.getRealName())) query.setRealName("%" + query.getRealName() + "%");
        if(StringUtils.isNotEmpty(query.getMobile())) query.setMobile("%" + query.getMobile() + "%");
        //List<Admin> list = adminDaoExt.listByEntity(query);
        List<Admin> list = adminDaoExt.selectList(query);
        return new PageInfo(list);
    }

    public List<Admin> selectList(AdminQuery query) {
        return adminDaoExt.selectList(query);
    }

    public Admin getByShopId(Long shopId) {
        Admin query = new Admin();
        query.setShopId(shopId);
        return adminDaoExt.getByEntity(query);
    }

    public Admin getByMobile(String mobile) {
        Admin query = new Admin();
        query.setMobile(mobile);
        return adminDaoExt.getByEntity(query);
    }

    public int deleteByMobile(String mobile) {
        Admin admin = new Admin();
        admin.setMobile(mobile);
        return adminDaoExt.deleteByEntity(admin);
    }

    public int updateWithoutPassword(Admin admin) {
        return adminDaoExt.update(admin);
    }


}
