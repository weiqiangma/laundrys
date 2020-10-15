package com.mawkun.core.dao;

import com.mawkun.core.base.dao.InvestOrderDao;
import com.mawkun.core.base.data.query.InvestOrderQuery;
import com.mawkun.core.base.data.vo.InvestOrderStatsVo;
import com.mawkun.core.base.entity.InvestOrder;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 23:04:25
 */
@Repository
public interface InvestOrderDaoExt extends InvestOrderDao {

    List<InvestOrder> selectListByTerms(InvestOrderQuery query);

    InvestOrderStatsVo statsInvestOrder(InvestOrderQuery query);
    
}