package com.mawkun.core.service;

import cn.pertech.common.spring.JsonResult;
import com.github.pagehelper.PageInfo;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserServiceExt extends UserService {

    @Resource
    private UserDaoExt userDaoExt;
    @Resource
    private ShopUserDaoExt shopUserDaoExt;
    @Resource
    private InvestLogServiceExt investLogServiceExt;
    @Resource
    private SysParamDaoExt sysParamDaoExt;
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
    public JsonResult rechargeMoney(User user, MemberCard cart, Long cartNum) {
        Long investMoney = cart.getModelAmount() * cartNum;
        Long giftMoney = cart.getModelAmount() * cartNum;
        Long amountMoney = investMoney + giftMoney;
        user.setSumOfMoney(user.getSumOfMoney() + amountMoney);
        Long residueMoeny = user.getSumOfMoney();
        user.setSumOfMoney(user.getSumOfMoney() + amountMoney);
        int upResult = userDaoExt.update(user);
        if(upResult < 1) return new JsonResult().error("用户充值失败");
        int saveResult = investLogServiceExt.save(user, cart, cartNum, investMoney, giftMoney, amountMoney, residueMoeny);
        if(saveResult < 1) return new JsonResult().error("用户充值日志添加失败");
        return new JsonResult().success("用户充值成功");
    }
}