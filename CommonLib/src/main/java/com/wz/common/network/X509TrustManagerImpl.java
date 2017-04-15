package com.wz.common.network;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class X509TrustManagerImpl implements X509TrustManager {

    public void checkClientTrusted(X509Certificate[] certificates, String authType) throws CertificateException {

        // TODO Auto-generated method stub
    }


    public void checkServerTrusted(X509Certificate[] certificates, String authType) throws CertificateException {

        // TODO Auto-generated method stub
    }


    public X509Certificate[] getAcceptedIssuers() {

        // TODO Auto-generated method stub
        X509Certificate[] certificates = new X509Certificate[]{};
        return certificates;
    }
}
