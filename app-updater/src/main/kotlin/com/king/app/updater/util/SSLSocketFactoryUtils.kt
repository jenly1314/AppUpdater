package com.king.app.updater.util

import android.annotation.SuppressLint
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * SSLSocketFactory 工具类
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
object SSLSocketFactoryUtils {

    /**
     * 创建 SSLSocketFactory
     *
     * @return [SSLSocketFactory]
     */
    @JvmStatic
    fun createSSLSocketFactory(): SSLSocketFactory {
        return try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf(TrustAllX509TrustManager(true, null)), null)
            sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    /**
     * 信任所有 X509TrustManager
     */
    @SuppressLint("CustomX509TrustManager")
    class TrustAllX509TrustManager(
        private val isTrustAll: Boolean,
        keystore: KeyStore?
    ) : X509TrustManager {

        private val standardTrustManager: X509TrustManager

        init {
            try {
                val factory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                factory.init(keystore)
                val trustManagers = factory.trustManagers
                if (trustManagers.isEmpty() || trustManagers[0] !is X509TrustManager) {
                    throw IllegalStateException("Unexpected default trust managers: ${trustManagers.contentToString()}")
                }
                standardTrustManager = trustManagers[0] as X509TrustManager
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException(e)
            } catch (e: KeyStoreException) {
                throw RuntimeException(e)
            }
        }

        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
            if (!isTrustAll) {
                standardTrustManager.checkClientTrusted(chain, authType)
            }
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
            if (!isTrustAll) {
                if (chain.size == 1) {
                    chain[0].checkValidity()
                } else {
                    standardTrustManager.checkServerTrusted(chain, authType)
                }
            }
        }

        override fun getAcceptedIssuers(): Array<X509Certificate> {
            return standardTrustManager.acceptedIssuers
        }
    }

    /**
     * 创建一个信任所有证书的 X509TrustManager
     *
     * @return [X509TrustManager]
     */
    @JvmStatic
    fun createTrustAllX509TrustManager(): X509TrustManager {
        return TrustAllX509TrustManager(true, null)
    }

    /**
     * 创建一个忽略校验信任所有主机地址的 HostnameVerifier
     *
     * @return [HostnameVerifier]
     */
    @JvmStatic
    fun createAllowAllHostnameVerifier(): HostnameVerifier {
        return HostnameVerifier { hostname, _ -> hostname != null }
    }
}
