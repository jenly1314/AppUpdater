package com.king.appupdater

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.king.app.dialog.AppDialog
import com.king.app.dialog.AppDialogConfig
import com.king.app.dialog.appDialogConfig
import com.king.app.updater.AppUpdater
import com.king.app.updater.appUpdater
import com.king.app.updater.http.OkHttpManager
import com.king.app.updater.listener.DownloadListener
import com.king.app.updater.listener.SimpleDownloadListener
import com.king.app.updater.notification.NotificationHandler
import com.king.app.updater.util.PermissionUtils
import com.king.appupdater.databinding.ActivityMainBinding
import java.io.File
import kotlinx.coroutines.launch

/**
 * AppUpdater使用示例
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class MainActivity : AppCompatActivity() {

    // 下载出现Failed to connect to raw.githubusercontent.com时，可以换个下载链接测试，github的raw.githubusercontent.com目前不太稳定。
    // private val apkUrl = "https://raw.githubusercontent.com/jenly1314/AppUpdater/master/app/release/app-release.apk"
    private val apkUrl = "https://gitlab.com/jenly1314/AppUpdater/-/raw/master/app/release/app-release.apk"

    private var tvProgress: TextView? = null
    private var progressBar: ProgressBar? = null

    private var toast: Toast? = null

    private var appUpdater: AppUpdater? = null

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    internal val context: Context get() = this

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            showToast("Notification permission granted!")
        } else {
            showToast("Notification permission denied!")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            AppUpdater.downloadState.flowWithLifecycle(lifecycle).collect {
                binding.tvStatus.text = if (it) "当前状态：下载中" else "当前状态：空闲中"
            }
        }

        // 检测通知栏权限：没有通知栏权限，只影响通知栏进度更新状态，不影响App下载
        checkNotificationPermission()
    }

    /**
     * 检测通知权限
     */
    private fun checkNotificationPermission() {
        // 适配Android 13以上版本，发送通知权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            val permission = Manifest.permission.POST_NOTIFICATIONS
            // 检测是否有 android.permission.POST_NOTIFICATIONS 权限
            if (!PermissionUtils.checkPermission(this, permission)) {
                // 如果没有发送通知权限，则申请授权
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    fun showToast(text: String) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        }
        toast?.setText(text)
        toast?.show()
    }

    /**
     * 简单一键后台升级
     */
    private fun clickBtn1() {
        appUpdater = AppUpdater(context, apkUrl)
        appUpdater?.start()
    }

    /**
     * 一键下载并监听
     */
    private fun clickBtn2() {
        appUpdater = AppUpdater.Builder(context)
            .setUrl(apkUrl)
            .addHeader("key", "value")
            .setHttpManager(OkHttpManager.getInstance())
            .setDownloadListener(object : DownloadListener {
                override fun onStart(url: String) {
                    val appDialogConfig = AppDialogConfig(context, R.layout.dialog_progress)
                    tvProgress = appDialogConfig.findView(R.id.tvProgress)
                    progressBar = appDialogConfig.findView(R.id.progressBar)
                    AppDialog.showDialog(
                        context,
                        appDialogConfig,
                        cancelable = false
                    )
                }

                override fun onProgress(progress: Int, total: Int) {
                    updateProgress(progress, total)

                }

                override fun onSuccess(file: File) {
                    AppDialog.dismissDialog()
                    showToast("下载成功")
                }

                override fun onError(cause: Throwable) {
                    AppDialog.dismissDialog()
                    showToast("下载出错")
                }

                override fun onCancel() {
                    AppDialog.dismissDialog()
                    showToast("取消下载")
                }
            }).build()
        appUpdater?.start()

    }

    private fun updateProgress(progress: Int, total: Int) {
        if (progress > 0) {
            val currProgress = progress * 100 / total
            tvProgress?.text = "正在下载… $currProgress%"
            progressBar?.progress = currProgress
        } else {
            tvProgress?.text = "正在获取下载数据…"
        }
    }

    /**
     * 系统弹框升级
     */
    private fun clickBtn3() {
        AlertDialog.Builder(this)
            .setTitle("发现新版本")
            .setMessage("1、新增某某功能；\n2、修改某某问题；\n3、优化某某BUG。")
            .setPositiveButton("升级") { _, _ ->
                appUpdater = AppUpdater.Builder(context)
                    .setUrl(apkUrl)
                    .setShowNotification(true)
                    .setShowPercentage(true)
                    .setInstallApk(true)
                    .setDownloadListener(object : SimpleDownloadListener() {
                        override fun onStart(url: String) {
                            super.onStart(url)
                            // 模仿系统Heads-Up悬浮通知效果
                            val appDialogConfig = AppDialogConfig(context, R.layout.dialog_heads_up)
                                .setStyleId(R.style.app_dialog_heads_up)
                                .setWidthRatio(.95f)
                                .setGravity(Gravity.TOP)
                            AppDialog.showDialog(appDialogConfig)
                            object : CountDownTimer(2000, 500) {
                                override fun onTick(millisUntilFinished: Long) {}

                                override fun onFinish() {
                                    AppDialog.dismissDialog()
                                }
                            }.start()
                        }

                        override fun onSuccess(file: File) {
                            showToast("下载成功")
                        }
                    }).build()
                appUpdater?.start()
            }.show()
    }

    /**
     * 简单弹框升级
     */
    private fun clickBtn4() {
        val appDialogConfig = AppDialogConfig(context)
            .setTitle("简单弹框升级")
            .setConfirm("升级")
            .setContent("1、新增某某功能；\n2、修改某某问题；\n3、优化某某BUG。")
            .setOnClickConfirm {
                appUpdater = AppUpdater(context, apkUrl)
                appUpdater?.start()
                AppDialog.dismissDialog()
            }
        AppDialog.showDialog(appDialogConfig)
    }

    /**
     * 简单自定义弹框升级
     */
    private fun clickBtn5() {
        val appDialogConfig = appDialogConfig(R.layout.dialog) {
            confirm = "升级"
            hideCancel = true
            title = "简单自定义弹框升级"
            content = "1、新增某某功能；\n2、修改某某问题；\n3、优化某某BUG。"
            onClickConfirm = View.OnClickListener {
                appUpdater = appUpdater {
                    url = apkUrl
                    httpManager = OkHttpManager.getInstance()
                    downloadListener = object : SimpleDownloadListener() {
                        override fun onStart(url: String) {
                            super.onStart(url)
                            showToast("开始下载")
                        }
                    }
                }
                appUpdater?.start()
                AppDialog.dismissDialog()
            }
        }
        AppDialog.showDialog(appDialogConfig, false)
    }

    /**
     * 自定义弹框，优先缓存升级
     */
    private fun clickBtn6() {
        // DSL
        val appDialogConfig = appDialogConfig(R.layout.dialog_custom) {
            cancelId = R.id.btnCancel
            confirmId = R.id.btnConfirm

            viewHolder.setText(R.id.tvTitle, "自定义弹框升级")
            viewHolder.setText(
                id = R.id.tvContent,
                text = "1、新增某某功能；\n2、修改某某问题；\n3、优化某某BUG。"
            )

            setOnClickConfirm {
                // DSL
                appUpdater = appUpdater {
                    url = apkUrl
                    filename = "app.apk"
                    versionCode = BuildConfig.VERSION_CODE
                    showNotification = true
                    showPercentage = true
                    installApk = true
                    httpManager = OkHttpManager.getInstance()
                }
                appUpdater?.start()

                AppDialog.dismissDialog()
            }
        }

        AppDialog.showDialog(appDialogConfig)
    }

    /**
     * 简单DialogFragment升级
     */
    private fun clickBtn7() {
        val appDialogConfig = AppDialogConfig(context)
            .setTitle("简单DialogFragment升级")
            .setConfirm("升级")
            .setContent("1、新增某某功能；\n2、修改某某问题；\n3、优化某某BUG。")
            .setOnClickConfirm {

                appUpdater = AppUpdater.Builder(context)
                    .setUrl(apkUrl)
                    .setHttpManager(OkHttpManager.getInstance())
                    .setNotificationHandler(NotificationHandler.getInstance())
                    .setDownloadListener(object : DownloadListener { // 下载监听
                        override fun onStart(url: String) {
                            // 开始下载
                        }

                        override fun onProgress(progress: Int, total: Int) {
                            // 下载进度更新
                        }

                        override fun onSuccess(file: File) {
                            // 下载成功
                        }

                        override fun onError(cause: Throwable) {
                            // 下载出错
                        }

                        override fun onCancel() {
                            // 取消下载
                        }
                    }).build()
                appUpdater?.start()
                AppDialog.dismissDialogFragment(supportFragmentManager)
            }
        AppDialog.showDialogFragment(supportFragmentManager, appDialogConfig)
    }

    override fun onDestroy() {
        // 如果有设置监听，在Activity销毁时，可清除监听
        appUpdater?.clearListener()
        AppDialog.dismissDialog()
        super.onDestroy()
    }


    private fun clickCancel() {
        appUpdater?.stop()
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.btn1 -> clickBtn1()
            R.id.btn2 -> clickBtn2()
            R.id.btn3 -> clickBtn3()
            R.id.btn4 -> clickBtn4()
            R.id.btn5 -> clickBtn5()
            R.id.btn6 -> clickBtn6()
            R.id.btn7 -> clickBtn7()
            R.id.btnCancel -> clickCancel()
        }
    }
}
