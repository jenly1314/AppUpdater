package com.king.app.updater.notify;

import android.content.Context;

import com.king.app.updater.UpdateConfig;

import java.io.File;

import androidx.annotation.DrawableRes;

/**
 * 通知栏进度更新
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface INotification {
    /**
     * 开始
     *
     * @param context                 上下文
     * @param notifyId                通知ID
     * @param channelId               通知通道ID
     * @param channelName             通知通道名称
     * @param smallIcon               通知图标
     * @param title                   通知标题
     * @param content                 通知内容
     * @param isVibrate               通知是否允许震动
     * @param isSound                 通知是否有铃声
     * @param isSupportCancelDownload 是否支持取消下载
     */
    void onStart(Context context, int notifyId, String channelId, String channelName, @DrawableRes int smallIcon, CharSequence title, CharSequence content, boolean isVibrate, boolean isSound, boolean isSupportCancelDownload);

    /**
     * 更新进度
     *
     * @param context                 上下文
     * @param notifyId                通知ID
     * @param channelId               通知通道ID
     * @param smallIcon               通知图标
     * @param title                   通知标题
     * @param content                 通知内容
     * @param progress                当前进度大小
     * @param size                    总进度大小
     * @param isSupportCancelDownload 是否支持取消下载
     */
    void onProgress(Context context, int notifyId, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, int progress, int size, boolean isSupportCancelDownload);

    /**
     * 完成
     *
     * @param context   上下文
     * @param notifyId  通知ID
     * @param channelId 通知通道ID
     * @param smallIcon 通知图标
     * @param title     通知标题
     * @param content   通知内容
     * @param file      APK文件
     * @param authority 文件访问授权
     */
    void onFinish(Context context, int notifyId, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, File file, String authority);

    /**
     * 错误
     *
     * @param context      上下文
     * @param notifyId     通知ID
     * @param channelId    通知通道ID
     * @param smallIcon    通知图标
     * @param title        通知标题
     * @param content      通知内容
     * @param isReDownload 是否重复下载
     * @param config       配置
     */
    void onError(Context context, int notifyId, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, boolean isReDownload, UpdateConfig config);

    /**
     * 取消
     *
     * @param context  上下文
     * @param notifyId 通知ID
     */
    void onCancel(Context context, int notifyId);
}
