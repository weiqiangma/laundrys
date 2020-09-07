package com.mawkun.core.base.service;

import com.mawkun.core.base.dao.OrderClothesDao;
import com.mawkun.core.base.entity.OrderClothes;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-22 15:42:44
 */
@Service
public class OrderClothesService {

    @Resource(type = OrderClothesDao.class)
    private OrderClothesDao orderClothesDao;

    public OrderClothesDao getOrderClothesDao() {
        return orderClothesDao;
    }

    public OrderClothes getById(Long id) {
        return orderClothesDao.getById(id);
    }

    public OrderClothes getByEntity(OrderClothes orderClothes) {
        return orderClothesDao.getByEntity(orderClothes);
    }

    public List<OrderClothes> listByEntity(OrderClothes orderClothes) {
        return orderClothesDao.listByEntity(orderClothes);
    }

    public List<OrderClothes> listByIds(List<Long> ids) {
        return orderClothesDao.listByIds(ids);
    }

    public int insert(OrderClothes orderClothes) {
        Date date = new Date();
        return orderClothesDao.insert(orderClothes);
    }

    public int insertBatch(List<OrderClothes> list) {
        return orderClothesDao.insertBatch(list);
    }

    public int update(OrderClothes orderClothes) {
        return orderClothesDao.update(orderClothes);
    }

    public int updateBatch(List<OrderClothes> list) {
        return orderClothesDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return orderClothesDao.deleteById(id);
    }

    public int deleteByEntity(OrderClothes orderClothes) {
        return orderClothesDao.deleteByEntity(orderClothes);
    }
  
    public int deleteByIds(List<Long> list) {
        return orderClothesDao.deleteByIds(list);
    }

    public int countAll() {
        return orderClothesDao.countAll();
    }
    
    public int countByEntity(OrderClothes orderClothes) {
        return orderClothesDao.countByEntity(orderClothes);
    }

}