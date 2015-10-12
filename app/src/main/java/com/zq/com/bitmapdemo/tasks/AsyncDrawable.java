package com.zq.com.bitmapdemo.tasks;

/**
 * Created with IntelliJ IDEA.
 * User: vhly[FR]
 * Date: 15/10/12
 * Email: vhly@163.com
 */

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * 异步下载的Drawable，实际上就是解决图片错位问题
 * 自身是不需要设置图片之类了
 */
public class AsyncDrawable extends BitmapDrawable {

    /**
     * 真正下载图片的部分
     */
    private final WeakReference<ImageLoadTask> taskReference;

    /**
     * 模拟一个 BitmapDrawable，这个Drawable 对象，就可以直接给ImageView设置
     *
     * @param res
     * @param bitmap
     * @param task
     */
    public AsyncDrawable(Resources res, Bitmap bitmap, ImageLoadTask task) {
        super(res, bitmap);
        taskReference = new WeakReference<ImageLoadTask>(task);
    }

    /**
     * 获取 当前 Drawable包含的 异步任务
     *
     * @return
     */
    public ImageLoadTask getImageLoadTask() {
        ImageLoadTask ret = null;
        ret = taskReference.get();
        return ret;
    }
}
