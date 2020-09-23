package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.OrderForm;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-08-19 20:42:20
 */
@Repository
public interface OrderFormDao {

    OrderForm getById(@NotNull Long id);

    List<OrderForm> listByEntity(OrderForm orderForm);

    OrderForm getByEntity(OrderForm orderForm);

    List<OrderForm> listByIds(@NotEmpty List<Long> list);

    int insert(OrderForm orderForm);

    int insertBatch(@NotEmpty List<OrderForm> list);

    int update(@NotNull OrderForm orderForm);

    int updateByField(@NotNull @Param("where") OrderForm where, @NotNull @Param("set") OrderForm set);

    int updateBatch(@NotEmpty List<OrderForm> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull OrderForm orderForm);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(OrderForm orderForm);
    
}