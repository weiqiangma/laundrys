package com.mawkun.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.query.PayFlowQuery;
import com.mawkun.core.base.entity.InvestOrder;
import com.mawkun.core.base.entity.OrderForm;
import com.mawkun.core.base.entity.PayFlow;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.base.service.PayFlowService;
import com.mawkun.core.dao.PayFlowDaoExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Date 2020/9/24 16:35
 * @Author mawkun
 */
@Service
public class PayFlowServiceExt extends PayFlowService {
    @Autowired
    private PayFlowDaoExt payFlowDaoExt;

    public int createPayFlow(User user, OrderForm order, Integer orderType) {
        PayFlow payFlow = new PayFlow();
        payFlow.setUserId(user.getId());
        payFlow.setUserName(user.getUserName());
        payFlow.setOrderId(order.getId());
        payFlow.setOrderNo(order.getOrderSerial());
        payFlow.setOrderType(orderType);
        payFlow.setTotalFee(order.getTotalAmount());
        payFlow.setPayTime(order.getPayTime());
        payFlow.setUpdateTime(new Date());
        payFlow.setCreateTime(new Date());
        return payFlowDaoExt.insert(payFlow);
    }

    public int createPayFlow(User user, InvestOrder order, Integer orderType) {
        PayFlow payFlow = new PayFlow();
        payFlow.setUserId(user.getId());
        payFlow.setUserName(user.getUserName());
        payFlow.setOrderId(order.getId());
        payFlow.setOrderNo(order.getOrderNo());
        payFlow.setOrderType(orderType);
        payFlow.setTotalFee(order.getAmountMoney());
        payFlow.setPayTime(order.getPayTime());
        payFlow.setUpdateTime(new Date());
        payFlow.setCreateTime(new Date());
        return payFlowDaoExt.insert(payFlow);
    }

    public PageInfo pageList(PayFlowQuery query) {
        query.init();
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<PayFlow> list = payFlowDaoExt.listByEntity(query);
        return new PageInfo<>(list);
    }
}
