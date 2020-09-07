package com.mawkun.core.dao;

import com.mawkun.core.base.dao.GoodsDao;
import com.mawkun.core.base.data.vo.GoodsVo;
import com.mawkun.core.base.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-19 20:40:42
 */
@Repository
public interface GoodsDaoExt extends GoodsDao {

    /**
     * 根据订单id查询商品
     * @param orderId
     * @return
     */
    List<GoodsVo> selectByOrderFormId(Long orderId);

    List<Goods> selectByName(String name);

}