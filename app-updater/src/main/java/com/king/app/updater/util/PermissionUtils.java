package com.king.app.updater.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public enum  PermissionUtils {

    INSTANCE;

    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    public boolean verifyReadAndWritePermissions(@NonNull Activity activity,int requestCode){

        int readResult = checkPermission(activity,Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeResult = checkPermission(activity,Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPhoneState = checkPermission(activity,Manifest.permission.READ_PHONE_STATE);
        if( readResult != PackageManager.PERMISSION_GRANTED || writeResult != PackageManager.PERMISSION_GRANTED || readPhoneState != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,requestCode);
            return false;
        }
        return true;
    }

    public int checkPermission(@NonNull Activity activity,@NonNull String permission){
        return ActivityCompat.checkSelfPermission(activity,permission);
    }
}
