package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.GoodsOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-25 19:55:25
 */
@Mapper
public interface GoodsOrderDao {

    GoodsOrder getById(@NotNull Long id);

    List<GoodsOrder> listByEntity(GoodsOrder goodsOrder);

    GoodsOrder getByEntity(GoodsOrder goodsOrder);

    List<GoodsOrder> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull GoodsOrder goodsOrder);

    int insertBatch(@NotEmpty List<GoodsOrder> list);

    int update(@NotNull GoodsOrder goodsOrder);

    int updateByField(@NotNull @Param("where") GoodsOrder where, @NotNull @Param("set") GoodsOrder set);

    int updateBatch(@NotEmpty List<GoodsOrder> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull GoodsOrder goodsOrder);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(GoodsOrder goodsOrder);
    
}