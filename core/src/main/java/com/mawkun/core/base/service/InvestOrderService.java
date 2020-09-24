package com.mawkun.core.base.service;

import com.mawkun.core.base.dao.InvestOrderDao;
import com.mawkun.core.base.entity.InvestOrder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 23:04:26
 */
@Service
public class InvestOrderService {

    @Resource(type = InvestOrderDao.class)
    private InvestOrderDao investOrderDao;

    public InvestOrderDao getInvestOrderDao() {
        return investOrderDao;
    }

    public InvestOrder getById(Long id) {
        return investOrderDao.getById(id);
    }

    public InvestOrder getByEntity(InvestOrder investLog) {
        return investOrderDao.getByEntity(investLog);
    }

    public List<InvestOrder> listByEntity(InvestOrder investLog) {
        return investOrderDao.listByEntity(investLog);
    }

    public List<InvestOrder> listByIds(List<Long> ids) {
        return investOrderDao.listByIds(ids);
    }

    public int insert(InvestOrder investLog) {
        Date date = new Date();
        investLog.setCreateTime(date);
        return investOrderDao.insert(investLog);
    }

    public int insertBatch(List<InvestOrder> list) {
        return investOrderDao.insertBatch(list);
    }

    public int update(InvestOrder investLog) {
        return investOrderDao.update(investLog);
    }

    public int updateBatch(List<InvestOrder> list) {
        return investOrderDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return investOrderDao.deleteById(id);
    }

    public int deleteByEntity(InvestOrder investLog) {
        return investOrderDao.deleteByEntity(investLog);
    }
  
    public int deleteByIds(List<Long> list) {
        return investOrderDao.deleteByIds(list);
    }

    public int countAll() {
        return investOrderDao.countAll();
    }
    
    public int countByEntity(InvestOrder investLog) {
        return investOrderDao.countByEntity(investLog);
    }

}