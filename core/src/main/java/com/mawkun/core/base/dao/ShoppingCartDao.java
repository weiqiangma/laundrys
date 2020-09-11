package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.util.List;

@Mapper
public interface ShoppingCartDao {

    ShoppingCart getById(@NotNull Long id);

    List<ShoppingCart> listByEntity(ShoppingCart shoppingCart);

    ShoppingCart getByEntity(ShoppingCart shoppingCart);

    List<ShoppingCart> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull ShoppingCart shoppingCart);

    int insertBatch(@NotEmpty List<ShoppingCart> list);

    int update(@NotNull ShoppingCart shoppingCart);

    int updateByField(@NotNull @Param("where") ShoppingCart where, @NotNull @Param("set") ShoppingCart set);

    int updateBatch(@NotEmpty List<ShoppingCart> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull ShoppingCart shoppingCart);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(ShoppingCart shoppingCart);
    
}