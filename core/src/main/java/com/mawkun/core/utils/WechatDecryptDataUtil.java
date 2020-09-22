package com.mawkun.core.utils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.security.Key;
import java.security.Security;
import java.util.regex.Pattern;

public class WechatDecryptDataUtil {

    public static void main(String[] args) throws UnknownHostException, UnsupportedEncodingException {
//        String appId = "wx4f4bc4dec97d474b";
//        String encryptedData = "ia8m7n+A9fvFyXYgRAvL+iPCZX1eg2yszvfN1QObjBA7V8Dhjf3S+GcineUxjev1lRDQfJpYOtZQ4TcJFe3UyNXZ9lVUwB10bnBE6LFCA9Y6a/ETgMw1aatck38IYn8OrEoK2Rf6zb1cH7cRwQUIJH8VcJOaXoKSAw8n/UsfA37MAWnM2UXg7o+5hwfe/TQpxQny4bUMkrFKWHjeQLs3XO2tXWXNgFVOG/hzMvGkFKAhvWM9cm2wurbXFSQwK8Kx8kPcsOszq7QKRB19RDZp9v7EM47mtu7UQponkalFh1OXeFw8Ky2CpbzO26s2jBGJHHtRqWv8KytZ2vSqQ4Nu4riU5cQuvJqQsRwKkSYnFqGMpSA0/Lb1ayk1qHxVhsG7oKzaVG6jRt6K7erWT4B/qnMOfafS83OjL+fvwNd7C+guYq8jZYXvelmxiGxw37LqomAv1edjutwTAv3ojg1BDRvVkZHVZMvOXBHzMBorBRy0IfHuo1NGFjYvhyKSwriDV6ME/43erUDpWKzFg2q1eA==";
//        String sessionKey = "9cZbb6alMMECYRaLAjzFiQ==";
//        String iv ="zTnqlW1TsJmkHgftI/Ka0A==";
//        String result = decrypt(appId, encryptedData, sessionKey, iv);
//        System.out.println(result);
        InetAddress addr = InetAddress.getLocalHost();
        String hostName = addr.getHostName();
        String ip = addr.getHostAddress();
        byte[] addressByte = addr.getAddress();
        String address = new String(addressByte, "UTF-8");
        System.out.println("host:" + addr.getHostName());
        System.out.println("ip:" + addr.getHostAddress());
    }

    private static final String KEY_ALGORITHM = "AES";
    private static final String ALGORITHM_STR = "AES/CBC/PKCS7Padding";
    private static Key key;
    private static Cipher cipher;

    private static void init(byte[] keyBytes) {
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
        int base = 16;
        if (keyBytes.length % base != 0) {
            int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
            keyBytes = temp;
        }
        // 初始化
        Security.addProvider(new BouncyCastleProvider());
        // 转化成JAVA的密钥格式
        key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
        try {
            // 初始化cipher
            cipher = Cipher.getInstance(ALGORITHM_STR, "BC");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 解密方法
     *
     * @param encryptedData 要解密的字符串
     * @param keyBytes      解密密钥
     * @param ivs           自定义对称解密算法初始向量 iv
     * @return 解密后的字节数组
     */
    private static byte[] decryptOfDiyIV(byte[] encryptedData, byte[] keyBytes, byte[] ivs) {
        byte[] encryptedText = null;
        init(keyBytes);
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(ivs));
            encryptedText = cipher.doFinal(encryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedText;
    }

    /**
     * 解密数据
     * @return
     * @throws Exception
     */
    public static String decrypt(String appId, String encryptedData, String sessionKey, String iv){
        String result = "";
        try {
            AesUtils aes = new AesUtils();
             byte[] resultByte = aes.decrypt(Base64.decodeBase64(encryptedData), Base64.decodeBase64(sessionKey), Base64.decodeBase64(iv));
            if(null != resultByte && resultByte.length > 0){
                result = new String(WxPKCS7Encoder.decode(resultByte));
                JSONObject jsonObject = JSONObject.parseObject(result);
                JSONObject object = jsonObject.getJSONObject("watermark");
                String decryptAppid = jsonObject.getJSONObject("watermark").getString("appid");
                if(!appId.equals(decryptAppid)){
                    result = "";
                }
            }
        } catch (Exception e) {
            result = "";
            e.printStackTrace();
        }
        return result;
    }
}
