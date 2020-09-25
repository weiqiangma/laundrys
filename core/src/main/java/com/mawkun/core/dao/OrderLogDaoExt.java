package com.mawkun.core.dao;

import com.mawkun.core.base.dao.OrderLogDao;
import com.mawkun.core.base.data.query.OrderLogQuery;
import com.mawkun.core.base.entity.OrderLog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderLogDaoExt extends OrderLogDao {

    List<OrderLog> selectList(OrderLogQuery query);
}
