package com.wz.common.network;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by WangZ on 2017-04-10.
 */
public class NetClient {
    private static final int SOCKET_TIMEOUT = 15000;
    private static final int CONNECTION_REQUEST_TIMEOUT = 15000;
    private static final int CONNECT_TIMEOUT = 15000;

    public static CloseableHttpResponse doGet(String url) {
        return doGet(url, genDefaultPcHeaders());
    }

    public static CloseableHttpResponse doGet(String url, Map<String, String> headers) {
        if (url == null || "".equals(url)) {
            return null;
        }
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(genRequestConfig());
        addHeaders(httpGet, headers);
        CloseableHttpClient httpClient = HttpClientFactory.getsInstance().getHttpClient();
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("NetClient_异常链接--->" + url);
        }
        return null;
    }

    public static CloseableHttpResponse doPost(String url, Map<String, String> params) {
        return doPost(url, params, genDefaultPcHeaders());
    }

    public static CloseableHttpResponse doPost(String url, JSONObject params) {
        return doPost(url, params, genDefaultPcHeaders());
    }

    public static CloseableHttpResponse doPost(String url, Map<String, String> params, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(genRequestConfig());

        if (params != null) {
            List<BasicNameValuePair> lparams = new LinkedList<BasicNameValuePair>();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (entry.getValue() != null) {
                    lparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                } else {
                    lparams.add(new BasicNameValuePair(entry.getKey(), ""));
                }
            }
            try {
                HttpEntity entity = new UrlEncodedFormEntity(lparams, "UTF-8");
                httpPost.setEntity(entity);
                addHeaders(httpPost, headers);
                CloseableHttpResponse response = HttpClientFactory.getsInstance().getHttpClient().execute(httpPost);
                return response;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("NetClient_异常链接--->" + url);
            }
        }
        return null;
    }

    public static CloseableHttpResponse doPost(String url, JSONObject params, Map<String, String> headers) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(genRequestConfig());
        addHeaders(httpPost, headers);
        if (params != null) {
            StringEntity entity = new StringEntity(params.toString(), "utf-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
        }
        httpPost.setConfig(genRequestConfig());
        try {
            CloseableHttpResponse response = HttpClientFactory.getsInstance().getHttpClient().execute(httpPost);
            return response;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("NetClient_异常链接--->" + url);
        }
        return null;
    }


    private static RequestConfig genRequestConfig() {
        RequestConfig.Builder builder = RequestConfig.custom();
        builder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
                .setConnectTimeout(CONNECT_TIMEOUT)
                .setSocketTimeout(SOCKET_TIMEOUT);
        builder.setCookieSpec(CookieSpecs.STANDARD);
        builder.setCircularRedirectsAllowed(true);
        builder.setRedirectsEnabled(false);
        return builder.build();
    }

    /**
     * 生成默认的pc浏览器header
     *
     * @return
     */
    private static HashMap<String, String> genDefaultPcHeaders() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Connection", "keep-alive");
        headers.put("Accept", "*/*");
        headers.put("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        return headers;
    }

    /**
     * 生成默认的手机浏览器header
     *
     * @return
     */
    private static HashMap<String, String> genDefaultMobileHeaders() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Connection", "keep-alive");
        headers.put("Accept", "*/*");
        headers.put("User-Agent",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        headers.put("Accept-Encoding", "gzip, deflate, br");
        headers.put("Accept-Language", "zh-CN,zh;q=0.8");
        return headers;
    }


    /**
     * 为HttpRequest添加header信息
     *
     * @param request
     * @param headers
     */
    private static void addHeaders(HttpRequest request, Map<String, String> headers) {

        if (headers == null || request == null || headers.size() == 0) {
            return;
        }

        Set<Map.Entry<String, String>> set = headers.entrySet();
        Iterator<Map.Entry<String, String>> iterator = set.iterator();
        for (; iterator.hasNext(); ) {
            Map.Entry<String, String> entry = iterator.next();
            request.addHeader(entry.getKey(), entry.getValue());
        }
    }

    /**
     * getCharSetFromResponse 从Response中获取编码
     *
     * @param response
     * @return
     * @throws @since 3.1.0
     * @permission String
     * @api 5
     */
    public static String getCharSetFromResponse(HttpResponse response, String defaultCharset) {

        if (response != null && 200 == response.getStatusLine().getStatusCode()) {
            Header header = response.getEntity().getContentType();
            if (header != null) {
                HeaderElement[] elements = header.getElements();
                if (elements != null) {
                    HeaderElement element = elements[0];
                    if (null != element.getParameterByName("charset")) {
                        return element.getParameterByName("charset").getValue();
                    }
                }
            }

        }
        return defaultCharset;
    }

    public static String getContentTypeFromResponse(HttpResponse response) {

        if (response != null && 200 == response.getStatusLine().getStatusCode()) {
            Header header = response.getEntity().getContentType();
            if (header != null) {
                HeaderElement[] elements = header.getElements();
                if (elements != null) {
                    HeaderElement element = elements[0];
                    return element.getName();
                }
            }

        }
        return "text/html";
    }
}
