package com.mawkun.core.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.common.constant.Constant;
import com.mawkun.core.base.data.query.GoodsQuery;
import com.mawkun.core.base.data.query.KindQuery;
import com.mawkun.core.base.data.vo.ShopVo;
import com.mawkun.core.base.entity.Goods;
import com.mawkun.core.base.entity.Kind;
import com.mawkun.core.base.service.KindService;
import com.mawkun.core.dao.KindDaoExt;
import com.mawkun.core.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KindServiceExt extends KindService {

    @Resource
    private KindDaoExt kindDaoExt;
    @Resource
    private GoodsServiceExt goodsServiceExt;

    /**
     * 列表分页
     * @param query
     * @return
     */
    public PageInfo pageByEntity(KindQuery query) {
        query.init();
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<Kind> list = kindDaoExt.listByEntity(query);
        return new<Kind> PageInfo(list);
    }


    /**
     * 添加商品分类
     * @param kind
     * @param file
     * @return
     */
    public int insertWithPic(Kind kind, MultipartFile file) {
        MultipartFile[] files = {file};
        String images = ImageUtils.uploadImages(files);
        kind.setIconName(images);
        kind.setCreateTime(new Date());
        return kindDaoExt.insert(kind);
    }

    /**
     * 商品分类编辑
     * @param kind
     * @param file
     * @return
     */
    public int updateWithPic(Kind kind, MultipartFile file) {
        if(file != null) {
            MultipartFile[] files = {file};
            String images = ImageUtils.uploadImages(files);
            kind.setIconName(images);
        }
        kind.setUpdateTime(new Date());
        return kindDaoExt.update(kind);
    }

    public Kind getByName(String name) {
        return kindDaoExt.selectByName(name);
    }

    public JSONArray getMainMenu() {
        JSONArray array = new JSONArray();
        Kind query = new Kind();
        query.setNavStatus(Constant.MAIN_KIND_SHOW);
        List<Kind> list = kindDaoExt.listByEntity(query);
        List<Kind> sortedList = list.stream().sorted(Comparator.comparingInt(Kind::getMainSort)).collect(Collectors.toList());
        for(Kind kind : sortedList) {
            JSONObject object = new JSONObject();
            Goods goodsQuery = new Goods();
            goodsQuery.setKindId(kind.getId());
            List<Goods> goods = goodsServiceExt.listByEntity(goodsQuery);
            List<Goods> resultList = goods.stream().limit(3).collect(Collectors.toList());
            object.put("title", kind.getKindName());
            object.put("list", resultList);
            array.add(object);
        }
        return array;
    }

}