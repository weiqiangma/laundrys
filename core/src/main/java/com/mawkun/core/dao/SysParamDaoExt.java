package com.mawkun.core.dao;

import com.mawkun.core.base.dao.SysParamDao;
import com.mawkun.core.base.entity.SysParam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysParamDaoExt extends SysParamDao {

    String selectSysValue(String realName);

    List<SysParam> selectTransportFee();

}
