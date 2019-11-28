package com.king.app.updater.util;

import android.Manifest;
import android.app.Activity;
import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public final class PermissionUtils {

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    private PermissionUtils(){
        throw new AssertionError();
    }

    /**
     * 校验权限
     * @param activity
     * @param requestCode
     * @return
     */
    public static boolean verifyReadAndWritePermissions(@NonNull Activity activity,int requestCode){

        int readResult = checkPermission(activity,Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeResult = checkPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPhoneState = checkPermission(activity,Manifest.permission.READ_PHONE_STATE);
        if(readResult != PackageManager.PERMISSION_GRANTED || writeResult != PackageManager.PERMISSION_GRANTED || readPhoneState != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,requestCode);
            return false;
        }
        return true;
    }

    public static int checkPermission(@NonNull Activity activity,@NonNull String permission){
        return ActivityCompat.checkSelfPermission(activity,permission);
    }

    /**
     * 获取通知权限
     * @param context
     */
    public static boolean isNotificationEnabled(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                return false;
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            String CHECK_OP_NO_THROW = "checkOpNoThrow";
            String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            try {
                Class appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

                int value = (Integer) opPostNotificationValue.get(Integer.class);
                return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        return true;
    }

}