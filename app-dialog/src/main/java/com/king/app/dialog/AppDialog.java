package com.king.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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

    public View createAppDialogView(Context context,AppDialogConfig config){
        View view = null;
        if(config!=null){
            view = LayoutInflater.from(context).inflate(config.getLayoutId(),null);
            TextView tvDialogTitle = view.findViewById(config.getTitleId());
            setText(tvDialogTitle,config.getTitle());

            TextView tvDialogContent = view.findViewById(config.getContentId());
            setText(tvDialogContent,config.getContent());

            Button btnDialogCancel = view.findViewById(config.getCancelId());
            setText(btnDialogCancel,config.getCancel());
            btnDialogCancel.setOnClickListener(config.getOnClickCancel() != null ? config.getOnClickCancel() : mOnClickDismissDialog);
            btnDialogCancel.setVisibility(config.isHideCancel() ? View.GONE : View.VISIBLE);

            try{
                //不强制要求要有横线
                View line = view.findViewById(R.id.line);
                line.setVisibility(config.isHideCancel() ? View.GONE : View.VISIBLE);
            }catch (Exception e){

            }

            Button btnDialogOK = view.findViewById(config.getOkId());
            setText(btnDialogOK,config.getOk());
            btnDialogOK.setOnClickListener(config.getOnClickOk() != null ? config.getOnClickOk() : mOnClickDismissDialog);
        }

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

    public String showDialogFragment(FragmentManager fragmentManager,AppDialogConfig config){
        AppDialogFragment dialogFragment = AppDialogFragment.newInstance(config);
        String tag = dialogFragment.getTag() !=null ? dialogFragment.getTag() : dialogFragment.getClass().getSimpleName();
        showDialogFragment(fragmentManager,dialogFragment,tag);
        mTag = tag;
        return tag;
    }

    public String showDialogFragment(FragmentManager fragmentManager,DialogFragment dialogFragment){
        String tag = dialogFragment.getTag() !=null ? dialogFragment.getTag() : dialogFragment.getClass().getSimpleName();
        showDialogFragment(fragmentManager,dialogFragment,tag);
        mTag = tag;
        return tag;
    }

    public String showDialogFragment(FragmentManager fragmentManager,DialogFragment dialogFragment, String tag) {
        dialogFragment.show(fragmentManager,tag);
        mTag = tag;
        return tag;
    }

    //-------------------------------------------

    public void showDialog(Context context,AppDialogConfig config){
        showDialog(context,createAppDialogView(context,config));
    }

    public void showDialog(Context context,View contentView){
        showDialog(context,contentView,DEFAULT_WIDTH_RATIO);
    }

    public void showDialog(Context context,View contentView,boolean isCancel){
        showDialog(context,contentView,R.style.app_dialog,DEFAULT_WIDTH_RATIO,isCancel);
    }

    public void showDialog(Context context,View contentView,float widthRatio){
        showDialog(context,contentView,widthRatio,true);
    }

    public void showDialog(Context context,View contentView,float widthRatio,boolean isCancel){
        showDialog(context,contentView,R.style.app_dialog,widthRatio,isCancel);
    }

    public void showDialog(Context context, View contentView, @StyleRes int resId, float widthRatio){
        showDialog(context,contentView,resId,widthRatio,true);
    }

    /**
     *
     * @param context
     * @param contentView
     * @param resId Dialog样式
     * @param widthRatio
     * @param isCancel 是否可取消（默认为true，false则拦截back键）
     */
    public void showDialog(Context context, View contentView, @StyleRes int resId, float widthRatio,final boolean isCancel){
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
