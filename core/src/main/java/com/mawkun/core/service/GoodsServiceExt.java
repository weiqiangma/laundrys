package com.mawkun.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.query.GoodsQuery;
import com.mawkun.core.base.data.vo.GoodsVo;
import com.mawkun.core.base.entity.Goods;
import com.mawkun.core.base.service.GoodsService;
import com.mawkun.core.dao.GoodsDaoExt;
import com.mawkun.core.utils.ImageUtils;
import com.mawkun.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mawkun
 * @date 2020-08-19 20:40:49
 */
@Service
public class GoodsServiceExt extends GoodsService {

    @Resource
    private GoodsDaoExt goodsDaoExt;

    public PageInfo pageByEntity(GoodsQuery query) {
        query.init();
        if(!StringUtils.isEmpty(query.getGoodsName())) {
            query.setGoodsName("%" + query.getGoodsName() + "%");
        }
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<Goods> list = goodsDaoExt.listByEntity(query);
        return new PageInfo(list);
    }

    public List<GoodsVo> selectList(GoodsQuery query) {
        return goodsDaoExt.selectByTerms(query);
    }

    public int insertWithPic(Goods goods, MultipartFile file) {
        MultipartFile[] files = {file};
        String image = ImageUtils.uploadImages(files);
        goods.setPicture(image);
        goods.setCreateTime(new Date());
        return goodsDaoExt.insert(goods);
    }

    public int updateWithPic(Goods goods, MultipartFile file) {
        if(file != null) {
            MultipartFile[] files = {file};
            String image = ImageUtils.uploadImages(files);
            goods.setPicture(image);
        }
        goods.setUpdateTime(new Date());
        return goodsDaoExt.update(goods);
    }

    public List<Goods> getByName(String name) {
        return goodsDaoExt.selectByName(name);
    }
}