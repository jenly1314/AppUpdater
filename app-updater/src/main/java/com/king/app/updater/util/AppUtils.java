package com.king.app.updater.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public enum AppUtils {

    INSTANCE;

    /**
     * 通过url获取App的全名称
     * @param context
     * @return AppName.apk
     */
    public String getAppFullName(Context context,String url,String defaultName){
        if(url.endsWith(".apk")){
            return url.substring(url.lastIndexOf("/") + 1);
        }

        String filename = getAppName(context);

        if(TextUtils.isEmpty(filename)){
            filename = defaultName;
        }

        return String.format("%s.apk",filename);
    }

    /**
     * 获取包信息
     * @param context
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo( context.getPackageName(), 0);
        return packageInfo;
    }

    /**
     * 通过APK路径获取包信息
     * @param context
     * @param archiveFilePath
     * @return
     */
    public static PackageInfo getPackageInfo(Context context, String archiveFilePath) throws Exception {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        return packageInfo;
    }


    /**
     * 获取App的名称
     */
    public String getAppName(Context context) {
        try{

            int labelRes = getPackageInfo(context).applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取App的图标
     * @param context
     * @return
     */
    public int getAppIcon(Context context){
        try{
            return getPackageInfo(context).applicationInfo.icon;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 安装apk
     * @param context
     * @param file
     */
    public void installApk(Context context,File file,String authority){

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uriData = null;
        String type = "application/vnd.android.package-archive";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uriData = FileProvider.getUriForFile(context, authority, file);
        }else{
            uriData = Uri.fromFile(file);
        }
        intent.setDataAndType(uriData, type);
        context.startActivity(intent);
    }

    /**
     * APK是否存在
     * @param context
     * @param versionCode
     * @param file
     * @return
     * @throws Exception
     */
    public boolean apkExists(Context context,int versionCode,File file) throws Exception{
        if(file!=null && file.exists()){
            String packageName = context.getPackageName();
            PackageInfo packageInfo = AppUtils.getPackageInfo(context,file.getAbsolutePath());
            if(packageInfo!=null && versionCode == packageInfo.versionCode){//比对versionCode
                ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                if(applicationInfo!=null && packageName.equals(applicationInfo.packageName)){//比对packageName
                    return true;
                }
            }
        }
        return false;
    }
}
