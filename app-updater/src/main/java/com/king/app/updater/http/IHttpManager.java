package com.king.app.updater.http;

import java.io.File;
import java.io.Serializable;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public interface IHttpManager {


    /**
     * 下载
     * @param url
     * @param path
     * @param filename
     * @param callback
     */
    void download(String url,String path,String filename,DownloadCallback callback);


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
