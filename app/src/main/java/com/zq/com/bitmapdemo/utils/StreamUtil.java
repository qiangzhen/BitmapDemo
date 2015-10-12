package com.zq.com.bitmapdemo.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;

/**
 * Author:甄强
 * Date:2015/10/12
 * Email:978823884@qq.com
 */
public final class StreamUtil {

    private StreamUtil() {

    }

    /**
     * 关闭流或者网络连接
     *
     * @param o
     */
    public static void close(Object o) {
        if (o != null) {
            try {
                if (o instanceof InputStream) {
                    ((InputStream) o).close();
                } else if (o instanceof OutputStream) {
                    ((OutputStream) o).close();
                } else if (o instanceof Readable) {
                    ((Reader) o).close();
                } else if (o instanceof Writer) {
                    ((Writer) o).close();
                } else if (o instanceof HttpURLConnection) {
                    ((HttpURLConnection) o).disconnect();
                }

            } catch (Exception ex) {

            }
        }
    }

    /**
     * 又输入流读出字节数组
     * @param in
     * @return
     * @throws IOException
     */
    public static byte[] readStream(InputStream in) throws IOException {
        byte[] ret = null;

        if (in != null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[128];
            int len;
            while (true) {
                len = in.read(buffer);
                if (len == -1) {
                    break;
                }
                bos.write(buffer, 0, len);
            }
            // 注意  buf 必须要进行 = null 的操作
            // 减少内存溢出的可能性
            buffer = null;
            ret = bos.toByteArray();
            bos.close();

        }


        return ret;
    }

}
