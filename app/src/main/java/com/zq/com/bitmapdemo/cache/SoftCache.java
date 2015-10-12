package com.zq.com.bitmapdemo.cache;

/**
 * Author:甄强
 * Date:2015/10/12
 * Email:978823884@qq.com
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 软引用缓存技术
 */
public class SoftCache {

    /**
     * 步骤：1，通过url查看缓存中是否有图片，如果有直接从缓存中获得
     * 如果没有，就开线程去下载
     * 2，下载完后放入缓存
     */


    private static Map<String, SoftReference<Bitmap>> imageMap = new HashMap<String, SoftReference<Bitmap>>();

    public static Bitmap loadBitmap(final String imgUrl, final ImageCallback imageCallback) {

        SoftReference<Bitmap> reference = imageMap.get(imgUrl);
        if (reference != null) {
            return reference.get();
        }

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                Bitmap bitmap = (Bitmap) msg.obj;
                imageMap.put(imgUrl, new SoftReference<Bitmap>(bitmap));
                if (imageCallback != null) {
                    imageCallback.getBitmap(bitmap);

                }
            }
        };

        new Thread() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                message.obj = downloadBitmao(imgUrl);
                handler.sendMessage(message);
            }
        }.start();
        return null;


    }

    private static Bitmap downloadBitmao(String imgUrl) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface ImageCallback {
        void getBitmap(Bitmap bitmap);
    }
}
