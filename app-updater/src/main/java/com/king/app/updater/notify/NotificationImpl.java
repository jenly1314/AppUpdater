package com.king.app.updater.notify;

import android.content.Context;

import com.king.app.updater.UpdateConfig;
import com.king.app.updater.util.NotificationUtils;

import java.io.File;

/**
 * {@link INotification} 的实现，如果需要自定义通知栏的布局，并对 {@link NotificationImpl} 的实现不满意，可通过自定义去实现一个 {@link INotification}
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class NotificationImpl implements INotification {

    @Override
    public void onStart(Context context, int notifyId, String channelId, String channelName, int smallIcon, CharSequence title, CharSequence content, boolean isVibrate, boolean isSound, boolean isSupportCancelDownload) {
        NotificationUtils.showStartNotification(context, notifyId, channelId, channelName, smallIcon, title, content, isVibrate, isSound, isSupportCancelDownload);
    }

    @Override
    public void onProgress(Context context, int notifyId, String channelId, int smallIcon, CharSequence title, CharSequence content, int progress, int size, boolean isSupportCancelDownload) {
        NotificationUtils.showProgressNotification(context, notifyId, channelId, smallIcon, title, content, progress, size, isSupportCancelDownload);
    }

    @Override
    public void onFinish(Context context, int notifyId, String channelId, int smallIcon, CharSequence title, CharSequence content, File file, String authority) {
        NotificationUtils.showFinishNotification(context, notifyId, channelId, smallIcon, title, content, file, authority);
    }

    @Override
    public void onError(Context context, int notifyId, String channelId, int smallIcon, CharSequence title, CharSequence content, boolean isReDownload, UpdateConfig config) {
        NotificationUtils.showErrorNotification(context, notifyId, channelId, smallIcon, title, content, isReDownload, config);
    }

    @Override
    public void onCancel(Context context, int notifyId) {
        NotificationUtils.cancelNotification(context, notifyId);
    }
}
