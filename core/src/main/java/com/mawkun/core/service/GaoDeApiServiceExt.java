package com.mawkun.core.service;

import cn.pertech.common.http.HttpResult;
import cn.pertech.common.http.HttpUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xiaoleilu.hutool.lang.Validator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GaoDeApiServiceExt {

    @Value("${gaode.key}")
    private String GaoDeKey;

    public String getDistanceWithUserAndShop(String originlal, String destincation) {
        String distance = "";
        String url = "https://restapi.amap.com/v3/distance?origins=" + originlal + "&destination=" + destincation +"&output=json&key=" + GaoDeKey;
        try {
            HttpResult result = HttpUtils.get(url);
            JSONObject object = result.asJSON();
            if(0 == object.getInteger("status")) log.info("查询出现异常");
            JSONArray array = object.getJSONArray("results");
            JSONObject resultObj = array.getJSONObject(0);
            distance = resultObj.getString("distance");
            Validate.notEmpty(distance, "距离计算异常");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distance;
    }

    public String getLalByAddress(String address) {
        String distance = "";
        String url = "https://restapi.amap.com/v3/geocode/geo?address=" + address +"北京市朝阳区阜通东大街6号&output=json&key=" + GaoDeKey;
        try {
            HttpResult result = HttpUtils.get(url);
            JSONObject object = result.asJSON();
            if(0 == object.getInteger("status")) log.info("查询出现异常");
            JSONArray array = object.getJSONArray("geocodes");
            JSONObject resultObj = array.getJSONObject(0);
            distance = resultObj.getString("location");
            Validate.notEmpty(distance, "根据收货地址获取经纬度出现异常");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distance;
    }
}
