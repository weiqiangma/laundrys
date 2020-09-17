package com.mawkun.core.dao;

import com.mawkun.core.base.dao.InvestLogDao;
import com.mawkun.core.base.entity.InvestLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 23:04:25
 */
@Repository
public interface InvestLogDaoExt extends InvestLogDao {


    
}