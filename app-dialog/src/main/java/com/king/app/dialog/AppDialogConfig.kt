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
 * App 对话框配置
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 */
public class AppDialogConfig extends BaseDialogConfig {

    private Context context;

    private SparseArray<View> views;

    private View view;

    private ViewHolder viewHolder;

    /**
     * 构造
     *
     * @param context 上下文
     */
    public AppDialogConfig(@NonNull Context context) {
        this(context, R.layout.app_dialog);
    }

    /**
     * 构造
     *
     * @param context  上下文
     * @param layoutId 布局ID
     */
    public AppDialogConfig(@NonNull Context context, @LayoutRes int layoutId) {
        super(layoutId);
        this.context = context;
        views = new SparseArray<>();
    }

    /**
     * 获取上下文
     *
     * @return {@link #context}
     */
    public Context getContext() {
        return context;
    }

    /**
     * 获取对话框视图
     *
     * @param context 上下文
     * @return 对话框视图
     * @deprecated 即将废弃，下一个版本可能会移除此方法。
     */
    @Deprecated
    public View getView(@NonNull Context context) {
        return getDialogView();
    }

    /**
     * 获取对话框视图
     *
     * @return 对话框视图
     */
    private View getDialogView() {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(getLayoutId(), null);
        }
        return view;
    }

    /**
     * 通过视图ID查找对应的视图
     *
     * @param id  视图ID
     * @param <T> 对应的视图类
     * @return 视图ID对应的视图
     */
    private <T extends View> T findView(@IdRes int id) {
        return getDialogView().findViewById(id);
    }

    /**
     * 根据视图ID获取对应的视图
     *
     * @param id  视图ID
     * @param <T> 对应的视图类
     * @return 视图ID对应的视图
     */
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int id) {
        View v = views.get(id);
        if (v == null) {
            v = findView(id);
            views.put(id, v);
        }

        return (T) v;
    }

    /**
     * 通过{@link AppDialogConfig} 创建一个视图
     *
     * @return {@link View}
     */
    View buildAppDialogView() {
        TextView tvDialogTitle = getView(titleId);
        if (tvDialogTitle != null) {
            setText(tvDialogTitle, title);
            tvDialogTitle.setVisibility(isHideTitle ? View.GONE : View.VISIBLE);
        }

        TextView tvDialogContent = getView(contentId);
        if (tvDialogContent != null) {
            setText(tvDialogContent, content);
        }

        Button btnDialogCancel = getView(cancelId);
        if (btnDialogCancel != null) {
            setText(btnDialogCancel, cancel);
            btnDialogCancel.setOnClickListener(onClickCancel != null ? onClickCancel : AppDialog.INSTANCE.mOnClickDismissDialog);
            btnDialogCancel.setVisibility(isHideCancel ? View.GONE : View.VISIBLE);
        }

        View line = getView(lineId);
        if (line != null) {
            line.setVisibility(isHideCancel ? View.GONE : View.VISIBLE);
        }

        Button btnDialogConfirm = getView(confirmId);
        if (btnDialogConfirm != null) {
            setText(btnDialogConfirm, confirm);
            btnDialogConfirm.setOnClickListener(onClickConfirm != null ? onClickConfirm : AppDialog.INSTANCE.mOnClickDismissDialog);

        }

        return view;
    }

    /**
     * 设置文本
     *
     * @param tv   {@link TextView}
     * @param text {@link CharSequence}
     */
    private void setText(TextView tv, CharSequence text) {
        if (text != null) {
            tv.setText(text);
        }
    }

    /**
     * 获取 {@link ViewHolder}
     *
     * @return {@link ViewHolder}
     */
    public final ViewHolder getViewHolder() {
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
        }
        return viewHolder;
    }


    /**
     * ViewHolder主要提供视图控件的一些常用设置（适用于Dialog，不适用于DialogFragment）
     */
    public final class ViewHolder {

        private ViewHolder() {

        }

        //---------------------- 控件常用设置

        /**
         * 设置视图的背景色
         * {@link View#setBackgroundResource(int)}
         *
         * @param id    视图ID
         * @param resId Drawable资源ID
         * @return {@link View}
         */
        public View setBackgroundResource(@IdRes int id, @DrawableRes int resId) {
            View v = getView(id);
            v.setBackgroundResource(resId);
            return v;
        }

        /**
         * 设置视图的背景色
         * {@link View#setBackground(Drawable)}
         *
         * @param id       视图ID
         * @param drawable {@link Drawable}
         * @return {@link View}
         */
        @TargetApi(16)
        public View setBackground(@IdRes int id, Drawable drawable) {
            View v = getView(id);
            v.setBackground(drawable);
            return v;
        }

        /**
         * 设置视图的背景色
         * {@link View#setBackgroundColor(int)}
         *
         * @param id    视图ID
         * @param color 颜色
         * @return {@link View}
         */
        public View setBackgroundColor(@IdRes int id, @ColorInt int color) {
            View v = getView(id);
            v.setBackgroundColor(color);
            return v;
        }

        /**
         * 设置视图的标签
         * {@link View#setTag(Object)}
         *
         * @param id  视图ID
         * @param tag 标签
         * @return {@link View}
         */
        public View setTag(@IdRes int id, Object tag) {
            View v = getView(id);
            v.setTag(tag);
            return v;
        }

        /**
         * 设置视图的标签
         * {@link View#setTag(int, Object)}
         *
         * @param id  视图ID
         * @param key 标签的key
         * @param tag 标签
         * @return {@link View}
         */
        public View setTag(@IdRes int id, int key, Object tag) {
            View v = getView(id);
            v.setTag(key, tag);
            return v;
        }

        /**
         * 设置视图的可见性
         * {@link View#setVisibility(int)}
         *
         * @param id         视图ID
         * @param visibility 可见性
         * @return {@link View}
         */
        public View setVisibility(@IdRes int id, int visibility) {
            View v = getView(id);
            v.setVisibility(visibility);
            return v;
        }

        /**
         * 设置视图的可见性
         * {@link View#setVisibility(int)}
         *
         * @param id        视图ID
         * @param isVisible 是否可见；true时设置为：{@link View#VISIBLE}；false时设置为：{@link View#GONE}
         * @return {@link View}
         */
        public View setVisibility(@IdRes int id, boolean isVisible) {
            View v = getView(id);
            if (isVisible) {
                v.setVisibility(View.VISIBLE);
            } else {
                v.setVisibility(View.GONE);
            }
            return v;
        }

        /**
         * 设置视图的可见性
         * {@link View#setVisibility(int)}
         *
         * @param id        视图ID
         * @param isVisible 是否可见；true时设置为：{@link View#VISIBLE}；false时设置为：{@link View#INVISIBLE}
         * @return {@link View}
         */
        public View setInVisibility(@IdRes int id, boolean isVisible) {
            View v = getView(id);
            if (isVisible) {
                v.setVisibility(View.VISIBLE);
            } else {
                v.setVisibility(View.INVISIBLE);
            }
            return v;
        }

        /**
         * 设置视图的透明度
         * {@link View#setAlpha(float)}
         *
         * @param id    视图ID
         * @param alpha 透明度
         * @return {@link View}
         */
        public View setAlpha(@IdRes int id, float alpha) {
            View v = getView(id);
            v.setAlpha(alpha);
            return v;
        }

        /**
         * 设置视图左方的复合绘图 {@link Drawable}
         * {@link #setCompoundDrawables(int, Drawable, Drawable, Drawable, Drawable)}
         *
         * @param id       视图ID
         * @param drawable {@link Drawable}
         * @return {@link TextView}
         */
        public TextView setCompoundDrawableLeft(@IdRes int id, Drawable drawable) {
            return setCompoundDrawables(id, drawable, null, null, null);
        }

        /**
         * 设置视图上方的复合绘图 {@link Drawable}
         * {@link #setCompoundDrawables(int, Drawable, Drawable, Drawable, Drawable)}
         *
         * @param id       视图ID
         * @param drawable {@link Drawable}
         * @return {@link TextView}
         */
        public TextView setCompoundDrawableTop(@IdRes int id, Drawable drawable) {
            return setCompoundDrawables(id, null, drawable, null, null);
        }

        /**
         * 设置视图右方的复合绘图 {@link Drawable}
         * {@link #setCompoundDrawables(int, Drawable, Drawable, Drawable, Drawable)}
         *
         * @param id       视图ID
         * @param drawable {@link Drawable}
         * @return {@link TextView}
         */
        public TextView setCompoundDrawableRight(@IdRes int id, Drawable drawable) {
            return setCompoundDrawables(id, null, null, drawable, null);
        }

        /**
         * 设置视图下方的复合绘图 {@link Drawable}
         * {@link #setCompoundDrawables(int, Drawable, Drawable, Drawable, Drawable)}
         *
         * @param id       视图ID
         * @param drawable {@link Drawable}
         * @return {@link TextView}
         */
        public TextView setCompoundDrawableBottom(@IdRes int id, Drawable drawable) {
            return setCompoundDrawables(id, null, null, null, drawable);
        }

        /**
         * 设置视图的复合绘图 {@link Drawable}
         * {@link TextView#setCompoundDrawables(Drawable, Drawable, Drawable, Drawable)}
         *
         * @param id     视图ID
         * @param left   左方的Drawable
         * @param top    上方的Drawable
         * @param right  右方的Drawable
         * @param bottom 下方的Drawable
         * @return {@link TextView}
         */
        public TextView setCompoundDrawables(@IdRes int id, Drawable left, Drawable top, Drawable right, Drawable bottom) {
            TextView tv = getView(id);
            tv.setCompoundDrawables(left, top, right, bottom);
            return tv;
        }

        /**
         * 设置视图的可填充内距
         * {@link TextView#setCompoundDrawablePadding(int)}
         *
         * @param id      视图ID
         * @param padding 内填充间距
         * @return
         */
        public TextView setCompoundDrawablePadding(@IdRes int id, int padding) {
            TextView tv = getView(id);
            tv.setCompoundDrawablePadding(padding);
            return tv;
        }

        /**
         * 设置视图的内在的复合绘图 {@link Drawable}
         * {@link TextView#setCompoundDrawablesWithIntrinsicBounds(int, int, int, int)}
         *
         * @param id     视图ID
         * @param left   左方的Drawable
         * @param top    上方的Drawable
         * @param right  右方的Drawable
         * @param bottom 下方的Drawable
         * @return {@link TextView}
         */
        public TextView setCompoundDrawablesWithIntrinsicBounds(@IdRes int id, @Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
            TextView tv = getView(id);
            tv.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
            return tv;
        }

        /**
         * 设置文本内容
         * {@link TextView#setText(int)}
         *
         * @param id    视图ID
         * @param resId 字符串资源ID
         * @return {@link TextView}
         */
        public TextView setText(@IdRes int id, @StringRes int resId) {
            TextView tv = getView(id);
            tv.setText(resId);
            return tv;
        }

        /**
         * 设置文本内容
         * {@link TextView#setText(CharSequence)}
         *
         * @param id   视图ID
         * @param text 文本
         * @return {@link TextView}
         */
        public TextView setText(@IdRes int id, CharSequence text) {
            TextView tv = getView(id);
            tv.setText(text);
            return tv;
        }

        /**
         * 设置字体颜色
         * {@link TextView#setTextColor(int)}
         *
         * @param id    视图ID
         * @param color 颜色
         * @return {@link TextView}
         */
        public TextView setTextColor(@IdRes int id, int color) {
            TextView tv = getView(id);
            tv.setTextColor(color);
            return tv;
        }

        /**
         * 设置字体颜色
         * {@link TextView#setTextColor(ColorStateList)}
         *
         * @param id     视图ID
         * @param colors 颜色状态列表
         * @return {@link TextView}
         */
        public TextView setTextColor(@IdRes int id, @NonNull ColorStateList colors) {
            TextView tv = getView(id);
            tv.setTextColor(colors);
            return tv;
        }

        /**
         * 设置字体大小
         * {@link TextView#setTextSize(float)}
         *
         * @param id   视图ID
         * @param size 字体大小（单位：sp）
         * @return {@link TextView}
         */
        public TextView setTextSize(@IdRes int id, float size) {
            return setTextSize(id, size);
        }

        /**
         * 设置字体大小
         * {@link TextView#setTextSize(int, float)}
         *
         * @param id   视图ID
         * @param unit 单位；推荐使用 {@link TypedValue#COMPLEX_UNIT_SP}
         * @param size 字体大小
         * @return {@link TextView}
         */
        public TextView setTextSize(@IdRes int id, int unit, float size) {
            TextView tv = getView(id);
            tv.setTextSize(unit, size);
            return tv;
        }

        /**
         * 设置字体
         * {@link TextView#setTypeface(Typeface)}
         *
         * @param id 视图ID
         * @param tf 字体
         * @return {@link TextView}
         */
        public TextView setTypeface(@IdRes int id, @Nullable Typeface tf) {
            TextView tv = getView(id);
            tv.setTypeface(tf);
            return tv;
        }

        /**
         * 设置字体
         * {@link TextView#setTypeface(Typeface, int)}
         *
         * @param id    视图ID
         * @param tf    字体
         * @param style 字体样式
         * @return {@link TextView}
         */
        public TextView setTypeface(@IdRes int id, @Nullable Typeface tf, int style) {
            TextView tv = getView(id);
            tv.setTypeface(tf, style);
            return tv;
        }

        /**
         * 添加链接
         * {@link #addLinks(int, int)}
         *
         * @param id 视图ID
         * @return {@link TextView}
         */
        public TextView addLinks(@IdRes int id) {
            return addLinks(id, Linkify.ALL);
        }

        /**
         * 添加链接
         * {@link Linkify#addLinks(TextView, int)}
         *
         * @param id   视图ID
         * @param mask 连接掩码；如：{@link Linkify#ALL}
         * @return {@link TextView}
         */
        public TextView addLinks(@IdRes int id, int mask) {
            TextView tv = getView(id);
            Linkify.addLinks(tv, mask);
            return tv;
        }

        /**
         * 添加链接
         * {@link Linkify#addLinks(TextView, Pattern, String)}
         *
         * @param id      视图ID
         * @param pattern 正则表达式模式
         * @param scheme  方案
         * @return {@link TextView}
         */
        public TextView addLinks(@IdRes int id, @NonNull Pattern pattern, @Nullable String scheme) {
            TextView tv = getView(id);
            Linkify.addLinks(tv, pattern, scheme);
            return tv;
        }

        /**
         * 根据Drawable资源ID设置图像
         * {@link ImageView#setImageResource(int)}
         *
         * @param id    视图ID
         * @param resId Drawable资源ID
         * @return {@link ImageView}
         */
        public ImageView setImageResource(@IdRes int id, @DrawableRes int resId) {
            ImageView iv = getView(id);
            iv.setImageResource(resId);
            return iv;
        }

        /**
         * 根据位图设置图像
         * {@link ImageView#setImageBitmap(Bitmap)}
         *
         * @param id     视图ID
         * @param bitmap 位图
         * @return {@link ImageView}
         */
        public ImageView setImageBitmap(@IdRes int id, Bitmap bitmap) {
            ImageView iv = getView(id);
            iv.setImageBitmap(bitmap);
            return iv;
        }

        /**
         * 根据 {@link Drawable} 设置图像
         * {@link ImageView#setImageResource(int)}
         *
         * @param id       视图ID
         * @param drawable {@link Drawable}
         * @return {@link ImageView}
         */
        public ImageView setImageDrawable(@IdRes int id, Drawable drawable) {
            ImageView iv = getView(id);
            iv.setImageDrawable(drawable);
            return iv;
        }

        /**
         * 设置是否选中
         * {@link CompoundButton#setChecked(boolean)}
         *
         * @param id        视图ID
         * @param isChecked 是否选中
         * @return {@link CompoundButton}
         */
        public CompoundButton setChecked(@IdRes int id, boolean isChecked) {
            CompoundButton cb = getView(id);
            cb.setChecked(isChecked);
            return cb;
        }

        /**
         * 是否选中
         * {@link CompoundButton#isChecked()}
         *
         * @param id 视图ID
         * @return {@code true} or {@code false}
         */
        public boolean isChecked(@IdRes int id) {
            CompoundButton cb = getView(id);
            return cb.isChecked();
        }

        /**
         * 切换
         * {@link CompoundButton#toggle()}
         *
         * @param id 视图ID
         * @return {@link CompoundButton}
         */
        public CompoundButton toggle(@IdRes int id) {
            CompoundButton cb = getView(id);
            cb.toggle();
            return cb;
        }

        /**
         * 设置进度值
         * {@link ProgressBar#setProgress(int)}
         *
         * @param id       视图ID
         * @param progress 进度
         * @return {@link ProgressBar}
         */
        public ProgressBar setProgress(@IdRes int id, int progress) {
            ProgressBar progressBar = getView(id);
            progressBar.setProgress(progress);
            return progressBar;
        }

        /**
         * 设置最大进度值
         * {@link ProgressBar#setMax(int)}
         *
         * @param id  视图ID
         * @param max 最大进度值
         * @return {@link ProgressBar}
         */
        public ProgressBar setMax(@IdRes int id, int max) {
            ProgressBar progressBar = getView(id);
            progressBar.setMax(max);
            return progressBar;
        }

        /**
         * 设置评分
         * {@link RatingBar#setRating(float)}
         *
         * @param id     视图ID
         * @param rating 评分
         * @return {@link RatingBar}
         */
        public RatingBar setRating(@IdRes int id, float rating) {
            RatingBar ratingBar = getView(id);
            ratingBar.setRating(rating);
            return ratingBar;
        }

        /**
         * 设置评分和最大评分值
         * {@link RatingBar#setRating(float)} and {@link RatingBar#setMax(int)}
         *
         * @param id     视图ID
         * @param rating 评分
         * @param max    最大评分值
         * @return {@link RatingBar}
         */
        public RatingBar setRating(@IdRes int id, float rating, int max) {
            RatingBar ratingBar = getView(id);
            ratingBar.setRating(rating);
            ratingBar.setMax(max);
            return ratingBar;
        }

        /**
         * 设置星星数量
         * {@link RatingBar#setNumStars(int)}
         *
         * @param id       视图ID
         * @param numStars 星星数量
         * @return {@link RatingBar}
         */
        public RatingBar setNumStars(@IdRes int id, int numStars) {
            RatingBar ratingBar = getView(id);
            ratingBar.setNumStars(numStars);
            return ratingBar;
        }

        /**
         * 设置是否选择
         * {@link View#setSelected(boolean)}
         *
         * @param id       视图ID
         * @param selected 是否选择
         * @return {@link View}
         */
        public View setSelected(@IdRes int id, boolean selected) {
            View view = getView(id);
            view.setSelected(selected);
            return view;
        }

        /**
         * 是否选择
         * {@link View#isSelected()}
         *
         * @param id 视图ID
         * @return {@code true} or {@code false}
         */
        public boolean isSelected(@IdRes int id) {
            return getView(id).isSelected();
        }

        /**
         * 设置是否启用
         * {@link View#setEnabled(boolean)}
         *
         * @param id      视图ID
         * @param enabled 是否启用
         * @return {@link View}
         */
        public View setEnabled(@IdRes int id, boolean enabled) {
            View view = getView(id);
            view.setEnabled(enabled);
            return view;
        }

        /**
         * 是否启用
         * {@link View#isEnabled()}
         *
         * @param id 视图ID
         * @return {@code true} or {@code false}
         */
        public boolean isEnabled(@IdRes int id) {
            return getView(id).isEnabled();
        }


        //---------------------- 监听事件

        /**
         * 设置点击监听事
         * {@link View#setOnClickListener(View.OnClickListener)}
         *
         * @param id              视图ID
         * @param onClickListener {@link View.OnClickListener}
         */
        public void setOnClickListener(@IdRes int id, View.OnClickListener onClickListener) {
            getView(id).setOnClickListener(onClickListener);
        }

        /**
         * 设置触摸监听
         * {@link View#setOnTouchListener(View.OnTouchListener)}
         *
         * @param id              视图ID
         * @param onTouchListener {@link View.OnTouchListener}
         */
        public void setOnTouchListener(@IdRes int id, View.OnTouchListener onTouchListener) {
            getView(id).setOnTouchListener(onTouchListener);
        }

        /**
         * 设置长按监听
         * {@link View#setOnLongClickListener(View.OnLongClickListener)}
         *
         * @param id                  视图ID
         * @param onLongClickListener {@link View.OnLongClickListener}
         */
        public void setOnLongClickListener(@IdRes int id, View.OnLongClickListener onLongClickListener) {
            getView(id).setOnLongClickListener(onLongClickListener);
        }

        /**
         * 设置按键监听
         * {@link View#setOnKeyListener(View.OnKeyListener)}
         *
         * @param id            视图ID
         * @param onKeyListener {@link View.OnKeyListener}
         */
        public void setOnKeyListener(@IdRes int id, View.OnKeyListener onKeyListener) {
            getView(id).setOnKeyListener(onKeyListener);
        }

    }

}