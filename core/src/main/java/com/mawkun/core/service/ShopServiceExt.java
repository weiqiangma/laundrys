package com.mawkun.core.service;

import cn.pertech.common.utils.DateUtils;
import cn.pertech.common.utils.NumberUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mawkun.core.base.data.ShopOrderData;
import com.mawkun.core.base.data.query.ShopQuery;
import com.mawkun.core.base.data.query.StateQuery;
import com.mawkun.core.base.data.vo.GoodsOrderVo;
import com.mawkun.core.base.data.vo.ShopVo;
import com.mawkun.core.base.entity.Shop;
import com.mawkun.core.base.entity.SysParam;
import com.mawkun.core.base.entity.UserAddress;
import com.mawkun.core.base.service.ShopService;
import com.mawkun.core.dao.GoodsOrderDaoExt;
import com.mawkun.core.dao.ShopDaoExt;
import com.mawkun.core.dao.SysParamDaoExt;
import com.mawkun.core.utils.ImageUtils;
import com.mawkun.core.utils.StringUtils;
import com.mawkun.core.utils.TimeUtils;
import com.xiaoleilu.hutool.util.NumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ShopServiceExt extends ShopService {

    @Resource
    private ShopDaoExt shopDaoExt;
    @Resource
    private UserAddressServiceExt userAddressServiceExt;
    @Resource
    private GoodsOrderDaoExt goodsOrderDaoExt;
    @Resource
    private GaoDeApiServiceExt gaoDeApiServiceExt;
    @Resource
    private SysParamDaoExt sysParamDaoExt;

    /**
     * 添加店铺
     * @param shop
     * @param files
     * @return
     */
    public int insertWithPic(Shop shop, MultipartFile[] files) {
        String image = ImageUtils.uploadImages(files);
        String location = shop.getLongitude() + "," + shop.getLatitude();
        shop.setPicture(image);
        shop.setLocation(location);
        shop.setCreateTime(new Date());
        shop.setUpdateTime(new Date());
        return shopDaoExt.insert(shop);
    }

    public Shop getFirstLevelShop() {
        Shop query = new Shop();
        query.setLevel(0);
        return shopDaoExt.getByEntity(query);
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
        if(StringUtils.isNotEmpty(shop.getLongitude())) {
            String location = shop.getLongitude() + "," + shop.getLatitude();
            shop.setLocation(location);
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
        List<GoodsOrderVo> list = goodsOrderDaoExt.statsShopIncome(query);
        //根据type进行分组
        Map<String, GoodsOrderVo> dataMap = list.stream().collect(Collectors.toMap(GoodsOrderVo::getType, m->m));
        Date sTime = query.getStartTime();
        Calendar ca = Calendar.getInstance();
        ca.setTime(sTime);
        JSONArray array = new JSONArray();
        for(int i = 0; i < query.getDateCount(); i++) {
            String key = "";
            if(query.getType()==1){
                key = (i < 10) ? String.valueOf("0"+i):String.valueOf(i);
            }else if(query.getType()==2){
                key = DateUtils.format("yyyy-MM-dd",ca.getTime());
            }else if(query.getType()==3 || query.getType()==4){
                key = DateUtils.format("yyyy-MM-dd",ca.getTime());
            }else{
                continue;
            }
            GoodsOrderVo form = dataMap.get(key);
            JSONObject object = new JSONObject();
            String shopName = (form == null) ? "" : form.getShopName();
            if(query.getShopId() == null) shopName = "";
            Double amount = (form == null) ? 0 : form.getAmount();
            object.put("time", key);
            object.put("shopName", shopName);
            object.put("amount", amount);
            array.add(object);
            ca.add(Calendar.DAY_OF_MONTH,1);
        }
        return array;
    }

    /**
     * 统计店铺订单
     * @param query
     * @return
     */
    public JSONArray statsShopOrder(StateQuery query) {
        fillQueryData(query);
        List<ShopOrderData> list = goodsOrderDaoExt.statsShopOrder(query);
        //根据type进行分组
        Map<String, ShopOrderData> dataMap = list.stream().collect(Collectors.toMap(ShopOrderData::getType, m->m));
        Date sTime = query.getStartTime();
        Calendar ca = Calendar.getInstance();
        ca.setTime(sTime);
        JSONArray array = new JSONArray();
        for(int i = 0; i < query.getDateCount(); i++) {
            String key = "";
            if(query.getType()==1){
                key = (i < 10) ? String.valueOf("0"+i):String.valueOf(i);
            }else if(query.getType()==2){
                key = DateUtils.format("yyyy-MM-dd",ca.getTime());
            }else if(query.getType()==3 || query.getType()==4){
                key = DateUtils.format("yyyy-MM-dd",ca.getTime());
            }else{
                continue;
            }
            ShopOrderData data = dataMap.get(key);
            JSONObject object = new JSONObject();
            String shopName = (data == null) ? "" : data.getShopName();
            if(query.getShopId() == null) shopName = "";
            Integer amount = (data == null) ? 0 : data.getAmount();
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
        List<ShopVo> resultList = new ArrayList<>();
        query.init();
        if(!StringUtils.isEmpty(query.getShopName())) {
            query.setShopName("%" + query.getShopName() + "%");
        }
        PageHelper.startPage(query.getPageNo(), query.getPageSize());
        List<ShopVo> list = shopDaoExt.selectList(query);
        //根据用户收货地址计和各门店距离
        List<SysParam> paramList = sysParamDaoExt.selectTransportFee();
        List<SysParam> sortList = paramList.stream().sorted(Comparator.comparingInt(SysParam::getDistance)).collect(Collectors.toList());
        int max = sortList.get(paramList.size() -1).getDistance() * 1000;

//        for(ShopVo shopVo : list) {
//            UserAddress address = userAddressServiceExt.getById(query.getAddressId());
//            String originalLal = address.getLocation();
//            String destincation =   shopVo.getLocation();
//            String distanceStr = gaoDeApiServiceExt.getDistanceWithUserAndShop(originalLal, destincation);
//            Integer distance = NumberUtils.str2Int(distanceStr);
//            shopVo.setLength(distance);
//            distanceStr = convertDistanceUnit(distanceStr);
//            shopVo.setDistance(distanceStr);
//        }

        if(query.getAddressId() == null) {
            //List<ShopVo> sortShopList = list.stream().sorted(Comparator.comparingInt(ShopVo::getLength)).collect(Collectors.toList());
            return new PageInfo<ShopVo>(list);
        } else {
            for(ShopVo shopVo : list) {
                UserAddress address = userAddressServiceExt.getById(query.getAddressId());
                String originalLal = address.getLocation();
                String destincation =   shopVo.getLocation();
                String distanceStr = gaoDeApiServiceExt.getDistanceWithUserAndShop(originalLal, destincation);
                Integer distance = NumberUtils.str2Int(distanceStr);
                if(distance < max) {
                    shopVo.setLength(distance);
                    distanceStr = convertDistanceUnit(distanceStr);
                    shopVo.setDistance(distanceStr);
                    resultList.add(shopVo);
                }
            }
            List<ShopVo> sortShopList = resultList.stream().sorted(Comparator.comparingInt(ShopVo::getLength)).collect(Collectors.toList());
            return new PageInfo<ShopVo>(sortShopList);
        }


//        if(query.getAddressId() != null) {
//            for(ShopVo shopVo : list) {
//                UserAddress address = userAddressServiceExt.getById(query.getAddressId());
//                String originalLal = address.getLocation();
//                String destincation =   shopVo.getLocation();
//                String distanceStr = gaoDeApiServiceExt.getDistanceWithUserAndShop(originalLal, destincation);
//                Integer distance = NumberUtils.str2Int(distanceStr);
//                if(distance < max) {
//                    shopVo.setLength(distance);
//                    distanceStr = convertDistanceUnit(distanceStr);
//                    shopVo.setDistance(distanceStr);
//                    resultList.add(shopVo);
//                }
//            }
//            List<ShopVo> sortShopList = list.stream().sorted(Comparator.comparingInt(ShopVo::getLength)).collect(Collectors.toList());
//            resultList = sortShopList;
//        } else {
//
//        }
//
//        return new PageInfo<ShopVo>(resultList);
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

    public String convertDistanceUnit(String distance) {
        String result = "";
        Integer length = NumberUtils.str2Int(distance);
        if(length > 1000) {
            Double convertData = NumberUtil.div(length, 1000, 1);
            result = convertData + "km";
        } else {
            result = length + "m";
        }
        return result;
    }
}