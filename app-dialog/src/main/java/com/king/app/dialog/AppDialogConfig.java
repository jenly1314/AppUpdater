package com.king.app.dialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class AppDialogConfig extends BaseDialogConfig{

    private Context context;

    private SparseArray<View> views;

    private View view;

    public AppDialogConfig(@NonNull Context context){
        this(context,R.layout.app_dialog);
    }

    public AppDialogConfig(@NonNull Context context,@LayoutRes int layoutId){
        super(layoutId);
        this.context = context;
        views = new SparseArray<>();
    }

    public Context getContext(){
        return context;
    }


    /**
     *
     * @param context
     * @return
     * @deprecated 即将废弃，下一个版本可能会移除此方法。
     */
    @Deprecated
    public View getView(@NonNull Context context){
        return getDialogView();
    }

    private View getDialogView(){
        if(view == null){
            view = LayoutInflater.from(context).inflate(getLayoutId(),null);
        }
        return view;
    }

    private <T extends View> T findView(@IdRes int id){
        return (T)getDialogView().findViewById(id);
    }

    public <T extends View> T getView(@IdRes int id){
        View v = views.get(id);
        if(v == null){
            v = findView(id);
            views.put(id,v);
        }

        return (T)v;
    }


    /**
     * 通过{@link AppDialogConfig} 创建一个视图
     * @return
     */
    View buildAppDialogView(){
        TextView tvDialogTitle = getView(titleId);
        if(tvDialogTitle != null){
            setText(tvDialogTitle,title);
            tvDialogTitle.setVisibility(isHideTitle ? View.GONE : View.VISIBLE);
        }

        TextView tvDialogContent = getView(contentId);
        if(tvDialogContent != null){
            setText(tvDialogContent,content);
        }

        Button btnDialogCancel = getView(cancelId);
        if(btnDialogCancel != null){
            setText(btnDialogCancel,cancel);
            btnDialogCancel.setOnClickListener(onClickCancel != null ? onClickCancel : AppDialog.INSTANCE.mOnClickDismissDialog);
            btnDialogCancel.setVisibility(isHideCancel ? View.GONE : View.VISIBLE);
        }

        View line = getView(lineId);
        if(line != null){
            line.setVisibility(isHideCancel ? View.GONE : View.VISIBLE);
        }

        Button btnDialogConfirm = getView(confirmId);
        if(btnDialogConfirm != null){
            setText(btnDialogConfirm,confirm);
            btnDialogConfirm.setOnClickListener(onClickConfirm != null ? onClickConfirm : AppDialog.INSTANCE.mOnClickDismissDialog);

        }

        return view;
    }

    private void setText(TextView tv, CharSequence text){
        if(text != null){
            tv.setText(text);
        }
    }


}