package com.mawkun.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.query.OperateOrderLogQuery;
import com.mawkun.core.base.entity.OperateOrderLog;
import com.mawkun.core.base.service.OperateOrderLogService;
import com.mawkun.core.dao.OperateOrderLogDaoExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperateOrderLogServiceExt extends OperateOrderLogService {
    @Autowired
    private OperateOrderLogDaoExt operateOrderLogDaoExt;

    public PageInfo<OperateOrderLog> pageList(OperateOrderLogQuery query) {
        query.init();
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<OperateOrderLog> list = operateOrderLogDaoExt.selectList(query);
        return new PageInfo<OperateOrderLog>(list);
    }
}
