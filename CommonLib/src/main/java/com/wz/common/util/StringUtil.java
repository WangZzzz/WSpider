package com.wz.common.util;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.*;

public class StringUtil {

    public static String responseToString(CloseableHttpResponse response) {

        return responseToString(response, "utf-8");
    }


    public static String responseToString(CloseableHttpResponse response, String charset) {

        if (response != null) {
            BufferedReader bfr = null;
            InputStreamReader isr = null;
            StringBuilder sBuilder = new StringBuilder();
            try {
//                InputStream is = null;
//                try {
//                    is = new GZIPInputStream(response.getEntity().getContent());
//                } catch (ZipException e) {
//                    is = response.getEntity().getContent();
//                } catch (EOFException e) {
//                    is = response.getEntity().getContent();
//                }
                InputStream is = response.getEntity().getContent();
                isr = new InputStreamReader(is, charset);
                bfr = new BufferedReader(isr);
                String line = null;
                while ((line = bfr.readLine()) != null) {
                    sBuilder.append(line);
                    sBuilder.append("\n");
                }
                return sBuilder.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                IoUtil.safeClose(bfr);
                IoUtil.safeClose(isr);
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
