package com.king.app.updater.http;

import android.os.AsyncTask;
import android.os.Build;

import com.king.app.updater.util.LogUtils;
import com.king.app.updater.util.SSLSocketFactoryUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.Nullable;

/**
 * HttpManager使用 {@link HttpURLConnection} 实现的 {@link IHttpManager}
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class HttpManager implements IHttpManager {

    private static final int HTTP_TEMP_REDIRECT = 307;
    private static final int HTTP_PERM_REDIRECT = 308;

    private static final int DEFAULT_TIME_OUT = 20000;

    private int mTimeout;

    private DownloadTask mDownloadTask;

    private static volatile HttpManager INSTANCE;

    public static HttpManager getInstance(){
        if(INSTANCE == null){
            synchronized (HttpManager.class){
                if(INSTANCE == null){
                    INSTANCE = new HttpManager();
                }
            }
        }

        return INSTANCE;
    }

    private HttpManager(){
        this(DEFAULT_TIME_OUT);
    }

    /**
     * HttpManager对外暴露。如果没有特殊需求，推荐使用{@link HttpManager#getInstance()}
     */
    public HttpManager(int timeout){
        this.mTimeout = timeout;
    }

    @Override
    public void download(String url, String path, String filename, @Nullable Map<String,String> requestProperty, DownloadCallback callback) {
        mDownloadTask = new DownloadTask(url, path, filename, mTimeout, requestProperty, callback);
        mDownloadTask.execute();
    }

    @Override
    public void cancel() {
        if(mDownloadTask != null){
            mDownloadTask.isCancel = true;
        }
    }

    /**
     * 异步下载任务
     */
    private static class DownloadTask extends AsyncTask<Void,Long,File> {

        private String url;

        private String path;

        private String filename;

        private Map<String,String> requestProperty;

        private DownloadCallback callback;

        private Exception exception;

        private int timeout;

        private volatile boolean isCancel;

        public DownloadTask(String url, String path, String filename, int timeout, @Nullable Map<String,String> requestProperty, DownloadCallback callback){
            this.url = url;
            this.path = path;
            this.filename = filename;
            this.timeout = timeout;
            this.callback = callback;
            this.requestProperty = requestProperty;
        }

        private File download(String url) throws Exception{
            HttpURLConnection connect = (HttpURLConnection)new URL(url).openConnection();
            connect.setRequestMethod("GET");
            connect.setRequestProperty("Accept-Encoding", "identity");

            connect.setReadTimeout(timeout);
            connect.setConnectTimeout(timeout);

            if(requestProperty != null){
                for(Map.Entry<String,String> entry : requestProperty.entrySet()){
                    connect.setRequestProperty(entry.getKey(),entry.getValue());
                }
            }

            connect.connect();

            LogUtils.d("Content-Type:" + connect.getContentType());
            int responseCode = connect.getResponseCode();
            switch (responseCode){
                case HttpURLConnection.HTTP_OK: {
                    InputStream is = connect.getInputStream();

                    long length = connect.getContentLength();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        length = connect.getContentLengthLong();
                    }

                    LogUtils.d( "contentLength:" + length);

                    long progress = 0;

                    byte[] buffer = new byte[8192];

                    int len;
                    File file = new File(path, filename);
                    FileOutputStream fos = new FileOutputStream(file);
                    while ((len = is.read(buffer)) != -1) {
                        if (isCancel) {
                            cancel(true);
                            break;
                        }
                        fos.write(buffer, 0, len);
                        progress += len;
                        // 更新进度
                        if (length > 0) {
                            publishProgress(progress, length);
                        }
                    }

                    fos.flush();
                    fos.close();
                    is.close();

                    connect.disconnect();

                    if(progress <= 0 && length <= 0){
                        throw new IllegalStateException(String.format("contentLength = %d",length));
                    }

                    return file;
                }
                case HttpURLConnection.HTTP_MULT_CHOICE:
                case HttpURLConnection.HTTP_MOVED_PERM:
                case HttpURLConnection.HTTP_MOVED_TEMP:
                case HttpURLConnection.HTTP_SEE_OTHER:
                case HTTP_TEMP_REDIRECT:
                case HTTP_PERM_REDIRECT: {// 重定向
                    String redirectUrl = connect.getHeaderField("Location");
                    LogUtils.d("redirectUrl = " + redirectUrl);
                    connect.disconnect();
                    return download(redirectUrl);
                }
                default://连接失败
                    throw new ConnectException(String.format("responseCode = %d",responseCode));

            }
        }

        @Override
        protected File doInBackground(Void... voids) {

            try{
                HttpsURLConnection.setDefaultSSLSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(SSLSocketFactoryUtils.createTrustAllHostnameVerifier());
                return download(url);
            } catch (Exception e) {
                this.exception = e;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(callback != null){
                callback.onStart(url);
            }
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if(callback != null){
                if(file != null){
                    callback.onFinish(file);
                }else{
                    callback.onError(exception);
                }

            }
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
            if(callback != null){
                if(!isCancelled()){
                    callback.onProgress(values[0],values[1]);
                }

            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(callback != null){
                callback.onCancel();
            }
        }

    }

}