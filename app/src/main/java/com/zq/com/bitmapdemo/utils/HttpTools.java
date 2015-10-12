package com.zq.com.bitmapdemo.utils;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 执行post请求
     *
     * @param url
     * @param map
     */
    public static void doPost(String url, HashMap<String, String> map) throws UnsupportedEncodingException {

        if (url != null) {

            //把药提交的数据组织起来
            StringBuilder sb = new StringBuilder();

            for (Map.Entry<String, String> en : map.entrySet()) {
                sb.append(en.getKey())
                        .append("=")
                        .append(URLEncoder.encode(en.getValue(), "utf-8"));
            }


            HttpURLConnection connection = null;

            try {
                URL u = new URL(url);
                connection = (HttpURLConnection) u.openConnection();

                //基本设置
                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoOutput(true);
                connection.setDoInput(true);

                //设置提交的数据的类型
                connection.setRequestProperty("Content-Type", "application/x-www-from-urlencoded");
                connection.connect();

                //设置提交的数据
                byte[] b = sb.toString().getBytes();

                //提交数据，向服务端写入数据
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(b, 0, b.length);
                outputStream.close();

                InputStream in = null;
                if (connection.getResponseCode() == 200) {
                    in = connection.getInputStream();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
