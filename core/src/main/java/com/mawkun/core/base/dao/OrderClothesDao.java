package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.OrderClothes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-19 20:41:58
 */
@Repository
public interface OrderClothesDao {

    OrderClothes getById(@NotNull Long id);

    List<OrderClothes> listByEntity(OrderClothes orderClothes);

    OrderClothes getByEntity(OrderClothes orderClothes);

    List<OrderClothes> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull OrderClothes orderClothes);

    int insertBatch(@NotEmpty List<OrderClothes> list);

    int update(@NotNull OrderClothes orderClothes);

    int updateByField(@NotNull @Param("where") OrderClothes where, @NotNull @Param("set") OrderClothes set);

    int updateBatch(@NotEmpty List<OrderClothes> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull OrderClothes orderClothes);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(OrderClothes orderClothes);
    
}