package com.mawkun.core.dao;

import com.mawkun.core.base.dao.AdminDao;
import com.mawkun.core.base.data.query.AdminQuery;
import com.mawkun.core.base.entity.Admin;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminDaoExt extends AdminDao {

    List<Admin> selectList(AdminQuery query);

}
