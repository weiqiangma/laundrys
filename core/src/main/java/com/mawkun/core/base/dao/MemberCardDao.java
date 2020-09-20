package com.mawkun.core.base.dao;

import com.mawkun.core.base.entity.MemberCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.util.List;

/**
 * @author mawkun
 * @date 2020-09-17 22:42:30
 */
@Mapper
public interface MemberCardDao {

    MemberCard getById(@NotNull Long id);

    List<MemberCard> listByEntity(MemberCard MemberCard);

    MemberCard getByEntity(MemberCard MemberCard);

    List<MemberCard> listByIds(@NotEmpty List<Long> list);

    int insert(@NotNull MemberCard MemberCard);

    int insertBatch(@NotEmpty List<MemberCard> list);

    int update(@NotNull MemberCard MemberCard);

    int updateByField(@NotNull @Param("where") MemberCard where, @NotNull @Param("set") MemberCard set);

    int updateBatch(@NotEmpty List<MemberCard> list);

    int deleteById(@NotNull Long id);

    int deleteByEntity(@NotNull MemberCard MemberCard);
  
    int deleteByIds(@NotEmpty List<Long> list);
    
    int countAll();
    
    int countByEntity(MemberCard MemberCard);
    
}