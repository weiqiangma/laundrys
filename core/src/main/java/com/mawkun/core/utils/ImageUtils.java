package com.mawkun.core.utils;

import cn.pertech.common.utils.RandomUtils;
import io.jsonwebtoken.lang.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImageUtils {

    public static String uploadImages(MultipartFile[] files) {
        Assert.notEmpty(files);
        List<String> imageList = new ArrayList<>();
        for(MultipartFile file : files) {
            try {
                String originalFileName = file.getOriginalFilename();
                assert originalFileName != null;
                String suffixName = originalFileName.substring(originalFileName.lastIndexOf("."));  // 后缀名
                String fileNewName = RandomUtils.uuid() + System.currentTimeMillis() + suffixName;
                //构建文件目录
                //File uploadDir = new File("D:/Images"); //如果目录不存在，则创建
                File uploadDir = new File("/usr/downLoad/image");
                if(!uploadDir.exists()){
                    uploadDir.mkdir();
                }
                File uploadFile = new File(uploadDir.getAbsolutePath() + "/" + fileNewName);
                //如果目录不存在，则创建
                if (!uploadFile.exists()) {
                    file.transferTo(uploadFile);
                    imageList.add(fileNewName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String images = StringUtils.join(imageList, ",");
        return images;
    }
}
