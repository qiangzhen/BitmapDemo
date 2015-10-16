package com.zq.encrypt.utils;

/**
 * Author:甄强
 * Date:2015/10/16
 * Email:978823884@qq.com
 */

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 常用加密工具类，包含：<br/>
 * <ol>
 * <li>Hex</li>
 * </ol>
 */
public final class EncrypUtils {

    private EncrypUtils() {

    }

    //----------------------------

    /**
     * RSA解密
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] rsaDecrypt(byte[] data, Key key) {

        byte[] ret = null;

        if (data != null && data.length > 0 && key != null) {


            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, key);

                ret = cipher.doFinal(data);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }

        }


        return ret;
    }


    /**
     * RSA加密
     *
     * @param data
     * @param key  可以是私钥也可以是密钥
     * @return
     */
    public static byte[] rsaEncrypt(byte[] data, Key key) {

        byte[] ret = null;

        if (data != null && data.length > 0 && key != null) {


            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, key);

                ret = cipher.doFinal(data);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }

        }


        return ret;
    }


    //----------------------------
    //RSA密钥的生成

    /**
     * 通过指定的密钥长度，生成非对称的密钥对
     *
     * @param keySize 推荐使用1024，2048，不用许低于1024
     * @return
     */
    public static KeyPair generateRSAKeyPair(int keySize) {

        KeyPair ret = null;
        try {
            //1 准备生成
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

            //2 初始化，设置密钥长度
            generator.initialize(keySize);

            //3生成，并且返回
            ret = generator.generateKeyPair();


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return ret;
    }


    //AES 支持带有加密模式的方法，形成的加密强度更高，需要iv参数！！！


    public static byte[] aesEncrypt(byte[] data, byte[] keyData, byte[] iv) {

        return aesSingle(Cipher.ENCRYPT_MODE, data, keyData);
    }

    public static byte[] aesDecrypt(byte[] data, byte[] keyData, byte[] iv) {
        return aesSingle(Cipher.DECRYPT_MODE, data, keyData);
    }


    public static byte[] aesWithIv(int mode, byte[] data, byte[] keyData, byte[] ivData) {
        byte[] ret = null;


        if (data != null
                && data.length > 0
                && keyData != null
                && keyData.length == 16
                && ivData != null
                && ivData.length == 16) {


            //支持的加密模式
            //AES/CBC/PKCS5Padding
            //AES/ECB/PKCS5Padding
            try {
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                //密码部分还是设置成AES即可
                SecretKeySpec keySpec = new SecretKeySpec(keyData, "AES");


                //准备iv 参数，用于支持CBC或者ECB模式
                IvParameterSpec iv = new IvParameterSpec(ivData);

                //设置密码以及iv参数
                cipher.init(mode, keySpec, iv);

                ret = cipher.doFinal(data);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }


        return ret;
    }


    //-----------------------------------------------
    //AES的加密与解密，（其中一种设置的方式，采用单一的密码）！！！


    public static byte[] aesEncrypt(byte[] data, byte[] keyData) {

        return aesSingle(Cipher.ENCRYPT_MODE, data, keyData);
    }

    public static byte[] aesDecrypt(byte[] data, byte[] keyData) {
        return aesSingle(Cipher.DECRYPT_MODE, data, keyData);
    }


    public static byte[] aesSingle(int mode, byte[] data, byte[] keyData) {
        byte[] ret = null;

        if (data != null && data.length > 0 && keyData != null && keyData.length == 16) {//128 位AES
            try {
                Cipher cipher = Cipher.getInstance("AES");

                //AES 方式1，单一密码的情况
                SecretKeySpec keySpec = new SecretKeySpec(keyData, "AES");
                cipher.init(mode, keySpec);
                ret = cipher.doFinal(data);

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }

        }


        return ret;
    }


    //-----------------------------------------------
    //DES加密解密

    private static byte[] des(int mode, byte[] data, byte[] keydata) {
        byte[] ret = null;

        if (data != null && data.length > 0 && keydata != null && keydata.length == 8) {
            try {
                Cipher cipher = Cipher.getInstance("DES");

                //3准备Key对象
                //3.1DES使用DESKeySpec
                DESKeySpec spec = new DESKeySpec(keydata);
                //3.2 DESKeySpec需要转换为Key对象，才可以继续使用
                // 需要使用SecretKeyFactory来处理
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                //3.3 生成key对象
                SecretKey secretKey = keyFactory.generateSecret(spec);
                //设置Cipher是加密还是解密，模式
                //同时对于对称加密，还需要设置密码Key对象
                cipher.init(mode, secretKey);
                //加密
                //doFinal（）可以设置字节数组，作为代价密的内容
                //返回值就是最终的加密结果
                ret = cipher.doFinal(data);


            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * DES加密解密
     *
     * @param data
     * @param keydata
     * @return
     */
    public static byte[] desEncrypt(byte[] data, byte[] keydata) {

        return des(Cipher.ENCRYPT_MODE, data, keydata);
    }


    /**
     * des解密
     *
     * @param data
     * @param keydata
     * @return
     */
    public static byte[] desDecrypt(byte[] data, byte[] keydata) {

        return des(Cipher.DECRYPT_MODE, data, keydata);
    }


    /**
     * 将字节数组转换为字符串
     * 一个字节会形成两个字符，最终长度是原始数据的二倍
     *
     * @param data
     * @return
     */
    public static String toHex(byte[] data) {
        String ret = null;

        //TODO 将字节数组转换为字符串

        if (data != null && data.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (byte b : data) {

                //分别获取高四位，低四位的内容，将两个数值转为字符
                int h = (b >> 4) & 0x0f;
                int l = b & 0x0f;

                char ch, cl;

                if (h > 9) {//0x0a!0x0f
                    ch = (char) ('A' + (h - 10));
                } else {
                    ch = (char) ('0' + h);
                }

                if (l > 9) {
                    cl = (char) ('A' + (l - 10));
                } else {
                    cl = (char) ('0' + l);
                }
                sb.append(ch).append(cl);
            }
            ret = sb.toString();
        }


        return ret;
    }

    public static byte[] fromHex(String string) {
        byte[] ret = null;

        //TODO 将Hex编码的字符串，转换为原始的字节数组

        if (string != null) {
            int len = string.length();

            char[] chs = string.toCharArray();
            ret = new byte[len / 2];
            if (len > 0 && len % 2 == 0) {
                for (int i = 0, j = 0; i < len - 1; i = i + 2, j++) {
                    char ch = chs[i];
                    char cl = chs[i + 1];

                    int ih = 0, il = 0, v;

                    if (ch >= 'A' && ch <= 'F') {
                        ih = 10 + (ch - 'A');
                    } else if (ch >= 'a' && ch <= 'f') {
                        ih = 10 + (ch - 'a');
                    } else if (ch >= '0' && ch <= '9') {
                        ih = ch - '0';
                    }

                    if (cl >= 'A' && cl <= 'F') {
                        il = 10 + (cl - 'A');
                    } else if (cl >= 'a' && cl <= 'f') {
                        il = 10 + (cl - 'a');
                    } else if (cl >= '0' && cl <= '9') {
                        il = cl - '0';
                    }

                    v = ((ih & 0x0f) << 4) | (il & 0x0f);
                    ret[j] = (byte) v;
                }
            }
        }

        return ret;
    }
}
