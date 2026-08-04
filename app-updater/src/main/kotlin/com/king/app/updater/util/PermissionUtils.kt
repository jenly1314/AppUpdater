package com.king.app.updater.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.king.logx.LogX

/**
 * 权限工具类
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
object PermissionUtils {

    /**
     * 检查权限
     *
     * @param context [Context]
     * @param permission 权限
     * @return
     */
    @JvmStatic
    fun checkPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查是否具有发送通知的权限
     *
     * @param context [Context]
     */
    @JvmStatic
    fun checkNotificationPermission(context: Context): Boolean {
        // Android 13以上版本：检测是否有发送通知的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = checkPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (granted) {
                LogX.d("POST_NOTIFICATIONS permission granted.")
            } else {
                LogX.w("POST_NOTIFICATIONS permission denied.")
            }
            return granted
        }

        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

}
