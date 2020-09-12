package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-12 22:21:57
 */
@Mapper
public interface UserAddressDao {

    UserAddress getById(@NotNull Long id);

    List<UserAddress> listByEntity(UserAddress userAddress);

    UserAddress getByEntity(UserAddress userAddress);

    List<UserAddress> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull UserAddress userAddress);

    int insertBatch(@NotEmpty List<UserAddress> list);

    int update(@NotNull UserAddress userAddress);

    int updateByField(@NotNull @Param("where") UserAddress where, @NotNull @Param("set") UserAddress set);

    int updateBatch(@NotEmpty List<UserAddress> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull UserAddress userAddress);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(UserAddress userAddress);
    
}