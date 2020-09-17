package com.mawkun.core.base.service;

import com.mawkun.core.base.dao.MemberCartDao;
import com.mawkun.core.base.entity.MemberCart;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 22:42:31
 */
@Service
public class MemberCartService {

    @Resource(type = MemberCartDao.class)
    private MemberCartDao memberCartDao;

    public MemberCartDao getMemberCartDao() {
        return memberCartDao;
    }

    public MemberCart getById(Long id) {
        return memberCartDao.getById(id);
    }

    public MemberCart getByEntity(MemberCart memberCart) {
        return memberCartDao.getByEntity(memberCart);
    }

    public List<MemberCart> listByEntity(MemberCart memberCart) {
        return memberCartDao.listByEntity(memberCart);
    }

    public List<MemberCart> listByIds(List<Long> ids) {
        return memberCartDao.listByIds(ids);
    }

    public int insert(MemberCart memberCart) {
        Date date = new Date();
        memberCart.setCreateTime(date);
        memberCart.setUpdateTime(date);
        return memberCartDao.insert(memberCart);
    }

    public int insertBatch(List<MemberCart> list) {
        return memberCartDao.insertBatch(list);
    }

    public int update(MemberCart memberCart) {
        memberCart.setUpdateTime(new Date());
        return memberCartDao.update(memberCart);
    }

    public int updateBatch(List<MemberCart> list) {
        return memberCartDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return memberCartDao.deleteById(id);
    }

    public int deleteByEntity(MemberCart memberCart) {
        return memberCartDao.deleteByEntity(memberCart);
    }
  
    public int deleteByIds(List<Long> list) {
        return memberCartDao.deleteByIds(list);
    }

    public int countAll() {
        return memberCartDao.countAll();
    }
    
    public int countByEntity(MemberCart memberCart) {
        return memberCartDao.countByEntity(memberCart);
    }

}