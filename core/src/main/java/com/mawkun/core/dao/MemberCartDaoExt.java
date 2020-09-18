package com.mawkun.core.dao;

import com.mawkun.core.base.dao.MemberCartDao;
import com.mawkun.core.base.entity.MemberCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 22:42:30
 */
@Repository
public interface MemberCartDaoExt extends MemberCartDao {

    
}