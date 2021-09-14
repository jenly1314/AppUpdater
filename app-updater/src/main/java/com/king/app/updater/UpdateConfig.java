package com.king.app.updater;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.king.app.updater.constant.Constants;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.DrawableRes;


/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class UpdateConfig implements Parcelable {


    private String mUrl;
    /**
     * 保存路径
     */
    private String mPath;
    /**
     * 保存文件名
     */
    private String mFilename;

    /**
     * 是否显示通知栏
     */
    private boolean isShowNotification = true;
    /**
     * 下载完成后是否自动弹出安装
     */
    private boolean isInstallApk = true;
    /**
     * 通知栏图标：默认取app图标
     */
    private int mNotificationIcon;

    /**
     * 通知栏ID
     */
    private int mNotificationId = Constants.DEFAULT_NOTIFICATION_ID;

    /**
     * 通知栏渠道ID
     */
    private String mChannelId;
    /**
     * 通知栏渠道名称
     */
    private String mChannelName;
    /**
     *  默认{@link Context#getPackageName() + ".fileProvider"}
     */
    private String mAuthority;
    /**
     * 下载失败是否支持点击通知栏重新下载
     */
    private boolean isReDownload = true;
    /**
     * 下载失败后，最大重新下载次数
     */
    private int reDownloads = 3;
    /**
     * 是否显示百分比
     */
    private boolean isShowPercentage = true;

    /**
     * 是否震动提示，为true时使用通知默认震动
     */
    private boolean isVibrate;

    /**
     * 是否铃声提示,为true时使用通知默认铃声
     */
    private boolean isSound;

    /**
     * 要下载的APK的versionCode
     */
    private Integer versionCode;

    /**
     * 请求头参数
     */
    private Map<String,String> mRequestProperty;

    /**
     * 是否删除取消下载的文件
     */
    private boolean isDeleteCancelFile = true;

    /**
     * 是否支持通过删除通知栏来取消下载
     */
    private boolean isCancelDownload = false;

    /**
     * APK文件的MD5
     */
    private String apkMD5;


    public UpdateConfig() {

    }

    public String getUrl() {
        return mUrl;
    }

    /**
     * 设置APK下载地址
     * @param url 下载地址
     */
    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getPath() {
        return mPath;
    }

    /**
     * 设置保存的路径，（建议使用默认，不做设置）
     * @param path  下载保存的文件路径
     * @return
     * @deprecated 因为适配Android Q的分区存储，所以此方法已弃用，不建议再使用
     */
    @Deprecated
    public void setPath(String path) {
        this.mPath = path;
    }

    public String getFilename() {
        return mFilename;
    }

    /**
     * 设置保存的文件名
     * @param filename 下载的保存的apk文件名 （默认优先取url文件名）
     */
    public void setFilename(String filename) {
        this.mFilename = filename;
    }

    public boolean isShowNotification() {
        return isShowNotification;
    }

    /**
     * 设置是否显示通知栏
     * @param isShowNotification 是否显示通知栏 （默认true）
     */
    public void setShowNotification(boolean isShowNotification) {
        this.isShowNotification = isShowNotification;
    }

    public String getChannelId() {
        return mChannelId;
    }

    /**
     * 设置通知渠道ID
     * @param channelId 通知渠道ID （默认兼容O）
     */
    public void setChannelId(String channelId) {
        this.mChannelId = channelId;
    }

    public String getChannelName() {
        return mChannelName;
    }

    /**
     * 设置通知渠道名称
     * @param channelName 通知渠道名称 （默认兼容O）
     */
    public void setChannelName(String channelName) {
        this.mChannelName = channelName;
    }
    /**
     * 设置通知ID
     * @param notificationId 通知ID
     */
    public void setNotificationId(int notificationId){
        this.mNotificationId = notificationId;
    }

    public int getNotificationId(){
        return this.mNotificationId;
    }


    /**
     * 设置通知图标
     * @param icon 通知栏图标 （默认取App的icon）
     */
    public void setNotificationIcon(@DrawableRes int icon){
        this.mNotificationIcon = icon;
    }

    public int getNotificationIcon(){
        return this.mNotificationIcon;
    }

    public boolean isInstallApk() {
        return isInstallApk;
    }

    /**
     * 设置下载完成后知否自动触发安装APK
     * @param isInstallApk 下载完成后是否自动调用安装APK（默认true）
     */
    public void setInstallApk(boolean isInstallApk) {
        this.isInstallApk = isInstallApk;
    }

    public String getAuthority() {
        return mAuthority;
    }

    /**
     * 设置FileProvider的authority
     * @param authority FileProvider的authority（默认兼容N，默认值{@link Context#getPackageName() + ".fileProvider"}）
     */
    public void setAuthority(String authority) {
        this.mAuthority = authority;
    }

    public boolean isShowPercentage() {
        return isShowPercentage;
    }

    /**
     * 设置下载时，通知栏是否显示下载百分比
     * @param showPercentage 下载时通知栏是否显示百分比
     */
    public void setShowPercentage(boolean showPercentage) {
        isShowPercentage = showPercentage;
    }

    public boolean isReDownload() {
        return isReDownload;
    }

    /**
     * 设置下载失败时，是否支持点击通知栏重新下载。与之相关联的方法{@link #setReDownloads(int)}
     * @param reDownload 下载失败时是否支持点击通知栏重新下载，默认true
     */
    public void setReDownload(boolean reDownload) {
        isReDownload = reDownload;
    }

    public int getReDownloads() {
        return reDownloads;
    }

    /**
     * 设置下载失败时，最多重新下载次数。与之相关联的方法{@link #setReDownload(boolean)}
     * @param reDownloads 下载失败时是否支持点击通知栏重新下载，默认最多重新下载3次
     */
    public void setReDownloads(int reDownloads) {
        this.reDownloads = reDownloads;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    /**
     * 设置通知是否震动提示
     * @param vibrate 是否震动提示，为true时使用通知默认震动，Android O(8.0)以上设置，只有初次创建channel时有效，后续修改属性无效，想要重新有效需修改channelId或卸载App重装。
     */
    public void setVibrate(boolean vibrate) {
        isVibrate = vibrate;
    }

    public boolean isSound() {
        return isSound;
    }

    /**
     * 设置通知是否铃声提示
     * @param sound 是否铃声提示，为true时使用通知默认铃声，Android O(8.0)以上设置，只有初次创建channel时有效，后续修改属性无效，想要重新有效需修改channelId或卸载App重装。
     */
    public void setSound(boolean sound) {
        isSound = sound;
    }

    public Integer getVersionCode(){
        return versionCode;
    }

    /**
     * 设置要下载APK的versionCode，用于优先取缓存时通过versionCode校验APK文件是否一致。
     * 缓存校验目前支持两种方式，一种是通过versionCode校验，即{@link #setVersionCode(Integer)}；一种是文件MD5校验，即{@link #setApkMD5(String)}。推荐使用MD5校验方式
     * 如果两种方式都设置了，则只校验MD5
     * @param versionCode 为null表示不处理，默认不存在则下载，存在则重新下载。不为null时，表示会优先校验本地是否存在已下载版本号为versionCode的APK。
     *                    如果存在则不会重新下载(AppUpdater会自动校验packageName一致性)，直接取本地APK，反之重新下载。
     */
    public void setVersionCode(Integer versionCode){
        this.versionCode = versionCode;
    }

    public Map<String, String> getRequestProperty() {
        return mRequestProperty;
    }

    /**
     * 设置APK文件的MD5，用于优先取缓存时通过MD5校验文件APK是否一致。
     * 缓存校验目前支持两种方式，一种是通过versionCode校验，即{@link #setVersionCode(Integer)}；一种是文件MD5校验，即{@link #setApkMD5(String)}。推荐使用MD5校验方式
     * 如果两种方式都设置了，则只校验MD5
     * @param md5 为null表示不处理，如果设置了MD5，则缓存APK的MD5相同时，只下载一次，优先取本地缓存
     */
    public void setApkMD5(String md5){
        this.apkMD5 = md5;
    }

    public String getApkMD5(){
        return apkMD5;
    }

    /**
     * 请求头添加参数
     * @param key
     * @param value
     */
    public void addHeader(String key, String value){
        initRequestProperty();
        mRequestProperty.put(key,value);
    }

    /**
     * 请求头添加参数
     * @param headers
     */
    public void addHeader(Map<String,String> headers){
        initRequestProperty();
        mRequestProperty.putAll(headers);
    }

    private void initRequestProperty(){
        if(mRequestProperty == null){
            mRequestProperty = new HashMap<>();
        }
    }

    public boolean isDeleteCancelFile() {
        return isDeleteCancelFile;
    }

    /**
     *  设置是否自动删除取消下载的文件
     * @param deleteCancelFile 是否删除取消下载的文件（默认为：true）
     */
    public void setDeleteCancelFile(boolean deleteCancelFile) {
        isDeleteCancelFile = deleteCancelFile;
    }

    public boolean isCancelDownload(){
        return isCancelDownload;
    }

    /**
     * 是否支持通过删除通知栏来取消下载（默认为：false）
     * @param cancelDownload
     */
    public void setCancelDownload(boolean cancelDownload) {
        isCancelDownload = cancelDownload;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUrl);
        dest.writeString(this.mPath);
        dest.writeString(this.mFilename);
        dest.writeByte(this.isShowNotification ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isInstallApk ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mNotificationIcon);
        dest.writeInt(this.mNotificationId);
        dest.writeString(this.mChannelId);
        dest.writeString(this.mChannelName);
        dest.writeString(this.mAuthority);
        dest.writeByte(this.isReDownload ? (byte) 1 : (byte) 0);
        dest.writeInt(this.reDownloads);
        dest.writeByte(this.isShowPercentage ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isVibrate ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSound ? (byte) 1 : (byte) 0);
        dest.writeValue(this.versionCode);
        if(mRequestProperty!=null){
            dest.writeInt(this.mRequestProperty.size());
            for (Map.Entry<String, String> entry : this.mRequestProperty.entrySet()) {
                dest.writeString(entry.getKey());
                dest.writeString(entry.getValue());
            }
        }else{
            dest.writeInt(0);
        }

        dest.writeByte(this.isDeleteCancelFile ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCancelDownload ? (byte) 1 : (byte) 0);
        dest.writeString(this.apkMD5);
    }

    protected UpdateConfig(Parcel in) {
        this.mUrl = in.readString();
        this.mPath = in.readString();
        this.mFilename = in.readString();
        this.isShowNotification = in.readByte() != 0;
        this.isInstallApk = in.readByte() != 0;
        this.mNotificationIcon = in.readInt();
        this.mNotificationId = in.readInt();
        this.mChannelId = in.readString();
        this.mChannelName = in.readString();
        this.mAuthority = in.readString();
        this.isReDownload = in.readByte() != 0;
        this.reDownloads = in.readInt();
        this.isShowPercentage = in.readByte() != 0;
        this.isVibrate = in.readByte() != 0;
        this.isSound = in.readByte() != 0;
        this.versionCode = (Integer) in.readValue(Integer.class.getClassLoader());
        int mRequestPropertySize = in.readInt();
        this.mRequestProperty = new HashMap<>(mRequestPropertySize);
        for (int i = 0; i < mRequestPropertySize; i++) {
            String key = in.readString();
            String value = in.readString();
            this.mRequestProperty.put(key, value);
        }
        this.isDeleteCancelFile = in.readByte() != 0;
        this.isCancelDownload = in.readByte() != 0;
        this.apkMD5 = in.readString();
    }

    public static final Creator<UpdateConfig> CREATOR = new Creator<UpdateConfig>() {
        @Override
        public UpdateConfig createFromParcel(Parcel source) {
            return new UpdateConfig(source);
        }

        @Override
        public UpdateConfig[] newArray(int size) {
            return new UpdateConfig[size];
        }
    };
}