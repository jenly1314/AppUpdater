package com.king.app.updater.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.king.app.updater.UpdateConfig;
import com.king.app.updater.constant.Constants;
import com.king.app.updater.service.DownloadService;

import java.io.File;

import androidx.annotation.DrawableRes;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * 通知栏工具
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class NotificationUtils {

    private NotificationUtils() {
        throw new AssertionError();
    }

    /**
     * 显示开始下载时的通知
     *
     * @param notifyId
     * @param channelId
     * @param channelName
     * @param smallIcon
     * @param title
     * @param content
     */
    public static void showStartNotification(Context context, int notifyId, String channelId, String channelName, @DrawableRes int smallIcon, CharSequence title, CharSequence content, boolean isVibrate, boolean isSound, boolean isSupportCancelDownload) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, channelId, channelName, isVibrate, isSound);
        }
        NotificationCompat.Builder builder = buildNotification(context, channelId, smallIcon, title, content);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (isVibrate && isSound) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND);
        } else if (isVibrate) {
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
        } else if (isSound) {
            builder.setDefaults(Notification.DEFAULT_SOUND);
        }
        // 如果支持取消下载，点击通知栏时，则取消下载
        if (isSupportCancelDownload) {
            Intent intent = new Intent(context, DownloadService.class);
            intent.putExtra(Constants.KEY_STOP_DOWNLOAD_SERVICE, true);
            PendingIntent deleteIntent = PendingIntent.getService(context, notifyId, intent, getPendingIntentFlags(PendingIntent.FLAG_CANCEL_CURRENT));
            builder.setDeleteIntent(deleteIntent);
        }

        Notification notification = builder.build();
        if (isSupportCancelDownload) {
            notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        } else {
            notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONLY_ALERT_ONCE;
        }

        notifyNotification(context, notifyId, notification);
    }

    /**
     * 显示下载中的通知（更新进度）
     *
     * @param notifyId
     * @param channelId
     * @param smallIcon
     * @param title
     * @param content
     * @param progress
     * @param size
     */
    public static void showProgressNotification(Context context, int notifyId, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, int progress, int size, boolean isSupportCancelDownload) {
        NotificationCompat.Builder builder = buildNotification(context, channelId, smallIcon, title, content, progress, size);
        // 如果支持取消下载，点击通知栏时，则取消下载
        if (isSupportCancelDownload) {
            Intent intent = new Intent(context, DownloadService.class);
            intent.putExtra(Constants.KEY_STOP_DOWNLOAD_SERVICE, true);
            PendingIntent deleteIntent = PendingIntent.getService(context, notifyId, intent, getPendingIntentFlags(PendingIntent.FLAG_CANCEL_CURRENT));
            builder.setDeleteIntent(deleteIntent);
        }

        Notification notification = builder.build();

        if (isSupportCancelDownload) {
            notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        } else {
            notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONLY_ALERT_ONCE;
        }

        notifyNotification(context, notifyId, notification);
    }

    /**
     * 显示下载完成时的通知（点击安装）
     *
     * @param notifyId
     * @param channelId
     * @param smallIcon
     * @param title
     * @param content
     * @param file
     */
    public static void showFinishNotification(Context context, int notifyId, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, File file, String authority) {
        cancelNotification(context, notifyId);
        NotificationCompat.Builder builder = buildNotification(context, channelId, smallIcon, title, content);
        builder.setAutoCancel(true);
        Intent intent = AppUtils.getInstallIntent(context, file, authority);
        PendingIntent clickIntent = PendingIntent.getActivity(context, notifyId, intent, getPendingIntentFlags(PendingIntent.FLAG_UPDATE_CURRENT));
        builder.setContentIntent(clickIntent);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notifyNotification(context, notifyId, notification);
    }

    /**
     * 现在下载失败通知
     *
     * @param context
     * @param notifyId
     * @param channelId
     * @param smallIcon
     * @param title
     * @param content
     * @param isReDownload
     * @param config
     */
    public static void showErrorNotification(Context context, int notifyId, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, boolean isReDownload, UpdateConfig config) {
        NotificationCompat.Builder builder = buildNotification(context, channelId, smallIcon, title, content);
        builder.setAutoCancel(true);
        int flag = getPendingIntentFlags(PendingIntent.FLAG_UPDATE_CURRENT);
        // 点击通知栏时，则重新下载
        if (isReDownload) {
            Intent intent = new Intent(context, DownloadService.class);
            intent.putExtra(Constants.KEY_RE_DOWNLOAD, true);
            intent.putExtra(Constants.KEY_UPDATE_CONFIG, config);
            PendingIntent clickIntent = PendingIntent.getService(context, notifyId, intent, flag);
            builder.setContentIntent(clickIntent);
        } else {
            // 点击通知栏时，则自动取消通知栏
            PendingIntent clickIntent = PendingIntent.getService(context, notifyId, new Intent(), flag);
            builder.setContentIntent(clickIntent);
        }

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notifyNotification(context, notifyId, notification);
    }


    /**
     * 显示通知信息（非第一次）
     *
     * @param notifyId
     * @param channelId
     * @param smallIcon
     * @param title
     * @param content
     */
    public static void showNotification(Context context, int notifyId, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, boolean isAutoCancel) {
        NotificationCompat.Builder builder = buildNotification(context, channelId, smallIcon, title, content);
        builder.setAutoCancel(isAutoCancel);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notifyNotification(context, notifyId, notification);
    }

    /**
     * 取消通知
     *
     * @param notifyId
     */
    public static void cancelNotification(Context context, int notifyId) {
        getNotificationManager(context).cancel(notifyId);
    }


    /**
     * 获取通知管理器
     *
     * @return {@link NotificationManagerCompat}
     */
    public static NotificationManagerCompat getNotificationManager(Context context) {
        return NotificationManagerCompat.from(context);
    }

    /**
     * 创建一个通知通道（兼容0以上版本）
     *
     * @param channelId
     * @param channelName
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationChannel(Context context, String channelId, String channelName, boolean isVibrate, boolean isSound) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.setShowBadge(true);
        channel.enableVibration(isVibrate);
        if (!isSound) {
            channel.setSound(null, null);
        }
        getNotificationManager(context).createNotificationChannel(channel);

    }

    /**
     * 构建一个通知构建器
     *
     * @param channelId
     * @param smallIcon
     * @param title
     * @param content
     * @return
     */
    private static NotificationCompat.Builder buildNotification(Context context, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content) {
        return buildNotification(context, channelId, smallIcon, title, content, Constants.NONE, Constants.NONE);
    }

    /**
     * 构建一个通知构建器
     *
     * @param channelId
     * @param smallIcon
     * @param title
     * @param content
     * @param progress
     * @param size
     * @return
     */
    private static NotificationCompat.Builder buildNotification(Context context, String channelId, @DrawableRes int smallIcon, CharSequence title, CharSequence content, int progress, int size) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId);
        builder.setSmallIcon(smallIcon);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setOngoing(true);

        if (progress != Constants.NONE) {
            builder.setProgress(size, progress, size <= 0);
        }
        return builder;
    }

    /**
     * 更新通知栏
     *
     * @param id
     * @param notification
     */
    private static void notifyNotification(Context context, int id, Notification notification) {
        getNotificationManager(context).notify(id, notification);
    }

    /**
     * 获取 PendingIntent 的 flags
     *
     * @param flag
     * @return
     */
    private static int getPendingIntentFlags(int flag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return flag | PendingIntent.FLAG_IMMUTABLE;
        }
        return flag;
    }
}
