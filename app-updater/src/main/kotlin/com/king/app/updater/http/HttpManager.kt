package com.king.app.updater.http

import android.os.Build
import com.king.app.updater.constant.Constants
import com.king.app.updater.exception.HttpException
import com.king.logx.LogX
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.Volatile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * HttpManager使用 [HttpURLConnection] 实现
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class HttpManager @JvmOverloads constructor(
    private val timeoutMillis: Int = Constants.TIME_OUT_MILLIS
) : IHttpManager {

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

        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            setRequestProperty("Accept-Encoding", "identity")

            readTimeout = timeoutMillis
            connectTimeout = timeoutMillis

            headers?.forEach { (key, value) ->
                setRequestProperty(key, value)
            }
        }

        LogX.d("Content-Type: ${connection.contentType}")

        when (val responseCode = connection.responseCode) {

            HttpURLConnection.HTTP_OK -> {

                val length = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    connection.contentLengthLong
                } else {
                    connection.contentLength.toLong()
                }
                val outputFile = File(filepath)
                try {
                    connection.inputStream.use { inputStream ->
                        FileOutputStream(outputFile).use { outputStream ->
                            val buffer = ByteArray(8192)
                            var progress = 0L
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
                    connection.disconnect()
                }
            }

            HttpURLConnection.HTTP_MULT_CHOICE,
            HttpURLConnection.HTTP_MOVED_PERM,
            HttpURLConnection.HTTP_MOVED_TEMP,
            HttpURLConnection.HTTP_SEE_OTHER,
            Constants.HTTP_TEMP_REDIRECT,
            Constants.HTTP_PERM_REDIRECT -> {
                // 重定向
                val redirectUrl = connection.getHeaderField("Location")
                LogX.d("Redirect url: $redirectUrl")

                connection.disconnect()

                download(redirectUrl, filepath, headers)
            }

            else -> {
                throw HttpException(responseCode, connection.responseMessage ?: "HTTP Error.")
            }
        }
    }

    override fun cancel() {
        isCancel = true
    }

    companion object {

        private val INSTANCE by lazy {
            HttpManager()
        }

        /**
         * 获取实例
         */
        @JvmStatic
        fun getInstance(): HttpManager {
            return INSTANCE
        }
    }
}
