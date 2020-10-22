package com.mawkun.core.service;

import cn.pertech.common.utils.CryptUtils;
import cn.pertech.common.utils.NumberUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.query.UserQuery;
import com.mawkun.core.base.data.vo.ShopUserVo;
import com.mawkun.core.base.data.vo.UserVo;
import com.mawkun.core.base.entity.*;
import com.mawkun.core.base.service.UserService;
import com.mawkun.core.dao.ShopUserDaoExt;
import com.mawkun.core.dao.SysParamDaoExt;
import com.mawkun.core.dao.UserDaoExt;
import com.mawkun.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceExt extends UserService {

    @Autowired
    private UserDaoExt userDaoExt;
    @Autowired
    private ShopUserDaoExt shopUserDaoExt;
    @Autowired
    private InvestOrderServiceExt investOrderServiceExt;
    @Autowired
    private SysParamDaoExt sysParamDaoExt;
    @Autowired
    private WxApiServiceExt wxApiServiceExt;
    @Resource
    private ShopServiceExt shopServiceExt;
    @Resource
    private AdminServiceExt adminServiceExt;
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

    public User getByMobile(String mobile) {
        User user = new User();
        user.setMobile(mobile);
        user.setStatus(Constant.USER_STATUS_ACTIVE);
        return userDaoExt.getByEntity(user);
    }

    public User getDistributorByMobile(String mobile) {
        User user = new User();
        user.setMobile(mobile);
        user.setKind(Constant.USER_TYPE_DISTRIBUTOR);
        user.setStatus(Constant.USER_STATUS_ACTIVE);
        return userDaoExt.getByEntity(user);
    }

    public List<User> getByIdAndStatus(Long id, Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        return userDaoExt.listByEntity(user);
    }

    /**
     * 充值
     */
    @Transactional
    public JSONObject rechargeMoney(User user, MemberCard card, String notifyUrl) {
        Long investMoney = card.getModelAmount();
        Long giftMoney = card.getModelGift();
        Long amountMoney = investMoney + giftMoney;
        long sumOfMoney = (user.getSumOfMoney() == null) ? 0 : user.getSumOfMoney();
        Long residueMoeny = sumOfMoney + amountMoney;
        //生成待支付订单
        long primaryKey = investOrderServiceExt.save(user, card, Constant.INVEST_ORDER_WAITING_WAY, investMoney, giftMoney, amountMoney, residueMoeny);
        String orderNo = StringUtils.createOrderFormNo(String.valueOf(primaryKey));
        InvestOrder investOrder = investOrderServiceExt.getById(primaryKey);
        investOrder.setOrderNo(orderNo);
        investOrder.setCreateTime(new Date());
        investOrder.setUpdateTime(new Date());
        investOrderServiceExt.update(investOrder);
        //调用微信接口生成支付参数供前端调用
        JSONObject object = wxApiServiceExt.unifyOrder(user.getOpenId(), orderNo, investMoney.toString(), Constant.INVEST_WITH_CARD, Constant.INVEST_WITH_CARD, notifyUrl);
        if(object != null) {
            object.put("orderNo", orderNo);
        }
        return object;
    }

    @Transactional
    public JSONObject rechargeMoney(User user, Long money, String notifyUrl) {
        Long investMoney = money;
        long giftMoney = 0;
        Long amountMoney = money;
        Long residueMoeny = user.getSumOfMoney() + money;
        //生成待支付订单
        long primaryKey = investOrderServiceExt.save(user, null, Constant.INVEST_ORDER_WAITING_WAY, investMoney, giftMoney, amountMoney, residueMoeny);
        String orderNo = StringUtils.createOrderFormNo(String.valueOf(primaryKey));
        InvestOrder investLog = investOrderServiceExt.getById(primaryKey);
        investLog.setOrderNo(orderNo);
        investOrderServiceExt.update(investLog);
        //调用微信接口生成支付参数供前端调用
        JSONObject object = wxApiServiceExt.unifyOrder(user.getOpenId(), orderNo, investMoney.toString(), Constant.INVEST_WITH_CARD, Constant.INVEST_WITH_CARD, notifyUrl);
        if(object != null) {
            object.put("orderNo", orderNo);
        }
        return object;
    }

    /**
     * 把用户设置为配送员
     * @param user
     * @param shopIds
     * @return
     */
    public int setUserToDistributor(User user, String password, String shopIds) {
        if(StringUtils.isNotEmpty(shopIds)) {
            List<String> shopIdList = Arrays.asList(shopIds.split(","));
            shopUserDaoExt.deleteByUserId(user.getId());
            for(String shopId : shopIdList) {
                Long sId = NumberUtils.str2Long(shopId);
                ShopUser shopUser = new ShopUser();
                shopUser.setUserId(user.getId());
                shopUser.setShopId(sId);
                shopUserDaoExt.insert(shopUser);
            }
        }

        Shop shop = shopServiceExt.getFirstLevelShop();
        Admin admin = new Admin();
        admin.setMobile(user.getMobile());
        admin.setPassword(password);
        admin.setLevel(Constant.ADMIN_TYPE_DISTRIBUTOR);
        admin.setUserName(user.getUserName());
        admin.setRealName(user.getRealName());
        admin.setStatus(Constant.USER_STATUS_ACTIVE);
        if(shop != null) admin.setShopId(shop.getId());
        admin.setUpdateTime(new Date());
        admin.setCreateTime(new Date());
        adminServiceExt.insert(admin);
        user.setUpdateTime(new Date());
        user.setKind(Constant.USER_TYPE_DISTRIBUTOR);
        return userDaoExt.update(user);
    }

    public int deleteDistributor(User user) {
        user.setKind(Constant.USER_TYPE_CUSTOMER);
        userDaoExt.update(user);
        shopUserDaoExt.deleteByUserId(user.getId());
        return adminServiceExt.deleteByMobile(user.getMobile());
    }

    public int updateDistributor(User user, String password, String shopIds) {
        if(StringUtils.isNotEmpty(shopIds)) {
            List<String> shopIdList = Arrays.asList(shopIds.split(","));
            shopUserDaoExt.deleteByUserId(user.getId());
            for(String shopId : shopIdList) {
                Long sId = NumberUtils.str2Long(shopId);
                ShopUser shopUser = new ShopUser();
                shopUser.setUserId(user.getId());
                shopUser.setShopId(sId);
                shopUserDaoExt.insert(shopUser);
            }
        }
        Admin admin = adminServiceExt.getByMobile(user.getMobile());
        if(password != null) admin.setPassword(password);
        if(user.getUserName() != null) admin.setUserName(user.getUserName());
        if(user.getRealName() != null) admin.setRealName(user.getRealName());
        admin.setUpdateTime(new Date());
        return adminServiceExt.update(admin);
    }
}