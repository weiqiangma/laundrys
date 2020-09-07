package com.mawkun.core.service;

import cn.pertech.common.utils.DateUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.query.ShopQuery;
import com.mawkun.core.base.data.query.StateQuery;
import com.mawkun.core.base.data.vo.OrderFormVo;
import com.mawkun.core.base.entity.Shop;
import com.mawkun.core.base.service.ShopService;
import com.mawkun.core.dao.OrderFormDaoExt;
import com.mawkun.core.dao.ShopDaoExt;
import com.mawkun.core.utils.ImageUtils;
import com.mawkun.core.utils.StringUtils;
import com.mawkun.core.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShopServiceExt extends ShopService {

    @Autowired
    private ShopDaoExt shopDaoExt;
    @Autowired
    private OrderFormDaoExt orderFormDaoExt;

    /**
     * 添加店铺
     * @param shop
     * @param files
     * @return
     */
    public int insertWithPic(Shop shop, MultipartFile[] files) {
        String image = ImageUtils.uploadImages(files);
        shop.setPicture(image);
        shop.setCreateTime(new Date());
        shop.setUpdateTime(new Date());
        return shopDaoExt.insert(shop);
    }

    /**
     * 编辑代纳普
     * @param shop
     * @param files
     * @return
     */
    public int updateWithPic(Shop shop, MultipartFile[] files) {
        if(files != null && files.length > 0) {
            String images = "";
            Shop resultShop = shopDaoExt.getById(shop.getId());
            String newImage = ImageUtils.uploadImages(files);
            StringBuilder strBuilder = new StringBuilder();
            if(shop.getPicture() != null) {
                //已有的图片如果有删除会传还存在的picture
                images = strBuilder.append(shop.getPicture()).append(",").append(newImage).toString();
            } else{
                //已有的图片没动，查数据库中是否存在picture，有就在其后添加，没有新增
                if(resultShop.getPicture() != null) {
                    images =strBuilder.append(resultShop.getPicture()).append(",").append(newImage).toString();
                } else {
                    images = newImage;
                }
            }
            shop.setPicture(images);
        }
        return shopDaoExt.update(shop);
    }

    /**
     * 统计店铺收入
     * @param query
     * @return
     */
    public JSONArray statsShopIncome(StateQuery query) {
        fillQueryData(query);
        List<OrderFormVo> list = orderFormDaoExt.statsShopIncome(query);
        //根据type进行分组
        Map<String, OrderFormVo> dataMap = list.stream().collect(Collectors.toMap(OrderFormVo::getType, m->m));
        Date sTime = query.getStartTime();
        Calendar ca = Calendar.getInstance();
        ca.setTime(sTime);
        JSONArray array = new JSONArray();
        for(int i = 0; i < query.getDateCount(); i++) {
            String key = "";
            if(query.getType()==1){
                key = String.valueOf(i);
            }else if(query.getType()==2){
                key = DateUtils.format("yyyy-MM-dd",ca.getTime());
            }else if(query.getType()==3 || query.getType()==4){
                key = DateUtils.format("yyyy-MM-dd",ca.getTime());
            }else{
                continue;
            }
            OrderFormVo form = dataMap.get(key);
            JSONObject object = new JSONObject();
            String shopName = (form == null) ? "" : form.getShopName();
            if(query.getShopId() == null) shopName = "";
            Double amount = (form == null) ? 0 : form.getTotalAmount();
            object.put("time", key);
            object.put("shopName", shopName);
            object.put("amount", amount);
            array.add(object);
            ca.add(Calendar.DAY_OF_MONTH,1);
        }
        return array;
    }

    /**
     * 门店列表分业
     * @param query
     * @return
     */
    public PageInfo pageByEntity(ShopQuery query) {
        query.init();
        if(!StringUtils.isEmpty(query.getShopName())) {
            query.setShopName("%" + query.getShopName() + "%");
        }
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<Shop> list = shopDaoExt.listByEntity(query);
        return new PageInfo<Shop>(list);
    }

    /**
     * 根据门店名查询
     * @param name
     * @return
     */
    public List<Shop> getByName(String name) {
        return shopDaoExt.selectByName(name);
    }

    private void fillQueryData(StateQuery queryVO) {
        Date now = new Date();
        if(queryVO.getType() == 1) {
            //当天统计
            queryVO.setStartTime(DateUtils.dateStartDate(now));
            queryVO.setEndTime(DateUtils.dateEndDate(now));
            queryVO.setFormatCode("%H");
            queryVO.setDateCount(24);
        }else if(queryVO.getType() == 2) {
            //本周统计
            queryVO.setStartTime(TimeUtils.getWeekStart());
            queryVO.setEndTime(TimeUtils.getWeekEnd());
            queryVO.setFormatCode("%Y-%m-%d");
            queryVO.setDateCount(7);
        }else if(queryVO.getType() == 3) {
            //本月统计
            queryVO.setStartTime(TimeUtils.getMonthStart());
            queryVO.setEndTime(TimeUtils.getMonthEnd());
            queryVO.setFormatCode("%Y-%m-%d");
            int year = DateUtils.getYear();
            int month = DateUtils.getMonth();
            queryVO.setDateCount(DateUtils.getDaysOfMonth(year,month));
        }else if(queryVO.getType()==4){
            //自定义传入
            queryVO.setFormatCode("%Y-%m-%d");
            queryVO.setStartTime(DateUtils.dateStartDate(queryVO.getStartTime()));
            //最后一天要取到时间
            queryVO.setEndTime(DateUtils.dateEndDate(queryVO.getEndTime()));
            int diff = (int)DateUtils.dayDiff(queryVO.getStartTime(), queryVO.getEndTime());
            queryVO.setDateCount(diff);
        }
        //System.out.println("格式化后范围: "+JsonUtils.toString(queryVO));
    }
}