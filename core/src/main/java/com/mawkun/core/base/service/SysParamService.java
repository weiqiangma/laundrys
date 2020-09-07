package com.mawkun.core.base.service;

import com.mawkun.core.base.dao.SysParamDao;
import com.mawkun.core.base.entity.SysParam;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-02 20:37:50
 */
@Service
public class SysParamService {

    @Resource(type = SysParamDao.class)
    private SysParamDao sysParamDao;

    public SysParamDao getSysParamDao() {
        return sysParamDao;
    }

    public SysParam getById(Long id) {
        return sysParamDao.getById(id);
    }

    public SysParam getByEntity(SysParam sysParam) {
        return sysParamDao.getByEntity(sysParam);
    }

    public List<SysParam> listByEntity(SysParam sysParam) {
        return sysParamDao.listByEntity(sysParam);
    }

    public List<SysParam> listByIds(List<Long> ids) {
        return sysParamDao.listByIds(ids);
    }

    public int insert(SysParam sysParam) {
        return sysParamDao.insert(sysParam);
    }

    public int insertBatch(List<SysParam> list) {
        return sysParamDao.insertBatch(list);
    }

    public int update(SysParam sysParam) {
        return sysParamDao.update(sysParam);
    }

    public int updateBatch(List<SysParam> list) {
        return sysParamDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return sysParamDao.deleteById(id);
    }

    public int deleteByEntity(SysParam sysParam) {
        return sysParamDao.deleteByEntity(sysParam);
    }
  
    public int deleteByIds(List<Long> list) {
        return sysParamDao.deleteByIds(list);
    }

    public int countAll() {
        return sysParamDao.countAll();
    }
    
    public int countByEntity(SysParam sysParam) {
        return sysParamDao.countByEntity(sysParam);
    }

}