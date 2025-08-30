package com.king.app.updater.notification

import android.content.Context
import androidx.annotation.DrawableRes
import java.io.File

/**
 * 通知处理器
 * @see [NotificationHandler]
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
interface INotificationHandler {

    /**
     * 开始
     *
     * @param context                 上下文
     * @param notificationId          通知ID
     * @param channelId               通知通道ID
     * @param channelName             通知通道名称
     * @param smallIcon               通知图标
     * @param title                   通知标题
     * @param content                 通知内容
     * @param isVibrate               通知是否允许震动
     * @param isSound                 通知是否有铃声
     * @param supportCancelDownload   是否支持取消下载
     */
    fun onStart(
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
    )

    /**
     * 更新进度
     *
     * @param context                 上下文
     * @param notificationId          通知ID
     * @param channelId               通知通道ID
     * @param smallIcon               通知图标
     * @param title                   通知标题
     * @param content                 通知内容
     * @param progress                当前进度大小
     * @param total                   总进度大小
     * @param supportCancelDownload   是否支持取消下载
     */
    fun onProgress(
        context: Context,
        notificationId: Int,
        channelId: String,
        @DrawableRes smallIcon: Int,
        title: CharSequence,
        content: CharSequence,
        progress: Int,
        total: Int,
        supportCancelDownload: Boolean
    )

    /**
     * 成功
     *
     * @param context        上下文
     * @param notificationId 通知ID
     * @param channelId      通知通道ID
     * @param smallIcon      通知图标
     * @param title          通知标题
     * @param content        通知内容
     * @param file           APK文件
     * @param authority      文件访问授权
     */
    fun onSuccess(
        context: Context,
        notificationId: Int,
        channelId: String,
        @DrawableRes smallIcon: Int,
        title: CharSequence,
        content: CharSequence,
        file: File,
        authority: String
    )

    /**
     * 错误
     *
     * @param context           上下文
     * @param notificationId    通知ID
     * @param channelId         通知通道ID
     * @param smallIcon         通知图标
     * @param title             通知标题
     * @param content           通知内容
     * @param allowRetry        是否允许重试
     */
    fun onError(
        context: Context,
        notificationId: Int,
        channelId: String,
        @DrawableRes smallIcon: Int,
        title: CharSequence,
        content: CharSequence,
        allowRetry: Boolean,
    )

    /**
     * 取消
     *
     * @param context        上下文
     * @param notificationId 通知ID
     */
    fun onCancel(context: Context, notificationId: Int)
}
