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
    private GoodsOrderDao GoodsOrderDao;
    @Autowired
    private OrderLogDao orderLogDao;

    public GoodsOrderDao getGoodsOrderDao() {
        return GoodsOrderDao;
    }

    public GoodsOrder getById(Long id) {
        return GoodsOrderDao.getById(id);
    }

    public GoodsOrder getByEntity(GoodsOrder GoodsOrder) {
        return GoodsOrderDao.getByEntity(GoodsOrder);
    }

    public List<GoodsOrder> listByEntity(GoodsOrder GoodsOrder) {
        if(StringUtils.isNotEmpty(GoodsOrder.getOrderNo())) {
            GoodsOrder.setOrderNo("%" + GoodsOrder.getOrderNo() + "%");
        }
        return GoodsOrderDao.listByEntity(GoodsOrder);
    }

    public List<GoodsOrder> listByIds(List<Long> ids) {
        return GoodsOrderDao.listByIds(ids);
    }

    public int insert(GoodsOrder GoodsOrder) {
        Date date = new Date();
        GoodsOrder.setCreateTime(date);
        GoodsOrder.setUpdateTime(date);
        return GoodsOrderDao.insert(GoodsOrder);
    }

    public int insertBatch(List<GoodsOrder> list) {
        return GoodsOrderDao.insertBatch(list);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancel(UserSession session, GoodsOrder order) {
        order.setStatus(Constant.ORDER_STATUS_CANCEL);
        GoodsOrderDao.update(order);
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

    public JsonResult update(UserSession session, GoodsOrder GoodsOrder) {
        /**
         * 1.判断status不为空
         * 2.判断前端status==数据库status+1(保证流程按顺序执行)
         */
        Integer status = GoodsOrder.getStatus();
        if(status != null) {
            GoodsOrder dbForm = GoodsOrderDao.getById(GoodsOrder.getId());
            if(!NumberUtils.equals(dbForm.getStatus() + 1, status)){
                return new JsonResult().success("请按顺序执行订单流程");
            }
            String operate = "";
            OrderLog log = new OrderLog();
            if(status == Constant.ORDER_STATUS_WAITING_REAP) {
                operate = "待收货";
            } else if(status == Constant.ORDER_STATUS_SURE_REAP) {
                operate = "确认收货";
            } else if(status == Constant.ORDER_STATUS_CLEANING) {
                operate = "洗涤中";
            } else if(status == Constant.ORDER_STATUS_WAITING_TAKE) {
                operate = "待取货";
            } else if(status == Constant.ORDER_STATUS_SURE_TAKE) {
                operate = "已完成";
            } else if(status == Constant.ORDER_STATUS_CANCEL) {
                operate = "取消订单";
            } else {
                return new JsonResult().success("该订单已完成");
            }
            log.setUserId(session.getId());
            log.setOrderId(GoodsOrder.getId());
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
        GoodsOrder.setUpdateTime(new Date());
        int result = GoodsOrderDao.update(GoodsOrder);
        return new JsonResult().success(Convert.toString(result));
    }

    public int updateBatch(List<GoodsOrder> list) {
        return GoodsOrderDao.updateBatch(list);
    }

    public int deleteById(Long id) {
        return GoodsOrderDao.deleteById(id);
    }

    public int deleteByEntity(GoodsOrder GoodsOrder) {
        return GoodsOrderDao.deleteByEntity(GoodsOrder);
    }
  
    public int deleteByIds(List<Long> list) {
        return GoodsOrderDao.deleteByIds(list);
    }

    public int countAll() {
        return GoodsOrderDao.countAll();
    }
    
    public int countByEntity(GoodsOrder GoodsOrder) {
        return GoodsOrderDao.countByEntity(GoodsOrder);
    }

}