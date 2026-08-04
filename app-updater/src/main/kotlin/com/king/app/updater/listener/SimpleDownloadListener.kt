package com.king.app.updater.listener

import java.io.File

/**
 * 简单的下载监听器
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
open class SimpleDownloadListener : DownloadListener {

    override fun onStart(url: String) {

    }

    override fun onProgress(progress: Long, total: Long) {

    }

    override fun onSuccess(file: File) {

    }

    override fun onError(cause: Throwable) {

    }

    override fun onCancel() {

    }
}
