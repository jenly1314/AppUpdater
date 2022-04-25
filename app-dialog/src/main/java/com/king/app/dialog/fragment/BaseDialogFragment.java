package com.king.app.dialog.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.king.app.dialog.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

/**
 * 基础对话框 Fragment
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public abstract class BaseDialogFragment extends DialogFragment {

    protected static final float DEFAULT_WIDTH_RATIO = 0.85f;

    private View mRootView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getRootLayoutId(), container, false);
        init(mRootView);
        return mRootView;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable  Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        initDialogWindow(getContext(),dialog,Gravity.NO_GRAVITY,DEFAULT_WIDTH_RATIO,0, 0, 0, 0, 0, 0, R.style.app_dialog_scale_animation);
        return dialog;
    }

    /**
     * 初始化对话框视图
     * @param context 上下文
     * @param dialog 对话框
     * @param gravity 对齐方式
     * @param widthRatio 宽度比例，根据屏幕宽度计算得来
     * @param x x轴偏移量，需与 gravity 结合使用
     * @param y y轴偏移量，需与 gravity 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin 垂直方向边距
     * @param horizontalWeight 水平方向权重
     * @param verticalWeight 垂直方向权重
     * @param animationStyleId 话框动画样式ID
     */
    protected void initDialogWindow(Context context,Dialog dialog,int gravity,float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, float horizontalWeight, float verticalWeight, int animationStyleId){
        setDialogWindow(context, dialog, gravity, widthRatio, x, y, horizontalMargin, verticalMargin, horizontalWeight, verticalWeight, animationStyleId);
    }

    /**
     * 设置弹框窗口配置
     * @param context
     * @param dialog
     * @param gravity Dialog的对齐方式
     * @param widthRatio 宽度比例，根据屏幕宽度计算得来
     * @param x x轴偏移量，需与 gravity 结合使用
     * @param y y轴偏移量，需与 gravity 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin 垂直方向边距
     * @param horizontalWeight 水平方向权重
     * @param verticalWeight 垂直方向权重
     * @param animationStyleId 动画样式
     */
    private void setDialogWindow(Context context,Dialog dialog,int gravity,float widthRatio,int x, int y, float horizontalMargin, float verticalMargin, float horizontalWeight, float verticalWeight, int animationStyleId){
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.windowAnimations = animationStyleId;
        lp.width = (int)(context.getResources().getDisplayMetrics().widthPixels * widthRatio);
        lp.gravity = gravity;
        lp.x = x;
        lp.y = y;
        lp.horizontalMargin = horizontalMargin;
        lp.verticalMargin = verticalMargin;
        lp.horizontalWeight = horizontalWeight;
        lp.verticalWeight = verticalWeight;
        window.setAttributes(lp);
    }

    /**
     * 根视图
     * @return
     */
    protected View getRootView(){
        return mRootView;
    }

    protected void setText(TextView tv, CharSequence text){
        if(text != null){
            tv.setText(text);
        }
    }

    protected View.OnClickListener getOnClickDismiss(){
        return mOnClickDismissDialog;
    }

    private View.OnClickListener mOnClickDismissDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    /**
     * 获取根布局ID
     * @return 根布局ID
     */
    public abstract int getRootLayoutId();

    /**
     * 初始化
     * @param rootView
     */
    public abstract void init(View rootView);

}