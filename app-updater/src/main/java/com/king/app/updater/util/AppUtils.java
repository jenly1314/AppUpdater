package com.king.app.updater.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.king.app.updater.constant.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Locale;

import androidx.core.content.FileProvider;
import androidx.core.content.pm.PackageInfoCompat;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public final class AppUtils {

    /**
     * 十六进制字符
     */
    private static char hexChars[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private AppUtils() {
        throw new AssertionError();
    }

    /**
     * 通过url获取App的全名称
     *
     * @param context 上下文
     * @return 返回App的名称；（例如：AppName.apk）
     */
    public static String getAppFullName(Context context, String url, String defaultName) {
        if (url.endsWith(".apk")) {
            String apkName = url.substring(url.lastIndexOf("/") + 1);
            if (apkName.length() <= 64) {
                return apkName;
            }
        }

        String filename = getAppName(context);
        LogUtils.d("AppName: " + filename);
        if (TextUtils.isEmpty(filename)) {
            filename = defaultName;
        }
        if (filename.endsWith(".apk")) {
            return filename;
        }
        return String.format("%s.apk", filename);
    }

    /**
     * 获取包信息
     *
     * @param context 上下文
     * @return {@link PackageInfo}
     * @throws PackageManager.NameNotFoundException
     */
    public static PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return packageInfo;
    }

    /**
     * 通过APK路径获取包信息
     *
     * @param context         上下文
     * @param archiveFilePath 文件路径
     * @return
     */
    public static PackageInfo getPackageInfo(Context context, String archiveFilePath) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES);
        return packageInfo;
    }

    /**
     * 获取App的名称
     *
     * @param context 上下文
     */
    public static String getAppName(Context context) {
        try {
            int labelRes = getPackageInfo(context).applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取App的图标
     *
     * @param context 上下文
     * @return
     */
    public static int getAppIcon(Context context) {
        try {
            return getPackageInfo(context).applicationInfo.icon;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 安装APK
     *
     * @param context   上下文
     * @param file      APK文件
     * @param authority 文件访问授权
     */
    public static void installApk(Context context, File file, String authority) {
        Intent intent = getInstallIntent(context, file, authority);
        context.startActivity(intent);
    }

    /**
     * 获取安装Intent
     *
     * @param context   上下文
     * @param file      APK文件
     * @param authority 文件访问授权
     * @return
     */
    public static Intent getInstallIntent(Context context, File file, String authority) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uriData;
        String type = "application/vnd.android.package-archive";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uriData = FileProvider.getUriForFile(context, authority, file);
        } else {
            uriData = Uri.fromFile(file);
        }
        intent.setDataAndType(uriData, type);
        return intent;
    }

    /**
     * APK是否存在
     *
     * @param context     上下文
     * @param versionCode 版本号
     * @param file        APK文件
     * @return
     * @throws Exception
     */
    public static boolean apkExists(Context context, long versionCode, File file) {
        if (file != null && file.exists()) {
            String packageName = context.getPackageName();
            PackageInfo packageInfo = AppUtils.getPackageInfo(context, file.getAbsolutePath());

            if (packageInfo != null) {
                // 比对versionCode
                long apkVersionCode = PackageInfoCompat.getLongVersionCode(packageInfo);
                LogUtils.d(String.format(Locale.getDefault(), "ApkVersionCode: %d", apkVersionCode));
                if (versionCode == apkVersionCode) {
                    ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                    if (applicationInfo != null && packageName.equals(applicationInfo.packageName)) {//比对packageName
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断文件是否存在
     *
     * @param context 上下文
     * @param path    文件路径
     * @return
     */
    public static boolean isAndroidQFileExists(Context context, String path) {
        return isAndroidQFileExists(context, new File(path));
    }

    /**
     * 判断文件是否存在
     *
     * @param context 上下文
     * @param file    文件
     * @return
     */
    public static boolean isAndroidQFileExists(Context context, File file) {
        AssetFileDescriptor descriptor = null;
        ContentResolver contentResolver = context.getContentResolver();
        try {
            Uri uri = Uri.fromFile(file);
            descriptor = contentResolver.openAssetFileDescriptor(uri, "r");
            if (descriptor == null) {
                return false;
            } else {
                close(descriptor);
            }
            return true;
        } catch (FileNotFoundException e) {
            LogUtils.w(e.getMessage());
        } finally {
            close(descriptor);
        }
        return false;
    }

    /**
     * 校验文件的MD5
     *
     * @param file 文件
     * @param md5  MD5
     * @return 如果文件的MD5与 传入的 MD5字符比对一致，则返回 true，反之返回 false
     */
    public static boolean verifyFileMD5(File file, String md5) {
        String fileMD5 = getFileMD5(file);
        LogUtils.d("FileMD5: " + fileMD5);
        if (!TextUtils.isEmpty(md5)) {
            return md5.equalsIgnoreCase(fileMD5);
        }

        return false;
    }

    /**
     * 获取文件的MD5
     *
     * @param file 文件
     * @return 返回文件的MD5
     */
    public static String getFileMD5(File file) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, length);
            }
            return byteArrayToHexString(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 字节转为十六进制字符串
     *
     * @param bytes 字节数组
     * @return 返回十六进制字符串
     */
    public static String byteArrayToHexString(byte bytes[]) {
        String hexString = null;
        if (bytes != null) {
            int length = bytes.length;
            StringBuilder out = new StringBuilder(length * 2);
            for (int x = 0; x < length; x++) {
                int nybble = bytes[x] & 0xF0;
                nybble = nybble >>> 4;
                out.append(hexChars[nybble]);
                out.append(hexChars[bytes[x] & 0x0F]);
            }
            hexString = out.toString();
        }
        return hexString;
    }

    /**
     * 获取文件访问授权
     *
     * @param context 上下文
     * @return 返回文件访问授权
     */
    public static String getFileProviderAuthority(Context context) {
        return context.getPackageName() + Constants.DEFAULT_FILE_PROVIDER;
    }

    /**
     * 关闭
     *
     * @param descriptor {@link AssetFileDescriptor}
     */
    private static void close(AssetFileDescriptor descriptor) {
        if (descriptor != null) {
            try {
                descriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}