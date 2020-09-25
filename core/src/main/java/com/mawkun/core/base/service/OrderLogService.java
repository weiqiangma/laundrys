package com.mawkun.core.base.service;

import com.mawkun.core.base.dao.OrderLogDao;

import javax.annotation.Resource;

import com.mawkun.core.base.entity.GoodsOrder;
import com.mawkun.core.base.entity.OrderLog;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-28 21:15:46
 */
@Service
public class OrderLogService {

    @Resource(type = OrderLogDao.class)
    private OrderLogDao orderLogDao;

    public OrderLogDao getOrderLogDao() {
        return orderLogDao;
    }

    public OrderLog getById(Long id) {
        return orderLogDao.getById(id);
    }

    public OrderLog getByEntity(OrderLog orderLog) {
        return orderLogDao.getByEntity(orderLog);
    }

    public List<OrderLog> listByEntity(OrderLog orderLog) {
        return orderLogDao.listByEntity(orderLog);
    }

    public List<OrderLog> listByIds(List<Long> ids) {
        return orderLogDao.listByIds(ids);
    }

    public int insert(OrderLog orderLog) {
        Date date = new Date();
        orderLog.setCreateTime(date);
        return orderLogDao.insert(orderLog);
    }

    public int insertBatch(List<OrderLog> list) {
        return orderLogDao.insertBatch(list);
    }

    public int update(OrderLog orderLog) {
        return orderLogDao.update(orderLog);
    }

    public int updateBatch(List<OrderLog> list) {
        return orderLogDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return orderLogDao.deleteById(id);
    }

    public int deleteByEntity(OrderLog orderLog) {
        return orderLogDao.deleteByEntity(orderLog);
    }
  
    public int deleteByIds(List<Long> list) {
        return orderLogDao.deleteByIds(list);
    }

    public int countAll() {
        return orderLogDao.countAll();
    }
    
    public int countByEntity(OrderLog orderLog) {
        return orderLogDao.countByEntity(orderLog);
    }

}