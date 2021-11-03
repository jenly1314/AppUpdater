package com.king.app.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class BaseDialogConfig {

    /**
     * 布局ID
     */
    @LayoutRes int layoutId;
    /**
     * 标题视图ID
     */
    @IdRes int titleId = R.id.tvDialogTitle;
    /**
     * 内容视图ID
     */
    @IdRes int contentId = R.id.tvDialogContent;
    /**
     * 取消视图ID（左边按钮）
     */
    @IdRes int cancelId = R.id.btnDialogCancel;
    /**
     * 确定视图ID（右边按钮）
     */
    @IdRes int confirmId = R.id.btnDialogConfirm;
    /**
     * 按钮中间分割线ID
     */
    @IdRes int lineId = R.id.line;

    /**
     * 样式ID
     */
    @StyleRes int styleId = R.style.app_dialog;

    /**
     * 对话框动画样式ID
     */
    @StyleRes int animationStyleId = R.style.app_dialog_scale_animation;

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
     * Dialog对齐方式
     */
    int gravity = Gravity.NO_GRAVITY;

    View.OnClickListener onClickCancel;

    View.OnClickListener onClickConfirm;

    public BaseDialogConfig(){
        this(R.layout.app_dialog);
    }

    /**
     * 构造
     * @param layoutId 布局ID
     */
    public BaseDialogConfig(@LayoutRes int layoutId){
        this.layoutId = layoutId;
    }


    public @LayoutRes int getLayoutId() {
        return layoutId;
    }

    /**
     * 此方法即将废弃，请通过构造{@link #BaseDialogConfig(int)}来初始化
     * @param layoutId
     * @return
     * @deprecated 即将废弃，下一个版本可能会移除此方法
     */
    @Deprecated
    public BaseDialogConfig setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public int getTitleId() {
        return titleId;
    }

    /**
     * 设置标题布局ID
     * @param titleId
     * @return
     */
    public BaseDialogConfig setTitleId(@IdRes int titleId) {
        this.titleId = titleId;
        return this;
    }

    public int getStyleId() {
        return styleId;
    }

    /**
     * 设置Dialog样式ID(仅对Dialog有效，如果使用的是DialogFragment，请使用{@link #setAnimationStyleId(int)})
     * @param styleId
     * @return
     */
    public BaseDialogConfig setStyleId(@StyleRes int styleId) {
        this.styleId = styleId;
        return this;
    }

    public int getAnimationStyleId(){
        return animationStyleId;
    }

    /**
     * 对话框动画样式ID (仅对DialogFragment有效，如果使用的是Dialog，请使用{@link #setStyleId(int)})
     * @param animationStyleId
     * @return
     */
    public BaseDialogConfig setAnimationStyleId(@StyleRes int animationStyleId) {
        this.animationStyleId = animationStyleId;
        return this;
    }

    public @IdRes int getContentId() {
        return contentId;
    }

    /**
     * 设置内容布局ID
     * @param contentId
     * @return
     */
    public BaseDialogConfig setContentId(@IdRes int contentId) {
        this.contentId = contentId;
        return this;
    }

    public @IdRes int getCancelId() {
        return cancelId;
    }

    /**
     * 设置取消按钮布局ID
     * @param cancelId
     * @return
     */
    public BaseDialogConfig setCancelId(@IdRes int cancelId) {
        this.cancelId = cancelId;
        return this;
    }

    /**
     * 获取确定按钮布局ID
     * @return
     * @Deprecated 请使用 {@link #getConfirmId()}来代替，后续版本可能会移除此方法
     */
    @Deprecated
    public @IdRes int getOkId() {
        return getConfirmId();
    }

    /**
     * 设置确定按钮布局ID
     * @param okId
     * @return
     * @Deprecated 请使用 {@link #setConfirmId(int)}来代替，后续版本可能移除此方法
     */
    @Deprecated
    public BaseDialogConfig setOkId(@IdRes int okId) {
        return setConfirmId(okId);
    }

    /**
     * 获取确定按钮布局ID
     * @return
     */
    public @IdRes int getConfirmId() {
        return confirmId;
    }

    /**
     * 设置确定按钮布局ID
     * @param confirmId
     * @return
     */
    public BaseDialogConfig setConfirmId(@IdRes int confirmId) {
        this.confirmId = confirmId;
        return this;
    }

    public @IdRes int getLineId() {
        return lineId;
    }

    /**
     * 设置分割线布局ID
     * @param lineId
     * @return
     */
    public BaseDialogConfig setLineId(@IdRes int lineId) {
        this.lineId = lineId;
        return this;
    }

    public CharSequence getTitle() {
        return title;
    }

    /**
     * 设置标题
     * @param title
     * @return
     */
    public BaseDialogConfig setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    /**
     * 设置标题
     * @param context
     * @param resId
     * @return
     */
    public BaseDialogConfig setTitle(@NonNull Context context, @StringRes int resId) {
        this.title = context.getString(resId);
        return this;
    }

    public CharSequence getContent() {
        return content;
    }

    /**
     * 设置文本内容
     * @param content
     * @return
     */
    public BaseDialogConfig setContent(CharSequence content) {
        this.content = content;
        return this;
    }

    public CharSequence getCancel() {
        return cancel;
    }

    /**
     * 设置取消按钮文本内容
     * @param cancel
     * @return
     */
    public BaseDialogConfig setCancel(CharSequence cancel) {
        this.cancel = cancel;
        return this;
    }

    /**
     * 设置取消按钮文本内容
     * @param context
     * @param resId
     * @return
     */
    public BaseDialogConfig setCancel(@NonNull Context context, @StringRes int resId) {
        this.cancel = context.getString(resId);
        return this;
    }

    /**
     * 获取确定按钮文本内容
     * @return
     * @deprecated 请使用 {@link #getConfirm()} 来代替，后续版本可能会移除此方法
     */
    public CharSequence getOk() {
        return getConfirm();
    }

    /**
     * 设置确定按钮文本内容
     * @param ok
     * @return
     * @deprecated 请使用 {@link #setConfirm(CharSequence)} 来代替，后续版本可能会移除此方法
     */
    public BaseDialogConfig setOk(CharSequence ok) {
        return setConfirm(ok);
    }

    /**
     * 设置确定按钮文本内容
     * @param context
     * @param resId
     * @return
     * @deprecated 请使用 {@link #setConfirm(Context, int)}来代替，后续版本可能会移除此方法
     */
    public BaseDialogConfig setOk(@NonNull Context context, @StringRes int resId) {
        return setConfirm(context,resId);
    }

    public CharSequence getConfirm() {
        return confirm;
    }

    /**
     * 设置确定按钮文本内容
     * @param confirm
     * @return
     */
    public BaseDialogConfig setConfirm(CharSequence confirm) {
        this.confirm = confirm;
        return this;
    }

    /**
     * 设置确定按钮文本内容
     * @param context
     * @param resId
     * @return
     *
     */
    public BaseDialogConfig setConfirm(@NonNull Context context, @StringRes int resId) {
        this.confirm = context.getString(resId);
        return this;
    }

    public boolean isHideCancel() {
        return isHideCancel;
    }

    /**
     * 设置是否隐藏取消按钮
     * @param hideCancel
     * @return
     */
    public BaseDialogConfig setHideCancel(boolean hideCancel) {
        isHideCancel = hideCancel;
        return this;
    }

    public boolean isHideTitle(){
        return isHideTitle;
    }

    /**
     * 设置是否隐藏标题
     * @param hideTitle
     * @return
     */
    public BaseDialogConfig setHideTitle(boolean hideTitle){
        isHideTitle = hideTitle;
        return this;
    }

    public float getWidthRatio() {
        return widthRatio;
    }

    /**
     * 设置Dialog的宽度比例，根据屏幕宽度计算得来
     * @param widthRatio
     * @return
     */
    public BaseDialogConfig setWidthRatio(float widthRatio){
        this.widthRatio = widthRatio;
        return this;
    }

    public int getGravity() {
        return gravity;
    }

    /**
     * 设置Dialog的对齐方式
     * @param gravity
     * @return
     */
    public BaseDialogConfig setGravity(int gravity){
        this.gravity = gravity;
        return this;
    }

    public View.OnClickListener getOnClickCancel() {
        return onClickCancel;
    }

    /**
     * 设置“取消”按钮点击监听，不设置默认点击关闭弹框
     * @param onClickCancel
     * @return
     */
    public BaseDialogConfig setOnClickCancel(View.OnClickListener onClickCancel) {
        this.onClickCancel = onClickCancel;
        return this;
    }

    public View.OnClickListener getOnClickConfirm() {
        return onClickConfirm;
    }

    /**
     * 设置“确定”按钮点击监听，不设置默认点击关闭弹框
     * @param onClickConfirm
     * @return
     */
    public BaseDialogConfig setOnClickConfirm(View.OnClickListener onClickConfirm) {
        this.onClickConfirm = onClickConfirm;
        return this;
    }


    /**
     * 获取“确定”按钮点击监听，不设置默认点击关闭弹框
     * @return
     * @deprecated 请使用 {@link #getOnClickConfirm()}来代替，后续版本可能会移除此方法
     */
    public View.OnClickListener getOnClickOk() {
        return getOnClickConfirm();
    }

    /**
     * 设置“确定”按钮点击监听，不设置默认点击关闭弹框
     * @param onClickOk
     * @return
     * @deprecated 请使用 {@link #setOnClickConfirm(View.OnClickListener)}来代替，后续版本可能会移除此方法
     */
    public BaseDialogConfig setOnClickOk(View.OnClickListener onClickOk) {
        return setOnClickConfirm(onClickOk);
    }


}