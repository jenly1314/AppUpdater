package com.king.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.king.app.dialog.fragment.AppDialogFragment;

import androidx.annotation.StyleRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * App对话框：封装便捷的对话框API，使用时更简单
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public enum AppDialog {

    INSTANCE;
    /**
     * 默认对话框宽度比例（基于屏幕的宽度）
     */
    final float DEFAULT_WIDTH_RATIO = 0.85f;
    /**
     * 对话框
     */
    private Dialog mDialog;
    /**
     * 标签
     */
    private String mTag;


    //-------------------------------------------

    /**
     * 点击监听器 - 解散对话框
     */
    View.OnClickListener mOnClickDismissDialog = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismissDialog();
        }
    };

    //-------------------------------------------

    /**
     * {@link DialogFragment#dismiss()}
     *
     * @param fragmentManager {@link FragmentManager}
     */
    public void dismissDialogFragment(FragmentManager fragmentManager) {
        dismissDialogFragment(fragmentManager, mTag);
        mTag = null;
    }

    /**
     * {@link DialogFragment#dismiss()}
     *
     * @param fragmentManager {@link FragmentManager}
     * @param tag             dialogFragment对应的标签
     */
    public void dismissDialogFragment(FragmentManager fragmentManager, String tag) {
        if (tag != null) {
            DialogFragment dialogFragment = (DialogFragment) fragmentManager.findFragmentByTag(tag);
            dismissDialogFragment(dialogFragment);
        }
    }

    /**
     * {@link DialogFragment#dismiss()}
     *
     * @param dialogFragment {@link DialogFragment}
     */
    public void dismissDialogFragment(DialogFragment dialogFragment) {
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

    //-------------------------------------------

    /**
     * 显示DialogFragment
     *
     * @param fragmentManager {@link FragmentManager}
     * @param config          {@link AppDialogConfig}
     * @return
     */
    public String showDialogFragment(FragmentManager fragmentManager, AppDialogConfig config) {
        AppDialogFragment dialogFragment = AppDialogFragment.newInstance(config);
        String tag = dialogFragment.getTag() != null ? dialogFragment.getTag() : dialogFragment.getClass().getSimpleName();
        showDialogFragment(fragmentManager, dialogFragment, tag);
        mTag = tag;
        return tag;
    }

    /**
     * 显示DialogFragment
     *
     * @param fragmentManager {@link FragmentManager}
     * @param dialogFragment  {@link DialogFragment}
     * @return
     */
    public String showDialogFragment(FragmentManager fragmentManager, DialogFragment dialogFragment) {
        String tag = dialogFragment.getTag() != null ? dialogFragment.getTag() : dialogFragment.getClass().getSimpleName();
        showDialogFragment(fragmentManager, dialogFragment, tag);
        mTag = tag;
        return tag;
    }

    /**
     * 显示DialogFragment
     *
     * @param fragmentManager {@link FragmentManager}
     * @param dialogFragment  {@link DialogFragment}
     * @param tag             dialogFragment对应的标签
     * @return
     */
    public String showDialogFragment(FragmentManager fragmentManager, DialogFragment dialogFragment, String tag) {
        dismissDialogFragment(fragmentManager);
        dialogFragment.show(fragmentManager, tag);
        mTag = tag;
        return tag;
    }

    //-------------------------------------------

    /**
     * 显示弹框
     *
     * @param config 弹框配置 {@link AppDialogConfig}
     */
    public void showDialog(AppDialogConfig config) {
        showDialog(config, true);
    }

    /**
     * 显示弹框
     *
     * @param config   弹框配置 {@link AppDialogConfig}
     * @param isCancel 是否可取消（默认为true，false则拦截back键）
     */
    public void showDialog(AppDialogConfig config, boolean isCancel) {
        showDialog(config.getContext(), config, isCancel);
    }

    /**
     * 显示弹框
     *
     * @param context 上下文
     * @param config  弹框配置 {@link AppDialogConfig}
     */
    public void showDialog(Context context, AppDialogConfig config) {
        showDialog(context, config, true);
    }

    /**
     * 显示弹框
     *
     * @param context  上下文
     * @param config   弹框配置 {@link AppDialogConfig}
     * @param isCancel 是否可取消（默认为true，false则拦截back键）
     */
    public void showDialog(Context context, AppDialogConfig config, boolean isCancel) {
        showDialog(context, config.buildAppDialogView(), config.getStyleId(), config.getGravity(),
                config.getWidthRatio(), config.x, config.y, config.horizontalMargin, config.verticalMargin,
                config.horizontalWeight, config.verticalWeight, isCancel);
    }

    /**
     * 显示弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     */
    public void showDialog(Context context, View contentView) {
        showDialog(context, contentView, DEFAULT_WIDTH_RATIO);
    }

    /**
     * 显示弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param isCancel    是否可取消（默认为true，false则拦截back键）
     */
    public void showDialog(Context context, View contentView, boolean isCancel) {
        showDialog(context, contentView, R.style.app_dialog, DEFAULT_WIDTH_RATIO, isCancel);
    }

    /**
     * 显示弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param widthRatio  宽度比例，根据屏幕宽度计算得来
     */
    public void showDialog(Context context, View contentView, float widthRatio) {
        showDialog(context, contentView, widthRatio, true);
    }

    /**
     * 显示弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param widthRatio  宽度比例，根据屏幕宽度计算得来
     * @param isCancel    是否可取消（默认为true，false则拦截back键）
     */
    public void showDialog(Context context, View contentView, float widthRatio, boolean isCancel) {
        showDialog(context, contentView, R.style.app_dialog, widthRatio, isCancel);
    }


    /**
     * 显示弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param styleId     Dialog样式
     * @param widthRatio  宽度比例，根据屏幕宽度计算得来
     */
    public void showDialog(Context context, View contentView, @StyleRes int styleId, float widthRatio) {
        showDialog(context, contentView, styleId, widthRatio, true);
    }

    /**
     * 显示弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param styleId     Dialog样式
     * @param widthRatio  宽度比例，根据屏幕宽度计算得来
     */
    public void showDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio) {
        showDialog(context, contentView, styleId, gravity, widthRatio, true);
    }

    /**
     * 显示弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param styleId     Dialog样式
     * @param widthRatio  宽度比例，根据屏幕宽度计算得来
     * @param isCancel    是否可取消（默认为true，false则拦截back键）
     */
    public void showDialog(Context context, View contentView, @StyleRes int styleId, float widthRatio, final boolean isCancel) {
        showDialog(context, contentView, styleId, Gravity.NO_GRAVITY, widthRatio, isCancel);
    }

    /**
     * 显示弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param styleId     Dialog样式
     * @param gravity     Dialog的对齐方式
     * @param widthRatio  宽度比例，根据屏幕宽度计算得来
     * @param isCancel    是否可取消（默认为true，false则拦截back键）
     */
    public void showDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, final boolean isCancel) {
        showDialog(context, contentView, styleId, gravity, widthRatio, 0, 0, isCancel);
    }


    /**
     * 显示弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param styleId     Dialog样式
     * @param gravity     Dialog的对齐方式
     * @param widthRatio  宽度比例，根据屏幕宽度计算得来
     * @param x           x轴偏移量，需与 gravity 结合使用
     * @param y           y轴偏移量，需与 gravity 结合使用
     * @param isCancel    是否可取消（默认为true，false则拦截back键）
     * @return
     */
    public void showDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, int x, int y, final boolean isCancel) {
        showDialog(context, contentView, styleId, gravity, widthRatio, x, y, 0, 0, isCancel);
    }

    /**
     * 显示弹框
     *
     * @param context          上下文
     * @param contentView      弹框内容视图
     * @param styleId          Dialog样式
     * @param gravity          Dialog的对齐方式
     * @param widthRatio       宽度比例，根据屏幕宽度计算得来
     * @param x                x轴偏移量，需与 gravity 结合使用
     * @param y                y轴偏移量，需与 gravity 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin   垂直方向边距
     * @param isCancel         是否可取消（默认为true，false则拦截back键）
     * @return
     */
    public void showDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, final boolean isCancel) {
        showDialog(context, contentView, styleId, gravity, widthRatio, x, y, horizontalMargin, verticalMargin, 0, 0, isCancel);
    }

    /**
     * 显示弹框
     *
     * @param context          上下文
     * @param contentView      弹框内容视图
     * @param styleId          Dialog样式
     * @param gravity          Dialog的对齐方式
     * @param widthRatio       宽度比例，根据屏幕宽度计算得来
     * @param x                x轴偏移量，需与 gravity 结合使用
     * @param y                y轴偏移量，需与 gravity 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin   垂直方向边距
     * @param horizontalWeight 水平方向权重
     * @param verticalWeight   垂直方向权重
     * @param isCancel         是否可取消（默认为true，false则拦截back键）
     * @return
     */
    public void showDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, float horizontalWeight, float verticalWeight, final boolean isCancel) {
        dismissDialog();
        mDialog = createDialog(context, contentView, styleId, gravity, widthRatio, x, y, horizontalMargin, verticalMargin, horizontalWeight, verticalWeight, isCancel);
        mDialog.show();
    }

    /**
     * 设置弹框窗口配置
     *
     * @param context          上下文
     * @param dialog           Dialog对话框
     * @param gravity          Dialog的对齐方式
     * @param widthRatio       宽度比例，根据屏幕宽度计算得来
     * @param x                x轴偏移量，需与 gravity 结合使用
     * @param y                y轴偏移量，需与 gravity 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin   垂直方向边距
     * @param horizontalWeight 水平方向权重
     * @param verticalWeight   垂直方向权重
     */
    private void setDialogWindow(Context context, Dialog dialog, int gravity, float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, float horizontalWeight, float verticalWeight) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = (int) (context.getResources().getDisplayMetrics().widthPixels * widthRatio);
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
     * 创建弹框
     *
     * @param config 弹框配置 {@link AppDialogConfig}
     */
    public Dialog createDialog(AppDialogConfig config) {
        return createDialog(config, true);
    }

    /**
     * 创建弹框
     *
     * @param config   弹框配置 {@link AppDialogConfig}
     * @param isCancel 是否可取消（默认为true，false则拦截back键）
     */
    public Dialog createDialog(AppDialogConfig config, boolean isCancel) {
        return createDialog(config.getContext(), config.buildAppDialogView(), config.getStyleId(), DEFAULT_WIDTH_RATIO, isCancel);
    }

    /**
     * 创建弹框
     *
     * @param context 上下文
     * @param config  弹框配置 {@link AppDialogConfig}
     */
    public Dialog createDialog(Context context, AppDialogConfig config) {
        return createDialog(context, config, true);
    }

    /**
     * 创建弹框
     *
     * @param context  上下文
     * @param config   弹框配置 {@link AppDialogConfig}
     * @param isCancel 是否可取消（默认为true，false则拦截back键）
     */
    public Dialog createDialog(Context context, AppDialogConfig config, boolean isCancel) {
        return createDialog(context, config.buildAppDialogView(), config.getStyleId(), DEFAULT_WIDTH_RATIO, isCancel);
    }

    /**
     * 创建弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     */
    public Dialog createDialog(Context context, View contentView) {
        return createDialog(context, contentView, DEFAULT_WIDTH_RATIO);
    }

    /**
     * 创建弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param isCancel    是否可取消（默认为true，false则拦截back键）
     */
    public Dialog createDialog(Context context, View contentView, boolean isCancel) {
        return createDialog(context, contentView, R.style.app_dialog, DEFAULT_WIDTH_RATIO, isCancel);
    }

    /**
     * 创建弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param widthRatio  宽度比例，根据屏幕宽度计算得来
     */
    public Dialog createDialog(Context context, View contentView, float widthRatio) {
        return createDialog(context, contentView, widthRatio, true);
    }

    /**
     * 创建弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param widthRatio  宽度比例，根据屏幕宽度计算得来
     * @param isCancel    是否可取消（默认为true，false则拦截back键）
     */
    public Dialog createDialog(Context context, View contentView, float widthRatio, boolean isCancel) {
        return createDialog(context, contentView, R.style.app_dialog, widthRatio, isCancel);
    }

    /**
     * 创建弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param styleId     Dialog样式
     * @param widthRatio  宽度比例，根据屏幕宽度计算得来
     */
    public Dialog createDialog(Context context, View contentView, @StyleRes int styleId, float widthRatio) {
        return createDialog(context, contentView, styleId, Gravity.NO_GRAVITY, widthRatio, true);
    }

    /**
     * 创建弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param styleId     Dialog样式
     * @param gravity     Dialog的对齐方式
     * @param widthRatio  宽度比例，根据屏幕宽度计算得来
     */
    public Dialog createDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio) {
        return createDialog(context, contentView, styleId, gravity, widthRatio, true);
    }


    /**
     * 创建弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param styleId     Dialog样式
     * @param widthRatio  宽度比例，根据屏幕宽度计算得来
     * @param isCancel    是否可取消（默认为true，false则拦截back键）
     */
    public Dialog createDialog(Context context, View contentView, @StyleRes int styleId, float widthRatio, final boolean isCancel) {
        return createDialog(context, contentView, styleId, Gravity.NO_GRAVITY, widthRatio, isCancel);
    }

    /**
     * 创建弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param styleId     Dialog样式
     * @param gravity     Dialog的对齐方式
     * @param widthRatio  宽度比例，根据屏幕宽度计算得来
     * @param isCancel    是否可取消（默认为true，false则拦截back键）
     */
    public Dialog createDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, final boolean isCancel) {
        return createDialog(context, contentView, styleId, gravity, widthRatio, 0, 0, isCancel);
    }

    /**
     * 创建弹框
     *
     * @param context     上下文
     * @param contentView 弹框内容视图
     * @param styleId     Dialog样式
     * @param gravity     Dialog的对齐方式
     * @param widthRatio  宽度比例，根据屏幕宽度计算得来
     * @param x           x轴偏移量，需与 gravity 结合使用
     * @param y           y轴偏移量，需与 gravity 结合使用
     * @param isCancel    是否可取消（默认为true，false则拦截back键）
     * @return
     */
    public Dialog createDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, int x, int y, final boolean isCancel) {
        return createDialog(context, contentView, styleId, gravity, widthRatio, x, y, 0, 0, isCancel);
    }

    /**
     * 创建弹框
     *
     * @param context          上下文
     * @param contentView      弹框内容视图
     * @param styleId          Dialog样式
     * @param gravity          Dialog的对齐方式
     * @param widthRatio       宽度比例，根据屏幕宽度计算得来
     * @param x                x轴偏移量，需与 gravity 结合使用
     * @param y                y轴偏移量，需与 gravity 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin   垂直方向边距
     * @param isCancel         是否可取消（默认为true，false则拦截back键）
     * @return
     */
    public Dialog createDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, final boolean isCancel) {
        return createDialog(context, contentView, styleId, gravity, widthRatio, x, y, horizontalMargin, verticalMargin, 0, 0, isCancel);
    }

    /**
     * 创建弹框
     *
     * @param context          上下文
     * @param contentView      弹框内容视图
     * @param styleId          Dialog样式
     * @param gravity          Dialog的对齐方式
     * @param widthRatio       宽度比例，根据屏幕宽度计算得来
     * @param x                x轴偏移量，需与 gravity 结合使用
     * @param y                y轴偏移量，需与 gravity 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin   垂直方向边距
     * @param horizontalWeight 水平方向权重
     * @param verticalWeight   垂直方向权重
     * @param isCancel         是否可取消（默认为true，false则拦截back键）
     * @return
     */
    public Dialog createDialog(Context context, View contentView, @StyleRes int styleId, int gravity, float widthRatio, int x, int y, float horizontalMargin, float verticalMargin, float horizontalWeight, float verticalWeight, final boolean isCancel) {
        Dialog dialog = new Dialog(context, styleId);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isCancel) {
                        dismissDialog();
                    }
                    return true;
                }
                return false;

            }
        });
        setDialogWindow(context, dialog, gravity, widthRatio, x, y, horizontalMargin, verticalMargin, horizontalWeight, verticalWeight);
        return dialog;
    }

    /**
     * 获取Dialog
     *
     * @return {@link #mDialog}
     */
    public Dialog getDialog() {
        return mDialog;
    }

    /**
     * {@link Dialog#dismiss()}
     */
    public void dismissDialog() {
        dismissDialog(mDialog);
        mDialog = null;
    }

    /**
     * {@link Dialog#dismiss()}
     *
     * @param dialog {@link Dialog}
     */
    public void dismissDialog(Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    //-------------------------------------------

}