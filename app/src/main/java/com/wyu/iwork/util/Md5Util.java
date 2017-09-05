package com.wyu.iwork.util;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * Created by sxliu on 2016/9/19.
 */
public class Md5Util {
    /**
     *
     * @param info
     * @return Md5加密后的字符串
     */
    public static String getMD5(String info)
    {
        try
        {
            //md5加密
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            //采用utf-8编码
            md5.update(info.getBytes("UTF-8"));
            byte[] encryption = md5.digest();

            StringBuffer strBuf = new StringBuffer();
            for (int i = 0; i < encryption.length; i++)
            {
                if (Integer.toHexString(0xff & encryption[i]).length() == 1)
                {
                    strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                }
                else
                {
                    strBuf.append(Integer.toHexString(0xff & encryption[i]));
                }
            }

            return strBuf.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            Logger.e("Log--------------","当请求特定的加密算法而它在该环境中不可用时抛出此异常");
            return "";
        }
        catch (UnsupportedEncodingException e)
        {
            Logger.e("Log---------------","不支持编码异常");
            return "";
        }
    }


    public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String md5(String inputStr) throws NoSuchAlgorithmException {
        String md5Str = inputStr;
        if(inputStr != null) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(inputStr.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            md5Str = hash.toString(16);
            if((md5Str.length() % 2) != 0) {
                md5Str = "0" + md5Str;
            }
        }
        return md5Str;
    }

    //密码加密 与php加密一致
    public static String md51(String input) throws NoSuchAlgorithmException {
        String result = input;
        if(input != null) {
            MessageDigest md = MessageDigest.getInstance("SignUtil"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) {
                result = "0" + result;
            }
        }
        return result;
    }

    public static String phpmd5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, SignUtil should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }



    /**
     * 得到加密签名
     */
    public static String getSign(String key){
        String sing = "";
        String SecretKey = "12345678";
        String mkey = Md5Util.getMD5(SecretKey+key);
        if (mkey!=null&&mkey.length()>20){
            sing =  mkey.substring(8,20);
        }
        return sing;
    }

    /**
     * 生成随机数
     */
    public static int getRandom(){
        Random random=new Random();//创建random对象
        int intNumber=random.nextInt();//获取一个整型数
        return intNumber;
    }
}
