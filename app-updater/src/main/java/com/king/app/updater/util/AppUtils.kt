package com.king.app.updater.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.pm.PackageInfoCompat
import com.king.app.updater.constant.Constants
import com.king.logx.LogX
import java.io.File
import java.io.FileInputStream
import java.security.MessageDigest

/**
 * App工具类
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
object AppUtils {

    /**
     * 十六进制字符
     */
    @JvmStatic
    private val hexChars = charArrayOf(
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    )

    /**
     * 获取包信息
     * @param context 上下文
     * @return [PackageInfo]
     * @throws PackageManager.NameNotFoundException
     */
    @Throws(PackageManager.NameNotFoundException::class)
    @JvmStatic
    fun getPackageInfo(context: Context): PackageInfo {
        val packageManager = context.packageManager
        return packageManager.getPackageInfo(context.packageName, 0)
    }

    /**
     * 通过APK路径获取包信息
     * @param context 上下文
     * @param archiveFilePath 文件路径
     * @return [PackageInfo]
     */
    @JvmStatic
    fun getPackageInfo(context: Context, archiveFilePath: String): PackageInfo? {
        val packageManager = context.packageManager
        return packageManager.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES)
    }

    /**
     * 获取App的名称
     * @param context 上下文
     * @return App名称
     */
    @JvmStatic
    fun getAppName(context: Context): String? {
        return try {
            val labelRes = getPackageInfo(context).applicationInfo.labelRes
            context.resources.getString(labelRes)
        } catch (e: Exception) {
            LogX.w(e)
            null
        }
    }

    /**
     * 获取App的图标
     * @param context 上下文
     * @return 图标资源ID
     */
    @JvmStatic
    fun getAppIcon(context: Context): Int {
        return try {
            getPackageInfo(context).applicationInfo.icon
        } catch (e: Exception) {
            LogX.w(e)
            0
        }
    }

    /**
     * 安装APK
     * @param context 上下文
     * @param file APK文件
     * @param authority 文件访问授权
     */
    @JvmStatic
    fun installApk(context: Context, file: File, authority: String) {
        val intent = getInstallIntent(context, file, authority)
        context.startActivity(intent)
    }

    /**
     * 获取安装Intent
     * @param context 上下文
     * @param file APK文件
     * @param authority 文件访问授权
     * @return [Intent]
     */
    @JvmStatic
    fun getInstallIntent(context: Context, file: File, authority: String): Intent {
        return Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addCategory(Intent.CATEGORY_DEFAULT)
            val type = "application/vnd.android.package-archive"
            val uriData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                FileProvider.getUriForFile(context, authority, file)
            } else {
                Uri.fromFile(file)
            }
            setDataAndType(uriData, type)
        }
    }

    /**
     * APK是否存在
     * @param context 上下文
     * @param versionCode 版本号
     * @param file APK文件
     * @return 是否存在
     */
    @JvmStatic
    fun apkExists(context: Context, versionCode: Int, file: File?): Boolean {
        if (file != null && file.exists()) {
            val packageName = context.packageName
            val packageInfo = getPackageInfo(context, file.absolutePath)

            if (packageInfo != null) {
                // 比对versionCode
                val apkVersionCode = PackageInfoCompat.getLongVersionCode(packageInfo)
                LogX.d("APK versionCode: $apkVersionCode")
                if (versionCode.toLong() == apkVersionCode) {
                    val applicationInfo = packageInfo.applicationInfo
                    // 比对packageName
                    return packageName == applicationInfo.packageName
                }
            }
        }
        return false
    }

    /**
     * 校验文件的MD5
     * @param file 文件
     * @param md5 MD5
     * @return 如果文件的MD5与传入的MD5字符比对一致，则返回true，反之返回false
     */
    @JvmStatic
    fun verifyFileMD5(file: File, md5: String): Boolean {
        val fileMD5 = getFileMD5(file)
        LogX.d("File MD5: $fileMD5")
        return if (!TextUtils.isEmpty(md5)) {
            md5.equals(fileMD5, ignoreCase = true)
        } else {
            false
        }
    }

    /**
     * 获取文件的MD5
     * @param file 文件
     * @return 文件的MD5
     */
    @JvmStatic
    fun getFileMD5(file: File): String? {
        var fileInputStream: FileInputStream? = null
        return try {
            fileInputStream = FileInputStream(file)
            val messageDigest = MessageDigest.getInstance("MD5")
            val buffer = ByteArray(8192)
            var length: Int
            while (fileInputStream.read(buffer).also { length = it } != -1) {
                messageDigest.update(buffer, 0, length)
            }
            byteArrayToHexString(messageDigest.digest())
        } catch (e: Exception) {
            LogX.w(e)
            null
        } finally {
            fileInputStream?.close()
        }
    }

    /**
     * 字节转为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    @JvmStatic
    fun byteArrayToHexString(bytes: ByteArray?): String? {
        if (bytes == null) return null

        val length = bytes.size
        val out = StringBuilder(length * 2)
        for (x in 0 until length) {
            var nybble = bytes[x].toInt() and 0xF0
            nybble = nybble ushr 4
            out.append(hexChars[nybble])
            out.append(hexChars[bytes[x].toInt() and 0x0F])
        }
        return out.toString()
    }

    /**
     * 获取文件访问授权
     * @param context 上下文
     * @return 文件访问授权
     */
    @JvmStatic
    fun getFileProviderAuthority(context: Context): String {
        return context.packageName + Constants.DEFAULT_FILE_PROVIDER
    }

    /**
     * 删除文件或文件夹
     * @param file 文件或文件夹
     * @return 是否删除成功
     */
    @JvmStatic
    fun deleteFile(file: File?): Boolean {
        if (file == null || !file.exists()) {
            return true
        }
        return if (file.isFile) {
            file.delete()
        } else if (file.isDirectory) {
            file.listFiles()?.forEach { f ->
                if (f.isFile) {
                    f.delete() // 删除所有文件
                } else if (f.isDirectory) {
                    deleteFile(f) // 递归删除文件夹
                }
            }
            file.delete() // 删除目录本身
        } else {
            false
        }
    }

    /**
     * 获取APK缓存的文件夹
     * @param context 上下文
     * @return 缓存文件夹路径
     */
    @JvmStatic
    fun getApkCacheFilesDir(context: Context): String {
        val files = ContextCompat.getExternalFilesDirs(context, Constants.DEFAULT_DIR)
        val file = files.firstOrNull()
        return file?.absolutePath ?: File(context.filesDir, Constants.DEFAULT_DIR).absolutePath
    }
}
