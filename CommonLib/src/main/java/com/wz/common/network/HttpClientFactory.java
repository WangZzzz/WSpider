package com.wz.common.network;


import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by WangZ on 2017-04-10.
 */
public class HttpClientFactory {

    private static volatile HttpClientFactory sInstance;

    private CloseableHttpClient mHttpClient;

    private CookieStore mCookieStore;

    private HttpClientFactory() {
        mCookieStore = new BasicCookieStore();
    }

    public static HttpClientFactory getsInstance() {
        if (sInstance == null) {
            synchronized (HttpClientFactory.class) {
                if (sInstance == null) {
                    sInstance = new HttpClientFactory();
                }
            }
        }
        return sInstance;
    }

    public CloseableHttpClient getHttpClient() {
        if (mHttpClient != null) {
            return mHttpClient;
        }
        HttpClientBuilder builder = HttpClients.custom();
        //HTTPS设置
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            TrustManager tm = new X509TrustManagerImpl();
            sslContext.init(null, new TrustManager[]{tm}, new java.security.SecureRandom());
            builder.setSSLContext(sslContext);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        builder.setSSLHostnameVerifier(new HostnameVerifierImpl());

        //设置代理，通过fiddler抓包
//        HttpHost httpHost = new HttpHost("127.0.0.1", 8888);
//        builder.setProxy(httpHost);
        //LaxRedirectStractegy 可以自动重定向所有的HEAD，GET，POST请求，解除了http规范对post请求重定向的限制
        builder.setRedirectStrategy(new LaxRedirectStrategy());
        mHttpClient = builder.build();
        return mHttpClient;
    }

    public CookieStore getCookieStore() {
        return mCookieStore;
    }

    public void addCookie(Cookie cookie) {
        if (cookie != null) {
            mCookieStore.addCookie(cookie);
        }
    }
}
