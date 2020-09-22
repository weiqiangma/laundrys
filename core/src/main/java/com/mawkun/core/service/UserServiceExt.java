package com.mawkun.core.service;

import cn.pertech.common.spring.JsonResult;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.query.UserQuery;
import com.mawkun.core.base.data.vo.ShopUserVo;
import com.mawkun.core.base.data.vo.UserVo;
import com.mawkun.core.base.entity.MemberCard;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.base.service.UserService;
import com.mawkun.core.dao.ShopUserDaoExt;
import com.mawkun.core.dao.SysParamDaoExt;
import com.mawkun.core.dao.UserDaoExt;
import com.mawkun.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceExt extends UserService {

    @Autowired
    private UserDaoExt userDaoExt;
    @Autowired
    private ShopUserDaoExt shopUserDaoExt;
    @Autowired
    private InvestLogServiceExt investLogServiceExt;
    @Autowired
    private SysParamDaoExt sysParamDaoExt;
    @Autowired
    private WxApiServiceExt wxApiServiceExt;
    /**
     * 列表分页
     * @param query
     * @return
     */
    public PageInfo pageByEntity(UserQuery query) {
        query.init();
        if(!StringUtils.isEmpty(query.getUserName())) {
            query.setUserName("%" + query.getUserName() + "%");
        }
        if(!StringUtils.isEmpty(query.getMobile())) {
            query.setMobile("%" + query.getMobile() + "%");
        }
        List<UserVo> list = userDaoExt.pageByEntity(query);
        for(UserVo vo : list) {
            List<ShopUserVo> shopUserVos = shopUserDaoExt.selectShopNameByUserId(vo.getId());
            vo.setList(shopUserVos);
        }
        return new PageInfo(list);
    }

    public List<UserVo> listByEntity(UserQuery query) {
        query.init();
        if(!StringUtils.isEmpty(query.getUserName())) {
            query.setUserName("%" + query.getUserName() + "%");
        }
        if(!StringUtils.isEmpty(query.getMobile())) {
            query.setMobile("%" + query.getMobile() + "%");
        }
        return userDaoExt.pageByEntity(query);
    }

    public User getByOpenId(String openId) {
        User user = new User();
        user.setOpenId(openId);
        return userDaoExt.getByEntity(user);
    }

    /**
     * 充值
     */
    @Transactional
    public int rechargeMoney(User user, MemberCard card) {
        int result = -1;
        Long investMoney = card.getModelAmount();
        Long giftMoney = card.getModelGift();
        Long amountMoney = investMoney + giftMoney;
        Long residueMoeny = user.getSumOfMoney() + amountMoney;
        String orderNo = StringUtils.createRandomStr(18);
        wxApiServiceExt.unifyOrder(user.getOpenId(), orderNo, investMoney.toString(), Constant.INVEST_WITH_CARD, Constant.INVEST_WITH_CARD);
        //更新用户余额
        user.setSumOfMoney(user.getSumOfMoney() + amountMoney);
        try {
            result = userDaoExt.update(user);
            if (result < 1) throw new Exception("用户余额更新失败");
            result = investLogServiceExt.save(user, card, investMoney, giftMoney, amountMoney, residueMoeny);
            if (result < 1) throw new Exception("充值日志添加失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Transactional
    public int rechargeMoney(User user, Long money) {
        int result = -1;
        Long investMoney = money;
        long giftMoney = 0;
        Long amountMoney = money;
        Long residueMoeny = user.getSumOfMoney() + money;
        user.setSumOfMoney(user.getSumOfMoney() + money);
        try {
            result = userDaoExt.update(user);
            if (result < 1) throw new Exception("用户余额更新失败");
            result = investLogServiceExt.save(user, null, investMoney, giftMoney, amountMoney, residueMoeny);
            if (result < 1) throw new Exception("充值日志添加失败");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}