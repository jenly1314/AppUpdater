package com.king.app.dialog;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class AppDialogConfig {
    /**
     * 布局ID
     */
    private @LayoutRes int layoutId = R.layout.app_dialog;
    /**
     * 标题视图ID
     */
    private @IdRes int titleId = R.id.tvDialogTitle;
    /**
     * 内容视图ID
     */
    private @IdRes int contentId = R.id.tvDialogContent;
    /**
     * 取消视图ID（左边按钮）
     */
    private @IdRes int cancelId = R.id.btnDialogCancel;
    /**
     * 确定视图ID（右边按钮）
     */
    private @IdRes int okId = R.id.btnDialogOK;
    /**
     * 按钮中间分割线ID
     */
    private @IdRes int line = R.id.line;
    /**
     * 标题文本
     */
    private CharSequence title;
    /**
     * 内容文本
     */
    private CharSequence content;
    /**
     * 取消按钮文本
     */
    private CharSequence cancel;
    /**
     * 确定按钮文本
     */
    private CharSequence ok;
    /**
     * 是否隐藏取消按钮，如果隐藏取消则底部只显示一个按钮
     */
    private boolean isHideCancel;
    /**
     * 是否隐藏标题
     */
    private boolean isHideTitle;

    private View.OnClickListener onClickCancel;

    private View.OnClickListener onClickOk;

    private View view;

    public @LayoutRes int getLayoutId() {
        return layoutId;
    }

    public AppDialogConfig setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public int getTitleId() {
        return titleId;
    }

    public AppDialogConfig setTitleId(@IdRes int titleId) {
        this.titleId = titleId;
        return this;
    }

    public @IdRes int getContentId() {
        return contentId;
    }

    public AppDialogConfig setContentId(@IdRes int contentId) {
        this.contentId = contentId;
        return this;
    }

    public @IdRes int getCancelId() {
        return cancelId;
    }

    public AppDialogConfig setCancelId(@IdRes int cancelId) {
        this.cancelId = cancelId;
        return this;
    }

    public @IdRes int getOkId() {
        return okId;
    }

    public AppDialogConfig setOkId(@IdRes int okId) {
        this.okId = okId;
        return this;
    }

    public @IdRes int getLine() {
        return line;
    }

    public AppDialogConfig setLine(@IdRes int line) {
        this.line = line;
        return this;
    }

    public CharSequence getTitle() {
        return title;
    }

    public AppDialogConfig setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    public CharSequence getContent() {
        return content;
    }

    public AppDialogConfig setContent(CharSequence content) {
        this.content = content;
        return this;
    }

    public CharSequence getCancel() {
        return cancel;
    }

    public AppDialogConfig setCancel(CharSequence cancel) {
        this.cancel = cancel;
        return this;
    }

    public CharSequence getOk() {
        return ok;
    }

    public AppDialogConfig setOk(CharSequence ok) {
        this.ok = ok;
        return this;
    }

    public boolean isHideCancel() {
        return isHideCancel;
    }

    public AppDialogConfig setHideCancel(boolean hideCancel) {
        isHideCancel = hideCancel;
        return this;
    }

    public boolean isHideTitle(){
        return isHideTitle;
    }

    public AppDialogConfig setHideTitle(boolean hideTitle){
        isHideTitle = hideTitle;
        return this;
    }

    public View.OnClickListener getOnClickCancel() {
        return onClickCancel;
    }

    public AppDialogConfig setOnClickCancel(View.OnClickListener onClickCancel) {
        this.onClickCancel = onClickCancel;
        return this;
    }

    public View.OnClickListener getOnClickOk() {
        return onClickOk;
    }

    public AppDialogConfig setOnClickOk(View.OnClickListener onClickOk) {
        this.onClickOk = onClickOk;
        return this;
    }

    public View getView(@NonNull Context context){
        if(view == null){
            view = LayoutInflater.from(context).inflate(layoutId,null);
        }
        return view;
    }



}
