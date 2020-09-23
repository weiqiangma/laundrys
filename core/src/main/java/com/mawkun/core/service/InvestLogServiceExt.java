package com.mawkun.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.query.InvestLogQuery;
import com.mawkun.core.base.entity.InvestLog;
import com.mawkun.core.base.entity.MemberCard;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.base.service.InvestLogService;
import com.mawkun.core.dao.InvestLogDaoExt;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 23:04:26
 */
@Service
public class InvestLogServiceExt extends InvestLogService {

    @Autowired
    private InvestLogDaoExt investLogDaoExt;

    public PageInfo pageList(InvestLogQuery query) {
        query.init();
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<InvestLog> list = investLogDaoExt.selectListByTerms(query);
        return new PageInfo<>(list);
    }

    public InvestLog getByOrderNo(String orderNo) {
        InvestLog investLog = new InvestLog();
        investLog.setOrderNo(orderNo);
        return investLogDaoExt.getByEntity(investLog);
    }

    public int save(User user, MemberCard cart, Integer status, Long investMoney, Long giftMoney, Long amoutMoney, Long residueMoney) {
        InvestLog investLog = new InvestLog();
        investLog.setUserId(user.getId());
        investLog.setUserName(user.getUserName());
        if(status != null) investLog.setStatus(status);
        if(cart != null) investLog.setCartId(cart.getId());
        if(cart != null) investLog.setCartName(cart.getModelName());
        investLog.setInvestMoney(investMoney);
        investLog.setGiftMoney(giftMoney);
        investLog.setAmountMoney(amoutMoney);
        investLog.setResidueMoney(residueMoney);
        investLog.setCreateTime(new Date());
        return investLogDaoExt.insert(investLog);
    }

}