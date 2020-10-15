package com.mawkun.core.service;

import cn.pertech.common.utils.DateUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.dao.GoodsOrderDao;
import com.mawkun.core.base.data.PageInfo;
import com.mawkun.core.base.data.ShopOrderData;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.data.query.GoodsOrderQuery;
import com.mawkun.core.base.data.query.GoodsQuery;
import com.mawkun.core.base.data.query.StateQuery;
import com.mawkun.core.base.data.vo.GoodsOrderVo;
import com.mawkun.core.base.data.vo.ShopUserVo;
import com.mawkun.core.base.entity.*;
import com.mawkun.core.base.service.GoodsOrderService;
import com.mawkun.core.dao.*;
import com.mawkun.core.utils.StringUtils;
import com.mawkun.core.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * @author js
 */
@Service
public class GoodsOrderServiceExt extends GoodsOrderService {

    @Resource
    GoodsOrderDaoExt goodsOrderDaoExt;
    @Resource
    GoodsOrderDao goodsOrderDao;
    @Resource
    UserDaoExt userDaoExt;
    @Resource
    ShopUserDaoExt shopUserDaoExt;
    @Resource
    OrderLogServiceExt orderLogServiceExt;
    @Resource
    ShoppingCartServiceExt shoppingCartServiceExt;
    @Resource
    OrderClothesServiceExt orderClothesServiceExt;

    /**
     * 列表分页
     * @param query
     * @return
     */
    public PageInfo<GoodsOrderVo> pageByEntity(GoodsOrderQuery query) {
        query.init();
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        if(StringUtils.isNotEmpty(query.getOrderNo())) {
            query.setOrderNo("%" + query.getOrderNo() + "%");
        }
        if(StringUtils.isNotEmpty(query.getShopName())) {
            query.setShopName("%" + query.getShopName() + "%");
        }
        if(StringUtils.isNotEmpty(query.getLinkMobile1())) {
            query.setLinkMobile1("%" + query.getLinkMobile1() + "%");
        }
        if(StringUtils.isNotEmpty(query.getLinkMobile2())) {
            query.setLinkMobile2("%" + query.getLinkMobile2() + "%");
        }
        if(StringUtils.isNotEmpty(query.getDistributorMobile())) {
            query.setLinkMobile2("%" + query.getDistributorMobile() + "%");
        }
        List<GoodsOrderVo> list = goodsOrderDaoExt.selectList(query);
        for(GoodsOrderVo orderVo : list) {
            List<OrderClothes> orderClothes = orderClothesServiceExt.getByOrderId(orderVo.getId());
            orderVo.setList(orderClothes);
        }
        return new PageInfo<>(list);
    }

    public int setOrderIsOld(List<GoodsOrderVo> list) {
        return goodsOrderDaoExt.setOrderIsOld(list);
    }

    /**
     * 配送员订单
     * @param userId
     * @param query
     * @return
     */
    public PageInfo<GoodsOrderVo> getDistributorOrder(Long userId, GoodsOrderQuery query) {
        query.init();
        if(query.getType() == 2) {
            query.setDistributorId(userId);
        }
        ShopUser shopUser = new ShopUser();
        shopUser.setUserId(userId);
        List<ShopUser> suList = shopUserDaoExt.listByEntity(shopUser);
        List<Long> shopIdList = suList.stream().map(ShopUser::getShopId).collect(Collectors.toList());
        query.setShopIdList(shopIdList);
        //query.setCreateTime(new Date());
        //query.setTransportWay(Constant.ORDER_DELIVERY_GET);
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<GoodsOrderVo> list = goodsOrderDaoExt.selectList(query);
        List<GoodsOrderVo> resultList = list.stream().filter(item -> !item.getUserId().equals(userId) || item.getStatus() == Constant.ORDER_STATUS_CANCEL).collect(Collectors.toList());
        //配送员已结单过滤已完成的订单
        if(query.getType() == 2) {
            resultList = resultList.stream().filter(item -> item.getStatus() != Constant.DELIVERY_ORDER_SURE_FINISH).collect(Collectors.toList());
        }
        if(!resultList.isEmpty()) {
            resultList.forEach(item -> item.setIsnew(Constant.ORDER_OLD));
            goodsOrderDaoExt.setOrderIsOld(resultList);
            for (GoodsOrderVo orderVo : resultList) {
                List<OrderClothes> orderClothes = orderClothesServiceExt.getByOrderId(orderVo.getId());
                orderVo.setList(orderClothes);
            }
        }
        return new PageInfo<>(resultList);
    }

    /**
     * 根据订单号和状态查询
     * @param orderNo
     * @return
     */
    public GoodsOrder getByOrderSerialAndStatus(String orderNo, Integer status) {
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setOrderNo(orderNo);
        goodsOrder.setStatus(status);
        return goodsOrderDao.getByEntity(goodsOrder);
    }

    public List<GoodsOrder> getGoodsOrderList(GoodsOrderQuery query) {
        return goodsOrderDaoExt.getGoodsOrderList(query);
    }

    public ShopOrderData statsGoodsOrder(GoodsOrderQuery query) {
        return goodsOrderDaoExt.statsGoodsOrder(query);
    }

    public GoodsOrder getByOrderNo(String orderNo) {
        GoodsOrder goodsOrder = new GoodsOrder();
        goodsOrder.setOrderNo(orderNo);
        return goodsOrderDao.getByEntity(goodsOrder);
    }

    /**
     * 订单详情
     * @param id
     * @return
     */
    public GoodsOrderVo getDetail(Long id) {
        GoodsOrderVo vo = goodsOrderDaoExt.selectDetail(id);
        List<OrderClothes> goodsList = orderClothesServiceExt.getByOrderId(id);
        vo.setList(goodsList);
        return vo;
    }

    public GoodsOrder getByUserIdAndId(Long id, Long userId) {
        GoodsOrder order = new GoodsOrder();
        order.setId(id);
        order.setUserId(userId);
        return goodsOrderDao.getByEntity(order);
    }

    /**
     * 生成待支付订单
     * @param user
     * @param query
     * @param address
     * @param resultAmount
     */
    @Transactional
    public GoodsOrder generateWaitingPayOrderForm(User user, Shop shop, GoodsOrderQuery query, UserAddress address, Long resultAmount, List<ShoppingCart> cartList, Integer payKind) throws Exception {
        long result = -1;
        long orderKey = -1;
        //生成订单
        GoodsOrder form = new GoodsOrder();
        form.setUserId(user.getId());
        form.setShopName(shop.getShopName());
        form.setShopId(shop.getId());
        if(address != null) {
            form.setAddressId(address.getId());
        }
        form.setUserName(user.getUserName());
        form.setRemark(query.getRemark());
        form.setStatus(Constant.ORDER_STATUS_WAITING_PAY);
        form.setOrderType(Constant.ORDER_ONLINE);
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
        form.setPayKind(payKind);
        form.setUpdateTime(new Date());
        form.setCreateTime(new Date());
        result = goodsOrderDao.insert(form);
        if(result < 1) {
            throw new Exception("订单插入失败");
        }
        //根据时间+主键生成订单
        String orderSerial = StringUtils.createOrderFormNo(String.valueOf(form.getId()));
        form.setOrderNo(orderSerial);
        goodsOrderDao.update(form);
        orderKey = form.getId();
        if(result < 1) {
            throw new Exception("生成订单号失败");
        }
        //生成订单操作记录
        result = orderLogServiceExt.createWaitingPayOrder(user.getId(), user.getUserName(), form.getId(), Constant.ORDER_STATUS_WAITING_PAY, Constant.USER_TYPE_CUSTOMER, "用户创建待支付订单", null);
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
        return form;
    }

    /**
     * 配送员确认收货
     * @param userId
     * @param order
     * @param description
     * @return
     */
    public int orderTaking(Long userId, GoodsOrder order, String description) {
        order.setStatus(Constant.DELIVERY_ORDER_SURE_TAKE);
        order.setDistributorId(userId);
        return goodsOrderDao.update(order);
    }

    /**
     * 配送员确认接单
     * @param userId
     * @param order
     */
    public int orderSure(Long userId, GoodsOrder order) {
        order.setDistributorId(userId);
        return goodsOrderDao.update(order);
    }

    /**
     * 检查订单是否属于该配送员所属门店
     * @param userId
     * @param orderId
     * @return
     */
    public boolean checkOrderIsDistributor(Long userId, Long orderId) {
        ShopUser shopUser = new ShopUser();
        shopUser.setUserId(userId);
        List<ShopUser> list = shopUserDaoExt.listByEntity(shopUser);
        List<Long> shopList = list.stream().map(ShopUser::getShopId).collect(Collectors.toList());
        GoodsOrderQuery query = new GoodsOrderQuery();
        query.setShopIdList(shopList);
        query.setId(orderId);
        List<GoodsOrderVo> orderList = goodsOrderDaoExt.selectList(query);
        if(orderList != null && orderList.size() > 0) {
            return true;
        }
        return false;
    }


    /**
     * 获取新订单
     * @param userId
     * @return
     */
    public List<GoodsOrderVo> getDistributorNewOrder(Long userId) {
        List<ShopUserVo> spUserList = shopUserDaoExt.selectShopNameByUserId(userId);
        List<Long> shopIdList = spUserList.stream().map(ShopUserVo::getShopId).collect(Collectors.toList());
        GoodsOrderQuery query = new GoodsOrderQuery();
        query.setShopIdList(shopIdList);
        query.setIsnew(Constant.ORDER_NEW);
        return goodsOrderDaoExt.selectList(query);
    }

    public List<GoodsOrderVo> getShopNewOrder(GoodsOrderQuery query) {
        query.setIsnew(Constant.ORDER_NEW);
        return goodsOrderDaoExt.selectList(query);
    }

    /**
     * 统计配送员订单
     * @param userId
     * @return
     */
    public JSONObject statsDistributorOrder(Long userId, Integer transportway) {
        GoodsOrderQuery query = new GoodsOrderQuery();
        query.setDistributorId(userId);
        query.setTransportWay(transportway);
        List<GoodsOrderVo> resultList = goodsOrderDaoExt.selectList(query);
        List<GoodsOrderVo> list = resultList.stream().filter(item -> item.getUserId() != userId).collect(Collectors.toList());
        List<GoodsOrderVo> waitTakeOrders = list.stream().filter(item -> item.getStatus() == Constant.DELIVERY_ORDER_WAITING_REAP).collect(Collectors.toList());
        List<GoodsOrderVo> sureTakeOrders = list.stream().filter(item -> item.getStatus() == Constant.DELIVERY_ORDER_SURE_TAKE).collect(Collectors.toList());
        List<GoodsOrderVo> waitSendOrders = list.stream().filter(item -> item.getStatus() == Constant.DELIVERY_ORDER_WAITING_TAKE).collect(Collectors.toList());
        List<GoodsOrderVo> finishOrders = list.stream().filter(item -> item.getStatus() == Constant.DELIVERY_ORDER_SURE_FINISH).collect(Collectors.toList());

        GoodsOrderQuery orderQuery = new GoodsOrderQuery();
        ShopUser shopUser = new ShopUser();
        shopUser.setUserId(userId);
        List<ShopUser> suList = shopUserDaoExt.listByEntity(shopUser);
        List<Long> shopIdList = suList.stream().map(ShopUser::getShopId).collect(Collectors.toList());
        orderQuery.setShopIdList(shopIdList);
        orderQuery.setTransportWay(transportway);
        orderQuery.setType(1);
        List<GoodsOrderVo> orderVos = goodsOrderDaoExt.selectList(orderQuery);
        List<GoodsOrderVo> resultOrders = orderVos.stream().filter(item -> !item.getUserId().equals(userId)).collect(Collectors.toList());

        JSONObject object = new JSONObject();
        if(query.getTransportWay() == Constant.ORDER_DELIVERY_GET) {
            Long finishOrderTransportFee = finishOrders.stream().mapToLong(GoodsOrder::getTransportFee).sum();
            object.put("waitTakeCount", waitTakeOrders.size());
            object.put("finishOrderTransportFee", finishOrderTransportFee);
        }
        object.put("waitTakeCount", waitTakeOrders.size());
        object.put("waitSureCount", resultOrders.size());
        object.put("sureTakeCount", sureTakeOrders.size());
        object.put("waitSendCount", waitSendOrders.size());
        object.put("finishCount", finishOrders.size());
        return object;
    }

    /**
     * 统计配送员订单(后台图表)
     * @param query
     */
    public JSONArray statsDistributorOrder(StateQuery query) {
        fillQueryData(query);
        List<ShopOrderData> list = goodsOrderDaoExt.statsShopOrder(query);
        //根据type进行分组
        Map<String, ShopOrderData> dataMap = list.stream().collect(Collectors.toMap(ShopOrderData::getType, m->m));
        Date sTime = query.getStartTime();
        Calendar ca = Calendar.getInstance();
        ca.setTime(sTime);
        JSONArray array = new JSONArray();
        for(int i = 0; i < query.getDateCount(); i++) {
            String key = "";
            if(query.getType()==1){
                key = (i < 10) ? String.valueOf("0"+i):String.valueOf(i);
            }else if(query.getType()==2){
                key = DateUtils.format("yyyy-MM-dd",ca.getTime());
            }else if(query.getType()==3 || query.getType()==4){
                key = DateUtils.format("yyyy-MM-dd",ca.getTime());
            }else{
                continue;
            }
            ShopOrderData data = dataMap.get(key);
            JSONObject object = new JSONObject();
            String shopName = (data == null) ? "" : data.getShopName();
            if(query.getShopId() == null) shopName = "";
            Integer amount = (data == null) ? 0 : data.getAmount();
            Integer fee = (data == null) ? 0 : data.getFee();
            object.put("time", key);
            object.put("fee", fee);
            object.put("amount", amount);
            array.add(object);
            ca.add(Calendar.DAY_OF_MONTH,1);
        }
        return array;
    }

    private void fillQueryData(StateQuery queryVO) {
        Date now = new Date();
        if(queryVO.getType() == 1) {
            //当天统计
            queryVO.setStartTime(DateUtils.dateStartDate(now));
            queryVO.setEndTime(DateUtils.dateEndDate(now));
            queryVO.setFormatCode("%H");
            queryVO.setDateCount(24);
        }else if(queryVO.getType() == 2) {
            //本周统计
            queryVO.setStartTime(TimeUtils.getWeekStart());
            queryVO.setEndTime(TimeUtils.getWeekEnd());
            queryVO.setFormatCode("%Y-%m-%d");
            queryVO.setDateCount(7);
        }else if(queryVO.getType() == 3) {
            //本月统计
            queryVO.setStartTime(TimeUtils.getMonthStart());
            queryVO.setEndTime(TimeUtils.getMonthEnd());
            queryVO.setFormatCode("%Y-%m-%d");
            int year = DateUtils.getYear();
            int month = DateUtils.getMonth();
            queryVO.setDateCount(DateUtils.getDaysOfMonth(year,month));
        }else if(queryVO.getType()==4){
            //自定义传入
            queryVO.setFormatCode("%Y-%m-%d");
            queryVO.setStartTime(DateUtils.dateStartDate(queryVO.getStartTime()));
            //最后一天要取到时间
            queryVO.setEndTime(DateUtils.dateEndDate(queryVO.getEndTime()));
            int diff = (int)DateUtils.dayDiff(queryVO.getStartTime(), queryVO.getEndTime());
            queryVO.setDateCount(diff);
        }
        //System.out.println("格式化后范围: "+JsonUtils.toString(queryVO));
    }

}