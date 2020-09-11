package com.mawkun.core.base.service;

import com.mawkun.core.base.dao.ShoppingCartDao;
import com.mawkun.core.base.entity.ShoppingCart;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ShoppingCartService {

    @Resource(type = ShoppingCartDao.class)
    private ShoppingCartDao shoppingCartDao;

    public ShoppingCartDao getShoppingCartDao() {
        return shoppingCartDao;
    }

    public ShoppingCart getById(Long id) {
        return shoppingCartDao.getById(id);
    }

    public ShoppingCart getByEntity(ShoppingCart shoppingCart) {
        return shoppingCartDao.getByEntity(shoppingCart);
    }

    public List<ShoppingCart> listByEntity(ShoppingCart shoppingCart) {
        return shoppingCartDao.listByEntity(shoppingCart);
    }

    public List<ShoppingCart> listByIds(List<Long> ids) {
        return shoppingCartDao.listByIds(ids);
    }

    public int insert(ShoppingCart shoppingCart) {
        Date date = new Date();
        shoppingCart.setCreateTime(date);
        shoppingCart.setUpdateTime(date);
        return shoppingCartDao.insert(shoppingCart);
    }

    public int insertBatch(List<ShoppingCart> list) {
        return shoppingCartDao.insertBatch(list);
    }

    public int update(ShoppingCart shoppingCart) {
        shoppingCart.setUpdateTime(new Date());
        return shoppingCartDao.update(shoppingCart);
    }

    public int updateBatch(List<ShoppingCart> list) {
        return shoppingCartDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return shoppingCartDao.deleteById(id);
    }

    public int deleteByEntity(ShoppingCart shoppingCart) {
        return shoppingCartDao.deleteByEntity(shoppingCart);
    }
  
    public int deleteByIds(List<Long> list) {
        return shoppingCartDao.deleteByIds(list);
    }

    public int countAll() {
        return shoppingCartDao.countAll();
    }
    
    public int countByEntity(ShoppingCart shoppingCart) {
        return shoppingCartDao.countByEntity(shoppingCart);
    }

}