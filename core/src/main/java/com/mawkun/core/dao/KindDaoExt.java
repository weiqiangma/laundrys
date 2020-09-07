package com.mawkun.core.dao;

import com.mawkun.core.base.dao.KindDao;
import com.mawkun.core.base.entity.Kind;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository
public interface KindDaoExt extends KindDao {

    Kind selectByName(String name);

}