package com.mawkun.core.dao;

import com.mawkun.core.base.dao.ShopUserDao;
import com.mawkun.core.base.dao.UserDao;
import com.mawkun.core.base.data.query.UserQuery;
import com.mawkun.core.base.data.vo.ShopUserVo;
import com.mawkun.core.base.data.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ShopUserDaoExt extends ShopUserDao {

    /**
     * 根据配送员ID查找关联店铺
     * @param userId
     * @return
     */
    List<ShopUserVo> selectShopNameByUserId(@Param("userId") Long userId);

    int deleteByUserId(Long userId);

}