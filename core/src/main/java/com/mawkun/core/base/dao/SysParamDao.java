package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.SysParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.*;
import java.util.List;

@Repository
public interface SysParamDao {

    SysParam getById(@NotNull Long id);

    List<SysParam> listByEntity(SysParam sysParam);

    SysParam getByEntity(SysParam sysParam);

    List<SysParam> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull SysParam sysParam);

    int insertBatch(@NotEmpty List<SysParam> list);

    int update(@NotNull SysParam sysParam);

    int updateByField(@NotNull @Param("where") SysParam where, @NotNull @Param("set") SysParam set);

    int updateBatch(@NotEmpty List<SysParam> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull SysParam sysParam);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(SysParam sysParam);
    
}