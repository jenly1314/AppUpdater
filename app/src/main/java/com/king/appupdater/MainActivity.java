package com.king.appupdater;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.king.app.dialog.AppDialog;
import com.king.app.dialog.AppDialogConfig;
import com.king.app.updater.AppUpdater;
import com.king.app.updater.UpdateConfig;
import com.king.app.updater.callback.AppUpdateCallback;
import com.king.app.updater.callback.UpdateCallback;
import com.king.app.updater.constant.Constants;
import com.king.app.updater.http.OkHttpManager;
import com.king.app.updater.util.PermissionUtils;

import java.io.File;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    private final Object mLock = new Object();

    //下载出现Failed to connect to raw.githubusercontent.com时，可以换个下载链接测试，github的raw.githubusercontent.com目前不太稳定。
//    private String mUrl = "https://raw.githubusercontent.com/jenly1314/AppUpdater/master/app/release/app-release.apk";
    private String mUrl = "https://gitlab.com/jenly1314/AppUpdater/-/raw/master/app/release/app-release.apk";

    private TextView tvProgress;
    private ProgressBar progressBar;

    private Toast toast;

    private AppUpdater mAppUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        progressBar = findViewById(R.id.progressBar);
//        progressBar.setVisibility(View.INVISIBLE);
//        progressBar.setMax(100);

        PermissionUtils.verifyReadAndWritePermissions(this,Constants.RE_CODE_STORAGE_PERMISSION);
    }

    public Context getContext(){
        return this;
    }

    public void showToast(String text){
        if(toast == null){
            synchronized (mLock){
                if(toast == null){
                    toast = Toast.makeText(getContext(),text,Toast.LENGTH_SHORT);
                }
            }
        }
        toast.setText(text);
        toast.show();
    }

    /**
     * 简单一键后台升级
     */
    private void clickBtn1(){
        mAppUpdater = new AppUpdater(getContext(),mUrl);
        mAppUpdater.start();
    }

    /**
     * 一键下载并监听
     */
    private void clickBtn2(){
        UpdateConfig config = new UpdateConfig();
        config.setUrl(mUrl);
        config.addHeader("token","xxxxxx");
        mAppUpdater = new AppUpdater(getContext(),config)
                .setHttpManager(OkHttpManager.getInstance())
                .setUpdateCallback(new UpdateCallback() {

                    @Override
                    public void onDownloading(boolean isDownloading) {
                        if(isDownloading){
                            showToast("已经在下载中,请勿重复下载。");
                        }else{
//                            showToast("开始下载…");
                            View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_progress,null);
                            tvProgress = view.findViewById(R.id.tvProgress);
                            progressBar = view.findViewById(R.id.progressBar);
                            AppDialog.INSTANCE.showDialog(getContext(),view,false);
                        }
                    }

                    @Override
                    public void onStart(String url) {
                        updateProgress(0,100);
                    }

                    @Override
                    public void onProgress(long progress, long total, boolean isChange) {
                        if(isChange){
                            updateProgress(progress,total);
                        }
                    }

                    @Override
                    public void onFinish(File file) {
                        AppDialog.INSTANCE.dismissDialog();
                        showToast("下载完成");
                    }

                    @Override
                    public void onError(Exception e) {
                        AppDialog.INSTANCE.dismissDialog();
                        showToast("下载失败");
                    }

                    @Override
                    public void onCancel() {
                        AppDialog.INSTANCE.dismissDialog();
                        showToast("取消下载");
                    }
                });
        mAppUpdater.start();
    }

    private void updateProgress(long progress, long total){
        if(tvProgress == null || progressBar == null){
            return;
        }
        if(progress > 0){
            int currProgress = (int)(progress * 1.0f / total * 100.0f);
            tvProgress.setText(getString(R.string.app_updater_progress_notification_content) + currProgress + "%");
            progressBar.setProgress(currProgress);
            Log.d(TAG,String.format("onProgress:%d/%d | %d%%",progress,total,currProgress));
        }else{
            tvProgress.setText(getString(R.string.app_updater_start_notification_content));
        }

    }

    /**
     * 系统弹框升级
     */
    private void clickBtn3(){
        new AlertDialog.Builder(this)
                .setTitle("发现新版本")
                .setMessage("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
                .setPositiveButton("升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAppUpdater = new AppUpdater.Builder()
                                .setUrl(mUrl)
                                .build(getContext())
                                .setUpdateCallback(new AppUpdateCallback() {
                                    @Override
                                    public void onStart(String url) {
                                        super.onStart(url);
                                        //模仿系统自带的横幅通知效果
                                        AppDialogConfig config = new AppDialogConfig(getContext(),R.layout.dialog_heads_up);
                                        config.setStyleId(R.style.app_dialog_heads_up)
                                                .setWidthRatio(.95f)
                                                .setGravity(Gravity.TOP);
                                        AppDialog.INSTANCE.showDialog(getContext(),config);
                                        new CountDownTimer(2000,500){

                                            @Override
                                            public void onTick(long millisUntilFinished) {

                                            }

                                            @Override
                                            public void onFinish() {
                                                AppDialog.INSTANCE.dismissDialog();
                                            }
                                        }.start();
                                    }

                                    @Override
                                    public void onProgress(long progress, long total, boolean isChange) {
                                        Log.d(TAG,String.format("onProgress:%d/%d",progress,total));
                                    }

                                    @Override
                                    public void onFinish(File file) {
                                        showToast("下载完成");
                                    }
                                });
                        mAppUpdater.start();
                    }
                }).show();
    }

    /**
     * 简单弹框升级
     */
    private void clickBtn4(){
        AppDialogConfig config = new AppDialogConfig(getContext());
        config.setTitle("简单弹框升级")
                .setConfirm("升级")
                .setContent("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
                .setOnClickConfirm(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAppUpdater = new AppUpdater(getContext(),mUrl);
                        mAppUpdater.start();
                        AppDialog.INSTANCE.dismissDialog();
                    }
                });
        AppDialog.INSTANCE.showDialog(config);
    }

    /**
     * 简单自定义弹框升级
     */
    private void clickBtn5(){
        AppDialogConfig config = new AppDialogConfig(getContext(),R.layout.dialog);
        config.setConfirm("升级")
                .setHideCancel(true)
                .setTitle("简单自定义弹框升级")
                .setContent("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
                .setOnClickConfirm(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAppUpdater = new AppUpdater.Builder()
                                .setUrl(mUrl)
                                .build(getContext());
                        mAppUpdater.start();
                        AppDialog.INSTANCE.dismissDialog();
                    }
                });
        AppDialog.INSTANCE.showDialog(config);
    }

    /**
     * 自定义弹框，优先缓存升级
     */
    private void clickBtn6(){
        View view  = LayoutInflater.from(getContext()).inflate(R.layout.dialog_custom,null);

        TextView tvTitle = view.findViewById(R.id.tvTitle);
        tvTitle.setText("自定义弹框升级");
        TextView tvContent = view.findViewById(R.id.tvContent);
        tvContent.setText("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、");

        View btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDialog.INSTANCE.dismissDialog();
            }
        });
        View btnConfirm = view.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppUpdater = new AppUpdater.Builder()
                        .setUrl(mUrl)
//                        .setApkMD5("3df5b1c1d2bbd01b4a7ddb3f2722ccca")//支持MD5校验，如果缓存APK的MD5与此MD5相同，则直接取本地缓存安装，推荐使用MD5校验的方式
                        .setVersionCode(BuildConfig.VERSION_CODE)//支持versionCode校验，设置versionCode之后，新版本versionCode相同的apk只下载一次,优先取本地缓存,推荐使用MD5校验的方式
                        .setFilename("AppUpdater.apk")
                        .setVibrate(true)
                        .build(getContext());
                mAppUpdater.setHttpManager(OkHttpManager.getInstance()).start();
                AppDialog.INSTANCE.dismissDialog();
            }
        });

        AppDialog.INSTANCE.showDialog(getContext(),view);
    }

    /**
     * 简单DialogFragment升级
     */
    private void clickBtn7(){
        AppDialogConfig config = new AppDialogConfig(getContext());
        config.setTitle("简单DialogFragment升级")
                .setConfirm("升级")
                .setContent("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
                .setOnClickConfirm(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAppUpdater = new AppUpdater.Builder()
                                .setUrl(mUrl)
                                .setVibrate(true)
                                .setSound(true)
                                .build(getContext());
                        mAppUpdater.setHttpManager(OkHttpManager.getInstance()).start();
                        AppDialog.INSTANCE.dismissDialogFragment(getSupportFragmentManager());
                    }
                });
        AppDialog.INSTANCE.showDialogFragment(getSupportFragmentManager(),config);

    }

    private void clickCancel(){
        if(mAppUpdater != null){
            mAppUpdater.stop();
        }

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn1:
                clickBtn1();
                break;
            case R.id.btn2:
                clickBtn2();
                break;
            case R.id.btn3:
                clickBtn3();
                break;
            case R.id.btn4:
                clickBtn4();
                break;
            case R.id.btn5:
                clickBtn5();
                break;
            case R.id.btn6:
                clickBtn6();
                break;
            case R.id.btn7:
                clickBtn7();
                break;
            case R.id.btnCancel:
                clickCancel();
                break;
        }
    }
}