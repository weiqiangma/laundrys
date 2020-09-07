package com.mawkun.core.base.service;

import com.mawkun.core.base.dao.KindDao;
import com.mawkun.core.base.entity.Kind;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-22 15:32:22
 */
@Service
public class KindService {

    @Resource(type = KindDao.class)
    private KindDao kindDao;

    public KindDao getKindDao() {
        return kindDao;
    }

    public Kind getById(Long id) {
        return kindDao.getById(id);
    }

    public Kind getByEntity(Kind kind) {
        return kindDao.getByEntity(kind);
    }

    public List<Kind> listByEntity(Kind kind) {
        return kindDao.listByEntity(kind);
    }

    public List<Kind> listByIds(List<Long> ids) {
        return kindDao.listByIds(ids);
    }

    public int insert(Kind kind) {
        Date date = new Date();
        kind.setCreateTime(date);
        kind.setUpdateTime(date);
        return kindDao.insert(kind);
    }

    public int insertBatch(List<Kind> list) {
        return kindDao.insertBatch(list);
    }

    public int update(Kind kind) {
        kind.setUpdateTime(new Date());
        return kindDao.update(kind);
    }

    public int updateBatch(List<Kind> list) {
        return kindDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return kindDao.deleteById(id);
    }

    public int deleteByEntity(Kind kind) {
        return kindDao.deleteByEntity(kind);
    }
  
    public int deleteByIds(List<Long> list) {
        return kindDao.deleteByIds(list);
    }

    public int countAll() {
        return kindDao.countAll();
    }
    
    public int countByEntity(Kind kind) {
        return kindDao.countByEntity(kind);
    }

}