package com.mawkun.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.OrderFormQuery;
import com.mawkun.core.base.data.vo.GoodsVo;
import com.mawkun.core.base.data.vo.OrderFormVo;
import com.mawkun.core.base.entity.OrderForm;
import com.mawkun.core.base.entity.ShopUser;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.base.entity.UserAddress;
import com.mawkun.core.base.service.OrderFormService;
import com.mawkun.core.dao.GoodsDaoExt;
import com.mawkun.core.dao.OrderFormDaoExt;
import com.mawkun.core.dao.ShopUserDaoExt;
import com.mawkun.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderFormServiceExt extends OrderFormService {

    @Autowired
    OrderFormDaoExt orderFormDaoExt;
    @Autowired
    GoodsDaoExt goodsDaoExt;
    @Autowired
    private ShopUserDaoExt shopUserDaoExt;
    @Autowired
    UserAddressServiceExt userAddressServiceExt;

    /**
     * 列表分页
     * @param query
     * @return
     */
    public PageInfo<OrderFormVo> pageByEntity(UserSession session, OrderFormQuery query) {
        query.init();
        //如果是顾客只能查看自己的订单
        if(session.isCustomer()) {
            query.setUserId(session.getId());
        }
        //配送员可以查看与之关联店铺的订单
        if(session.isDistributor()) {
            ShopUser shopUser = new ShopUser();
            shopUser.setUserId(session.getId());
            List<ShopUser> suList = shopUserDaoExt.listByEntity(shopUser);
            List<Long> shopIdList = suList.stream().map(ShopUser::getShopId).collect(Collectors.toList());
            query.setShopIdList(shopIdList);
            query.setCreateTime(new Date());
        }
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<OrderFormVo> list = orderFormDaoExt.selectList(query);
        return new PageInfo<>(list);
    }

    /**
     * 订单详情
     * @param id
     * @return
     */
    public OrderFormVo getDetail(Long id) {
        OrderFormVo vo = orderFormDaoExt.selectDetail(id);
        List<GoodsVo> goodsList = goodsDaoExt.selectByOrderFormId(id);
        vo.setList(goodsList);
        return vo;
    }

    /**
     * 生成订单
     * @param user
     * @param query
     * @param address
     * @param resultAmount
     */
    @Transactional
    public void generateOrderForm(User user, OrderFormQuery query, UserAddress address, Long resultAmount) {
        String exactAddress = address.getExactAddress();
        //生成订单
        OrderForm form = new OrderForm();
        form.setUserId(user.getId());
        form.setShopId(query.getShopId());
        form.setAddressId(address.getId());
        form.setUserName(user.getUserName());
        form.setRemark(query.getRemark());
        form.setStatus(Constant.ORDER_STATUS_WAITING_PAY);
        form.setTotalAmount(query.getAmount());
        form.setRealAmount(resultAmount);
        form.setUserAddress(exactAddress);
        form.setTransportWay(query.getTransportWay());
        form.setPayKind(Constant.PAY_WITH_WEIXIN);
        form.setUpdateTime(new Date());
        form.setCreateTime(new Date());
        int orderKey = orderFormDaoExt.insert(form);
        String orderSerial = StringUtils.createOrderFormNo(String.valueOf(orderKey));
        form.setOrderSerial(orderSerial);
        orderFormDaoExt.update(form);
    }

}