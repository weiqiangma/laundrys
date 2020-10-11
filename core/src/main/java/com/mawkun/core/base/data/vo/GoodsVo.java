package com.mawkun.core.base.data.vo;

import com.mawkun.core.base.entity.Goods;
import lombok.Data;

@Data
public class GoodsVo extends Goods {

    private Integer goodsNum;
    private String kindName;
}
