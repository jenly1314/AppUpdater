package com.king.app.updater.notification

import android.content.Context
import com.king.app.updater.util.NotificationUtils
import java.io.File

/**
 * [INotificationHandler] 的默认实现，如果需要自定义通知栏的布局，或对 [NotificationHandler] 的实现不满意，可通过自定义去实现一个 [INotificationHandler]
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
open class NotificationHandler : INotificationHandler {

    override fun onStart(
        context: Context,
        notificationId: Int,
        channelId: String,
        channelName: String,
        smallIcon: Int,
        title: CharSequence,
        content: CharSequence,
        isVibrate: Boolean,
        isSound: Boolean,
        supportCancelDownload: Boolean
    ) {
        NotificationUtils.showStartNotification(
            context,
            notificationId,
            channelId,
            channelName,
            smallIcon,
            title,
            content,
            isVibrate,
            isSound,
            supportCancelDownload
        )
    }

    override fun onProgress(
        context: Context,
        notificationId: Int,
        channelId: String,
        smallIcon: Int,
        title: CharSequence,
        content: CharSequence,
        progress: Long,
        total: Long,
        supportCancelDownload: Boolean
    ) {
        NotificationUtils.showProgressNotification(
            context,
            notificationId,
            channelId,
            smallIcon,
            title,
            content,
            progress,
            total,
            supportCancelDownload
        )
    }

    override fun onSuccess(
        context: Context,
        notificationId: Int,
        channelId: String,
        smallIcon: Int,
        title: CharSequence,
        content: CharSequence,
        file: File,
        authority: String
    ) {
        NotificationUtils.showSuccessNotification(
            context,
            notificationId,
            channelId,
            smallIcon,
            title,
            content,
            file,
            authority
        )
    }

    override fun onError(
        context: Context,
        notificationId: Int,
        channelId: String,
        smallIcon: Int,
        title: CharSequence,
        content: CharSequence,
        allowRetry: Boolean,
    ) {
        NotificationUtils.showErrorNotification(
            context,
            notificationId,
            channelId,
            smallIcon,
            title,
            content,
            allowRetry,
        )
    }

    override fun onCancel(context: Context, notificationId: Int) {
        NotificationUtils.cancelNotification(context, notificationId)
    }

    companion object {

        private val INSTANCE by lazy {
            NotificationHandler()
        }

        /**
         * 获取实例
         */
        @JvmStatic
        fun getInstance(): NotificationHandler {
            return INSTANCE
        }
    }
}
