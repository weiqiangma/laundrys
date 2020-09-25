package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.GoodsOrder;
import com.mawkun.core.base.entity.OrderLog;
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
public interface OrderLogDao {

    OrderLog getById(@NotNull Long id);

    List<OrderLog> listByEntity(OrderLog orderLog);

    OrderLog getByEntity(OrderLog orderLog);

    List<OrderLog> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull OrderLog orderLog);

    int insertBatch(@NotEmpty List<OrderLog> list);

    int update(@NotNull OrderLog orderLog);

    int updateByField(@NotNull @Param("where") OrderLog where, @NotNull @Param("set") OrderLog set);

    int updateBatch(@NotEmpty List<OrderLog> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull OrderLog orderLog);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(OrderLog orderLog);
    
}