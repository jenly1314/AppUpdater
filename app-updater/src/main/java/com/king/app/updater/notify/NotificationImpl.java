package com.king.app.updater.notify;

import android.content.Context;

import com.king.app.updater.UpdateConfig;
import com.king.app.updater.util.NotificationUtils;

import java.io.File;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class NotificationImpl implements INotification {
    @Override
    public void onStart(Context context, int notifyId, String channelId, String channelName, int icon, CharSequence title, CharSequence content, boolean isVibrate, boolean isSound, boolean isCancelDownload) {
        NotificationUtils.showStartNotification(context, notifyId, channelId, channelName, icon, title, content, isVibrate, isSound, isCancelDownload);
    }

    @Override
    public void onProgress(Context context, int notifyId, String channelId, int icon, CharSequence title, CharSequence content, int progress, int size, boolean isCancelDownload) {
        NotificationUtils.showProgressNotification(context, notifyId, channelId, icon, title, content, progress, size, isCancelDownload);
    }

    @Override
    public void onFinish(Context context, int notifyId, String channelId, int icon, CharSequence title, CharSequence content, File file, String authority) {
        NotificationUtils.showFinishNotification(context, notifyId, channelId, icon, title, content, file, authority);
    }

    @Override
    public void onError(Context context, int notifyId, String channelId, int icon, CharSequence title, CharSequence content, boolean isReDownload, UpdateConfig config) {
        NotificationUtils.showErrorNotification(context, notifyId, channelId, icon, title, content, isReDownload, config);
    }

    @Override
    public void onCancel(Context context, int notifyId) {
        NotificationUtils.cancelNotification(context, notifyId);
    }
}
