package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.GoodsOrder;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-19 20:42:20
 */
@Repository
public interface GoodsOrderDao {

    GoodsOrder getById(@NotNull Long id);

    List<GoodsOrder> listByEntity(GoodsOrder goodsOrder);

    GoodsOrder getByEntity(GoodsOrder goodsOrder);

    List<GoodsOrder> listByIds(@NotEmpty List<Long> list);

    int insert(GoodsOrder goodsOrder);

    int insertBatch(@NotEmpty List<GoodsOrder> list);

    int update(@NotNull GoodsOrder goodsOrder);

    int updateByField(@NotNull @Param("where") GoodsOrder where, @NotNull @Param("set") GoodsOrder set);

    int updateBatch(@NotEmpty List<GoodsOrder> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull GoodsOrder GoodsOrder);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(GoodsOrder GoodsOrder);
    
}