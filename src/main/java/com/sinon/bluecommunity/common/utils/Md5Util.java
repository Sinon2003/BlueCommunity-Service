package com.sinon.bluecommunity.common.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public class Md5Util {

    // 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    private static MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("初始化失败，MessageDigest不支持MD5。", e);
        }
    }

    // 生成字符串的MD5校验值
    public static String getMD5String(String s) {
        return getMD5String(s.getBytes());
    }

    // 判断字符串的MD5校验码是否与一个已知的MD5码相匹配
    public static boolean checkPassword(String password, String md5PwdStr) {
        String s = getMD5String(password);
        return s.equals(md5PwdStr);
    }

    // 生成字节数组的MD5校验值
    public static String getMD5String(byte[] bytes) {
        messageDigest.update(bytes);
        return bufferToHex(messageDigest.digest());
    }

    private static String bufferToHex(byte[] bytes) {
        StringBuilder stringBuffer = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            stringBuffer.append(HEX_DIGITS[(b & 0xf0) >> 4]);
            stringBuffer.append(HEX_DIGITS[b & 0x0f]);
        }
        return stringBuffer.toString();
    }
}
