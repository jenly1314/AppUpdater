package com.king.app.updater.callback;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public abstract class AppUpdateCallback implements UpdateCallback {
    @Override
    public void onDownloading(boolean isDownloading) {

    }

    @Override
    public void onStart(String url) {

    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onCancel() {

    }
}
