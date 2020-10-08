package com.mawkun.core.service;


import com.mawkun.core.base.dao.SysParamDao;
import com.mawkun.core.base.entity.SysParam;
import com.mawkun.core.base.service.SysParamService;
import com.mawkun.core.dao.SysParamDaoExt;
import com.mawkun.core.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysParamServiceExt extends SysParamService {

    @Autowired
    private SysParamDaoExt sysParamDaoExt;

    public int updateWithPic(SysParam param, MultipartFile[] files) {
        if(files != null && files.length > 0) {
            String images = "";
            SysParam sysParam = sysParamDaoExt.getById(param.getId());
            String newImage = ImageUtils.uploadImages(files);
            StringBuilder strBuilder = new StringBuilder();
            if(param.getSysValue() != null) {
                //已有的图片如果有删除会传还存在的picture
                images = strBuilder.append(param.getSysValue()).append(",").append(newImage).toString();
            } else{
                //已有的图片没动，查数据库中是否存在picture，有就在其后添加，没有新增
                if(sysParam.getSysValue() != null) {
                    images =strBuilder.append(sysParam.getSysValue()).append(",").append(newImage).toString();
                } else {
                    images = newImage;
                }
            }
            param.setSysValue(images);
        }
        return sysParamDaoExt.update(param);
    }

    public List<SysParam> getSysParamList() {
        SysParam query = new SysParam();
        query.setType(0);
        return sysParamDaoExt.listByEntity(query);
    }

    /**
     * 获取首页图片
     * @return
     */
    public List<SysParam> getMainPicture() {
        List<SysParam> list = sysParamDaoExt.selectMainPicture("main_picture");
        return list;
    }

    public String getMainAdvice() {
        return sysParamDaoExt.selectSysValue("main_advice");
    }

    public List<SysParam> getTransportFee() { return sysParamDaoExt.selectTransportFee(); }

    public List<SysParam> getOrderNotice() { return sysParamDaoExt.selectOrderNotice(); }

    public String getConsultMobile() {
        return sysParamDaoExt.selectSysValue("consult_mobile");
    }
}
