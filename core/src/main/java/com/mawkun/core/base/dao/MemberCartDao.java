package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.MemberCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 22:42:30
 */
@Mapper
public interface MemberCartDao {

    MemberCart getById(@NotNull Long id);

    List<MemberCart> listByEntity(MemberCart memberCart);

    MemberCart getByEntity(MemberCart memberCart);

    List<MemberCart> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull MemberCart memberCart);

    int insertBatch(@NotEmpty List<MemberCart> list);

    int update(@NotNull MemberCart memberCart);

    int updateByField(@NotNull @Param("where") MemberCart where, @NotNull @Param("set") MemberCart set);

    int updateBatch(@NotEmpty List<MemberCart> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull MemberCart memberCart);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(MemberCart memberCart);
    
}