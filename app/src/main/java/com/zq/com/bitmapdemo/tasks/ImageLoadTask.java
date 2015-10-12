package com.zq.com.bitmapdemo.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.zq.com.bitmapdemo.cache.FileCache;
import com.zq.com.bitmapdemo.utils.HttpTools;

import java.lang.ref.WeakReference;

/**
 * Created
 * Author: vhly[FR]
 * Email: vhly@163.com
 * Date: 2015/10/12
 */
public class ImageLoadTask extends AsyncTask<String, Integer, Bitmap> {

    /**
     * 使用弱引用来进行ImageView对象的引用,当UI销毁,任务不再使用ImageView
     */
    private final WeakReference<ImageView> imageViewReference;

    /**
     * 加载的图片，最终显示的宽度
     */
    private int requestWidth;

    private int requestHeight;

    /**
     * 异步任务的构造
     *
     * @param imageView ImageView 需要显示的ImageView
     * @param reqWidth  请求的宽度 0 代表显示原始图像，> 0 将图像缩小
     * @param reqHeight 请求的高度 0 代表显示原始图像，> 0 将图像缩小
     */
    public ImageLoadTask(ImageView imageView, int reqWidth, int reqHeight) {
        imageViewReference = new WeakReference<ImageView>(imageView);

        requestWidth = reqWidth;

        requestHeight = reqHeight;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap ret = null;

        if (params != null && params.length > 0) {

            String url = params[0];

            // 获取 url 对应的文件缓存
            byte[] data = FileCache.getInstance().load(url);

            if (data != null) {
                // TODO 有文件数据,那么不需要联网
            } else {
                // TODO 联网下载图片
                data = HttpTools.doGet(url);
                FileCache.getInstance().save(url, data);
            }

            if (data != null) {


                // 按照原始的图片尺寸，进行Bitmap的生成，
                // 按照Bitmap生成，是按照 图片原始宽高，进行生成，并且每一个像素占用四个字节 也就是 ARGB
                // ret = BitmapFactory.decodeByteArray(data, 0, data.length);


                // 采用二次采样(缩小图片尺寸的方式)

                // 1. 步骤1 获取原始图片的宽高信息，用于进行采样的计算

                // 1.1 创建 Options ，给BitmapFactory 的内部解码器传递参数
                BitmapFactory.Options options = new BitmapFactory.Options();

                // 1.2 设置 inJustDecodeBounds 来控制解码器，只进行图片宽高的获取，不会加载 Bitmap
                //     不占用内存，当使用这个参数，代表 BitmapFactory.decodeXxxx 类似的方法，不会返回 Bitmap
                options.inJustDecodeBounds = true;

                // 解码，使用 Options 参数设置 解码方式
                BitmapFactory.decodeByteArray(data, 0, data.length, options);

                //------------------------------------------

                // 2. 步骤2 根据图片的真实尺寸，与当前需要显示的尺寸，进行计算，生成图片采样率。

                // 2.1

                int picW = options.outWidth;   // 6000
                int picH = options.outHeight;  // 4000

                // 2.2 准备 显示在 手机上的尺寸。 256x128   128x64
                //     尺寸是根据程序需要来设置的。

                // maxWidth, maxHeight
                int reqW = requestWidth;

                int reqH = requestHeight;  // 测试数据

                // 2.3 计算、设置 图片采样率

                options.inSampleSize =
                        calculateInSampleSize(options, reqW, reqH);  // 宽度的 1/32  高度的 1/32

                // 2.4 开放 解码，实际生成Bitmap
                options.inJustDecodeBounds = false;

                // 2.4.1 Bitmap.Config 的说明
                // 要求解码器对于每一个采样的像素，使用 RGB_565 存储方式
                // 一个像素，占用两个 字节；比 ARGB_8888 小了一半。
                // 如果解码器不能够使用指定配置，就自动使用 ARGB_8888
                options.inPreferredConfig = Bitmap.Config.RGB_565;

                // 2.4.2 是一个过时的设置
                options.inPurgeable = true;

                // 2.5 使用设置采样的参数，来进行 解码，获取 Bitmap
                ret = BitmapFactory.decodeByteArray(data, 0, data.length, options);

                // data 需要显示释放
                data = null;

            }
        }

        return ret;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {

            // 获取弱引用包含的对象,可能为null
            ImageView imageView = imageViewReference.get();

            if (imageView != null) {
                // 每一个图片都可以包含 AsyncDrawable对象
                // 这个对象用于处理图片错位的
                Drawable drawable = imageView.getDrawable();

                if (drawable != null && drawable instanceof AsyncDrawable) {
                    // 用于检测图片错位

                    AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;

                    ImageLoadTask task = asyncDrawable.getImageLoadTask();

                    // 当前ImageView 内部包含的 AsyncDrawable和当前
                    // 任务是对应的，代表 当前任务可以设置图片
                    if (this == task) {
                        imageView.setImageBitmap(bitmap);
                    }

                } else { // 不用检测 图片错位的情况
                    imageView.setImageBitmap(bitmap);
                }
            }

        }
    }

    /**
     * 计算图片二次采样的采样率，使用获取图片宽高之后的 Options 作为第一个参数；
     * 并且，通过请求的 宽度、高度尺寸，进行采样率的计算；
     *
     * @param options
     * @param reqWidth  请求的宽度
     * @param reqHeight 请求的高度
     * @return int 采样率
     */
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        // 当请求的宽度、高度 > 0 时候，进行缩放，
        // 否则，图片不进行缩放；
        if (reqWidth > 0 && reqHeight > 0) {
            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) >= reqHeight
                        && (halfWidth / inSampleSize) >= reqWidth) {
                    inSampleSize *= 2;
                }
            }
        }

        return inSampleSize;
    }

}
