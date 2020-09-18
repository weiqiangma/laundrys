package com.mawkun.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.query.OrderFormQuery;
import com.mawkun.core.base.data.vo.GoodsVo;
import com.mawkun.core.base.data.vo.OrderFormVo;
import com.mawkun.core.base.entity.OrderForm;
import com.mawkun.core.base.entity.User;
import com.mawkun.core.base.entity.UserAddress;
import com.mawkun.core.base.service.OrderFormService;
import com.mawkun.core.dao.GoodsDaoExt;
import com.mawkun.core.dao.OrderFormDaoExt;
import com.mawkun.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderFormServiceExt extends OrderFormService {

    @Autowired
    OrderFormDaoExt orderFormDaoExt;
    @Autowired
    GoodsDaoExt goodsDaoExt;
    @Autowired
    UserAddressServiceExt userAddressServiceExt;

    /**
     * 列表分页
     * @param orderFormQuery
     * @return
     */
    public PageInfo<OrderFormVo> pageByEntity(OrderFormQuery orderFormQuery) {
        orderFormQuery.init();
        PageHelper.startPage(orderFormQuery.getPageNo(), orderFormQuery.getPageSize());
        List<OrderFormVo> list = orderFormDaoExt.selectList(orderFormQuery);
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
    public void generateOrderForm(User user, OrderFormQuery query, UserAddress address, Double resultAmount) {
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