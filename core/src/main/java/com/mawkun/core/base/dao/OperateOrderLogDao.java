package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.OperateOrderLog;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-28 21:17:49
 */
@Repository
public interface OperateOrderLogDao {

    OperateOrderLog getById(@NotNull Long id);

    List<OperateOrderLog> listByEntity(OperateOrderLog operateOrderLog);

    OperateOrderLog getByEntity(OperateOrderLog operateOrderLog);

    List<OperateOrderLog> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull OperateOrderLog operateOrderLog);

    int insertBatch(@NotEmpty List<OperateOrderLog> list);

    int update(@NotNull OperateOrderLog operateOrderLog);

    int updateByField(@NotNull @Param("where") OperateOrderLog where, @NotNull @Param("set") OperateOrderLog set);

    int updateBatch(@NotEmpty List<OperateOrderLog> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull OperateOrderLog operateOrderLog);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(OperateOrderLog operateOrderLog);
    
}