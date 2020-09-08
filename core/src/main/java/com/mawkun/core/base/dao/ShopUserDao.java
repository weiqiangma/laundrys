package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.ShopUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-08 22:15:59
 */
@Mapper
public interface ShopUserDao {

    ShopUser getById(@NotNull Long id);

    List<ShopUser> listByEntity(ShopUser shopUser);

    ShopUser getByEntity(ShopUser shopUser);

    List<ShopUser> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull ShopUser shopUser);

    int insertBatch(@NotEmpty List<ShopUser> list);

    int update(@NotNull ShopUser shopUser);

    int updateByField(@NotNull @Param("where") ShopUser where, @NotNull @Param("set") ShopUser set);

    int updateBatch(@NotEmpty List<ShopUser> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull ShopUser shopUser);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(ShopUser shopUser);
    
}