package com.mawkun.core.dao;

import com.mawkun.core.base.dao.GoodsOrderDao;
import com.mawkun.core.base.data.ShopOrderData;
import com.mawkun.core.base.data.query.GoodsOrderQuery;
import com.mawkun.core.base.data.query.StateQuery;
import com.mawkun.core.base.data.vo.GoodsOrderVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsOrderDaoExt extends GoodsOrderDao {

    /**
     * 订单详情
     * @param id
     * @return
     */
    GoodsOrderVo selectDetail(Long id);

    /**
     *统计门店收入
     * @param query
     * @return
     */
    List<GoodsOrderVo> statsShopIncome(StateQuery query);

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
    List<GoodsOrderVo> selectList(GoodsOrderQuery query);

}