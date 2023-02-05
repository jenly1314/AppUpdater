package com.king.app.updater.http;

import android.os.AsyncTask;

import com.king.app.updater.util.LogUtils;
import com.king.app.updater.util.SSLSocketFactoryUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * OkHttpManager使用 {@link OkHttpClient} 实现的 {@link IHttpManager}
 * <p>使用 OkHttpManager 时必须依赖 OkHttp 库
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class OkHttpManager implements IHttpManager {

    private static final int DEFAULT_TIME_OUT = 20000;

    private OkHttpClient okHttpClient;

    private DownloadTask mDownloadTask;

    private static volatile OkHttpManager INSTANCE;

    public static OkHttpManager getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OkHttpManager();
                }
            }
        }

        return INSTANCE;
    }

    private OkHttpManager() {
        this(DEFAULT_TIME_OUT);
    }

    /**
     * HttpManager对外暴露。如果没有特殊需求，推荐使用{@link HttpManager#getInstance()}
     *
     * @param timeout 超时时间，单位：毫秒
     */
    public OkHttpManager(int timeout) {
        this(new OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createX509TrustManager())
                .hostnameVerifier(SSLSocketFactoryUtils.createAllowAllHostnameVerifier())
                .build());
    }

    /**
     * HttpManager对外暴露，推荐使用{@link HttpManager#getInstance()}
     *
     * @param okHttpClient {@link OkHttpClient}
     */
    public OkHttpManager(@NonNull OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }


    @Override
    public void download(String url, String saveFilePath, @Nullable Map<String, String> requestProperty, final DownloadCallback callback) {
        mDownloadTask = new DownloadTask(okHttpClient, url, saveFilePath, requestProperty, callback);
        mDownloadTask.execute();
    }

    @Override
    public void cancel() {
        if (mDownloadTask != null) {
            mDownloadTask.isCancel = true;
        }
    }


    /**
     * 异步下载任务
     */
    private static class DownloadTask extends AsyncTask<Void, Long, File> {

        private String url;

        private String saveFilePath;

        private Map<String, String> requestProperty;

        private DownloadCallback callback;

        private Exception exception;

        private OkHttpClient okHttpClient;

        private volatile boolean isCancel;

        public DownloadTask(OkHttpClient okHttpClient, String url, String saveFilePath, @Nullable Map<String, String> requestProperty, DownloadCallback callback) {
            this.okHttpClient = okHttpClient;
            this.url = url;
            this.saveFilePath = saveFilePath;
            this.callback = callback;
            this.requestProperty = requestProperty;

        }

        @Override
        protected File doInBackground(Void... voids) {
            try {
                Request.Builder builder = new Request.Builder()
                        .url(url)
                        .addHeader("Accept-Encoding", "identity")
                        .get();

                if (requestProperty != null) {
                    for (Map.Entry<String, String> entry : requestProperty.entrySet()) {
                        builder.addHeader(entry.getKey(), entry.getValue());
                    }
                }

                Call call = okHttpClient.newCall(builder.build());
                Response response = call.execute();

                if (response.isSuccessful()) {
                    InputStream is = response.body().byteStream();

                    long length = response.body().contentLength();

                    LogUtils.d("contentLength: " + length);

                    long progress = 0;

                    byte[] buffer = new byte[4096];

                    int len;
                    File file = new File(saveFilePath);
                    FileOutputStream fos = new FileOutputStream(file);
                    while ((len = is.read(buffer)) != -1) {
                        if (isCancel) {
                            if (call != null) {
                                call.cancel();
                            }
                            cancel(true);
                            break;
                        }
                        fos.write(buffer, 0, len);
                        progress += len;
                        // 更新进度
                        publishProgress(progress, length);
                    }

                    fos.flush();
                    fos.close();
                    is.close();

                    response.close();

                    if (progress <= 0 && length <= 0) {
                        throw new IllegalStateException(String.format("contentLength = %d", length));
                    }

                    return file;

                } else {// 连接失败
                    throw new ConnectException(String.format("responseCode = %d", response.code()));
                }

            } catch (Exception e) {
                this.exception = e;
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (callback != null) {
                callback.onStart(url);
            }
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if (callback != null) {
                if (file != null) {
                    callback.onFinish(file);
                } else {
                    callback.onError(exception);
                }

            }
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            super.onProgressUpdate(values);
            if (callback != null) {
                if (!isCancelled()) {
                    callback.onProgress(values[0], values[1]);
                }

            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            if (callback != null) {
                callback.onCancel();
            }
        }

    }
}