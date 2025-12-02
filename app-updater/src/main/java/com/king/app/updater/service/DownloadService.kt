package com.king.app.updater.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.king.app.updater.AppUpdater
import com.king.app.updater.R
import com.king.app.updater.constant.Constants
import com.king.app.updater.http.DownloadState
import com.king.app.updater.listener.DownloadListener
import com.king.app.updater.util.AppUtils
import com.king.logx.LogX
import com.king.logx.logger.LogFormat
import java.io.File
import java.util.Locale
import java.util.concurrent.atomic.AtomicInteger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 下载服务
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class DownloadService : Service() {

    private val coroutineScope by lazy {
        CoroutineScope(Dispatchers.Main + Job())
    }

    /**
     * DownloadBinder
     */
    private val downloadBinder = DownloadBinder()

    private var appUpdater: AppUpdater? = null

    @Volatile
    private var downloadListener: DownloadListener? = null

    /**
     * 失败后重新下载次数
     */
    private val retryCount = AtomicInteger(0)

    /**
     * 最后进度更新时间，用来降频刷新
     */
    private var lastTime: Long = 0L

    /**
     * 获取Context
     */
    private val context: Context get() = this

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.getStringExtra(Constants.EXTRA_ACTION)
        LogX.format(LogFormat.PLAIN).d("Action: $action")
        when (action) {
            Constants.ACTION_START_DOWNLOAD -> {
                prepare(false)
            }

            Constants.ACTION_RETRY_DOWNLOAD -> {
                prepare(true)
            }

            Constants.ACTION_CLEAR_LISTENER -> {
                clearListener()
            }

            Constants.ACTION_CANCEL_DOWNLOAD -> {
                stopDownload()
                return START_NOT_STICKY
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun init(appUpdater: AppUpdater) {
        this.appUpdater = appUpdater
        this.downloadListener = appUpdater.downloadListener
    }

    private fun clearListener() {
        this.downloadListener = null
    }

    @Synchronized
    private fun prepare(retry: Boolean) {
        if (appUpdater == null) {
            LogX.w("AppUpdater instance is null. Cannot proceed with download preparation.")
            return
        }

        if (AppUpdater.isDownloading()) {
            LogX.w("Download already in progress. Skipping duplicate request.")
            return
        }

        if (retry) {
            retryCount.incrementAndGet()
        } else {
            retryCount.set(0)
        }

        startDownload(appUpdater!!)
    }

    /**
     * 开始下载
     *
     */
    private fun startDownload(appUpdater: AppUpdater) = coroutineScope.launch {

        val path = AppUtils.getApkCacheFilesDir(context)

        val dirFile = File(path)
        if (!dirFile.exists()) {
            dirFile.mkdirs()
        }

        val apkFile = File(path, appUpdater.filename)

        // 文件是否存在
        if (apkFile.exists()) {
            val versionCode = appUpdater.versionCode
            val apkMd5 = appUpdater.apkMd5
            var isExistApk = false
            if (!apkMd5.isNullOrEmpty()) {
                // 如果存在MD5，则优先校验MD5
                LogX.d("AppUpdater.apkMd5: $apkMd5")
                isExistApk = withContext(Dispatchers.IO) {
                    AppUtils.verifyFileMD5(apkFile, apkMd5)
                }
            } else if (versionCode > 0) {
                // 如果存在versionCode，则校验versionCode
                LogX.d("AppUpdater.versionCode: $versionCode")

                isExistApk = AppUtils.apkExists(context, versionCode, apkFile)
            }

            if (isExistApk) {
                // 本地已经存在要下载的APK
                LogX.d("CacheFile: $apkFile")
                if (appUpdater.installApk) {
                    AppUtils.installApk(context, apkFile, appUpdater.authority)
                }
                downloadListener?.onSuccess(apkFile)
                stopService()
                return@launch
            }

            // 删除旧文件
            apkFile.delete()
        }

        val allowRetry =
            appUpdater.retryOnNotification && retryCount.get() < appUpdater.maxRetryCount
        val url = appUpdater.url

        appUpdater.httpManager.download(
            url = url, filepath = apkFile.absolutePath, headers = appUpdater.headers
        ).collect {
            when (it) {
                is DownloadState.Start -> {
                    LogX.i("Url: $url")
                    AppUpdater.internalDownloadState.emit(true)
                    if (appUpdater.showNotification) {
                        appUpdater.notificationHandler.onStart(
                            context,
                            appUpdater.notificationId,
                            appUpdater.channelId,
                            appUpdater.channelName,
                            appUpdater.notificationIcon,
                            context.getString(R.string.app_updater_notification_start_title),
                            context.getString(R.string.app_updater_notification_start_content),
                            appUpdater.isVibrate,
                            appUpdater.isSound,
                            appUpdater.cancelDownloadOnNotification
                        )
                    }
                    downloadListener?.onStart(url)

                }

                is DownloadState.Progress -> {
                    val currentTime = System.currentTimeMillis()
                    // 降低更新频率
                    if (lastTime + appUpdater.progressUpdateInterval < currentTime || it.progress == it.total) {
                        lastTime = currentTime
                        var progressPercentage = 0
                        if (it.total > 0) {
                            progressPercentage = (it.progress * 100L / it.total).toInt()
                            LogX.format(LogFormat.PLAIN)
                                .d(
                                    "%-4s | %d/%d",
                                    "$progressPercentage%", it.progress, it.total
                                )
                        } else {
                            LogX.format(LogFormat.PLAIN).d("%d/%d", it.progress, it.total)
                        }

                        if (appUpdater.showNotification) {
                            var content = context.getString(
                                R.string.app_updater_notification_progress_content
                            )
                            if (appUpdater.showPercentage && it.total > 0) {
                                content = String.format(
                                    Locale.getDefault(),
                                    "%s %d%%",
                                    content,
                                    progressPercentage
                                )
                            }
                            appUpdater.notificationHandler.onProgress(
                                context,
                                appUpdater.notificationId,
                                appUpdater.channelId,
                                appUpdater.notificationIcon,
                                context.getString(R.string.app_updater_notification_progress_title),
                                content,
                                it.progress,
                                it.total,
                                appUpdater.cancelDownloadOnNotification
                            )
                        }
                        downloadListener?.onProgress(it.progress, it.total)
                    }
                }

                is DownloadState.Success -> {
                    LogX.d("File: ${it.file}")
                    AppUpdater.internalDownloadState.emit(false)
                    retryCount.set(0)
                    if (appUpdater.showNotification) {
                        appUpdater.notificationHandler.onSuccess(
                            context,
                            appUpdater.notificationId,
                            appUpdater.channelId,
                            appUpdater.notificationIcon,
                            context.getString(R.string.app_updater_notification_success_title),
                            context.getString(R.string.app_updater_notification_success_content),
                            it.file,
                            appUpdater.authority
                        )
                    }
                    if (appUpdater.installApk) {
                        AppUtils.installApk(context, it.file, appUpdater.authority)
                    }
                    downloadListener?.onSuccess(it.file)
                    stopService()
                }

                is DownloadState.Error -> {
                    LogX.w(it.cause)
                    AppUpdater.internalDownloadState.emit(false)
                    if (appUpdater.showNotification) {
                        val content = if (allowRetry) {
                            context.getString(R.string.app_updater_notification_error_content_retry_action)
                        } else {
                            context.getString(R.string.app_updater_notification_error_content)
                        }
                        appUpdater.notificationHandler.onError(
                            context,
                            appUpdater.notificationId,
                            appUpdater.channelId,
                            appUpdater.notificationIcon,
                            context.getString(R.string.app_updater_notification_error_title),
                            content,
                            allowRetry,
                        )
                    }
                    downloadListener?.onError(it.cause)
                    if (!allowRetry) {
                        stopService()
                    }
                }

                is DownloadState.Cancel -> {
                    LogX.d("Cancel download.")
                    AppUpdater.internalDownloadState.emit(false)
                    retryCount.set(0)
                    appUpdater.notificationHandler.onCancel(context, appUpdater.notificationId)
                    downloadListener?.onCancel()
                    if (appUpdater.deleteFileOnCancel) {
                        apkFile.delete()
                    }
                    stopService()
                }
            }
        }
    }

    /**
     * 停止下载
     */
    private fun stopDownload() {
        appUpdater?.httpManager?.cancel()
        AppUpdater.internalDownloadState.value = false
    }

    /**
     * 停止服务
     */
    private fun stopService() {
        retryCount.set(0)
        stopSelf()
    }

    override fun onDestroy() {
        AppUpdater.internalDownloadState.value = false
        super.onDestroy()
    }

    //---------------------------------------- Binder

    override fun onBind(intent: Intent): IBinder {
        return downloadBinder
    }

    /**
     * 提供绑定服务的方式进行下载
     */
    inner class DownloadBinder : Binder() {

        /**
         * 开始
         *
         * @param appUpdater [AppUpdater]
         */
        fun start(appUpdater: AppUpdater) {
            init(appUpdater)
            prepare(false)
        }
    }
}
