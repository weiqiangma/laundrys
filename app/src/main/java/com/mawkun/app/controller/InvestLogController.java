package com.mawkun.app.controller;

import com.mawkun.core.base.entity.InvestLog;
import com.mawkun.core.base.service.InvestLogService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 23:06:57
 */
@RestController
@RequestMapping("/investLog")
public class InvestLogController {
    
    @Autowired
    private InvestLogService investLogService;

    @GetMapping("/get/{id}")
    public InvestLog getById(@PathVariable Long id) {
        InvestLog investLog = investLogService.getById(id);
        return investLog!=null?investLog:new InvestLog();
    }

    @GetMapping("/get")
    public InvestLog getByEntity(InvestLog investLog) {
        return investLogService.getByEntity(investLog);
    }

    @GetMapping("/list")
    public List<InvestLog> list(InvestLog investLog) {
        List<InvestLog> investLogList = investLogService.listByEntity(investLog);
        return investLogList;
    }

    @PostMapping("/insert")
    public InvestLog insert(@RequestBody InvestLog investLog){
        investLogService.insert(investLog);
        return investLog;
    }

    @PutMapping("/update")
    public int update(@RequestBody InvestLog investLog){
        return investLogService.update(investLog);
    }

    @DeleteMapping("/delete/{id}")
    public int deleteOne(@PathVariable Long id){
        return investLogService.deleteById(id);
    }

    @DeleteMapping("/delete")
    public int deleteBatch(@RequestBody List<Long> ids){
        int result = 0;
        if (ids!=null&&ids.size()>0) result = investLogService.deleteByIds(ids);
        return result;
    }

}