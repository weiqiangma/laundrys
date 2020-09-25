package com.mawkun.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.query.OrderLogQuery;
import com.mawkun.core.base.entity.OrderLog;
import com.mawkun.core.base.service.OrderLogService;
import com.mawkun.core.dao.OrderLogDaoExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author js
 */
@Service
public class OrderLogServiceExt extends OrderLogService {
    @Autowired
    private OrderLogDaoExt orderLogDaoExt;

    public PageInfo<OrderLog> pageList(OrderLogQuery query) {
        query.init();
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<OrderLog> list = orderLogDaoExt.selectList(query);
        return new PageInfo<OrderLog>(list);
    }

    /**
     * 创建生成订单的操作记录
     */
    public int createWaitingPayOrder(Long userId, String userName, Long orderId, Integer userKind, Integer status, String operate, String description) {
        OrderLog log = new OrderLog();
        log.setUserId(userId);
        log.setOrderId(orderId);
        log.setUserName(userName);
        log.setStatus(status);
        log.setUserKind(userKind);
        log.setOperate(operate);
        log.setDescription(description);
        log.setCreateTime(new Date());
        return orderLogDaoExt.insert(log);
    }
}
