package com.wz.common.network;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by WangZ on 2017-04-10.
 */
public class HostnameVerifierImpl implements HostnameVerifier {
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
}
