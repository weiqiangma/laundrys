package com.mawkun.core.base.service;

import com.mawkun.core.base.dao.OperateOrderLogDao;
import com.mawkun.core.base.entity.OperateOrderLog;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-28 21:15:46
 */
@Service
public class OperateOrderLogService {

    @Resource(type = OperateOrderLogDao.class)
    private OperateOrderLogDao operateOrderLogDao;

    public OperateOrderLogDao getOperateOrderLogDao() {
        return operateOrderLogDao;
    }

    public OperateOrderLog getById(Long id) {
        return operateOrderLogDao.getById(id);
    }

    public OperateOrderLog getByEntity(OperateOrderLog operateOrderLog) {
        return operateOrderLogDao.getByEntity(operateOrderLog);
    }

    public List<OperateOrderLog> listByEntity(OperateOrderLog operateOrderLog) {
        return operateOrderLogDao.listByEntity(operateOrderLog);
    }

    public List<OperateOrderLog> listByIds(List<Long> ids) {
        return operateOrderLogDao.listByIds(ids);
    }

    public int insert(OperateOrderLog operateOrderLog) {
        Date date = new Date();
        operateOrderLog.setCreateTime(date);
        return operateOrderLogDao.insert(operateOrderLog);
    }

    public int insertBatch(List<OperateOrderLog> list) {
        return operateOrderLogDao.insertBatch(list);
    }

    public int update(OperateOrderLog operateOrderLog) {
        return operateOrderLogDao.update(operateOrderLog);
    }

    public int updateBatch(List<OperateOrderLog> list) {
        return operateOrderLogDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return operateOrderLogDao.deleteById(id);
    }

    public int deleteByEntity(OperateOrderLog operateOrderLog) {
        return operateOrderLogDao.deleteByEntity(operateOrderLog);
    }
  
    public int deleteByIds(List<Long> list) {
        return operateOrderLogDao.deleteByIds(list);
    }

    public int countAll() {
        return operateOrderLogDao.countAll();
    }
    
    public int countByEntity(OperateOrderLog operateOrderLog) {
        return operateOrderLogDao.countByEntity(operateOrderLog);
    }

}