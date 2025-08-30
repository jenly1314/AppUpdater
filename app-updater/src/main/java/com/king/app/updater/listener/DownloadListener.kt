package com.king.app.updater.listener

import java.io.File

/**
 * 下载监听器
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
interface DownloadListener {

    /**
     * 开始下载
     */
    fun onStart(url: String)

    /**
     * 更新进度
     *
     * @param progress  当前进度大小
     * @param total     总文件大小
     */
    fun onProgress(progress: Int, total: Int)

    /**
     * 下载成功
     *
     * @param file APK文件
     */
    fun onSuccess(file: File)

    /**
     * 下载出错
     *
     * @param cause 异常原因
     */
    fun onError(cause: Throwable)

    /**
     * 下载取消
     */
    fun onCancel()
}
