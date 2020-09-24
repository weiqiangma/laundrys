package com.mawkun.core.base.service;

import com.mawkun.core.base.dao.PayFlowDao;
import com.mawkun.core.base.entity.PayFlow;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class PayFlowService {

    @Resource(type = PayFlowDao.class)
    private PayFlowDao payFlowDao;

    public PayFlow getById(Long id) {
        return payFlowDao.getById(id);
    }

    public PayFlow getByEntity(PayFlow payFlow) {
        return payFlowDao.getByEntity(payFlow);
    }

    public List<PayFlow> listByEntity(PayFlow payFlow) {
        return payFlowDao.listByEntity(payFlow);
    }

    public List<PayFlow> listByIds(List<Long> ids) {
        return payFlowDao.listByIds(ids);
    }

    public int insert(PayFlow payFlow) {
        Date date = new Date();
        payFlow.setCreateTime(date);
        payFlow.setUpdateTime(date);
        return payFlowDao.insert(payFlow);
    }

    public int insertBatch(List<PayFlow> list) {
        return payFlowDao.insertBatch(list);
    }

    public int update(PayFlow payFlow) {
        payFlow.setUpdateTime(new Date());
        return payFlowDao.update(payFlow);
    }

    public int updateBatch(List<PayFlow> list) {
        return payFlowDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return payFlowDao.deleteById(id);
    }

    public int deleteByEntity(PayFlow payFlow) {
        return payFlowDao.deleteByEntity(payFlow);
    }
  
    public int deleteByIds(List<Long> list) {
        return payFlowDao.deleteByIds(list);
    }

    public int countAll() {
        return payFlowDao.countAll();
    }
    
    public int countByEntity(PayFlow payFlow) {
        return payFlowDao.countByEntity(payFlow);
    }

}