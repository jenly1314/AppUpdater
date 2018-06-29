package com.king.app.dialog;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class AppDialogConfig {

    private @LayoutRes int layoutId = R.layout.app_dialog;

    private @IdRes int titleId = R.id.tvDialogTitle;

    private @IdRes int contentId = R.id.tvDialogContent;

    private @IdRes int cancelId = R.id.btnDialogCancel;

    private @IdRes int okId = R.id.btnDialogOK;

    private @IdRes int line = R.id.line;

    private CharSequence title;

    private CharSequence content;

    private CharSequence cancel;

    private CharSequence ok;

    private boolean isHideCancel;

    private View.OnClickListener onClickCancel;

    private View.OnClickListener onClickOk;

    public int getLayoutId() {
        return layoutId;
    }

    public AppDialogConfig setLayoutId(int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public int getTitleId() {
        return titleId;
    }

    public AppDialogConfig setTitleId(int titleId) {
        this.titleId = titleId;
        return this;
    }

    public int getContentId() {
        return contentId;
    }

    public AppDialogConfig setContentId(int contentId) {
        this.contentId = contentId;
        return this;
    }

    public int getCancelId() {
        return cancelId;
    }

    public AppDialogConfig setCancelId(int cancelId) {
        this.cancelId = cancelId;
        return this;
    }

    public int getOkId() {
        return okId;
    }

    public AppDialogConfig setOkId(int okId) {
        this.okId = okId;
        return this;
    }

    public int getLine() {
        return line;
    }

    public AppDialogConfig setLine(int line) {
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


}
