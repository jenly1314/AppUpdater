package com.king.app.updater.callback;

import java.io.File;

/**
 * 更新回调接口
 *
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface UpdateCallback {

    /**
     * 最开始调用(在onStart之前调用)
     *
     * @param isDownloading 为true时，表示已经在下载；为false时，表示当前未开始下载，即将开始下载
     */
    void onDownloading(boolean isDownloading);

    /**
     * 开始
     */
    void onStart(String url);

    /**
     * 更新进度
     *
     * @param progress  当前进度大小
     * @param total     总文件大小
     * @param isChanged 进度百分比是否有改变，（主要可以用来过滤无用的刷新，从而降低刷新频率）
     */
    void onProgress(long progress, long total, boolean isChanged);

    /**
     * 完成
     *
     * @param file APK文件
     */
    void onFinish(File file);

    /**
     * 错误
     *
     * @param e 异常
     */
    void onError(Exception e);

    /**
     * 取消
     */
    void onCancel();
}
