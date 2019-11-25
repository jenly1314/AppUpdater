package com.king.appupdater;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
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
import com.king.app.updater.util.PermissionUtils;

import java.io.File;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private final Object mLock = new Object();

    private String mUrl = "https://raw.githubusercontent.com/jenly1314/AppUpdater/master/app/release/app-release.apk";

    private ProgressBar progressBar;

    private Toast toast;

    private AppUpdater mAppUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        PermissionUtils.INSTANCE.verifyReadAndWritePermissions(this,Constants.RE_CODE_STORAGE_PERMISSION);
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
                    public void onProgress(int progress, int total, boolean isChange) {
                        if(isChange){
                            progressBar.setMax(total);
                            progressBar.setProgress(progress);
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
                                .serUrl(mUrl)
                                .build(getContext())
                                .setUpdateCallback(new AppUpdateCallback() {
                                    @Override
                                    public void onProgress(int progress, int total, boolean isChange) {

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
        AppDialogConfig config = new AppDialogConfig();
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
        AppDialogConfig config = new AppDialogConfig();
        config.setLayoutId(R.layout.dialog)
                .setOk("升级")
                .setHideCancel(true)
                .setTitle("简单自定义弹框升级")
                .setContent("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
                .setOnClickOk(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAppUpdater = new AppUpdater.Builder()
                                .serUrl(mUrl)
                                .build(getContext());
                        mAppUpdater.start();
                        AppDialog.INSTANCE.dismissDialog();
                    }
                });
        AppDialog.INSTANCE.showDialog(getContext(),AppDialog.INSTANCE.createAppDialogView(getContext(),config),true);
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
                        .serUrl(mUrl)
                        .setPath(Environment.getExternalStorageDirectory() + "/.AppUpdater")
                        .setVersionCode(BuildConfig.VERSION_CODE)//设置versionCode之后，新版本相同的apk只下载一次,优先取本地缓存。
                        .setFilename("AppUpdater1.apk")
                        .setVibrate(true)
                        .build(getContext());
                mAppUpdater.start();
                AppDialog.INSTANCE.dismissDialog();
            }
        });

        AppDialog.INSTANCE.showDialog(getContext(),view);
    }

    /**
     * 简单DialogFragment升级
     */
    private void clickBtn7(){
        AppDialogConfig config = new AppDialogConfig();
        config.setTitle("简单DialogFragment升级")
                .setOk("升级")
                .setContent("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
                .setOnClickOk(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAppUpdater = new AppUpdater.Builder()
                                .serUrl(mUrl)
                                .setVibrate(true)
                                .setSound(true)
                                .build(getContext());
                        mAppUpdater.start();
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
