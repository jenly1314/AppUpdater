package com.king.app.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

/**
 * 基础对话框配置
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class BaseDialogConfig {

    /**
     * 布局ID
     */
    @LayoutRes
    int layoutId;
    /**
     * 标题视图ID
     */
    @IdRes
    int titleId = R.id.tvDialogTitle;
    /**
     * 内容视图ID
     */
    @IdRes
    int contentId = R.id.tvDialogContent;
    /**
     * 取消视图ID（左边按钮）
     */
    @IdRes
    int cancelId = R.id.btnDialogCancel;
    /**
     * 确定视图ID（右边按钮）
     */
    @IdRes
    int confirmId = R.id.btnDialogConfirm;
    /**
     * 按钮中间分割线ID
     */
    @IdRes
    int lineId = R.id.line;

    /**
     * 样式ID
     */
    @StyleRes
    int styleId = R.style.app_dialog;

    /**
     * 对话框动画样式ID
     */
    @StyleRes
    int animationStyleId = R.style.app_dialog_scale_animation;

    /**
     * 标题文本
     */
    CharSequence title;
    /**
     * 内容文本
     */
    CharSequence content;
    /**
     * 取消按钮文本
     */
    CharSequence cancel;
    /**
     * 确定按钮文本
     */
    CharSequence confirm;
    /**
     * 是否隐藏取消按钮，如果隐藏取消则底部只显示一个按钮
     */
    boolean isHideCancel;
    /**
     * 是否隐藏标题
     */
    boolean isHideTitle;

    /**
     * 宽度比例，根据屏幕宽度计算得来
     */
    float widthRatio = AppDialog.INSTANCE.DEFAULT_WIDTH_RATIO;

    /**
     * Dialog对齐方式 {@link WindowManager.LayoutParams#gravity}
     */
    int gravity = Gravity.NO_GRAVITY;

    /**
     * {@link WindowManager.LayoutParams#x}
     */
    int x;
    /**
     * {@link WindowManager.LayoutParams#y}
     */
    int y;
    /**
     * {@link WindowManager.LayoutParams#verticalMargin}
     */
    float verticalMargin;
    /**
     * {@link WindowManager.LayoutParams#horizontalMargin}
     */
    float horizontalMargin;
    /**
     * {@link WindowManager.LayoutParams#horizontalWeight}
     */
    float horizontalWeight;
    /**
     * {@link WindowManager.LayoutParams#verticalWeight}
     */
    float verticalWeight;
    /**
     * 点击“取消”按钮监听
     */
    View.OnClickListener onClickCancel;
    /**
     * 点击“确定”按钮监听
     */
    View.OnClickListener onClickConfirm;

    /**
     * 构造
     */
    public BaseDialogConfig() {
        this(R.layout.app_dialog);
    }

    /**
     * 构造
     *
     * @param layoutId 布局ID
     */
    public BaseDialogConfig(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
    }

    /**
     * 布局ID
     *
     * @return 布局ID
     */
    @LayoutRes
    public int getLayoutId() {
        return layoutId;
    }

    /**
     * 此方法即将废弃，请通过构造{@link #BaseDialogConfig(int)}来初始化
     *
     * @param layoutId 布局ID
     * @return {@link BaseDialogConfig}
     * @deprecated 即将废弃，下一个版本可能会移除此方法
     */
    @Deprecated
    public BaseDialogConfig setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    /**
     * 标题视图ID
     *
     * @return 视图ID
     */
    @IdRes
    public int getTitleId() {
        return titleId;
    }

    /**
     * 设置标题视图ID
     *
     * @param titleId 视图ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setTitleId(@IdRes int titleId) {
        this.titleId = titleId;
        return this;
    }

    /**
     * 样式ID
     *
     * @return 样式ID
     */
    @StyleRes
    public int getStyleId() {
        return styleId;
    }

    /**
     * 设置Dialog样式ID(仅对Dialog有效，如果使用的是DialogFragment，请使用{@link #setAnimationStyleId(int)})
     *
     * @param styleId
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setStyleId(@StyleRes int styleId) {
        this.styleId = styleId;
        return this;
    }

    /**
     * 对话框动画样式ID
     *
     * @return
     */
    @StyleRes
    public int getAnimationStyleId() {
        return animationStyleId;
    }

    /**
     * 对话框动画样式ID (仅对DialogFragment有效，如果使用的是Dialog，请使用{@link #setStyleId(int)})
     *
     * @param animationStyleId
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setAnimationStyleId(@StyleRes int animationStyleId) {
        this.animationStyleId = animationStyleId;
        return this;
    }

    /**
     * 内容视图ID
     *
     * @return 视图ID
     */
    @IdRes
    public int getContentId() {
        return contentId;
    }

    /**
     * 设置内容视图ID
     *
     * @param contentId 内容视图ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setContentId(@IdRes int contentId) {
        this.contentId = contentId;
        return this;
    }

    /**
     * 取消按钮视图ID
     *
     * @return 视图ID
     */
    @IdRes
    public int getCancelId() {
        return cancelId;
    }

    /**
     * 设置取消按钮视图ID
     *
     * @param cancelId 取消按钮视图ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setCancelId(@IdRes int cancelId) {
        this.cancelId = cancelId;
        return this;
    }

    /**
     * 获取确定按钮视图ID
     *
     * @return 确定按钮视图ID
     * @Deprecated 请使用 {@link #getConfirmId()}来代替，后续版本可能会移除此方法
     */
    @Deprecated
    @IdRes
    public int getOkId() {
        return getConfirmId();
    }

    /**
     * 设置确定按钮视图ID
     *
     * @param okId 确定按钮视图ID
     * @return {@link BaseDialogConfig}
     * @Deprecated 请使用 {@link #setConfirmId(int)}来代替，后续版本可能移除此方法
     */
    @Deprecated
    public BaseDialogConfig setOkId(@IdRes int okId) {
        return setConfirmId(okId);
    }

    /**
     * 获取确定按钮视图ID
     *
     * @return 视图ID
     */
    @IdRes
    public int getConfirmId() {
        return confirmId;
    }

    /**
     * 设置确定按钮视图ID
     *
     * @param confirmId 确定按钮视图ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setConfirmId(@IdRes int confirmId) {
        this.confirmId = confirmId;
        return this;
    }

    /**
     * 分割线视图ID
     *
     * @return 视图ID
     */
    @IdRes
    public int getLineId() {
        return lineId;
    }

    /**
     * 设置分割线视图ID
     *
     * @param lineId 分割线视图ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setLineId(@IdRes int lineId) {
        this.lineId = lineId;
        return this;
    }

    /**
     * 标题
     *
     * @return 标题
     */
    public CharSequence getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    /**
     * 设置标题
     *
     * @param context 上下文
     * @param resId   标题资源ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setTitle(@NonNull Context context, @StringRes int resId) {
        this.title = context.getString(resId);
        return this;
    }

    /**
     * 文本内容
     *
     * @return 文本内容
     */
    public CharSequence getContent() {
        return content;
    }

    /**
     * 设置文本内容
     *
     * @param content 文本内容
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setContent(CharSequence content) {
        this.content = content;
        return this;
    }

    /**
     * 取消按钮文本内容
     *
     * @return 取消按钮文本内容
     */
    public CharSequence getCancel() {
        return cancel;
    }

    /**
     * 设置取消按钮文本内容
     *
     * @param cancel 取消按钮文本内容
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setCancel(CharSequence cancel) {
        this.cancel = cancel;
        return this;
    }

    /**
     * 设置取消按钮文本内容
     *
     * @param context 上下文
     * @param resId   取消按钮文本内容资源ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setCancel(@NonNull Context context, @StringRes int resId) {
        this.cancel = context.getString(resId);
        return this;
    }

    /**
     * 获取确定按钮文本内容
     *
     * @return 确定按钮文本内容
     * @deprecated 请使用 {@link #getConfirm()} 来代替，后续版本可能会移除此方法
     */
    public CharSequence getOk() {
        return getConfirm();
    }

    /**
     * 设置确定按钮文本内容
     *
     * @param ok 确定按钮文本内容
     * @return {@link BaseDialogConfig}
     * @deprecated 请使用 {@link #setConfirm(CharSequence)} 来代替，后续版本可能会移除此方法
     */
    public BaseDialogConfig setOk(CharSequence ok) {
        return setConfirm(ok);
    }

    /**
     * 设置确定按钮文本内容
     *
     * @param context 上下文
     * @param resId   确定按钮文本内容资源ID
     * @return {@link BaseDialogConfig}
     * @deprecated 请使用 {@link #setConfirm(Context, int)}来代替，后续版本可能会移除此方法
     */
    public BaseDialogConfig setOk(@NonNull Context context, @StringRes int resId) {
        return setConfirm(context, resId);
    }

    /**
     * 确定按钮文本内容
     *
     * @return 确定按钮文本内容
     */
    public CharSequence getConfirm() {
        return confirm;
    }

    /**
     * 设置确定按钮文本内容
     *
     * @param confirm 确定按钮文本内容
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setConfirm(CharSequence confirm) {
        this.confirm = confirm;
        return this;
    }

    /**
     * 设置确定按钮文本内容
     *
     * @param context 上下文
     * @param resId   确定按钮文本内容资源ID
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setConfirm(@NonNull Context context, @StringRes int resId) {
        this.confirm = context.getString(resId);
        return this;
    }

    /**
     * 是否隐藏取消按钮
     *
     * @return {@link #isHideCancel}
     */
    public boolean isHideCancel() {
        return isHideCancel;
    }

    /**
     * 设置是否隐藏取消按钮
     *
     * @param hideCancel 是否隐藏取消按钮
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setHideCancel(boolean hideCancel) {
        isHideCancel = hideCancel;
        return this;
    }

    /**
     * 是否隐藏标题
     *
     * @return {@link #isHideTitle}
     */
    public boolean isHideTitle() {
        return isHideTitle;
    }

    /**
     * 设置是否隐藏标题
     *
     * @param hideTitle 是否隐藏标题
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setHideTitle(boolean hideTitle) {
        isHideTitle = hideTitle;
        return this;
    }

    /**
     * Dialog的宽度比例，根据屏幕宽度计算得来
     *
     * @return {@link #widthRatio}
     */
    public float getWidthRatio() {
        return widthRatio;
    }

    /**
     * 设置Dialog的宽度比例，根据屏幕宽度计算得来
     *
     * @param widthRatio Dialog的宽度比例；默认值：{@link AppDialog#DEFAULT_WIDTH_RATIO}
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setWidthRatio(float widthRatio) {
        this.widthRatio = widthRatio;
        return this;
    }

    /**
     * Dialog的对齐方式  {@link WindowManager.LayoutParams#gravity}
     *
     * @return Dialog的对齐方式
     */
    public int getGravity() {
        return gravity;
    }

    /**
     * 设置Dialog的对齐方式  {@link WindowManager.LayoutParams#gravity}
     *
     * @param gravity Dialog的对齐方式
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    /**
     * “取消”按钮点击监听，不设置默认点击关闭弹框
     *
     * @return “取消”按钮点击监听
     */
    public View.OnClickListener getOnClickCancel() {
        return onClickCancel;
    }

    /**
     * 设置“取消”按钮点击监听，不设置默认点击关闭弹框
     *
     * @param onClickCancel “取消”按钮点击监听
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setOnClickCancel(View.OnClickListener onClickCancel) {
        this.onClickCancel = onClickCancel;
        return this;
    }

    /**
     * “确定”按钮点击监听，不设置默认点击关闭弹框
     *
     * @return “确定”按钮点击监听
     */
    public View.OnClickListener getOnClickConfirm() {
        return onClickConfirm;
    }

    /**
     * 设置“确定”按钮点击监听，不设置默认点击关闭弹框
     *
     * @param onClickConfirm “确定”按钮点击监听
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setOnClickConfirm(View.OnClickListener onClickConfirm) {
        this.onClickConfirm = onClickConfirm;
        return this;
    }


    /**
     * 获取“确定”按钮点击监听，不设置默认点击关闭弹框
     *
     * @return “确定”按钮点击监听
     * @deprecated 请使用 {@link #getOnClickConfirm()}来代替，后续版本可能会移除此方法
     */
    public View.OnClickListener getOnClickOk() {
        return getOnClickConfirm();
    }

    /**
     * 设置“确定”按钮点击监听，不设置默认点击关闭弹框
     *
     * @param onClickOk “确定”按钮点击监听
     * @return {@link BaseDialogConfig}
     * @deprecated 请使用 {@link #setOnClickConfirm(View.OnClickListener)}来代替，后续版本可能会移除此方法
     */
    public BaseDialogConfig setOnClickOk(View.OnClickListener onClickOk) {
        return setOnClickConfirm(onClickOk);
    }

    /**
     * {@link WindowManager.LayoutParams#x}
     *
     * @return {@link #x}
     */
    public int getX() {
        return x;
    }

    /**
     * {@link WindowManager.LayoutParams#x}
     *
     * @param x x轴坐标
     */
    public BaseDialogConfig setX(int x) {
        this.x = x;
        return this;
    }

    /**
     * {@link WindowManager.LayoutParams#y}
     *
     * @return {@link #y}
     */
    public int getY() {
        return y;
    }

    /**
     * {@link WindowManager.LayoutParams#y}
     *
     * @param y y轴坐标
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setY(int y) {
        this.y = y;
        return this;
    }

    /**
     * {@link WindowManager.LayoutParams#verticalMargin}
     *
     * @return {@link #verticalMargin}
     */
    public float getVerticalMargin() {
        return verticalMargin;
    }

    /**
     * {@link WindowManager.LayoutParams#verticalMargin}
     *
     * @param verticalMargin 垂直边距
     */
    public void setVerticalMargin(float verticalMargin) {
        this.verticalMargin = verticalMargin;
    }

    /**
     * {@link WindowManager.LayoutParams#horizontalMargin}
     *
     * @return {@link #horizontalMargin}
     */
    public float getHorizontalMargin() {
        return horizontalMargin;
    }

    /**
     * {@link WindowManager.LayoutParams#horizontalMargin}
     *
     * @param horizontalMargin 水平边距
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setHorizontalMargin(float horizontalMargin) {
        this.horizontalMargin = horizontalMargin;
        return this;
    }

    /**
     * {@link WindowManager.LayoutParams#horizontalWeight}
     *
     * @return {@link #horizontalWeight}
     */
    public float getHorizontalWeight() {
        return horizontalWeight;
    }

    /**
     * {@link WindowManager.LayoutParams#horizontalWeight}
     *
     * @param horizontalWeight 水平方向权重
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setHorizontalWeight(float horizontalWeight) {
        this.horizontalWeight = horizontalWeight;
        return this;
    }

    /**
     * {@link WindowManager.LayoutParams#verticalWeight}
     *
     * @return {@link #verticalWeight}
     */
    public float getVerticalWeight() {
        return verticalWeight;
    }

    /**
     * {@link WindowManager.LayoutParams#verticalWeight}
     *
     * @param verticalWeight 垂直方向权重
     * @return {@link BaseDialogConfig}
     */
    public BaseDialogConfig setVerticalWeight(float verticalWeight) {
        this.verticalWeight = verticalWeight;
        return this;
    }
}