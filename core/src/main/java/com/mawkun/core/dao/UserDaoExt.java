package com.mawkun.core.dao;

import com.mawkun.core.base.dao.UserDao;
import com.mawkun.core.base.data.query.UserQuery;
import com.mawkun.core.base.data.vo.UserVo;
import com.mawkun.core.base.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserDaoExt extends UserDao {
    /**
     * 查找用户信息
     * @param query
     * @return
     */
    List<UserVo> pageByEntity(UserQuery query);
}