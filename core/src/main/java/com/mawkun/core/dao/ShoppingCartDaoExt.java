package com.mawkun.core.dao;

import com.mawkun.core.base.dao.ShoppingCartDao;
import com.mawkun.core.base.entity.ShoppingCart;
import com.mawkun.core.base.service.ShoppingCartService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Date 2020/9/11 14:44
 * @Author mawkun
 */
@Repository
public interface ShoppingCartDaoExt extends ShoppingCartDao {

    ShoppingCart selectByUserId(@Param("userId") Long userId, @Param("goodsId") Long goodsId, @Param("idList") List<Long> idList);
}
