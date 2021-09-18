package com.king.app.dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.regex.Pattern;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/**
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class AppDialogConfig extends BaseDialogConfig{

    private Context context;

    private SparseArray<View> views;

    private View view;

    private ViewHolder viewHolder;

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
        return getDialogView().findViewById(id);
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

    /**
     * 获取 {@link ViewHolder}
     * @return
     */
    public final ViewHolder getViewHolder(){
        if(viewHolder == null){
            viewHolder = new ViewHolder();
        }
        return viewHolder;
    }


    /**
     * ViewHolder主要提供控件的一些常用设置（适用于Dialog，不适用于DialogFragment）
     */
    public final class ViewHolder {

        private ViewHolder(){

        }

        //---------------------- 控件常用设置

        public View setBackgroundResource(@IdRes int id,@DrawableRes int resId){
            View v = getView(id);
            v.setBackgroundResource(resId);
            return v;
        }

        @TargetApi(16)
        public View setBackground(@IdRes int id, Drawable drawable){
            View v = getView(id);
            v.setBackground(drawable);
            return v;
        }

        public View setBackgroundColor(@IdRes int id,@ColorInt int color){
            View v = getView(id);
            v.setBackgroundColor(color);
            return v;
        }

        public View setTag(@IdRes int id,Object tag){
            View v = getView(id);
            v.setTag(tag);
            return v;
        }

        public View setTag(@IdRes int id,int key,Object tag){
            View v = getView(id);
            v.setTag(key,tag);
            return v;
        }

        public View setVisibility(@IdRes int id,int visibility){
            View v = getView(id);
            v.setVisibility(visibility);
            return v;
        }

        public View setVisibility(@IdRes int id,boolean isVisible){
            View v = getView(id);
            if(isVisible){
                v.setVisibility(View.VISIBLE);
            }else{
                v.setVisibility(View.GONE);
            }
            return v;
        }

        public View setAlpha(@IdRes int id,float alpha){
            View v = getView(id);
            v.setAlpha(alpha);
            return v;
        }

        public TextView setCompoundDrawableLeft(@IdRes int id,Drawable drawable){
            return setCompoundDrawables(id,drawable,null,null,null);
        }

        public TextView setCompoundDrawableTop(@IdRes int id,Drawable drawable){
            return setCompoundDrawables(id,null,drawable,null,null);
        }

        public TextView setCompoundDrawableRight(@IdRes int id,Drawable drawable){
            return setCompoundDrawables(id,null,null,drawable,null);
        }

        public TextView setCompoundDrawableBottom(@IdRes int id,Drawable drawable){
            return setCompoundDrawables(id,null,null,null,drawable);
        }


        public TextView setCompoundDrawables(@IdRes int id,Drawable left,Drawable top,Drawable right,Drawable bottom){
            TextView tv = getView(id);
            tv.setCompoundDrawables(left, top, right, bottom);
            return tv;
        }

        public TextView setCompoundDrawablePadding(@IdRes int id,int padding){
            TextView tv = getView(id);
            tv.setCompoundDrawablePadding(padding);
            return tv;
        }

        public TextView setCompoundDrawablesWithIntrinsicBounds(@IdRes int id, @Nullable Drawable left,@Nullable Drawable top,@Nullable Drawable right,@Nullable Drawable bottom){
            TextView tv = getView(id);
            tv.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
            return tv;
        }

        public TextView setText(@IdRes int id,@StringRes int resId){
            TextView tv = getView(id);
            tv.setText(resId);
            return tv;
        }

        public TextView setText(@IdRes int id,CharSequence text){
            TextView tv = getView(id);
            tv.setText(text);
            return tv;
        }

        public TextView setTextColor(@IdRes int id,int color){
            TextView tv = getView(id);
            tv.setTextColor(color);
            return tv;
        }

        public TextView setTextColor(@IdRes int id,@NonNull ColorStateList colors){
            TextView tv = getView(id);
            tv.setTextColor(colors);
            return tv;
        }

        public TextView setTextSize(@IdRes int id,float size){
            return setTextSize(id,TypedValue.COMPLEX_UNIT_SP, size);
        }

        public TextView setTextSize(@IdRes int id,int unit, float size){
            TextView tv = getView(id);
            tv.setTextSize(unit,size);
            return tv;
        }

        public TextView setTypeface(@IdRes int id,@Nullable Typeface tf){
            TextView tv = getView(id);
            tv.setTypeface(tf);
            return tv;
        }

        public TextView setTypeface(@IdRes int id,@Nullable Typeface tf, int style){
            TextView tv = getView(id);
            tv.setTypeface(tf,style);
            return tv;
        }

        public TextView addLinks(@IdRes int id){
            return addLinks(id,Linkify.ALL);
        }

        public TextView addLinks(@IdRes int id,int mask){
            TextView tv = getView(id);
            Linkify.addLinks(tv,mask);
            return tv;
        }

        public TextView addLinks(@IdRes int id,@NonNull Pattern pattern,@Nullable String scheme){
            TextView tv = getView(id);
            Linkify.addLinks(tv,pattern,scheme);
            return tv;
        }

        public ImageView setImageResource(@IdRes int id,@DrawableRes int resId){
            ImageView iv = getView(id);
            iv.setImageResource(resId);
            return iv;
        }

        public ImageView setImageBitmap(@IdRes int id, Bitmap bitmap){
            ImageView iv = getView(id);
            iv.setImageBitmap(bitmap);
            return iv;
        }

        public ImageView setImageDrawable(@IdRes int id,Drawable drawable){
            ImageView iv = getView(id);
            iv.setImageDrawable(drawable);
            return iv;
        }

        public CompoundButton setChecked(@IdRes int id, boolean isChecked){
            CompoundButton cb = getView(id);
            cb.setChecked(isChecked);
            return cb;
        }

        public boolean isChecked(@IdRes int id){
            CompoundButton cb = getView(id);
            return cb.isChecked();
        }

        public CompoundButton toggle(@IdRes int id){
            CompoundButton cb = getView(id);
            cb.toggle();
            return cb;
        }

        public ProgressBar setProgress(@IdRes int id, int progress){
            ProgressBar progressBar = getView(id);
            progressBar.setProgress(progress);
            return progressBar;
        }

        public ProgressBar setMax(@IdRes int id,int max){
            ProgressBar progressBar = getView(id);
            progressBar.setMax(max);
            return progressBar;
        }

        public RatingBar setRating(@IdRes int id, float rating){
            RatingBar ratingBar = getView(id);
            ratingBar.setRating(rating);
            return ratingBar;
        }

        public RatingBar setRating(@IdRes int id,float rating,int max){
            RatingBar ratingBar = getView(id);
            ratingBar.setRating(rating);
            ratingBar.setMax(max);
            return ratingBar;
        }

        public RatingBar setNumStars(@IdRes int id,int numStars){
            RatingBar ratingBar = getView(id);
            ratingBar.setNumStars(numStars);
            return ratingBar;
        }

        public View setSelected(@IdRes int id,boolean selected){
            View view = getView(id);
            view.setSelected(selected);
            return view;
        }

        public boolean isSelected(@IdRes int id){
            return getView(id).isSelected();
        }

        public View setEnabled(@IdRes int id,boolean enabled){
            View view = getView(id);
            view.setEnabled(enabled);
            return view;
        }

        public boolean isEnabled(@IdRes int id){
            return getView(id).isEnabled();
        }


        //---------------------- 监听事件


        public void setOnClickListener(@IdRes int id, View.OnClickListener onClickListener){
            getView(id).setOnClickListener(onClickListener);
        }

        public void setOnTouchListener(@IdRes int id, View.OnTouchListener onTouchListener){
            getView(id).setOnTouchListener(onTouchListener);
        }

        public void setOnLongClickListener(@IdRes int id, View.OnLongClickListener onLongClickListener){
            getView(id).setOnLongClickListener(onLongClickListener);
        }

        public void setOnKeyListener(@IdRes int id, View.OnKeyListener onKeyListener){
            getView(id).setOnKeyListener(onKeyListener);
        }

    }

}