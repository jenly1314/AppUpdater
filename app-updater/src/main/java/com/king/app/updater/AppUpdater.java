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
import android.util.Log;

import com.king.app.updater.callback.UpdateCallback;
import com.king.app.updater.constant.Constants;
import com.king.app.updater.http.IHttpManager;
import com.king.app.updater.service.DownloadService;
import com.king.app.updater.util.PermissionUtils;

import java.util.Map;


/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class AppUpdater {
    /**
     * {@link #mContext}不强制要求是{@link Activity}，但能传{@link Activity}尽量传。AppUpdater本应该只专注于App更新，尽量不涉及动态权限相关的处理。如果mContext传的是{@link Activity}，则默认会校验一次动态权限。
     */
    private Context mContext;
    /**
     * 配置信息
     */
    private UpdateConfig mConfig;
    /**
     * 更新回调
     */
    private UpdateCallback mCallback;
    /**
     * http管理接口，可自定义实现。如：使用okHttp
     */
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
            //如果mContext是Activity,则默认会校验一次动态权限。
            if(mContext instanceof Activity){
                PermissionUtils.INSTANCE.verifyReadAndWritePermissions((Activity) mContext,Constants.RE_CODE_STORAGE_PERMISSION);
            }

            if(mConfig.isShowNotification() && !PermissionUtils.INSTANCE.isNotificationEnabled(mContext)){
                Log.w(Constants.TAG,"Notification permission not enabled.");
            }

            startDownloadService();
        }else{
            throw new NullPointerException("Url = null");
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
         * 设置APK下载地址
         * @param url 下载地址
         * @return
         */
        public Builder serUrl(@NonNull String url){
            mConfig.setUrl(url);
            return this;
        }

        /**
         * 设置保存的路径
         * @param path  下载保存的文件路径 （默认SD卡/.AppUpdater目录）
         * @return
         */
        public Builder setPath(String path){
            mConfig.setPath(path);
            return this;
        }

        /**
         * 设置保存的文件名
         * @param filename 下载的保存的apk文件名 （默认优先取url文件名）
         * @return
         */
        public Builder setFilename(String filename){
            mConfig.setFilename(filename);
            return this;
        }

        /**
         * 设置是否显示通知栏
         * @param isShowNotification 是否显示通知栏 （默认true）
         * @return
         */
        public Builder setShowNotification(boolean isShowNotification) {
            mConfig.setShowNotification(isShowNotification);
            return this;
        }

        /**
         * 设置通知ID
         * @param notifyId 通知ID
         * @return
         */
        public Builder setNotificationId(int notifyId) {
            mConfig.setNotificationId(notifyId);
            return this;
        }

        /**
         * 设置通知渠道ID
         * @param channelId 通知渠道ID （默认兼容O）
         * @return
         */
        public Builder setChannelId(String channelId) {
            mConfig.setChannelId(channelId);
            return this;
        }

        /**
         * 设置通知渠道名称
         * @param channelName 通知渠道名称 （默认兼容O）
         * @return
         */
        public Builder setChannelName(String channelName) {
            mConfig.setChannelName(channelName);
            return this;
        }

        /**
         * 设置通知图标
         * @param icon 通知栏图标 （默认取App的icon）
         * @return
         */
        public Builder setNotificationIcon(@DrawableRes int icon) {
            mConfig.setNotificationIcon(icon);
            return this;
        }

        /**
         * 设置通知是否震动提示
         * @param vibrate 是否震动提示，为true时使用通知默认震动，Android O(8.0)以上设置，只有初次创建channel时有效，后续修改属性无效，想要重新有效需修改channelId或卸载App重装。
         * @return
         */
        public Builder setVibrate(boolean vibrate) {
            mConfig.setVibrate(vibrate);
            return this;
        }

        /**
         * 设置通知是否铃声提示
         * @param sound 是否铃声提示，为true时使用通知默认铃声，Android O(8.0)以上设置，只有初次创建channel时有效，后续修改属性无效，想要重新有效需修改channelId或卸载App重装。
         * @return
         */
        public Builder setSound(boolean sound) {
            mConfig.setSound(sound);
            return this;
        }


        /**
         * 设置下载完成后知否自动触发安装APK
         * @param isInstallApk 下载完成后是否自动调用安装APK（默认true）
         * @return
         */
        public Builder setInstallApk(boolean isInstallApk){
            mConfig.setInstallApk(isInstallApk);
            return this;
        }

        /**
         * 设置FileProvider的authority
         * @param authority FileProvider的authority（默认兼容N，默认值context.getPackageName() + ".fileProvider"）
         * @return
         */
        public Builder setAuthority(String authority){
            mConfig.setAuthority(authority);
            return this;
        }

        /**
         * 设置下载时，通知栏是否显示下载百分比
         * @param showPercentage 下载时通知栏是否显示百分比
         * @return
         */
        public Builder setShowPercentage(boolean showPercentage) {
            mConfig.setShowPercentage(showPercentage);
            return this;
        }

        /**
         * 设置下载失败是是否支持点击通知栏重新下载
         * @param reDownload 下载失败时是否支持点击通知栏重新下载，默认true 最多重新下载3次
         * @return
         */
        public Builder setReDownload(boolean reDownload) {
            mConfig.setReDownload(reDownload);
            return this;
        }

        /**
         * 设置要下载APK的versionCode
         * @param versionCode 为null表示不处理，默认不存在则下载，存在则重新下载。不为null时，表示会优先校验本地是否存在已下载版本号为versionCode的APK。
         *                    如果存在则不会重新下载(AppUpdater会自动校验packageName一致性)，直接取本地APK，反之重新下载。
         * @return
         */
        public Builder setVersionCode(Integer versionCode) {
            mConfig.setVersionCode(versionCode);
            return this;
        }

        /**
         * 请求头添加参数
         * @param key
         * @param value
         * @return
         */
        public Builder addHeader(String key, String value){
            mConfig.addHeader(key,value);
            return this;
        }

        /**
         * 请求头添加参数
         * @param headers
         * @return
         */
        public Builder addHeader(Map<String,String> headers){
            mConfig.addHeader(headers);
            return this;
        }

        public AppUpdater build(@NonNull Context context){
            AppUpdater appUpdater = new AppUpdater(context,mConfig);
            return appUpdater;
        }

    }


}
