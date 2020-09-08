package com.mawkun.core.base.data.vo;

import com.mawkun.core.base.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class UserVo extends User {
    private List<ShopUserVo> list;
}
