package com.mawkun.core.dao;

import com.mawkun.core.base.data.ShopOrderData;
import com.mawkun.core.base.data.query.GoodsOrderQuery;
import com.mawkun.core.base.data.query.StateQuery;
import com.mawkun.core.base.data.vo.GoodsOrderVo;
import com.mawkun.core.base.entity.GoodsOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsOrderDaoExt {
    /**
     * 订单详情
     * @param id
     * @return
     */
    GoodsOrderVo selectDetail(Long id);

    int updateBatch(List<GoodsOrderVo> list);

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
