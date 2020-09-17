package com.mawkun.core.service;

import com.mawkun.core.base.dao.InvestLogDao;
import com.mawkun.core.base.data.query.InvestLogQuery;
import com.mawkun.core.base.entity.InvestLog;
import com.mawkun.core.base.service.InvestLogService;
import com.mawkun.core.dao.InvestLogDaoExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 23:04:26
 */
@Service
public class InvestLogServiceExt extends InvestLogService {

    @Autowired
    private InvestLogDaoExt investLogDaoExt;

    public void pageList(InvestLogQuery query) {
        query.init();

    }

}