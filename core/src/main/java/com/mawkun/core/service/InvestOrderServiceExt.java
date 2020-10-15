package com.mawkun.core.service;

import com.github.pagehelper.PageHelper;
import com.mawkun.core.base.data.PageInfo;
import com.mawkun.core.base.data.query.InvestOrderQuery;
import com.mawkun.core.base.data.vo.InvestOrderStatsVo;
import com.mawkun.core.base.entity.InvestOrder;
import com.mawkun.core.base.entity.MemberCard;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.base.service.InvestOrderService;
import com.mawkun.core.dao.InvestOrderDaoExt;
import com.mawkun.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 23:04:26
 */
@Service
public class InvestOrderServiceExt extends InvestOrderService {

    @Autowired
    private InvestOrderDaoExt investOrderDaoExt;

    public PageInfo pageList(InvestOrderQuery query) {
        query.init();
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        if(StringUtils.isNotEmpty(query.getUserName())) {
            query.setUserName("%" + query.getUserName() + "%");
        }
        if(StringUtils.isNotEmpty(query.getOrderNo())) {
            query.setOrderNo("%" + query.getOrderNo() + "%");
        }
        if(StringUtils.isNotEmpty(query.getMobile())) {
            query.setMobile("%" + query.getMobile() + "%");
        }
        List<InvestOrder> list = investOrderDaoExt.selectListByTerms(query);
        return new PageInfo<>(list);
    }

    public InvestOrder getByOrderNo(String orderNo) {
        InvestOrder investOrder = new InvestOrder();
        investOrder.setOrderNo(orderNo);
        return investOrderDaoExt.getByEntity(investOrder);
    }

    public InvestOrder getByOrderNoAndStatus(String orderNo, Integer status) {
        InvestOrder investLog = new InvestOrder();
        investLog.setOrderNo(orderNo);
        investLog.setStatus(status);
        return investOrderDaoExt.getByEntity(investLog);
    }

    public InvestOrderStatsVo statsInvestOrder(InvestOrderQuery query) {
        return investOrderDaoExt.statsInvestOrder(query);
    }

    public Long save(User user, MemberCard cart, Integer status, Long investMoney, Long giftMoney, Long amoutMoney, Long residueMoney) {
        InvestOrder investOrder = new InvestOrder();
        investOrder.setUserId(user.getId());
        investOrder.setUserName(user.getUserName());
        if(status != null) investOrder.setStatus(status);
        if(cart != null) investOrder.setCartId(cart.getId());
        if(cart != null) investOrder.setCartName(cart.getModelName());
        investOrder.setInvestMoney(investMoney);
        investOrder.setGiftMoney(giftMoney);
        investOrder.setAmountMoney(amoutMoney);
        investOrder.setResidueMoney(residueMoney);
        investOrder.setUpdateTime(new Date());
        investOrder.setCreateTime(new Date());
        investOrderDaoExt.insert(investOrder);
        return investOrder.getId();
    }

}