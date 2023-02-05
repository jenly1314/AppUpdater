package com.king.app.dialog.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.king.app.dialog.BaseDialogConfig;
import com.king.app.dialog.R;

/**
 * App对话框 Fragment：封装便捷的对话框API，使用时更简单
 *
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class AppDialogFragment extends BaseDialogFragment {
    /**
     * 对话框配置
     */
    private BaseDialogConfig config;

    /**
     * 新建一个 {@link AppDialogFragment} 实例
     *
     * @param config
     * @return
     */
    public static AppDialogFragment newInstance(BaseDialogConfig config) {
        Bundle args = new Bundle();
        AppDialogFragment fragment = new AppDialogFragment();
        fragment.config = config;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getRootLayoutId() {
        if (config != null) {
            return config.getLayoutId();
        }
        return R.layout.app_dialog;
    }

    /**
     * 初始化
     *
     * @param rootView
     */
    public void init(View rootView) {
        if (config != null) {
            TextView tvDialogTitle = rootView.findViewById(config.getTitleId());
            if (tvDialogTitle != null) {
                setText(tvDialogTitle, config.getTitle());
                tvDialogTitle.setVisibility(config.isHideTitle() ? View.GONE : View.VISIBLE);
            }

            TextView tvDialogContent = rootView.findViewById(config.getContentId());
            if (tvDialogContent != null) {
                setText(tvDialogContent, config.getContent());
            }

            Button btnDialogCancel = rootView.findViewById(config.getCancelId());
            if (btnDialogCancel != null) {
                setText(btnDialogCancel, config.getCancel());
                btnDialogCancel.setOnClickListener(config.getOnClickCancel() != null ? config.getOnClickCancel() : getOnClickDismiss());
                btnDialogCancel.setVisibility(config.isHideCancel() ? View.GONE : View.VISIBLE);
            }

            View line = rootView.findViewById(config.getLineId());
            if (line != null) {
                line.setVisibility(config.isHideCancel() ? View.GONE : View.VISIBLE);
            }

            Button btnDialogConfirm = rootView.findViewById(config.getConfirmId());
            if (btnDialogConfirm != null) {
                setText(btnDialogConfirm, config.getConfirm());
                btnDialogConfirm.setOnClickListener(config.getOnClickConfirm() != null ? config.getOnClickConfirm() : getOnClickDismiss());
            }

        }
    }

    /**
     * 初始化对话框
     * @param context          上下文
     * @param dialog           对话框
     * @param gravity          对齐方式
     * @param widthRatio       宽度比例，根据屏幕宽度计算得来
     * @param x                x轴偏移量，需与 gravity 结合使用
     * @param y                y轴偏移量，需与 gravity 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin   垂直方向边距
     * @param horizontalWeight 水平方向权重
     * @param verticalWeight   垂直方向权重
     * @param animationStyleId 话框动画样式ID
     */
    @Override
    protected void initDialogWindow(Context context, Dialog dialog, int gravity, float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, float horizontalWeight, float verticalWeight, int animationStyleId) {
        if (config != null) {
            super.initDialogWindow(context, dialog, config.getGravity(), config.getWidthRatio(), config.getX(), config.getY(), config.getHorizontalMargin(), config.getVerticalMargin(), config.getHorizontalWeight(), config.getVerticalWeight(), config.getAnimationStyleId());
        } else {
            super.initDialogWindow(context, dialog, gravity, widthRatio, x, y, horizontalMargin, verticalMargin, horizontalWeight, verticalWeight, animationStyleId);
        }
    }
}