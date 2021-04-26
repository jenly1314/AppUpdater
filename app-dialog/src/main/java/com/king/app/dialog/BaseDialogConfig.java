package com.king.app.dialog;

import android.content.Context;
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
    @IdRes int contentId = R.id.tvDialogContent;
    /**
     * 取消视图ID（左边按钮）
     */
    @IdRes int cancelId = R.id.btnDialogCancel;
    /**
     * 确定视图ID（右边按钮）
     */
    @IdRes int okId = R.id.btnDialogOK;
    /**
     * 按钮中间分割线ID
     */
    @IdRes int lineId = R.id.line;

    /**
     * 样式ID
     */
    @StyleRes
    int styleId = R.style.app_dialog;

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
    CharSequence ok;
    /**
     * 是否隐藏取消按钮，如果隐藏取消则底部只显示一个按钮
     */
    boolean isHideCancel;
    /**
     * 是否隐藏标题
     */
    boolean isHideTitle;

    View.OnClickListener onClickCancel;

    View.OnClickListener onClickOk;

    public BaseDialogConfig(){
        this(R.layout.app_dialog);
    }

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
    public BaseDialogConfig setLayoutId(@IdRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public int getTitleId() {
        return titleId;
    }

    public BaseDialogConfig setTitleId(@IdRes int titleId) {
        this.titleId = titleId;
        return this;
    }

    public int getStyleId() {
        return styleId;
    }

    public BaseDialogConfig setStyleId(@IdRes int styleId) {
        this.styleId = styleId;
        return this;
    }

    public @IdRes int getContentId() {
        return contentId;
    }

    public BaseDialogConfig setContentId(@IdRes int contentId) {
        this.contentId = contentId;
        return this;
    }

    public @IdRes int getCancelId() {
        return cancelId;
    }

    public BaseDialogConfig setCancelId(@IdRes int cancelId) {
        this.cancelId = cancelId;
        return this;
    }

    public @IdRes int getOkId() {
        return okId;
    }

    public BaseDialogConfig setOkId(@IdRes int okId) {
        this.okId = okId;
        return this;
    }

    public @IdRes int getLineId() {
        return lineId;
    }

    public BaseDialogConfig setLineId(@IdRes int lineId) {
        this.lineId = lineId;
        return this;
    }

    public CharSequence getTitle() {
        return title;
    }

    public BaseDialogConfig setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    public BaseDialogConfig setTitle(@NonNull Context context, @StringRes int resId) {
        this.title = context.getString(resId);
        return this;
    }

    public CharSequence getContent() {
        return content;
    }

    public BaseDialogConfig setContent(CharSequence content) {
        this.content = content;
        return this;
    }

    public CharSequence getCancel() {
        return cancel;
    }

    public BaseDialogConfig setCancel(CharSequence cancel) {
        this.cancel = cancel;
        return this;
    }

    public BaseDialogConfig setCancel(@NonNull Context context, @StringRes int resId) {
        this.cancel = context.getString(resId);
        return this;
    }

    public CharSequence getOk() {
        return ok;
    }

    public BaseDialogConfig setOk(CharSequence ok) {
        this.ok = ok;
        return this;
    }

    public BaseDialogConfig setOk(@NonNull Context context, @StringRes int resId) {
        this.ok = context.getString(resId);
        return this;
    }

    public boolean isHideCancel() {
        return isHideCancel;
    }

    public BaseDialogConfig setHideCancel(boolean hideCancel) {
        isHideCancel = hideCancel;
        return this;
    }

    public boolean isHideTitle(){
        return isHideTitle;
    }

    public BaseDialogConfig setHideTitle(boolean hideTitle){
        isHideTitle = hideTitle;
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

    public View.OnClickListener getOnClickOk() {
        return onClickOk;
    }

    /**
     * 设置“确定”按钮点击监听，不设置默认点击关闭弹框
     * @param onClickOk
     * @return
     */
    public BaseDialogConfig setOnClickOk(View.OnClickListener onClickOk) {
        this.onClickOk = onClickOk;
        return this;
    }


}