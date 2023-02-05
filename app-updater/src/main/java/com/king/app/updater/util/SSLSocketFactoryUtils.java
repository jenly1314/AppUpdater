package com.king.app.updater.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

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
    public static SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, getTrustAllManager(), null);
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建 X509TrustManager
     *
     * @return {@link X509TrustManager}
     */
    @SuppressLint("TrustAllX509TrustManager")
    public static X509TrustManager createX509TrustManager() {
        return new X509TrustManager() {

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
        };
    }

    /**
     * 创建一个忽略校验信任所有主机的验证器
     *
     * @return {@link HostnameVerifier}
     */
    public static HostnameVerifier createAllowAllHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return !TextUtils.isEmpty(hostname);
            }
        };
    }

    /**
     * 获得信任所有服务器端证书
     *
     * @return {@link TrustManager}
     */
    private static TrustManager[] getTrustAllManager() {
        return new TrustManager[]{createX509TrustManager()};
    }

}
