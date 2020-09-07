package com.mawkun.core.dao;

import com.mawkun.core.base.dao.OperateOrderLogDao;
import com.mawkun.core.base.data.query.OperateOrderLogQuery;
import com.mawkun.core.base.entity.OperateOrderLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperateOrderLogDaoExt extends OperateOrderLogDao {

    List<OperateOrderLog> selectList(OperateOrderLogQuery query);
}
