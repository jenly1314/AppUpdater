package com.king.app.updater.callback;

import java.io.File;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface UpdateCallback {

    /**
     * 最开始调用(在onStart之前调用)
     * @param isDownloading true 表示已经在下载，false表示准备刚调用下载
     */
    void onDownloading(boolean isDownloading);

    /**
     * 开始
     */
    void onStart(String url);

    /**
     * 加载进度…
     * @param progress
     * @param total
     * @param isChange 进度百分比是否有改变，（主要可以用来过滤无用的刷新，从而降低刷新频率）
     */
    void onProgress(int progress,int total,boolean isChange);

    /**
     * 完成
     * @param file
     */
    void onFinish(File file);

    /**
     * 错误
     * @param e
     */
    void onError(Exception e);


    /**
     * 取消
     */
    void onCancel();
}
