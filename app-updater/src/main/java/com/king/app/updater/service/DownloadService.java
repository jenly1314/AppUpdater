package com.king.app.updater.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import com.king.app.updater.R;
import com.king.app.updater.UpdateConfig;
import com.king.app.updater.callback.UpdateCallback;
import com.king.app.updater.constant.Constants;
import com.king.app.updater.http.HttpManager;
import com.king.app.updater.http.IHttpManager;
import com.king.app.updater.notify.INotification;
import com.king.app.updater.notify.NotificationImpl;
import com.king.app.updater.util.AppUtils;
import com.king.app.updater.util.LogUtils;

import java.io.File;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;


/**
 * 下载服务
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class DownloadService extends Service {

    private DownloadBinder mDownloadBinder = new DownloadBinder();
    /**
     * 是否在下载，防止重复下载。
     */
    private boolean isDownloading;
    /**
     * 失败后重新下载次数
     */
    private int mCount = 0;
    /**
     * Http管理器
     */
    private IHttpManager mHttpManager;

    private File mApkFile;

    private Context getContext(){
        return this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null){
            boolean isStop = intent.getBooleanExtra(Constants.KEY_STOP_DOWNLOAD_SERVICE,false);
            if(isStop){
                stopDownload();
            } else if(!isDownloading){
                //是否实通过通知栏触发重复下载
                boolean isReDownload = intent.getBooleanExtra(Constants.KEY_RE_DOWNLOAD,false);
                if(isReDownload){
                    mCount++;
                }
                //获取配置信息
                UpdateConfig config = intent.getParcelableExtra(Constants.KEY_UPDATE_CONFIG);
                startDownload(config,null,null, new NotificationImpl());
            }else{
                LogUtils.w("Please do not duplicate downloads.");
            }
        }

        return super.onStartCommand(intent, flags, startId);

    }


    //----------------------------------------

    /**
     * 开始下载
     * @param config
     * @param httpManager
     * @param callback
     */
    public void startDownload(@NonNull UpdateConfig config,@Nullable IHttpManager httpManager,@Nullable UpdateCallback callback, @Nullable INotification notification){
        if(callback != null){
            callback.onDownloading(isDownloading);
        }

        if(isDownloading){
            LogUtils.w("Please do not duplicate downloads.");
            return;
        }

        String url = config.getUrl();
        String path = config.getPath();
        String filename = config.getFilename();

        //如果保存路径为空则使用缓存路径
        if(TextUtils.isEmpty(path)){
            path = getCacheFilesDir(getContext());
        }
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }

        //如果文件名为空则使用路径
        if(TextUtils.isEmpty(filename)){
            filename = AppUtils.getAppFullName(getContext(),url,getResources().getString(R.string.app_name));
        }

        mApkFile = new File(path, filename);
        if(mApkFile.exists()){//文件是否存在
            Integer versionCode = config.getVersionCode();
            String apkMD5 = config.getApkMD5();
            //是否存在相同的apk
            boolean isExistApk = false;
            if(!TextUtils.isEmpty(apkMD5)){//如果存在MD5，则优先校验MD5
                LogUtils.d(String.format(Locale.getDefault(),"UpdateConfig.apkMD5:%s",apkMD5));
                isExistApk = AppUtils.checkFileMD5(mApkFile,apkMD5);
            }else if(versionCode != null){//如果存在versionCode，则校验versionCode
                LogUtils.d(String.format(Locale.getDefault(),"UpdateConfig.versionCode:%d",versionCode));
                isExistApk = AppUtils.apkExists(getContext(),versionCode,mApkFile);
            }

            if(isExistApk){
                //本地已经存在要下载的APK
                LogUtils.d("CacheFile:" + mApkFile);
                if(config.isInstallApk()){
                    String authority = config.getAuthority();
                    if(TextUtils.isEmpty(authority)){//如果为空则默认
                        authority = AppUtils.getFileProviderAuthority(getContext());
                    }
                    AppUtils.installApk(getContext(), mApkFile, authority);
                }
                if(callback != null){
                    callback.onFinish(mApkFile);
                }
                stopService();
                return;
            }

            //删除旧文件
            mApkFile.delete();
        }
        LogUtils.d("File:" + mApkFile);

        mHttpManager = httpManager != null ? httpManager : HttpManager.getInstance();
        IHttpManager.DownloadCallback downloadCallback = new AppDownloadCallback(getContext(),this, config, mApkFile, callback, notification);
        mHttpManager.download(url,path,filename,config.getRequestProperty(), downloadCallback);

    }

    /**
     * 停止下载
     */
    public void stopDownload(){
        if(mHttpManager != null){
            mHttpManager.cancel();
        }
    }

    /**
     * 获取缓存路径
     * @param context
     * @return
     */
    private String getCacheFilesDir(Context context) {
        File[] files = ContextCompat.getExternalFilesDirs(context, Constants.DEFAULT_DIR);
        if(files != null && files.length > 0){
            return files[0].getAbsolutePath();
        }

        File externalFilesDir = context.getExternalFilesDir(Constants.DEFAULT_DIR);
        if(externalFilesDir != null){
            return externalFilesDir.getAbsolutePath();
        }

        return new File(context.getFilesDir(), Constants.DEFAULT_DIR).getAbsolutePath();

    }

    /**
     * 停止服务
     */
    private void stopService(){
        mCount = 0;
        stopSelf();
    }


    //---------------------------------------- DownloadCallback

    /**
     * App下载回调接口
     */
    public static class AppDownloadCallback implements IHttpManager.DownloadCallback {

        private Context context;

        private DownloadService downloadService;

        public UpdateConfig config;

        private boolean isShowNotification;

        private int notifyId;

        private String channelId;

        private String channelName;

        private int notificationIcon;

        private boolean isInstallApk;

        private String authority;

        private boolean isShowPercentage;

        private boolean isReDownload;

        private boolean isDeleteCancelFile;

        private boolean isCancelDownload;

        private UpdateCallback callback;

        private INotification notification;

        /**
         * 最后更新进度，用来降频刷新
         */
        private int lastProgress;
        /**
         * 最后进度更新时间，用来降频刷新
         */
        private long lastTime;
        /**
         * APK文件
         */
        private File apkFile;


        private AppDownloadCallback(Context context, DownloadService downloadService, UpdateConfig config, File apkFile, UpdateCallback callback, INotification notification){
            this.context = context;
            this.downloadService = downloadService;
            this.config = config;
            this.apkFile = apkFile;
            this.callback = callback;
            this.notification = notification;
            this.isShowNotification = config.isShowNotification();
            this.notifyId = config.getNotificationId();

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                this.channelId = TextUtils.isEmpty(config.getChannelId()) ? Constants.DEFAULT_NOTIFICATION_CHANNEL_ID : config.getChannelId();
                this.channelName = TextUtils.isEmpty(config.getChannelName()) ? Constants.DEFAULT_NOTIFICATION_CHANNEL_NAME : config.getChannelName();
            }
            if(config.getNotificationIcon() <= 0){
                this.notificationIcon = AppUtils.getAppIcon(context);
            }else{
                this.notificationIcon = config.getNotificationIcon();
            }

            this.isInstallApk = config.isInstallApk();

            this.authority = config.getAuthority();
            if(TextUtils.isEmpty(config.getAuthority())){//如果为空则默认
                authority = AppUtils.getFileProviderAuthority(context);
            }

            this.isShowPercentage = config.isShowPercentage();
            this.isDeleteCancelFile = config.isDeleteCancelFile();
            this.isCancelDownload = config.isCancelDownload();

            //支持下载失败时重新下载，当重新下载次数不超过限制时才被允许
            this.isReDownload = config.isReDownload() && downloadService.mCount < config.getReDownloads();

        }

        @Override
        public void onStart(String url) {
            LogUtils.i("url:" + url);
            downloadService.isDownloading = true;
            lastProgress = 0;
            if(isShowNotification && notification != null){
                notification.onStart(context,notifyId,channelId,channelName,notificationIcon,getString(R.string.app_updater_start_notification_title),getString(R.string.app_updater_start_notification_content),config.isVibrate(),config.isSound(),isCancelDownload);
            }

            if(callback != null){
                callback.onStart(url);
            }
        }

        @Override
        public void onProgress(long progress, long total) {
            boolean isChange = false;
            long curTime = System.currentTimeMillis();
            if(lastTime + 200 < curTime || progress == total) {//降低更新频率
                lastTime = curTime;

                int currProgress = Math.round(progress * 1.0f / total * 100.0f);
                if(currProgress != lastProgress){//百分比改变了才更新
                    isChange = true;
                    lastProgress = currProgress;
                    String percentage = currProgress + "%";
                    LogUtils.i(String.format(Locale.getDefault(),"%s \t(%d/%d)", percentage, progress, total));
                    if(isShowNotification && notification != null) {
                        String content = context.getString(R.string.app_updater_progress_notification_content);
                        if (isShowPercentage) {
                            content += percentage;
                        }

                        notification.onProgress(context,notifyId, channelId, notificationIcon, context.getString(R.string.app_updater_progress_notification_title), content, currProgress, 100,isCancelDownload);
                    }
                }
            }

            if(callback != null){
                callback.onProgress(progress,total,isChange);
            }
        }

        @Override
        public void onFinish(File file) {
            LogUtils.d("File:" + file);
            downloadService.isDownloading = false;
            if(isShowNotification && notification != null){
                notification.onFinish(context,notifyId,channelId,notificationIcon,getString(R.string.app_updater_finish_notification_title),getString(R.string.app_updater_finish_notification_content),file,authority);
            }
            if(isInstallApk){
                AppUtils.installApk(context,file,authority);
            }
            if(callback != null){
                callback.onFinish(file);
            }
            downloadService.stopService();
        }

        @Override
        public void onError(Exception e) {
            LogUtils.w(e.getMessage());
            downloadService.isDownloading = false;
            if(isShowNotification && notification != null){
                String content = isReDownload ? getString(R.string.app_updater_error_notification_content_re_download) : getString(R.string.app_updater_error_notification_content);
                notification.onError(context,notifyId,channelId,notificationIcon,getString(R.string.app_updater_error_notification_title),content,isReDownload,config);
            }

            if(callback != null){
                callback.onError(e);
            }
            if(!isReDownload){
                downloadService.stopService();
            }

        }

        @Override
        public void onCancel() {
            LogUtils.d("Cancel download.");
            downloadService.isDownloading = false;
            if(isShowNotification && notification != null){
                notification.onCancel(context,notifyId);
            }
            if(callback != null){
                callback.onCancel();
            }
            if(isDeleteCancelFile && apkFile != null){
                apkFile.delete();
            }
            downloadService.stopService();
        }

        private String getString(@StringRes int resId){
            return context.getString(resId);
        }
    }

    @Override
    public void onDestroy() {
        isDownloading = false;
        mHttpManager = null;
        super.onDestroy();
    }

    //---------------------------------------- Binder

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mDownloadBinder;
    }

    /**
     * 提供绑定服务的方式进行下载
     */
    public class DownloadBinder extends Binder {

        /**
         * 开始下载
         * @param config {@link UpdateConfig}
         */
        public void start(@NonNull UpdateConfig config){
            start(config,null);
        }

        /**
         * 开始下载
         * @param config {@link UpdateConfig}
         * @param callback {@link UpdateCallback}
         */
        public void start(@NonNull UpdateConfig config, @Nullable UpdateCallback callback){
            start(config,null, callback);
        }

        /**
         * 开始下载
         * @param config {@link UpdateConfig}
         * @param httpManager {@link IHttpManager}
         * @param callback {@link UpdateCallback}
         */
        public void start(@NonNull UpdateConfig config, @Nullable IHttpManager httpManager, @Nullable UpdateCallback callback){
            start(config, httpManager, callback, new NotificationImpl());
        }

        /**
         * 开始下载
         * @param config {@link UpdateConfig}
         * @param httpManager {@link IHttpManager}
         * @param callback {@link UpdateCallback}
         * @param notification {@link INotification}
         */
        public void start(@NonNull UpdateConfig config, @Nullable IHttpManager httpManager, @Nullable UpdateCallback callback,@NonNull INotification notification){
            startDownload(config, httpManager, callback, notification);
        }
    }


}