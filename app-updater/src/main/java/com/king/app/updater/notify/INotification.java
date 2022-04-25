package com.king.app.updater.notify;

import android.content.Context;

import com.king.app.updater.UpdateConfig;

import java.io.File;

import androidx.annotation.DrawableRes;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface INotification {

    void onStart(Context context, int notifyId, String channelId, String channelName, @DrawableRes int icon, CharSequence title, CharSequence content, boolean isVibrate, boolean isSound, boolean isCancelDownload);

    void onProgress(Context context, int notifyId, String channelId, @DrawableRes int icon, CharSequence title, CharSequence content, int progress, int size, boolean isCancelDownload);

    void onFinish(Context context, int notifyId, String channelId, @DrawableRes int icon, CharSequence title, CharSequence content, File file, String authority);

    void onError(Context context, int notifyId, String channelId, @DrawableRes int icon, CharSequence title, CharSequence content, boolean isReDownload, UpdateConfig config);

    void onCancel(Context context, int notifyId);
}
