package com.zq.com.bitmapdemo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Author:甄强
 * Date:2015/10/12
 * Email:978823884@qq.com
 */
public final class EncryptUtils {
    private EncryptUtils() {

    }

    /**
     * 将字节数组转换为十六进制的字符串
     *
     * @param data
     * @return
     */

    public static String toHex(byte[] data) {
        String ret = null;

        if (data != null && data.length > 0) {

            StringBuilder sb = new StringBuilder();//线程安全
            for (byte b : data) {
                int v = b & 0x0FF;
                String hexString = Integer.toHexString(v);
                if (v > 0x0F) {
                    sb.append(hexString);
                } else {
                    sb.append('0').append(hexString);
                }
            }
            ret = sb.toString();
        }
        return ret;
    }

    /**
     * 将网址映射为文件名
     *
     * @param stringContent
     * @return
     */
    public static String md5(String stringContent) {

        String ret = null;

        if (stringContent != null) {
            try {
                //创建 消息摘要的对象，使用MD5算法
                MessageDigest digest = MessageDigest.getInstance("MD5");

                // 计算 stringContent 对应的MD5数据，生成的数据是字节数组，
                // 内部包含不可显示的字节，需要进行编码，才可以转换成字符串
                // 不要使用  new String(byte[]) !!!
                // 需要转换成十六进制内容
                byte[] data = digest.digest(stringContent.getBytes());

                // byte[] 每一个字节 转换为十六进制表示,并且,拼接成一个字符串
                // 0x3C  -> "3C"
                // 0x5   -> "05"

                ret = toHex(data);


            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

}
