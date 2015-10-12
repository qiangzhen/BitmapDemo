package com.zq.com.bitmapdemo.cache;

import android.content.Context;
import android.os.Environment;

import com.zq.com.bitmapdemo.utils.EncryptUtils;
import com.zq.com.bitmapdemo.utils.StreamUtil;

import java.io.*;

/**
 * Created
 * Author: vhly[FR]
 * Email: vhly@163.com
 * Date: 2015/10/12
 */
public class FileCache {
    private static FileCache ourInstance;

    public static FileCache newInstance(Context context) {

        if (context != null) {
            if (ourInstance == null) {
                ourInstance = new FileCache(context);
            }
        } else {
            throw new IllegalArgumentException("Context must be set");
        }

        return ourInstance;

    }

    public static FileCache getInstance() {

        if (ourInstance == null) {
            throw new IllegalStateException("newInstance invoke");
        }

        return ourInstance;
    }

    private Context context;

    private FileCache(Context context) {
        this.context = context;
    }

    /**
     * 从文件存储加载对应网址的内容
     *
     * @param url
     * @return
     */
    public byte[] load(String url) {

        // TODO 通过网址找文件

        byte[] ret = null;

        if (url != null) {

            // 1. 最终获取出来的文件缓存目录
            File cacheDir = null;

            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // 获取存储卡上面应用程序的缓存目录
                cacheDir = context.getExternalCacheDir();
            } else {
                // 内部存储缓存
                cacheDir = context.getCacheDir();
            }

            // 2. 影射文件名称
            String fileName = EncryptUtils.md5(url);

            File targetFile = new File(cacheDir, fileName);

            if (targetFile.exists()) {


                FileInputStream fin = null;

                try {
                    fin = new FileInputStream(targetFile);
                    ret = StreamUtil.readStream(fin);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    StreamUtil.close(fin);

                }

            }
        }

        return ret;
    }

    /**
     * 保存对应网址的数据,到文件中
     *
     * @param url
     * @param data
     */
    public void save(String url, byte[] data) {
        // TODO 通过网址存文件


        if (url != null && data != null) {
            // 1. 最终获取出来的文件缓存目录
            File cacheDir = null;

            String state = Environment.getExternalStorageState();

            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // 获取存储卡上面应用程序的缓存目录
                cacheDir = context.getExternalCacheDir();
            } else {
                // 内部存储缓存
                cacheDir = context.getCacheDir();
            }

            // 2. 影射文件名称
            String fileName = EncryptUtils.md5(url);

            File targetFile = new File(cacheDir, fileName);

            // 3. IO操作
            FileOutputStream fout = null;

            try {
                fout = new FileOutputStream(targetFile);
                fout.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {

                if (fout != null) {
                    try {
                        fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

        }

    }

}
