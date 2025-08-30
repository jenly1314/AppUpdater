package com.king.app.updater.util;

import android.annotation.SuppressLint;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
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
            sslContext.init(null, new TrustManager[]{new TrustAllX509TrustManager(true, null)}, null);
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
    @SuppressLint("CustomX509TrustManager")
    public static class TrustAllX509TrustManager implements X509TrustManager {
        private X509TrustManager standardTrustManager;
        private boolean isTrustAll;

        public TrustAllX509TrustManager(Boolean trustAll, KeyStore keystore) {
            this.isTrustAll = trustAll;
            try {
                TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                factory.init(keystore);
                TrustManager[] trustManagers = factory.getTrustManagers();
                if (trustManagers.length == 0 || !(trustManagers[0] instanceof X509TrustManager)) {
                    throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
                }
                this.standardTrustManager = (X509TrustManager) trustManagers[0];
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyStoreException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            if (!isTrustAll) {
                standardTrustManager.checkClientTrusted(chain, authType);
            }
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            if (!isTrustAll) {
                if (chain != null && chain.length == 1) {
                    chain[0].checkValidity();
                } else {
                    standardTrustManager.checkServerTrusted(chain, authType);
                }
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return standardTrustManager.getAcceptedIssuers();
        }
    }

    /**
     * 创建一个信任所有证书的 X509TrustManager
     *
     * @return {@link X509TrustManager}
     */
    public static X509TrustManager createTrustAllX509TrustManager() {
        return new TrustAllX509TrustManager(true, null);
    }

    /**
     * 创建一个忽略校验信任所有主机地址的 HostnameVerifier
     *
     * @return {@link HostnameVerifier}
     */
    public static HostnameVerifier createAllowAllHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return hostname != null;
            }
        };
    }

}
