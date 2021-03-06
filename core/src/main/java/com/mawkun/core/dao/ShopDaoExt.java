package com.mawkun.core.dao;

import com.mawkun.core.base.dao.ShopDao;
import com.mawkun.core.base.data.query.ShopQuery;
import com.mawkun.core.base.data.vo.ShopVo;
import com.mawkun.core.base.entity.Shop;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopDaoExt extends ShopDao {

    List<Shop> selectByName(String name);

    List<ShopVo> selectList(ShopQuery query);

}