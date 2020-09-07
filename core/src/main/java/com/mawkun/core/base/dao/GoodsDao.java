package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.Goods;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-19 20:41:16
 */
@Repository
public interface GoodsDao {

    Goods getById(@NotNull Long id);

    List<Goods> listByEntity(Goods goods);

    Goods getByEntity(Goods goods);

    List<Goods> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull Goods goods);

    int insertBatch(@NotEmpty List<Goods> list);

    int update(@NotNull Goods goods);

    int updateByField(@NotNull @Param("where") Goods where, @NotNull @Param("set") Goods set);

    int updateBatch(@NotEmpty List<Goods> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull Goods goods);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(Goods goods);
    
}