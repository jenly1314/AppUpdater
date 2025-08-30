package com.king.app.updater.util

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.king.app.updater.constant.Constants
import com.king.app.updater.service.DownloadService
import java.io.File

/**
 * 通知栏工具类
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
object NotificationUtils {

    /**
     * 显示开始下载时的通知
     * @param context [Context]
     * @param notificationId 通知ID
     * @param channelId 通知渠道ID
     * @param channelName 通知渠道名称
     * @param smallIcon 小图标资源ID
     * @param title 通知标题
     * @param content 通知内容
     * @param isVibrate 是否震动
     * @param isSound 是否播放声音
     * @param supportCancelDownload 是否支持取消下载
     */
    @JvmStatic
    fun showStartNotification(
        context: Context,
        notificationId: Int,
        channelId: String,
        channelName: String,
        @DrawableRes smallIcon: Int,
        title: CharSequence,
        content: CharSequence,
        isVibrate: Boolean,
        isSound: Boolean,
        supportCancelDownload: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, channelId, channelName, isVibrate, isSound)
        }
        val builder = buildNotification(context, channelId, smallIcon, title, content)
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
        when {
            isVibrate && isSound -> builder.setDefaults(Notification.DEFAULT_VIBRATE or Notification.DEFAULT_SOUND)
            isVibrate -> builder.setDefaults(Notification.DEFAULT_VIBRATE)
            isSound -> builder.setDefaults(Notification.DEFAULT_SOUND)
        }

        if (supportCancelDownload) {
            val intent = Intent(context, DownloadService::class.java).apply {
                putExtra(Constants.EXTRA_ACTION, Constants.ACTION_CANCEL_DOWNLOAD)
            }
            val deleteIntent = PendingIntent.getService(
                context,
                notificationId,
                intent,
                getPendingIntentFlags(PendingIntent.FLAG_CANCEL_CURRENT)
            )
            builder.setDeleteIntent(deleteIntent)
        }

        val notification = builder.build().apply {
            flags = if (supportCancelDownload) {
                Notification.FLAG_ONLY_ALERT_ONCE
            } else {
                Notification.FLAG_NO_CLEAR or Notification.FLAG_ONLY_ALERT_ONCE
            }
        }

        notifyNotification(context, notificationId, notification)
    }

    /**
     * 显示下载中的通知（更新进度）
     * @param context [Context]
     * @param notificationId 通知ID
     * @param channelId 通知渠道ID
     * @param smallIcon 小图标资源ID
     * @param title 通知标题
     * @param content 通知内容
     * @param progress 当前进度
     * @param total 总大小
     * @param supportCancelDownload 是否支持取消下载
     */
    @JvmStatic
    fun showProgressNotification(
        context: Context,
        notificationId: Int,
        channelId: String,
        @DrawableRes smallIcon: Int,
        title: CharSequence,
        content: CharSequence,
        progress: Int,
        total: Int,
        supportCancelDownload: Boolean
    ) {
        val builder = buildNotification(
            context, channelId, smallIcon, title, content, progress, total
        )

        if (supportCancelDownload) {
            val intent = Intent(context, DownloadService::class.java).apply {
                putExtra(Constants.EXTRA_ACTION, Constants.ACTION_CANCEL_DOWNLOAD)
            }
            val deleteIntent = PendingIntent.getService(
                context,
                notificationId,
                intent,
                getPendingIntentFlags(PendingIntent.FLAG_CANCEL_CURRENT)
            )
            builder.setDeleteIntent(deleteIntent)
        }

        val notification = builder.build().apply {
            flags = if (supportCancelDownload) {
                Notification.FLAG_ONLY_ALERT_ONCE
            } else {
                Notification.FLAG_NO_CLEAR or Notification.FLAG_ONLY_ALERT_ONCE
            }
        }

        notifyNotification(context, notificationId, notification)
    }

    /**
     * 显示下载成功时的通知（点击安装）
     * @param context [Context]
     * @param notificationId 通知ID
     * @param channelId 通知渠道ID
     * @param smallIcon 小图标资源ID
     * @param title 通知标题
     * @param content 通知内容
     * @param file 下载完成的文件
     * @param authority FileProvider的authority
     */
    @JvmStatic
    fun showSuccessNotification(
        context: Context,
        notificationId: Int,
        channelId: String,
        @DrawableRes smallIcon: Int,
        title: CharSequence,
        content: CharSequence,
        file: File,
        authority: String
    ) {
        cancelNotification(context, notificationId)
        val builder = buildNotification(context, channelId, smallIcon, title, content)
        builder.setAutoCancel(true)
        val intent = AppUtils.getInstallIntent(context, file, authority)
        val clickIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            getPendingIntentFlags(PendingIntent.FLAG_UPDATE_CURRENT)
        )
        builder.setContentIntent(clickIntent)
        val notification = builder.build().apply {
            flags = Notification.FLAG_AUTO_CANCEL
        }
        notifyNotification(context, notificationId, notification)
    }

    /**
     * 显示下载出错通知
     * @param context [Context]
     * @param notificationId 通知ID
     * @param channelId 通知渠道ID
     * @param smallIcon 小图标资源ID
     * @param title 通知标题
     * @param content 通知内容
     * @param allowRetry 是否允许重新下载
     */
    @SuppressLint("LaunchActivityFromNotification")
    @JvmStatic
    fun showErrorNotification(
        context: Context,
        notificationId: Int,
        channelId: String,
        @DrawableRes smallIcon: Int,
        title: CharSequence,
        content: CharSequence,
        allowRetry: Boolean,
    ) {
        val builder = buildNotification(context, channelId, smallIcon, title, content)
        val flag = getPendingIntentFlags(PendingIntent.FLAG_UPDATE_CURRENT)

        if (allowRetry) {
            // 点击通知栏时，则重新下载
            val intent = Intent(context, DownloadService::class.java).apply {
                putExtra(Constants.EXTRA_ACTION, Constants.ACTION_RETRY_DOWNLOAD)
            }
            val clickIntent = PendingIntent.getService(context, notificationId, intent, flag)
            builder.setContentIntent(clickIntent)
        } else {
            // 点击通知栏时，则自动取消通知栏
            val clickIntent = PendingIntent.getService(context, notificationId, Intent(), flag)
            builder.setContentIntent(clickIntent)
        }

        val notification = builder.build().apply {
            flags = if (allowRetry) {
                Notification.FLAG_ONLY_ALERT_ONCE
            } else {
                Notification.FLAG_AUTO_CANCEL
            }
        }
        notifyNotification(context, notificationId, notification)
    }

    /**
     * 显示通知信息（非第一次）
     * @param context [Context]
     * @param notificationId 通知ID
     * @param channelId 通知渠道ID
     * @param smallIcon 小图标资源ID
     * @param title 通知标题
     * @param content 通知内容
     * @param isAutoCancel 是否自动取消
     */
    @JvmStatic
    fun showNotification(
        context: Context,
        notificationId: Int,
        channelId: String,
        @DrawableRes smallIcon: Int,
        title: CharSequence,
        content: CharSequence,
        isAutoCancel: Boolean
    ) {
        val builder = buildNotification(context, channelId, smallIcon, title, content)
        builder.setAutoCancel(isAutoCancel)
        val notification = builder.build().apply {
            flags = Notification.FLAG_AUTO_CANCEL
        }
        notifyNotification(context, notificationId, notification)
    }

    /**
     * 取消通知
     * @param context [Context]
     * @param notificationId 通知ID
     */
    @JvmStatic
    fun cancelNotification(context: Context, notificationId: Int) {
        getNotificationManager(context).cancel(notificationId)
    }

    /**
     * 获取通知管理器
     * @param context [Context]
     * @return NotificationManagerCompat
     */
    @JvmStatic
    fun getNotificationManager(context: Context): NotificationManagerCompat {
        return NotificationManagerCompat.from(context)
    }

    /**
     * 创建一个通知通道（兼容O以上版本）
     * @param context [Context]
     * @param channelId 渠道ID
     * @param channelName 渠道名称
     * @param isVibrate 是否震动
     * @param isSound 是否播放声音
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @JvmStatic
    fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String,
        isVibrate: Boolean,
        isSound: Boolean
    ) {
        val channel = NotificationChannel(
            channelId, channelName, NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setShowBadge(true)
            enableVibration(isVibrate)
            if (!isSound) {
                setSound(null, null)
            }
        }
        getNotificationManager(context).createNotificationChannel(channel)
    }

    @JvmStatic
    private fun buildNotification(
        context: Context,
        channelId: String,
        @DrawableRes smallIcon: Int,
        title: CharSequence,
        content: CharSequence,
        progress: Int = Constants.NONE,
        total: Int = Constants.NONE,
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(smallIcon)
            setContentTitle(title)
            setContentText(content)
            setOngoing(true)
            if (progress != Constants.NONE) {
                setProgress(total, progress, total <= 0)
            }
        }
    }

    @JvmStatic
    private fun notifyNotification(context: Context, id: Int, notification: Notification) {
        getNotificationManager(context).notify(id, notification)
    }

    @JvmStatic
    private fun getPendingIntentFlags(flag: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flag or PendingIntent.FLAG_IMMUTABLE
        } else {
            flag
        }
    }
}
