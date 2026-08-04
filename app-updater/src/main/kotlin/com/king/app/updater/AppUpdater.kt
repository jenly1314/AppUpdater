package com.king.app.updater

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.king.app.updater.AppUpdater.Builder
import com.king.app.updater.constant.Constants
import com.king.app.updater.http.HttpManager
import com.king.app.updater.http.IHttpManager
import com.king.app.updater.listener.DownloadListener
import com.king.app.updater.notification.INotificationHandler
import com.king.app.updater.notification.NotificationHandler
import com.king.app.updater.service.DownloadService
import com.king.app.updater.util.AppUtils
import com.king.app.updater.util.PermissionUtils
import com.king.logx.LogX
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * AppUpdater：是一个轻量级App更新库，封装了完整的下载更新流程，提供开箱即用的一键更新功能。无需关注技术细节，简单调用即可完成更新，同时也支持自定义配置。
 *
 * @param builder [Builder]
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class AppUpdater private constructor(builder: Builder) {

    /**
     * 快捷构造
     *
     * @param context [Context]
     * @param url APK下载地址
     */
    constructor(context: Context, url: String) : this(Builder(context).setUrl(url))

    private val applicationContext = builder.applicationContext

    /** APK下载地址 */
    internal val url: String = builder.url ?: ""

    /** 保存的apk文件名；例如：app.apk */
    internal val filename: String

    /** 是否显示通知栏 */
    internal val showNotification = builder.showNotification

    /** 下载完成后是否自动请求安装APK */
    internal val installApk = builder.installApk

    /** 通知栏图标：默认取app图标 */
    internal val notificationIcon: Int

    /** 通知栏ID */
    internal val notificationId = builder.notificationId

    /** 通知栏渠道ID */
    internal val channelId = builder.channelId

    /** 通知栏渠道名称 */
    internal val channelName = builder.channelName

    /** 默认 Context.getPackageName() + ".AppUpdaterFileProvider" */
    internal val authority = builder.authority

    /** 下载失败是否支持点击通知栏重新下载 */
    internal val retryOnNotification = builder.retryOnNotification

    /** 下载失败后，最大重新下载次数 */
    internal val maxRetryCount = builder.maxRetryCount

    /** 是否显示百分比 */
    internal val showPercentage = builder.showPercentage

    /** 是否震动提示，为true时使用通知默认震动 */
    internal val isVibrate = builder.isVibrate

    /** 是否铃声提示，为true时使用通知默认铃声 */
    internal val isSound = builder.isSound

    /** 要下载的APK的versionCode */
    internal val versionCode = builder.versionCode

    /** 请求头参数 */
    internal val headers: Map<String, String> = builder.headers

    /** 是否删除取消下载的文件 */
    internal val deleteFileOnCancel = builder.deleteFileOnCancel

    /** 是否支持通过删除通知栏来取消下载 */
    internal val cancelDownloadOnNotification = builder.cancelDownloadOnNotification

    /** APK文件的MD5 */
    internal val apkMd5 = builder.apkMd5

    /** 进度更新间隔时长（单位：毫秒） */
    internal val progressUpdateInterval = builder.progressUpdateInterval

    /** HTTP管理器 */
    internal val httpManager = builder.httpManager

    /** 通知栏处理器实现 */
    internal val notificationHandler = builder.notificationHandler

    /** 下载监听 */
    @set:JvmSynthetic
    internal var downloadListener = builder.downloadListener

    private var isServiceBound = false

    init {
        this.filename = obtainApkName(applicationContext, builder.filename)
        this.notificationIcon = obtainNotificationIcon(applicationContext, builder.notificationIcon)
    }

    private fun obtainApkName(context: Context, filename: String?): String {
        val apkName = if (filename.isNullOrEmpty()) {
            AppUtils.getAppName(context) ?: context.packageName
        } else {
            filename
        }
        return if (apkName.endsWith(".apk", true)) {
            apkName
        } else {
            "$apkName.apk"
        }
    }

    private fun obtainNotificationIcon(context: Context, notificationIcon: Int): Int {
        return notificationIcon.takeIf { it > 0 }
            ?: AppUtils.getAppIcon(context).takeIf { it > 0 }
            ?: R.drawable.app_updater_notification_icon
    }

    /**
     * 开始下载
     */
    fun start() {
        if (url.isEmpty()) {
            throw IllegalArgumentException("Url must not be empty.")
        }

        if (showNotification && !PermissionUtils.checkNotificationPermission(applicationContext)) {
            LogX.w("Notification permission denied. Some features may be unavailable.")
        }

        // 没有通知栏权限，只影响通知栏进度更新状态，不影响App下载
        startDownloadService()
    }

    private fun startDownloadService() {
        val intent = Intent(applicationContext, DownloadService::class.java).apply {
            putExtra(Constants.EXTRA_ACTION, Constants.ACTION_START_DOWNLOAD)
        }

        if (!isServiceBound) {
            val serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName, service: IBinder) {
                    (service as DownloadService.DownloadBinder).apply {
                        start(this@AppUpdater)
                        isServiceBound = true
                    }
                }

                override fun onServiceDisconnected(name: ComponentName) {
                    isServiceBound = false
                }
            }
            applicationContext.bindService(
                intent,
                serviceConnection,
                Context.BIND_AUTO_CREATE
            )
        } else {
            applicationContext.startService(intent)
        }
    }

    /**
     * 停止下载
     */
    fun stop() {
        startDownloadService(Constants.ACTION_CANCEL_DOWNLOAD)
    }

    /**
     * 清除监听器
     */
    fun clearListener() {
        this.downloadListener = null
        startDownloadService(Constants.ACTION_CLEAR_LISTENER)
    }

    private fun startDownloadService(action: String) {
        val intent = Intent(applicationContext, DownloadService::class.java)
        intent.putExtra(Constants.EXTRA_ACTION, action)
        applicationContext.startService(intent)
    }

    companion object {

        internal val internalDownloadState = MutableStateFlow(false)

        /**
         * 下载状态
         */
        val downloadState: StateFlow<Boolean> = internalDownloadState

        /**
         * 是否正在下载
         */
        @JvmStatic
        fun isDownloading() = internalDownloadState.value

    }

    /**
     * [AppUpdater.Builder]
     */
    class Builder(context: Context) {

        internal val applicationContext: Context = context.applicationContext

        /** APK下载地址 */
        @set:JvmSynthetic
        var url: String? = null

        /** 保存的apk文件名；例如：app.apk */
        @set:JvmSynthetic
        var filename: String? = null

        /** 是否显示通知栏 */
        @set:JvmSynthetic
        var showNotification: Boolean = true

        /** 下载完成后是否自动请求安装APK */
        @set:JvmSynthetic
        var installApk: Boolean = true

        /** 通知栏图标：默认取app图标 */
        @set:JvmSynthetic
        var notificationIcon: Int = 0

        /** 通知栏ID */
        @set:JvmSynthetic
        var notificationId: Int = Constants.DEFAULT_NOTIFICATION_ID

        /** 通知栏渠道ID */
        @set:JvmSynthetic
        var channelId: String = Constants.DEFAULT_NOTIFICATION_CHANNEL_ID

        /** 通知栏渠道名称 */
        @set:JvmSynthetic
        var channelName: String = Constants.DEFAULT_NOTIFICATION_CHANNEL_NAME

        /** 默认 Context.getPackageName() + ".AppUpdaterFileProvider" */
        @set:JvmSynthetic
        var authority: String = AppUtils.getFileProviderAuthority(context)

        /** 下载失败时是否支持点击通知栏重新下载 */
        @set:JvmSynthetic
        var retryOnNotification: Boolean = true

        /** 下载失败后，最大重新下载次数 */
        @set:JvmSynthetic
        var maxRetryCount: Int = 3

        /** 是否显示百分比 */
        @set:JvmSynthetic
        var showPercentage: Boolean = true

        /** 是否震动提示，为true时使用通知默认震动 */
        @set:JvmSynthetic
        var isVibrate: Boolean = false

        /** 是否铃声提示，为true时使用通知默认铃声 */
        @set:JvmSynthetic
        var isSound: Boolean = false

        /** 要下载的APK的versionCode */
        @set:JvmSynthetic
        var versionCode: Int = Constants.NONE

        /** 请求头参数 */
        @set:JvmSynthetic
        var headers: MutableMap<String, String> = mutableMapOf()

        /** 是否删除取消下载的文件 */
        @set:JvmSynthetic
        var deleteFileOnCancel: Boolean = true

        /** 是否支持通过删除通知栏来取消下载 */
        @set:JvmSynthetic
        var cancelDownloadOnNotification: Boolean = false

        /** APK文件的MD5 */
        @set:JvmSynthetic
        var apkMd5: String? = null

        /** 进度更新间隔时长（单位：毫秒） */
        @set:JvmSynthetic
        var progressUpdateInterval: Long = Constants.PROGRESS_UPDATE_INTERVAL

        /** HTTP管理器 */
        @set:JvmSynthetic
        var httpManager: IHttpManager = HttpManager.getInstance()

        /** 通知栏处理器实现 */
        @set:JvmSynthetic
        var notificationHandler: INotificationHandler = NotificationHandler.getInstance()

        /** 下载监听 */
        @set:JvmSynthetic
        var downloadListener: DownloadListener? = null

        /**
         * 设置APK下载地址
         *
         * @param url 下载地址
         * @return
         */
        fun setUrl(url: String) = apply {
            this.url = url
        }

        /**
         * 设置保存的文件名
         *
         * @param filename 下载的保存的apk文件名（例如：app.apk）
         * @return
         */
        fun setFilename(filename: String) = apply {
            this.filename = filename
        }

        /**
         * 设置是否显示通知栏
         *
         * @param isShowNotification 是否显示通知栏（默认true）
         * @return
         */
        fun setShowNotification(isShowNotification: Boolean) = apply {
            this.showNotification = isShowNotification
        }

        /**
         * 设置通知ID
         *
         * @param notificationId 通知ID
         * @return
         */
        fun setNotificationId(notificationId: Int) = apply {
            this.notificationId = notificationId
        }

        /**
         * 设置通知通道ID
         *
         * @param channelId 通知通道ID（默认兼容O）
         * @return
         */
        fun setChannelId(channelId: String) = apply {
            this.channelId = channelId
        }

        /**
         * 设置通知通道名称
         *
         * @param channelName 通知通道名称（默认兼容O）
         * @return
         */
        fun setChannelName(channelName: String) = apply {
            this.channelName = channelName
        }

        /**
         * 设置通知图标
         *
         * @param notificationIcon 通知栏图标（默认取App的icon）
         * @return
         */
        fun setNotificationIcon(@DrawableRes notificationIcon: Int) = apply {
            this.notificationIcon = notificationIcon
        }

        /**
         * 设置通知是否震动提示
         *
         * @param vibrate 是否震动提示，为true时使用通知默认震动，Android O(8.0)以上设置，只有初次创建channel时有效，后续修改属性无效，想要重新有效需修改channelId或卸载App重装。
         * @return
         */
        fun setVibrate(vibrate: Boolean) = apply {
            this.isVibrate = vibrate
        }

        /**
         * 设置通知是否铃声提示
         *
         * @param sound 是否铃声提示，为true时使用通知默认铃声，Android O(8.0)以上设置，只有初次创建channel时有效，后续修改属性无效，想要重新有效需修改channelId或卸载App重装。
         * @return
         */
        fun setSound(sound: Boolean) = apply {
            this.isSound = sound
        }

        /**
         * 设置下载完成后是否自动请求安装APK
         *
         * @param installApk 下载完成后是否自动请求安装APK（默认true）
         * @return
         */
        fun setInstallApk(installApk: Boolean) = apply {
            this.installApk = installApk
        }

        /**
         * 设置FileProvider的authority
         *
         * @param authority FileProvider的authority（默认兼容N，默认值为：Context#getPackageName() + ".AppUpdaterFileProvider"）
         * @return
         */
        fun setAuthority(authority: String) = apply {
            this.authority = authority
        }

        /**
         * 设置下载时，通知栏是否显示下载百分比
         *
         * @param showPercentage 下载时通知栏是否显示百分比
         * @return
         */
        fun setShowPercentage(showPercentage: Boolean) = apply {
            this.showPercentage = showPercentage
        }

        /**
         * 设置下载失败时，是否支持点击通知栏重新下载。与之相关联的方法[setMaxRetryCount]
         *
         * @param retryOnNotification 下载失败时是否支持点击通知栏重新下载，默认true
         * @return
         */
        fun setRetryOnNotification(retryOnNotification: Boolean) = apply {
            this.retryOnNotification = retryOnNotification
        }

        /**
         * 设置下载失败时，最多重新下载次数。与之相关联的方法[setRetryOnNotification]
         *
         * @param maxRetryCount 下载失败时是否支持点击通知栏重新下载，默认最多重新下载3次
         * @return
         */
        fun setMaxRetryCount(maxRetryCount: Int) = apply {
            this.maxRetryCount = maxRetryCount
        }

        /**
         * 设置要下载APK的versionCode，用于优先取缓存时通过versionCode校验APK文件是否一致。
         * 缓存校验目前支持两种方式，一种是通过versionCode校验，对应方法：[setVersionCode]；一种是文件MD5校验，对应方法：[setApkMd5]。推荐使用MD5校验方式
         * 如果两种方式都设置了，则只校验MD5
         *
         * @param versionCode 为null表示不处理，默认不存在则下载，存在则重新下载。不为null时，表示会优先校验本地是否存在已下载版本号为versionCode的APK。
         *                    如果存在则不会重新下载(AppUpdater会自动校验packageName一致性)，直接取本地APK，反之重新下载。
         * @return
         */
        fun setVersionCode(versionCode: Int) = apply {
            this.versionCode = versionCode
        }

        /**
         * 设置APK文件的MD5，用于优先取缓存时通过MD5校验文件APK是否一致。
         * 缓存校验目前支持两种方式，一种是通过versionCode校验，对应方法：[setVersionCode]；一种是文件MD5校验，对应方法：[setApkMd5]。推荐使用MD5校验方式
         * 如果两种方式都设置了，则只校验MD5
         *
         * @param md5 为null表示不处理，如果设置了MD5，则缓存APK的MD5相同时，只下载一次，优先取本地缓存
         * @return
         */
        fun setApkMd5(md5: String) = apply {
            this.apkMd5 = md5
        }

        /**
         * 设置是否自动删除取消下载的文件
         *
         * @param deleteFileOnCancel 是否删除取消下载的文件（默认为true）
         */
        fun setDeleteFileOnCancel(deleteFileOnCancel: Boolean) = apply {
            this.deleteFileOnCancel = deleteFileOnCancel
        }

        /**
         * 是否支持通过删除通知栏来取消下载（默认为：false）
         *
         * @param cancelDownloadOnNotification
         * @return
         */
        fun setCancelDownloadOnNotification(cancelDownloadOnNotification: Boolean) = apply {
            this.cancelDownloadOnNotification = cancelDownloadOnNotification
        }

        /**
         * 设置HTTP管理器
         */
        fun setHttpManager(httpManager: IHttpManager) = apply {
            this.httpManager = httpManager
        }

        /**
         * 设置通知栏处理器
         */
        fun setNotificationHandler(notificationHandler: INotificationHandler) = apply {
            this.notificationHandler = notificationHandler
        }

        /**
         * 设置下载监听
         */
        fun setDownloadListener(listener: DownloadListener?) = apply {
            this.downloadListener = listener
        }

        /**
         * 添加请求头
         */
        fun addHeader(key: String, value: String) = apply {
            headers[key] = value
        }

        /**
         * 添加请求头
         */
        fun addHeader(headers: Map<String, String>) = apply {
            this.headers.putAll(headers)
        }

        /**
         * 移除请求头
         */
        fun removeHeader(key: String) = apply {
            this.headers.remove(key)
        }

        /**
         * 清空请求头
         */
        fun clearHeaders() = apply {
            this.headers.clear()
        }

        /**
         * 构建[AppUpdater]
         */
        fun build(): AppUpdater {
            return AppUpdater(this)
        }
    }
}

/**
 * DSL
 */
@JvmSynthetic
fun appUpdater(context: Context, block: Builder.() -> Unit): AppUpdater {
    return Builder(context).apply(block).build()
}

/**
 * 扩展DSL
 */
@JvmName("appUpdaterWithContext")
@JvmSynthetic
fun Context.appUpdater(block: Builder.() -> Unit): AppUpdater {
    return Builder(this).apply(block).build()
}

/**
 * 扩展DSL
 */
@JvmName("appUpdaterWithContext")
@JvmSynthetic
fun Context.appUpdater(url: String): AppUpdater {
    return Builder(this).setUrl(url).build()
}

/**
 * 扩展DSL
 */
@JvmName("appUpdaterWithContext")
@JvmSynthetic
fun Fragment.appUpdater(block: Builder.() -> Unit): AppUpdater {
    return Builder(requireContext()).apply(block).build()
}

/**
 * 扩展DSL
 */
@JvmName("appUpdaterWithContext")
@JvmSynthetic
fun Fragment.appUpdater(url: String): AppUpdater {
    return Builder(requireContext()).setUrl(url).build()
}
