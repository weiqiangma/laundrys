package com.mawkun.core.base.service;

import com.mawkun.core.base.dao.InvestLogDao;
import com.mawkun.core.base.entity.InvestLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 23:04:26
 */
@Service
public class InvestLogService {

    @Resource(type = InvestLogDao.class)
    private InvestLogDao investLogDao;

    public InvestLogDao getInvestLogDao() {
        return investLogDao;
    }

    public InvestLog getById(Long id) {
        return investLogDao.getById(id);
    }

    public InvestLog getByEntity(InvestLog investLog) {
        return investLogDao.getByEntity(investLog);
    }

    public List<InvestLog> listByEntity(InvestLog investLog) {
        return investLogDao.listByEntity(investLog);
    }

    public List<InvestLog> listByIds(List<Long> ids) {
        return investLogDao.listByIds(ids);
    }

    public int insert(InvestLog investLog) {
        Date date = new Date();
        investLog.setCreateTime(date);
        return investLogDao.insert(investLog);
    }

    public int insertBatch(List<InvestLog> list) {
        return investLogDao.insertBatch(list);
    }

    public int update(InvestLog investLog) {
        return investLogDao.update(investLog);
    }

    public int updateBatch(List<InvestLog> list) {
        return investLogDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return investLogDao.deleteById(id);
    }

    public int deleteByEntity(InvestLog investLog) {
        return investLogDao.deleteByEntity(investLog);
    }
  
    public int deleteByIds(List<Long> list) {
        return investLogDao.deleteByIds(list);
    }

    public int countAll() {
        return investLogDao.countAll();
    }
    
    public int countByEntity(InvestLog investLog) {
        return investLogDao.countByEntity(investLog);
    }

}