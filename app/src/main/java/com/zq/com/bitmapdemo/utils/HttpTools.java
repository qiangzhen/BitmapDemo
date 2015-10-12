package com.zq.com.bitmapdemo.utils;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Author:甄强
 * Date:2015/10/12
 * Email:978823884@qq.com
 */
public final class HttpTools {

    private HttpTools() {

    }

    /**
     * 根据url地址下载数据，返回字节数组
     *
     * @param url 地址
     * @return
     */

    public static byte[] doGet(String url) {
        byte[] ret = null;
        if (url != null) {
            HttpURLConnection connection = null;
            try {
                URL u = new URL(url);
                connection = (HttpURLConnection) u.openConnection();
                connection.connect();
                int code = connection.getResponseCode();
                if (code == 200) {
                    InputStream fin = null;
                    fin = connection.getInputStream();
                    ret = StreamUtil.readStream(fin);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                StreamUtil.close(connection);
            }

        }

        return ret;
    }

}
