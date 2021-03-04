package com.king.appupdater;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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

    private final Object mLock = new Object();

    //下载出现Failed to connect to raw.githubusercontent.com时，可以换个下载链接测试，github的raw.githubusercontent.com目前不太稳定。
//    private String mUrl = "https://raw.githubusercontent.com/jenly1314/AppUpdater/master/app/release/app-release.apk";
    private String mUrl = "https://gitlab.com/jenly1314/AppUpdater/-/raw/master/app/release/app-release.apk";

    private ProgressBar progressBar;

    private Toast toast;

    private AppUpdater mAppUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setMax(100);

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
                        }
                    }

                    @Override
                    public void onStart(String url) {
                        progressBar.setProgress(0);
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onProgress(long progress, long total, boolean isChange) {
                        if(isChange){
                            int currProgress = Math.round(progress * 1.0f / total * 100.0f);
                            progressBar.setProgress(currProgress);
                        }
                    }

                    @Override
                    public void onFinish(File file) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancel() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
        mAppUpdater.start();
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
                                    public void onProgress(long progress, long total, boolean isChange) {

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
                .setOk("升级")
                .setContent("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
                .setOnClickOk(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAppUpdater = new AppUpdater(getContext(),mUrl);
                        mAppUpdater.start();
                        AppDialog.INSTANCE.dismissDialog();
                    }
                });
        AppDialog.INSTANCE.showDialog(getContext(),config);
    }

    /**
     * 简单自定义弹框升级
     */
    private void clickBtn5(){
        AppDialogConfig config = new AppDialogConfig(getContext(),R.layout.dialog);
        config.setOk("升级")
                .setHideCancel(true)
                .setTitle("简单自定义弹框升级")
                .setContent("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
                .setOnClickOk(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAppUpdater = new AppUpdater.Builder()
                                .setUrl(mUrl)
                                .build(getContext());
                        mAppUpdater.start();
                        AppDialog.INSTANCE.dismissDialog();
                    }
                });
        //强制升级，拦截返回
        AppDialog.INSTANCE.showDialog(getContext(),AppDialog.INSTANCE.createAppDialogView(getContext(),config),false);
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

        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDialog.INSTANCE.dismissDialog();
            }
        });
        Button btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppUpdater = new AppUpdater.Builder()
                        .setUrl(mUrl)
//                        .setPath(Environment.getExternalStorageDirectory() + "/.AppUpdater")//如果适配Android Q，则Environment.getExternalStorageDirectory()将废弃
//                        .setPath(getExternalFilesDir(Constants.DEFAULT_DIR).getAbsolutePath())//自定义路径，推荐使用默认
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
                .setOk("升级")
                .setContent("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
                .setOnClickOk(new View.OnClickListener() {
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