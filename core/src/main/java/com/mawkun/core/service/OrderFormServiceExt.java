package com.mawkun.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.OrderFormQuery;
import com.mawkun.core.base.data.vo.GoodsVo;
import com.mawkun.core.base.data.vo.OrderFormVo;
import com.mawkun.core.base.entity.*;
import com.mawkun.core.base.service.OrderFormService;
import com.mawkun.core.dao.*;
import com.mawkun.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderFormServiceExt extends OrderFormService {

    @Autowired
    OrderFormDaoExt orderFormDaoExt;
    @Autowired
    OrderClothesDaoExt orderClothesDaoExt;
    @Autowired
    GoodsDaoExt goodsDaoExt;
    @Autowired
    UserDaoExt userDaoExt;
    @Autowired
    private ShopUserDaoExt shopUserDaoExt;
    @Autowired
    UserAddressServiceExt userAddressServiceExt;
    @Autowired
    private OperateOrderLogServiceExt operateOrderLogServiceExt;
    @Autowired
    private ShoppingCartServiceExt shoppingCartServiceExt;
    @Autowired
    private OrderClothesServiceExt orderClothesServiceExt;

    /**
     * 列表分页
     * @param query
     * @return
     */
    public PageInfo<OrderFormVo> pageByEntity(OrderFormQuery query) {
        query.init();
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        if(StringUtils.isNotEmpty(query.getOrderSerial())) query.setOrderSerial("%" + query.getOrderSerial() + "%");
        if(StringUtils.isNotEmpty(query.getShopName())) query.setShopName("%" + query.getShopName() + "%");
        List<OrderFormVo> list = orderFormDaoExt.selectList(query);
        return new PageInfo<>(list);
    }

    public PageInfo<OrderFormVo> getDistributorOrder(Long userId, OrderFormQuery query) {
        query.init();
        ShopUser shopUser = new ShopUser();
        shopUser.setUserId(userId);
        List<ShopUser> suList = shopUserDaoExt.listByEntity(shopUser);
        List<Long> shopIdList = suList.stream().map(ShopUser::getShopId).collect(Collectors.toList());
        query.setShopIdList(shopIdList);
        query.setCreateTime(new Date());
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
        List<OrderClothes> goodsList = orderClothesServiceExt.getByOrderId(id);
        vo.setList(goodsList);
        return vo;
    }

    public OrderForm getByUserIdAndId(Long id, Long userId) {
        OrderForm order = new OrderForm();
        order.setId(id);
        order.setUserId(userId);
        return orderFormDaoExt.getByEntity(order);
    }

    /**
     * 生成订单
     * @param user
     * @param query
     * @param address
     * @param resultAmount
     */
    @Transactional
    public int generateOrderForm(User user, OrderFormQuery query, UserAddress address, Long resultAmount, List<ShoppingCart> cartList) throws Exception {
        int result = -1;
        //生成订单
        OrderForm form = new OrderForm();
        form.setUserId(user.getId());
        form.setShopId(query.getShopId());
        if(address != null)form.setAddressId(address.getId());
        form.setUserName(user.getUserName());
        form.setRemark(query.getRemark());
        form.setStatus(Constant.ORDER_STATUS_WAITING_PAY);
        form.setTotalAmount(query.getAmount());
        form.setRealAmount(resultAmount);
        if(address != null && StringUtils.isNotEmpty(address.getDetail())) {
            String exactAddress = address.getExactAddress();
            form.setUserAddress(exactAddress);
        }
        if(query.getTransportFee() != null) {
            form.setTransportFee(query.getTransportFee());
        }
        form.setTransportWay(query.getTransportWay());
        form.setPayKind(Constant.PAY_WITH_WEIXIN);
        form.setUpdateTime(new Date());
        form.setCreateTime(new Date());
        int orderKey = orderFormDaoExt.insert(form);
        if(orderKey < 1) {
            throw new Exception("订单插入失败");
        }
        //根据时间+主键生成订单
        String orderSerial = StringUtils.createOrderFormNo(String.valueOf(form.getId()));
        form.setOrderSerial(orderSerial);
        result = orderFormDaoExt.update(form);
        if(result < 1) {
            throw new Exception("生成订单号失败");
        }
        //生成订单操作记录
        result = operateOrderLogServiceExt.createWaitingPayOrder(user.getId(), form.getId(), Constant.ORDER_STATUS_WAITING_PAY, Constant.USER_TYPE_CUSTOMER, "用户创建待支付订单");
        if(result < 1) {
            throw new Exception("生成订单操作记录失败");
        }
        //订单中的商品加入订单商品表方便后续查询
        result = orderClothesServiceExt.addByShoppingCarts(cartList, form.getId());
        if(result < 1) {
            throw new Exception("订单商品加入失败");
        }
        //清空购物车
        result = shoppingCartServiceExt.deleteByUserId(user.getId());
        if(result < 1) {
            throw new Exception("清空购物车失败");
        }
        //更新用户余额
        result = userDaoExt.update(user);
        if(result < 1) {
            throw new Exception("用户余额更新失败");
        }
        return result;
    }

    public void orderTaking(UserSession session, OrderForm order) {
        /**
         * 1.更新订单状态
         * 2.生成订单操作记录
         */
        order.setStatus(Constant.ORDER_STATUS_SURE_REAP);
        orderFormDaoExt.update(order);
        //operateOrderLogServiceExt.createWaitingPayOrder(session.getId(), order.getId())
    }

}