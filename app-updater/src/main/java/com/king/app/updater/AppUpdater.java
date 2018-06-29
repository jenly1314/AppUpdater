package com.king.app.updater;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.king.app.updater.callback.UpdateCallback;
import com.king.app.updater.constant.Constants;
import com.king.app.updater.http.IHttpManager;
import com.king.app.updater.service.DownloadService;
import com.king.app.updater.util.PermissionUtils;


/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class AppUpdater {

    private Context mContext;

    private UpdateConfig mConfig;

    private UpdateCallback mCallback;

    private IHttpManager mHttpManager;

    private ServiceConnection mServiceConnection;


    public AppUpdater(@NonNull Context context,@NonNull UpdateConfig config){
        this.mContext = context;
        this.mConfig = config;
    }

    public AppUpdater(@NonNull Context context,@NonNull String url){
        this.mContext = context;
        mConfig = new UpdateConfig();
        mConfig.setUrl(url);
    }

    public AppUpdater setUpdateCallback(UpdateCallback callback){
        this.mCallback = callback;
        return this;
    }

    public AppUpdater setHttpManager(IHttpManager httpManager){
        this.mHttpManager = httpManager;
        return this;
    }

    /**
     * 开始下载
     */
    public void start(){
        if(mConfig!=null && !TextUtils.isEmpty(mConfig.getUrl())){
            //mContext不强制要求是Activity，能传Activity尽量传。AppUpdater本应该只专注于App更新，尽量不涉及动态权限相关的处理。如果mContext不强制要求是Activity是Activity默认会优先校验一次动态权限。
            if(mContext instanceof Activity){
                PermissionUtils.INSTANCE.verifyReadAndWritePermissions((Activity) mContext,Constants.RE_CODE_STORAGE_PERMISSION);
            }
            startDownloadService();
        }else{
            throw new NullPointerException("url = null");
        }
    }

    /**
     * 启动下载服务
     */
    private void startDownloadService(){

        Intent intent = new Intent(mContext, DownloadService.class);
        if(mCallback!=null || mHttpManager!=null){//bindService
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    DownloadService.DownloadBinder binder = ((DownloadService.DownloadBinder)service);
                    binder.start(mConfig,mHttpManager,mCallback);
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };

            mContext.getApplicationContext().bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
        }else{//startService
            intent.putExtra(Constants.KEY_UPDATE_CONFIG,mConfig);
            mContext.startService(intent);
        }
    }


    /**
     * AppUpdater构建器
     */
    public static class Builder{

        private UpdateConfig mConfig;

        public Builder(){
            mConfig = new UpdateConfig();
        }

        /**
         *
         * @param url 下载地址
         * @return
         */
        public Builder serUrl(@NonNull String url){
            mConfig.setUrl(url);
            return this;
        }

        /**
         *
         * @param path  下载保存的文件路径 （默认SD卡/.AppUpdater目录）
         * @return
         */
        public Builder setPath(String path){
            mConfig.setPath(path);
            return this;
        }

        /**
         *
         * @param filename 下载的保存的apk文件名 （默认优先取url文件名）
         * @return
         */
        public Builder setFilename(String filename){
            mConfig.setFilename(filename);
            return this;
        }

        /**
         *
         * @param isShowNotification 是否显示通知栏 （默认true）
         * @return
         */
        public Builder setShowNotification(boolean isShowNotification) {
            mConfig.setShowNotification(isShowNotification);
            return this;
        }

        /**
         *
         * @param notifyId 通知ID
         * @return
         */
        public Builder setNotificationId(int notifyId) {
            mConfig.setNotificationId(notifyId);
            return this;
        }

        /**
         *
         * @param channelId 通知渠道ID （默认兼容O）
         * @return
         */
        public Builder setChannelId(String channelId) {
            mConfig.setChannelId(channelId);
            return this;
        }

        /**
         *
         * @param channelName 通知渠道名称 （默认兼容O）
         * @return
         */
        public Builder setChannelName(String channelName) {
            mConfig.setChannelName(channelName);
            return this;
        }

        /**
         *
         * @param icon 通知栏图标 （默认取App的icon）
         * @return
         */
        public Builder setNotificationIcon(@DrawableRes int icon) {
            mConfig.setNotificationIcon(icon);
            return this;
        }

        /**
         *
         * @param isInstallApk 下载完成后是否自动调用安装APK（默认true）
         * @return
         */
        public Builder setInstallApk(boolean isInstallApk){
            mConfig.setInstallApk(isInstallApk);
            return this;
        }

        /**
         *
         * @param authority FileProvider的authority（默认兼容N，默认值context.getPackageName() + ".fileProvider"）
         * @return
         */
        public Builder setAuthority(String authority){
            mConfig.setAuthority(authority);
            return this;
        }

        /**
         *
         * @param showPercentage 下载时通知栏是否显示百分比
         * @return
         */
        public Builder setShowPercentage(boolean showPercentage) {
            mConfig.setShowPercentage(showPercentage);
            return this;
        }

        /**
         *
         * @param reDownload 下载失败时是否支持点击通知栏重新下载，默认true 最多重新下载3次
         * @return
         */
        public Builder setReDownload(boolean reDownload) {
            mConfig.setReDownload(reDownload);
            return this;
        }

        public AppUpdater build(@NonNull Context context){
            AppUpdater appUpdater = new AppUpdater(context,mConfig);
            return appUpdater;
        }

    }


}
