package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.InvestOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 23:04:25
 */
@Mapper
public interface InvestOrderDao {

    InvestOrder getById(@NotNull Long id);

    List<InvestOrder> listByEntity(InvestOrder investLog);

    InvestOrder getByEntity(InvestOrder investLog);

    List<InvestOrder> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull InvestOrder investLog);

    int insertBatch(@NotEmpty List<InvestOrder> list);

    int update(@NotNull InvestOrder investLog);

    int updateByField(@NotNull @Param("where") InvestOrder where, @NotNull @Param("set") InvestOrder set);

    int updateBatch(@NotEmpty List<InvestOrder> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull InvestOrder investLog);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(InvestOrder investLog);
    
}