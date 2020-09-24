package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.PayFlow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.util.List;

@Mapper
public interface PayFlowDao {

    PayFlow getById(@NotNull Long id);

    List<PayFlow> listByEntity(PayFlow payFlow);

    PayFlow getByEntity(PayFlow payFlow);

    List<PayFlow> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull PayFlow payFlow);

    int insertBatch(@NotEmpty List<PayFlow> list);

    int update(@NotNull PayFlow payFlow);

    int updateByField(@NotNull @Param("where") PayFlow where, @NotNull @Param("set") PayFlow set);

    int updateBatch(@NotEmpty List<PayFlow> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull PayFlow payFlow);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(PayFlow payFlow);
    
}