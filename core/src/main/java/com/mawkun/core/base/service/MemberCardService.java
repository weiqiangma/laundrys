package com.mawkun.core.base.service;

import com.mawkun.core.base.dao.MemberCardDao;
import com.mawkun.core.base.entity.MemberCard;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 22:42:31
 */
@Service
public class MemberCardService {

    @Resource(type = MemberCardDao.class)
    private MemberCardDao memberCardDao;

    public MemberCardDao getMemberCardDao() {
        return memberCardDao;
    }

    public MemberCard getById(Long id) {
        return memberCardDao.getById(id);
    }

    public MemberCard getByEntity(MemberCard memberCard) {
        return memberCardDao.getByEntity(memberCard);
    }

    public List<MemberCard> listByEntity(MemberCard memberCard) {
        return memberCardDao.listByEntity(memberCard);
    }

    public List<MemberCard> listByIds(List<Long> ids) {
        return memberCardDao.listByIds(ids);
    }

    public int insert(MemberCard memberCard) {
        Date date = new Date();
        memberCard.setCreateTime(date);
        memberCard.setUpdateTime(date);
        return memberCardDao.insert(memberCard);
    }

    public int insertBatch(List<MemberCard> list) {
        return memberCardDao.insertBatch(list);
    }

    public int update(MemberCard memberCard) {
        memberCard.setUpdateTime(new Date());
        return memberCardDao.update(memberCard);
    }

    public int updateBatch(List<MemberCard> list) {
        return memberCardDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return memberCardDao.deleteById(id);
    }

    public int deleteByEntity(MemberCard memberCard) {
        return memberCardDao.deleteByEntity(memberCard);
    }
  
    public int deleteByIds(List<Long> list) {
        return memberCardDao.deleteByIds(list);
    }

    public int countAll() {
        return memberCardDao.countAll();
    }
    
    public int countByEntity(MemberCard memberCard) {
        return memberCardDao.countByEntity(memberCard);
    }

}