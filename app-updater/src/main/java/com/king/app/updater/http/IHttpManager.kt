package com.king.app.updater.http

import java.io.File
import kotlinx.coroutines.flow.Flow

/**
 * Http下载管理器 默认提供 [HttpManager] 和 [OkHttpManager] 两种实现。
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
interface IHttpManager {

    /**
     * 下载
     *
     * @param url 下载地址
     * @param filepath 文件路径
     * @param headers 请求头
     */
    fun download(
        url: String,
        filepath: String,
        headers: Map<String, String>? = null,
    ): Flow<DownloadState>

    /**
     * 取消下载
     */
    fun cancel()

}

/**
 * 下载状态
 */
sealed class DownloadState {
    /**
     * 开始下载
     */
    data class Start(val url: String) : DownloadState()

    /**
     * 下载进度
     */
    data class Progress(
        val progress: Int,
        val total: Int,
    ) : DownloadState()

    /**
     * 下载成功
     */
    data class Success(val file: File) : DownloadState()

    /**
     * 下载出错
     */
    data class Error(val cause: Throwable) : DownloadState()

    /**
     * 下载取消
     */
    data object Cancel : DownloadState()
}
