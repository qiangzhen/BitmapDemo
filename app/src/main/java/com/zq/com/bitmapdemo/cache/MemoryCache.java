package com.zq.com.bitmapdemo.cache;

/**
 * Author:甄强
 * Date:2015/10/12
 * Email:978823884@qq.com
 */


import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * 另外一种图片缓存的方式就是内存缓存技术。在Android中，有一个叫做LruCache类专门用来做图片缓存处理的。
 * 它有一个特点，当缓存的图片达到了预先设定的值的时候，那么近期使用次数最少的图片就会被回收掉。
 * 步骤：（1）要先设置缓存图片的内存大小，我这里设置为手机内存的1/8,
 * 手机内存的获取方式：int MAXMEMONRY = (int) (Runtime.getRuntime() .maxMemory() / 1024);
 * （2）LruCache里面的键值对分别是URL和对应的图片
 * （3）重写了一个叫做sizeOf的方法，返回的是图片数量。
 */
public class MemoryCache {


    private LruCache<String, Bitmap> mMemoryCache;

    private MemoryCache() {
        int MAXMEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024);
        if (mMemoryCache == null) {
            mMemoryCache = new LruCache<String, Bitmap>(MAXMEMORY / 8) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    //// 重写此方法来衡量每张图片的大小，默认返回图片数量。
                    return value.getRowBytes() * value.getHeight() / 1024;
                }

                @Override
                protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
                    super.entryRemoved(evicted, key, oldValue, newValue);
                }
            };
        }
    }


    public void clearCache() {
        if (mMemoryCache != null) {
            if (mMemoryCache.size() > 0) {
                Log.d("CacheUtils",
                        "mMemoryCache.size() " + mMemoryCache.size());
                mMemoryCache.evictAll();
                Log.d("CacheUtils", "mMemoryCache.size()" + mMemoryCache.size());
            }
            mMemoryCache = null;
        }
    }

    public synchronized void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (mMemoryCache.get(key) == null) {
            if (key != null && bitmap != null)
                mMemoryCache.put(key, bitmap);
        } else
            Log.w("add", "the res is aready exits");
    }

    public synchronized Bitmap getBitmapFromMemCache(String key) {
        Bitmap bm = mMemoryCache.get(key);
        if (key != null) {
            return bm;
        }
        return null;
    }

    /**
     * 移除缓存
     *
     * @param key
     */
    public synchronized void removeImageCache(String key) {
        if (key != null) {
            if (mMemoryCache != null) {
                Bitmap bm = mMemoryCache.remove(key);
                if (bm != null)
                    bm.recycle();
            }
        }
    }


}
