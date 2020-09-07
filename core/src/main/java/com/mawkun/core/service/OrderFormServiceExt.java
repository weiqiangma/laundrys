package com.mawkun.core.service;

import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.query.OrderFormQuery;
import com.mawkun.core.base.data.vo.GoodsVo;
import com.mawkun.core.base.data.vo.OrderFormVo;
import com.mawkun.core.base.service.OrderFormService;
import com.mawkun.core.dao.GoodsDaoExt;
import com.mawkun.core.dao.OrderFormDaoExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderFormServiceExt extends OrderFormService {

    @Autowired
    OrderFormDaoExt orderFormDaoExt;
    @Autowired
    GoodsDaoExt goodsDaoExt;

    /**
     * 列表分页
     * @param orderFormQuery
     * @return
     */
    public PageInfo pageByEntity(OrderFormQuery orderFormQuery) {
        orderFormQuery.init();
        List<OrderFormVo> list = orderFormDaoExt.selectList(orderFormQuery);
        return new PageInfo(list);
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

}