package com.king.app.updater.util;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.RawRes;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
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
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public final class SSLSocketFactoryUtils {

    private static final String[] VERIFY_HOST_NAME = new String[]{};

    private SSLSocketFactoryUtils() {
        throw new AssertionError();
    }

    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, getTrustAllManager(), new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {

        }
        return sslSocketFactory;
    }

    public static X509TrustManager createTrustAllManager() {
        X509TrustManager tm = null;
        try {
            tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    //do nothing
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    //do nothing
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tm;
    }

    public static TrustAllHostnameVerifier createTrustAllHostnameVerifier() {
        return new TrustAllHostnameVerifier();
    }

    public static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            if (TextUtils.isEmpty(hostname)) {
                return false;
            }
            return !Arrays.asList(VERIFY_HOST_NAME).contains(hostname);
        }
    }

    /**
     * @param context
     * @param keyServerStoreID
     * @return
     */
    public static SSLSocketFactory createSSLSocketFactory(Context context, @RawRes int keyServerStoreID) {
        InputStream trustStream = context.getResources().openRawResource(keyServerStoreID);
        return createSSLSocketFactory(trustStream);
    }

    /**
     * @param certificates
     * @return
     */
    public static SSLSocketFactory createSSLSocketFactory(InputStream... certificates) {
        SSLSocketFactory sSLSocketFactory = null;
        if (sSLSocketFactory == null) {
            synchronized (SSLSocketFactoryUtils.class) {
                if (sSLSocketFactory == null) {
                    try {
                        SSLContext sslContext = SSLContext.getInstance("TLS");
                        sslContext.init(null, getTrustManager(certificates), new SecureRandom());
                        sSLSocketFactory = sslContext.getSocketFactory();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return sSLSocketFactory;
    }

    /**
     * 获得指定流中的服务器端证书库
     *
     * @param certificates
     * @return
     */
    public static TrustManager[] getTrustManager(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            int index = 0;
            for (InputStream certificate : certificates) {
                if (certificate == null) {
                    continue;
                }
                Certificate certificate1;
                try {
                    certificate1 = certificateFactory.generateCertificate(certificate);
                } finally {
                    certificate.close();
                }

                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificate1);
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
                    .getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();

        } catch (Exception e) {
            e.printStackTrace();

        }

        return getTrustAllManager();
    }

    /**
     * 获得信任所有服务器端证书库
     */
    public static TrustManager[] getTrustAllManager() {
        return new TrustManager[]{createTrustAllManager()};
    }


}
