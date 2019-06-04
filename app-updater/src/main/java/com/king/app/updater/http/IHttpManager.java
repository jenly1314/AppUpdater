package com.king.app.updater.http;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface IHttpManager {


    /**
     * 下载
     * @param url
     * @param path
     * @param filename
     * @param requestProperty
     * @param callback
     */
    void download(String url, String path, String filename, @Nullable Map<String,String> requestProperty, DownloadCallback callback);


    interface DownloadCallback extends Serializable{
        /**
         * 开始
         * @param url
         */
        void onStart(String url);

        /**
         * 加载进度…
         * @param progress
         * @param total
         */
        void onProgress(int progress,int total);

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
}
