package com.king.app.updater.http;

import android.os.AsyncTask;

import com.king.app.updater.util.SSLSocketFactoryUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class HttpManager implements IHttpManager {

    private static final int DEFAULT_TIME_OUT = 20000;

    private int mTimeout = DEFAULT_TIME_OUT;

    private static HttpManager INSTANCE;

    public static HttpManager getInstance(){
        if(INSTANCE == null){
            synchronized (HttpManager.class){
                INSTANCE = new HttpManager();
            }
        }

        return INSTANCE;
    }

    private HttpManager(){
        this(DEFAULT_TIME_OUT);
    }

    public HttpManager(int timeout){
        this.mTimeout = timeout;
    }

    @Override
    public void download(String url, String path, String filename, DownloadCallback callback) {
        new DownloadTask(url,path,filename,callback).execute();
    }


    /**
     * 异步下载任务
     */
    private class DownloadTask extends AsyncTask<Void,Integer,File> {
        private String url;

        private String path;

        private String filename;

        private DownloadCallback callback;

        private Exception exception;

        public DownloadTask(String url,String path,String filename,DownloadCallback callback){
            this.url = url;
            this.path = path;
            this.filename = filename;
            this.callback = callback;

        }

        @Override
        protected File doInBackground(Void... voids) {

            try {
                HttpsURLConnection.setDefaultSSLSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(SSLSocketFactoryUtils.createTrustAllHostnameVerifier());
                HttpURLConnection connect = (HttpURLConnection)new URL(url).openConnection();
                connect.setRequestMethod("GET");
                connect.setReadTimeout(mTimeout);
                connect.setConnectTimeout(mTimeout);
                connect.connect();
                int responseCode = connect.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){

                    InputStream is = connect.getInputStream();

                    int length = connect.getContentLength();

                    int progress = 0;

                    byte[] buffer = new byte[4096];

                    int len;
                    File file = new File(path,filename);
                    FileOutputStream fos = new FileOutputStream(file);
                    while ((len = is.read(buffer)) != -1){
                        fos.write(buffer,0,len);
                        progress += len;
                        //更新进度
                        publishProgress(progress,length);
                    }

                    fos.flush();
                    fos.close();
                    is.close();

                    connect.disconnect();

                    return file;
                }else {//连接失败
                    throw new ConnectException(String.format("responseCode = %d",responseCode));
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
            if(callback!=null){
                callback.onStart(url);
            }
        }

        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            if(callback!=null){
                if(file!=null){
                    callback.onFinish(file);
                }else{
                    callback.onError(exception);
                }

            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(callback!=null){
                callback.onProgress(values[0],values[1]);
            }
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            if(callback!=null){
                callback.onCancel();
            }
        }


    }

}
