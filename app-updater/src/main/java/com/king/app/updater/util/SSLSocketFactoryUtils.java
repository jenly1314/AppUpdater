package com.king.app.updater.util;

import android.annotation.SuppressLint;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * SSLSocketFactory 工具
 *
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public final class SSLSocketFactoryUtils {

    private SSLSocketFactoryUtils() {
        throw new AssertionError();
    }

    /**
     * 创建 SSLSocketFactory
     *
     * @return {@link SSLSocketFactory}
     */
    @SuppressLint("TrulyRandom")
    public static SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new TrustAllX509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 信任所有 X509TrustManager
     *
     * @return {@link X509TrustManager}
     */
    public static class TrustAllX509TrustManager implements X509TrustManager {

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            //do nothing
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            //do nothing
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * 创建一个信任所有证书的 X509TrustManager
     *
     * @return {@link X509TrustManager}
     */
    public static X509TrustManager createTrustAllX509TrustManager() {
        return new TrustAllX509TrustManager();
    }

    /**
     * 创建一个忽略校验信任所有主机地址的 HostnameVerifier
     *
     * @return {@link HostnameVerifier}
     */
    public static HostnameVerifier createAllowAllHostnameVerifier() {

        return new HostnameVerifier() {
            @SuppressLint("BadHostnameVerifier")
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }


}
