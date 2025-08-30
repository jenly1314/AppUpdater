package com.king.app.updater.http

import com.king.app.updater.constant.Constants
import com.king.app.updater.exception.HttpException
import com.king.app.updater.util.SSLSocketFactoryUtils
import com.king.logx.LogX
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.closeQuietly

/**
 * OkHttpManager使用 [OkHttpClient] 实现
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class OkHttpManager(private val okHttpClient: OkHttpClient) : IHttpManager {

    @JvmOverloads
    constructor(timeoutMillis: Int = Constants.TIME_OUT_MILLIS) : this(
        okHttpClient = OkHttpClient.Builder()
            .readTimeout(timeoutMillis.toLong(), TimeUnit.MILLISECONDS)
            .connectTimeout(timeoutMillis.toLong(), TimeUnit.MILLISECONDS)
            .sslSocketFactory(
                SSLSocketFactoryUtils.createSSLSocketFactory(),
                SSLSocketFactoryUtils.createTrustAllX509TrustManager()
            )
            .hostnameVerifier(SSLSocketFactoryUtils.createAllowAllHostnameVerifier())
            .build()
    )

    @Volatile
    private var isCancel = false

    override fun download(
        url: String,
        filepath: String,
        headers: Map<String, String>?
    ): Flow<DownloadState> {
        return flow {
            isCancel = false
            emit(DownloadState.Start(url))
            startDownload(url, filepath, headers)
        }.catch {
            LogX.w(it)
            emit(DownloadState.Error(it))
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun FlowCollector<DownloadState>.startDownload(
        url: String,
        filepath: String,
        headers: Map<String, String>?,
    ) {
        val builder: Request.Builder = Request.Builder()
            .url(url)
            .addHeader("Accept-Encoding", "identity")
            .get()

        headers?.forEach {
            builder.addHeader(it.key, it.value)
        }

        val call = okHttpClient.newCall(builder.build())
        val response = call.execute()

        LogX.d("Content-Type: ${response.headers["Content-Type"]}")

        if (response.isSuccessful) {
            try {
                val body = response.body ?: throw IllegalStateException("Response body is null.")

                val length = body.contentLength().toInt()
                val outputFile = File(filepath)

                body.byteStream().use { inputStream ->
                    FileOutputStream(outputFile).use { outputStream ->
                        val buffer = ByteArray(8192)
                        var progress = 0
                        while (true) {
                            val bytesRead = inputStream.read(buffer)
                            if (bytesRead == -1 || isCancel) break

                            outputStream.write(buffer, 0, bytesRead)
                            progress += bytesRead

                            emit(DownloadState.Progress(progress, length))
                        }
                        outputStream.flush()

                        check(progress > 0 || length > 0) {
                            "Invalid download state: progress=$progress, contentLength=$length"
                        }
                    }
                }

                if (isCancel) {
                    emit(DownloadState.Cancel)
                } else {
                    emit(DownloadState.Success(outputFile))
                }
            } finally {
                response.closeQuietly()
            }
        } else {
            throw HttpException(response.code, response.message)
        }
    }

    override fun cancel() {
        isCancel = true
    }

    companion object {

        private val INSTANCE by lazy {
            OkHttpManager()
        }

        /**
         * 获取实例
         */
        @JvmStatic
        fun getInstance(): OkHttpManager {
            return INSTANCE
        }
    }
}
