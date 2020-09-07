package com.mawkun.core.base.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.dao.ShopDao;
import com.mawkun.core.base.data.query.ShopQuery;
import com.mawkun.core.base.entity.Shop;
import com.mawkun.core.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-22 15:34:39
 */
@Service
public class ShopService {

    @Resource(type = ShopDao.class)
    private ShopDao shopDao;

    public ShopDao getShopDao() {
        return shopDao;
    }

    public Shop getById(Long id) {
        return shopDao.getById(id);
    }

    public Shop getByEntity(Shop shop) {
        return shopDao.getByEntity(shop);
    }

    public List<Shop> listByEntity(Shop shop) {
        if(!StringUtils.isEmpty(shop.getShopName())) {
            shop.setShopName("%" + shop.getShopName() + "%");
        }
        return shopDao.listByEntity(shop);
    }


    public List<Shop> listByIds(List<Long> ids) {
        return shopDao.listByIds(ids);
    }

    public int insert(Shop shop) {
        Date date = new Date();
        shop.setCreateTime(date);
        shop.setUpdateTime(date);
        return shopDao.insert(shop);
    }

    public int insertBatch(List<Shop> list) {
        return shopDao.insertBatch(list);
    }

    public int update(Shop shop) {
        shop.setUpdateTime(new Date());
        return shopDao.update(shop);
    }

    public int updateBatch(List<Shop> list) {
        return shopDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return shopDao.deleteById(id);
    }

    public int deleteByEntity(Shop shop) {
        return shopDao.deleteByEntity(shop);
    }
  
    public int deleteByIds(List<Long> list) {
        return shopDao.deleteByIds(list);
    }

    public int countAll() {
        return shopDao.countAll();
    }
    
    public int countByEntity(Shop shop) {
        return shopDao.countByEntity(shop);
    }

}