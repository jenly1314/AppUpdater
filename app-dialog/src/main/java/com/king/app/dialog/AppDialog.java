package com.king.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.king.app.dialog.fragment.AppDialogFragment;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public enum AppDialog {

    INSTANCE;

    private final float DEFAULT_WIDTH_RATIO = 0.85f;

    private Dialog mDialog;

    private String mTag;

    //-------------------------------------------

    /**
     * 通过{@link AppDialogConfig} 创建一个视图
     * @param context
     * @param config 弹框配置 {@link AppDialogConfig}
     * @return
     */
    public View createAppDialogView(@NonNull Context context,@NonNull AppDialogConfig config){
        View view = config.getView(context);
        TextView tvDialogTitle = view.findViewById(config.getTitleId());
        setText(tvDialogTitle,config.getTitle());
        tvDialogTitle.setVisibility(config.isHideTitle() ? View.GONE : View.VISIBLE);

        TextView tvDialogContent = view.findViewById(config.getContentId());
        setText(tvDialogContent,config.getContent());

        Button btnDialogCancel = view.findViewById(config.getCancelId());
        setText(btnDialogCancel,config.getCancel());
        btnDialogCancel.setOnClickListener(config.getOnClickCancel() != null ? config.getOnClickCancel() : mOnClickDismissDialog);
        btnDialogCancel.setVisibility(config.isHideCancel() ? View.GONE : View.VISIBLE);

        try{
            //不强制要求要有中间的线
            View line = view.findViewById(R.id.line);
            line.setVisibility(config.isHideCancel() ? View.GONE : View.VISIBLE);
        }catch (Exception e){

        }

        Button btnDialogOK = view.findViewById(config.getOkId());
        setText(btnDialogOK,config.getOk());
        btnDialogOK.setOnClickListener(config.getOnClickOk() != null ? config.getOnClickOk() : mOnClickDismissDialog);

        return view;
    }

    //-------------------------------------------

    private View.OnClickListener mOnClickDismissDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismissDialog();
        }
    };

    private void setText(TextView tv,CharSequence text){
        if(!TextUtils.isEmpty(text)){
            tv.setText(text);
        }
    }

    //-------------------------------------------

    public void dismissDialogFragment(FragmentManager fragmentManager){
        dismissDialogFragment(fragmentManager,mTag);
    }

    public void dismissDialogFragment(FragmentManager fragmentManager,String tag){
        if(tag!=null){
            DialogFragment dialogFragment = (DialogFragment) fragmentManager.findFragmentByTag(tag);
            dismissDialogFragment(dialogFragment);
        }
    }

    public void dismissDialogFragment(DialogFragment dialogFragment){
        if(dialogFragment!=null){
            dialogFragment.dismiss();
        }
    }

    //-------------------------------------------

    /**
     * 显示DialogFragment
     * @param fragmentManager
     * @return
     */
    public String showDialogFragment(FragmentManager fragmentManager,AppDialogConfig config){
        AppDialogFragment dialogFragment = AppDialogFragment.newInstance(config);
        String tag = dialogFragment.getTag() !=null ? dialogFragment.getTag() : dialogFragment.getClass().getSimpleName();
        showDialogFragment(fragmentManager,dialogFragment,tag);
        mTag = tag;
        return tag;
    }

    /**
     * 显示DialogFragment
     * @param fragmentManager
     * @param dialogFragment
     * @return
     */
    public String showDialogFragment(FragmentManager fragmentManager,DialogFragment dialogFragment){
        String tag = dialogFragment.getTag() !=null ? dialogFragment.getTag() : dialogFragment.getClass().getSimpleName();
        showDialogFragment(fragmentManager,dialogFragment,tag);
        mTag = tag;
        return tag;
    }

    /**
     * 显示DialogFragment
     * @param fragmentManager
     * @param dialogFragment
     * @param tag
     * @return
     */
    public String showDialogFragment(FragmentManager fragmentManager,DialogFragment dialogFragment, String tag) {
        dismissDialogFragment(fragmentManager);
        dialogFragment.show(fragmentManager,tag);
        mTag = tag;
        return tag;
    }

    //-------------------------------------------

    /**
     * 显示弹框
     * @param context
     * @param config 弹框配置 {@link AppDialogConfig}
     */
    public void showDialog(Context context,AppDialogConfig config){
        showDialog(context,config,true);
    }

    /**
     * 显示弹框
     * @param context
     * @param config 弹框配置 {@link AppDialogConfig}
     * @param isCancel 是否可取消（默认为true，false则拦截back键）
     */
    public void showDialog(Context context,AppDialogConfig config,boolean isCancel){
        showDialog(context,createAppDialogView(context,config),R.style.app_dialog,DEFAULT_WIDTH_RATIO,isCancel);
    }

    /**
     * 显示弹框
     * @param context
     * @param contentView 弹框内容视图
     */
    public void showDialog(Context context,View contentView){
        showDialog(context,contentView,DEFAULT_WIDTH_RATIO);
    }

    /**
     * 显示弹框
     * @param context
     * @param contentView 弹框内容视图
     * @param isCancel 是否可取消（默认为true，false则拦截back键）
     */
    public void showDialog(Context context,View contentView,boolean isCancel){
        showDialog(context,contentView,R.style.app_dialog,DEFAULT_WIDTH_RATIO,isCancel);
    }

    /**
     * 显示弹框
     * @param context
     * @param contentView 弹框内容视图
     * @param widthRatio 宽度比例，根据屏幕宽度计算得来
     */
    public void showDialog(Context context,View contentView,float widthRatio){
        showDialog(context,contentView,widthRatio,true);
    }

    /**
     * 显示弹框
     * @param context
     * @param contentView 弹框内容视图
     * @param widthRatio 宽度比例，根据屏幕宽度计算得来
     * @param isCancel 是否可取消（默认为true，false则拦截back键）
     */
    public void showDialog(Context context,View contentView,float widthRatio,boolean isCancel){
        showDialog(context,contentView,R.style.app_dialog,widthRatio,isCancel);
    }

    /**
     * 显示弹框
     * @param context
     * @param contentView 弹框内容视图
     * @param resId Dialog样式
     * @param widthRatio 宽度比例，根据屏幕宽度计算得来
     */
    public void showDialog(Context context, View contentView, @StyleRes int resId, float widthRatio){
        showDialog(context,contentView,resId,widthRatio,true);
    }

    /**
     * 显示弹框
     * @param context
     * @param contentView 弹框内容视图
     * @param resId Dialog样式
     * @param widthRatio 宽度比例，根据屏幕宽度计算得来
     * @param isCancel 是否可取消（默认为true，false则拦截back键）
     */
    public void showDialog(Context context, View contentView, @StyleRes int resId, float widthRatio,final boolean isCancel){
        dismissDialog();
        mDialog = new Dialog(context,resId);
        mDialog.setContentView(contentView);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && isCancel){
                    dismissDialog();
                }
                return true;

            }
        });
        setDialogWindow(context,mDialog,widthRatio);
        mDialog.show();
    }

    private void setDialogWindow(Context context,Dialog dialog,float widthRatio){
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int)(context.getResources().getDisplayMetrics().widthPixels * widthRatio);
        window.setAttributes(lp);
    }

    public void dismissDialog(){
        dismissDialog(mDialog);
    }

    private void dismissDialog(Dialog dialog){
        if(dialog!=null){
            dialog.dismiss();
        }
    }

    //-------------------------------------------

}
