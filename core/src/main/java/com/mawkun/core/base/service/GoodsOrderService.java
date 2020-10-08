package com.mawkun.core.base.service;

import cn.pertech.common.spring.JsonResult;
import cn.pertech.common.utils.NumberUtils;
import cn.pertech.common.utils.StringUtils;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.dao.OrderLogDao;
import com.mawkun.core.base.dao.GoodsOrderDao;
import com.mawkun.core.base.data.UserSession;
import com.mawkun.core.base.entity.GoodsOrder;
import com.mawkun.core.base.entity.OrderLog;
import jodd.typeconverter.Convert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-22 15:34:12
 */
@Service
public class GoodsOrderService {

    @Resource(type = GoodsOrderDao.class)
    private GoodsOrderDao goodsOrderDao;
    @Autowired
    private OrderLogDao orderLogDao;

    public GoodsOrderDao getGoodsOrderDao() {
        return goodsOrderDao;
    }

    public GoodsOrder getById(Long id) {
        return goodsOrderDao.getById(id);
    }

    public GoodsOrder getByEntity(GoodsOrder GoodsOrder) {
        return goodsOrderDao.getByEntity(GoodsOrder);
    }

    public List<GoodsOrder> listByEntity(GoodsOrder GoodsOrder) {
        if(StringUtils.isNotEmpty(GoodsOrder.getOrderNo())) {
            GoodsOrder.setOrderNo("%" + GoodsOrder.getOrderNo() + "%");
        }
        return goodsOrderDao.listByEntity(GoodsOrder);
    }

    public List<GoodsOrder> listByIds(List<Long> ids) {
        return goodsOrderDao.listByIds(ids);
    }

    public int insert(GoodsOrder GoodsOrder) {
        Date date = new Date();
        GoodsOrder.setCreateTime(date);
        GoodsOrder.setUpdateTime(date);
        return goodsOrderDao.insert(GoodsOrder);
    }

    public int insertBatch(List<GoodsOrder> list) {
        return goodsOrderDao.insertBatch(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancel(UserSession session, GoodsOrder order) {
        order.setStatus(Constant.ORDER_STATUS_CANCEL);
        goodsOrderDao.update(order);
        OrderLog log = new OrderLog();
        log.setUserId(session.getId());
        log.setOrderId(order.getId());
        log.setStatus(Constant.ORDER_STATUS_CANCEL);
        if(session.isAdmin()) {
            log.setUserKind(Constant.USER_TYPE_ADMIN);
        }
        if(session.isDistributor()) {
            log.setUserKind(Constant.USER_TYPE_DISTRIBUTOR);
        }
        if(session.isCustomer()) {
            log.setUserKind(Constant.USER_TYPE_CUSTOMER);
        }
        log.setDescription("确定操作无误");
        log.setCreateTime(new Date());
        orderLogDao.insert(log);
    }

    public int update(GoodsOrder order) {
        return goodsOrderDao.update(order);
    }

    public JsonResult update(UserSession session, GoodsOrder goodsOrder) {
        /**
         * 1.判断status不为空
         * 2.判断前端status==数据库status+1(保证流程按顺序执行)
         */
        Integer status = goodsOrder.getStatus();
        if(status != null) {
            GoodsOrder dbForm = goodsOrderDao.getById(goodsOrder.getId());
            if(!NumberUtils.equals(dbForm.getStatus() + 1, status) && !NumberUtils.equals(status, Constant.DELIVERY_ORDER_WAITING_REAP)){
                return new JsonResult().error("请按顺序执行订单流程");
            }
            String operate = "";
            //客户送至门店
            if(dbForm.getTransportWay() == Constant.ORDER_DELIVERY_SEND) {
                if(status == Constant.SELF_ORDER_WAITING_SEND) {
                    operate = "待送达门店";
                }
                else if(status == Constant.SELF_ORDER_SURE_SEND) {
                    operate = "确认送达门店";
                }
                else if(status == Constant.SELF_ORDER_CLEANING) {
                    operate = "清洗中";
                }
                else if(status == Constant.SELF_ORDER_WAITING_TAKE) {
                    operate = "待取货";
                }
                else if(status == Constant.SELF_ORDER_SURE_TAKE) {
                    operate = "已完成";
                } else {
                    return new JsonResult().success("该订单已完成");
                }
            }
            //配送员上门取货
            if(dbForm.getTransportWay() == Constant.ORDER_DELIVERY_GET) {
                if(status == Constant.DELIVERY_ORDER_WAITING_REAP) {
                    goodsOrder.setDistributorId(session.getId());
                    operate = "待收货";
                }
                else if(status == Constant.DELIVERY_ORDER_SURE_TAKE) {
                    operate = "确认收货";
                }
                else if(status == Constant.DELIVERY_ORDER_CLEANING) {
                    operate = "洗涤中";
                }
                else if(status == Constant.DELIVERY_ORDER_WAITING_TAKE) {
                    operate = "待送达";
                }
                else if(status == Constant.DELIVERY_ORDER_SURE_FINISH) {
                    operate = "已完成";
                } else {
                    return new JsonResult().success("该订单已完成");
                }
            }
            OrderLog log = new OrderLog();
            log.setUserId(session.getId());
            log.setUserName(session.getUserName());
            log.setOrderId(goodsOrder.getId());
            if(session.isAdmin()) {
                log.setUserKind(Constant.USER_TYPE_ADMIN);
            }
            if(session.isDistributor()) {
                log.setUserKind(Constant.USER_TYPE_DISTRIBUTOR);
            }
            if(session.isCustomer()) {
                log.setUserKind(Constant.USER_TYPE_CUSTOMER);
            }
            log.setOperate(operate);
            log.setStatus(status);
            log.setDescription("确定操作无误");
            log.setCreateTime(new Date());
            orderLogDao.insert(log);
        }
        goodsOrder.setUpdateTime(new Date());
        int result = goodsOrderDao.update(goodsOrder);
        return new JsonResult().success(Convert.toString(result));
    }

    public int updateBatch(List<GoodsOrder> list) {
        return goodsOrderDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return goodsOrderDao.deleteById(id);
    }

    public int deleteByEntity(GoodsOrder GoodsOrder) {
        return goodsOrderDao.deleteByEntity(GoodsOrder);
    }
  
    public int deleteByIds(List<Long> list) {
        return goodsOrderDao.deleteByIds(list);
    }

    public int countAll() {
        return goodsOrderDao.countAll();
    }
    
    public int countByEntity(GoodsOrder GoodsOrder) {
        return goodsOrderDao.countByEntity(GoodsOrder);
    }

}