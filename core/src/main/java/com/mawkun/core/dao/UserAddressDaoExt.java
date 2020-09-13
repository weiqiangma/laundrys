package com.mawkun.core.dao;

import com.mawkun.core.base.dao.UserAddressDao;
import com.mawkun.core.base.dao.UserDao;
import com.mawkun.core.base.data.query.UserQuery;
import com.mawkun.core.base.data.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface UserAddressDaoExt extends UserAddressDao {

}