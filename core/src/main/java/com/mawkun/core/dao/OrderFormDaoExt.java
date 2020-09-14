package com.mawkun.core.dao;

import com.mawkun.core.base.dao.OrderFormDao;
import com.mawkun.core.base.data.ShopIncomeData;
import com.mawkun.core.base.data.ShopOrderData;
import com.mawkun.core.base.data.query.OrderFormQuery;
import com.mawkun.core.base.data.query.ShopIncomeQuery;
import com.mawkun.core.base.data.query.StateQuery;
import com.mawkun.core.base.data.vo.OrderFormVo;
import com.mawkun.core.base.entity.OrderForm;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderFormDaoExt extends OrderFormDao {

    /**
     * 订单详情
     * @param id
     * @return
     */
    OrderFormVo selectDetail(Long id);

    /**
     *统计门店收入
     * @param query
     * @return
     */
    List<OrderFormVo> statsShopIncome(StateQuery query);

    /**
     * 统计门店订单
     * @param query
     * @return
     */
    List<ShopOrderData> statsShopOrder(StateQuery query);

    /**
     * 订单列表
     * @return
     */
    List<OrderFormVo> selectList(OrderFormQuery query);

}