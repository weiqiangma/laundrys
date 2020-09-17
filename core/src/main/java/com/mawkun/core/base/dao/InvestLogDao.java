package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.InvestLog;
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
public interface InvestLogDao {

    InvestLog getById(@NotNull Long id);

    List<InvestLog> listByEntity(InvestLog investLog);

    InvestLog getByEntity(InvestLog investLog);

    List<InvestLog> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull InvestLog investLog);

    int insertBatch(@NotEmpty List<InvestLog> list);

    int update(@NotNull InvestLog investLog);

    int updateByField(@NotNull @Param("where") InvestLog where, @NotNull @Param("set") InvestLog set);

    int updateBatch(@NotEmpty List<InvestLog> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull InvestLog investLog);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(InvestLog investLog);
    
}