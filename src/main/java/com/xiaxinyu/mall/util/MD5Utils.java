package com.xiaxinyu.mall.util;

import com.xiaxinyu.mall.common.Constant;
import org.apache.tomcat.util.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Description: MD5工具
 * @author: xiaxinyu
 * @Email: xiaxinyuxy@163.com
 * @date: 2022年12月03日 16:20
 * @Copyright:
 * @version: 1.0.0
 */

public class MD5Utils {
    public static String getMD5Str(String strValue) throws NoSuchAlgorithmException{
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        return Base64.encodeBase64String(md5.digest((strValue + Constant.SALT).getBytes()));
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(getMD5Str("1234"));
    }
}