package com.mawkun.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.query.InvestLogQuery;
import com.mawkun.core.base.entity.InvestLog;
import com.mawkun.core.base.entity.MemberCard;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.base.service.InvestLogService;
import com.mawkun.core.dao.InvestLogDaoExt;
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

    public int save(User user, MemberCard cart, Integer cartNum, Double investMoney, Double giftMoney, Double amoutMoney, Double residueMoney) {
        InvestLog investLog = new InvestLog();
        investLog.setUserId(user.getId());
        investLog.setUserName(user.getUserName());
        investLog.setCartId(cart.getId());
        investLog.setCartName(cart.getModelName());
        investLog.setCartNum(cartNum);
        investLog.setInvestMoney(investMoney);
        investLog.setGiftMoney(giftMoney);
        investLog.setAmountMoney(amoutMoney);
        investLog.setResidueMoney(residueMoney);
        investLog.setCreateTime(new Date());
        return investLogDaoExt.insert(investLog);
    }

}