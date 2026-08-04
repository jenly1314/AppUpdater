package com.king.app.dialog

import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.util.Linkify
import android.util.SparseArray
import android.util.TypedValue
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import java.util.regex.Pattern

/**
 * ViewHolder主要提供视图控件的一些常用设置
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
class ViewHolder(private val rootView: View) {

    private val views: SparseArray<View> by lazy {
        SparseArray()
    }

    /**
     * 通过视图ID查找对应的视图
     *
     * @param id  视图ID
     * @param <T> 对应的视图类
     * @return 视图ID对应的视图
     */
    private fun <T : View?> findView(@IdRes id: Int): T? {
        return rootView.findViewById(id)
    }

    /**
     * 根据视图ID获取对应的视图
     *
     * @param id  视图ID
     * @param <T> 对应的视图类
     * @return 视图ID对应的视图
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : View?> getView(@IdRes id: Int): T {
        var v = views[id]
        if (v == null) {
            v = findView<View>(id)
            views.put(id, v)
        }

        return v as T
    }

    //---------------------- 控件常用设置
    /**
     * 设置视图的背景色
     * [View.setBackgroundResource]
     *
     * @param id    视图ID
     * @param resId Drawable资源ID
     * @return [View]
     */
    fun setBackgroundResource(@IdRes id: Int, @DrawableRes resId: Int): View {
        val v = getView<View>(id)
        v.setBackgroundResource(resId)
        return v
    }

    /**
     * 设置视图的背景色
     * [View.setBackground]
     *
     * @param id       视图ID
     * @param drawable [Drawable]
     * @return [View]
     */
    fun setBackground(@IdRes id: Int, drawable: Drawable?): View {
        val v = getView<View>(id)
        v.background = drawable
        return v
    }

    /**
     * 设置视图的背景色
     * [View.setBackgroundColor]
     *
     * @param id    视图ID
     * @param color 颜色
     * @return [View]
     */
    fun setBackgroundColor(@IdRes id: Int, @ColorInt color: Int): View {
        val v = getView<View>(id)
        v.setBackgroundColor(color)
        return v
    }

    /**
     * 设置视图的标签
     * [View.setTag]
     *
     * @param id  视图ID
     * @param tag 标签
     * @return [View]
     */
    fun setTag(@IdRes id: Int, tag: Any?): View {
        val v = getView<View>(id)
        v.tag = tag
        return v
    }

    /**
     * 设置视图的标签
     * [View.setTag]
     *
     * @param id  视图ID
     * @param key 标签的key
     * @param tag 标签
     * @return [View]
     */
    fun setTag(@IdRes id: Int, key: Int, tag: Any?): View {
        val v = getView<View>(id)
        v.setTag(key, tag)
        return v
    }

    /**
     * 设置视图的可见性
     * [View.setVisibility]
     *
     * @param id         视图ID
     * @param visibility 可见性
     * @return [View]
     */
    fun setVisibility(@IdRes id: Int, visibility: Int): View {
        val v = getView<View>(id)
        v.visibility = visibility
        return v
    }

    /**
     * 设置视图的可见性
     * [View.setVisibility]
     *
     * @param id        视图ID
     * @param isVisible 是否可见；true时设置为：[View.VISIBLE]；false时设置为：[View.GONE]
     * @return [View]
     */
    fun setVisibility(@IdRes id: Int, isVisible: Boolean): View {
        val v = getView<View>(id)
        if (isVisible) {
            v.visibility = View.VISIBLE
        } else {
            v.visibility = View.GONE
        }
        return v
    }

    /**
     * 设置视图的可见性
     * [View.setVisibility]
     *
     * @param id        视图ID
     * @param isVisible 是否可见；true时设置为：[View.VISIBLE]；false时设置为：[View.INVISIBLE]
     * @return [View]
     */
    fun setInVisibility(@IdRes id: Int, isVisible: Boolean): View {
        val v = getView<View>(id)
        if (isVisible) {
            v.visibility = View.VISIBLE
        } else {
            v.visibility = View.INVISIBLE
        }
        return v
    }

    /**
     * 设置视图的透明度
     * [View.setAlpha]
     *
     * @param id    视图ID
     * @param alpha 透明度
     * @return [View]
     */
    fun setAlpha(@IdRes id: Int, alpha: Float): View {
        val v = getView<View>(id)
        v.alpha = alpha
        return v
    }

    /**
     * 设置视图左方的复合绘图 [Drawable]
     * [setCompoundDrawables]
     *
     * @param id       视图ID
     * @param drawable [Drawable]
     * @return [TextView]
     */
    fun setCompoundDrawableLeft(@IdRes id: Int, drawable: Drawable?): TextView {
        return setCompoundDrawables(id, drawable, null, null, null)
    }

    /**
     * 设置视图上方的复合绘图 [Drawable]
     * [setCompoundDrawables]
     *
     * @param id       视图ID
     * @param drawable [Drawable]
     * @return [TextView]
     */
    fun setCompoundDrawableTop(@IdRes id: Int, drawable: Drawable?): TextView {
        return setCompoundDrawables(id, null, drawable, null, null)
    }

    /**
     * 设置视图右方的复合绘图 [Drawable]
     * [setCompoundDrawables]
     *
     * @param id       视图ID
     * @param drawable [Drawable]
     * @return [TextView]
     */
    fun setCompoundDrawableRight(@IdRes id: Int, drawable: Drawable?): TextView {
        return setCompoundDrawables(id, null, null, drawable, null)
    }

    /**
     * 设置视图下方的复合绘图 [Drawable]
     * [setCompoundDrawables]
     *
     * @param id       视图ID
     * @param drawable [Drawable]
     * @return [TextView]
     */
    fun setCompoundDrawableBottom(@IdRes id: Int, drawable: Drawable?): TextView {
        return setCompoundDrawables(id, null, null, null, drawable)
    }

    /**
     * 设置视图的复合绘图 [Drawable]
     * [TextView.setCompoundDrawables]
     *
     * @param id     视图ID
     * @param left   左方的Drawable
     * @param top    上方的Drawable
     * @param right  右方的Drawable
     * @param bottom 下方的Drawable
     * @return [TextView]
     */
    fun setCompoundDrawables(
        @IdRes id: Int,
        left: Drawable?,
        top: Drawable?,
        right: Drawable?,
        bottom: Drawable?
    ): TextView {
        val tv = getView<TextView>(id)
        tv.setCompoundDrawables(left, top, right, bottom)
        return tv
    }

    /**
     * 设置视图的可填充内距
     * [TextView.setCompoundDrawablePadding]
     *
     * @param id      视图ID
     * @param padding 内填充间距
     * @return
     */
    fun setCompoundDrawablePadding(@IdRes id: Int, padding: Int): TextView {
        val tv = getView<TextView>(id)
        tv.compoundDrawablePadding = padding
        return tv
    }

    /**
     * 设置视图的内在的复合绘图 [Drawable]
     * [TextView.setCompoundDrawablesWithIntrinsicBounds]
     *
     * @param id     视图ID
     * @param left   左方的Drawable
     * @param top    上方的Drawable
     * @param right  右方的Drawable
     * @param bottom 下方的Drawable
     * @return [TextView]
     */
    fun setCompoundDrawablesWithIntrinsicBounds(
        @IdRes id: Int,
        left: Drawable?,
        top: Drawable?,
        right: Drawable?,
        bottom: Drawable?
    ): TextView {
        val tv = getView<TextView>(id)
        tv.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom)
        return tv
    }

    /**
     * 设置文本内容
     * [TextView.setText]
     *
     * @param id    视图ID
     * @param resId 字符串资源ID
     * @return [TextView]
     */
    fun setText(@IdRes id: Int, @StringRes resId: Int): TextView {
        val tv = getView<TextView>(id)
        tv.setText(resId)
        return tv
    }

    /**
     * 设置文本内容
     * [TextView.setText]
     *
     * @param id   视图ID
     * @param text 文本
     * @return [TextView]
     */
    fun setText(@IdRes id: Int, text: CharSequence?): TextView {
        val tv = getView<TextView>(id)
        tv.text = text
        return tv
    }

    /**
     * 设置字体颜色
     * [TextView.setTextColor]
     *
     * @param id    视图ID
     * @param color 颜色
     * @return [TextView]
     */
    fun setTextColor(@IdRes id: Int, color: Int): TextView {
        val tv = getView<TextView>(id)
        tv.setTextColor(color)
        return tv
    }

    /**
     * 设置字体颜色
     * [TextView.setTextColor]
     *
     * @param id     视图ID
     * @param colors 颜色状态列表
     * @return [TextView]
     */
    fun setTextColor(@IdRes id: Int, colors: ColorStateList): TextView {
        val tv = getView<TextView>(id)
        tv.setTextColor(colors)
        return tv
    }

    /**
     * 设置字体大小
     * [TextView.setTextSize]
     *
     * @param id   视图ID
     * @param size 字体大小（单位：sp）
     * @return [TextView]
     */
    fun setTextSize(@IdRes id: Int, size: Float): TextView {
        return setTextSize(id, size)
    }

    /**
     * 设置字体大小
     * [TextView.setTextSize]
     *
     * @param id   视图ID
     * @param unit 单位；推荐使用 [TypedValue.COMPLEX_UNIT_SP]
     * @param size 字体大小
     * @return [TextView]
     */
    fun setTextSize(@IdRes id: Int, unit: Int, size: Float): TextView {
        val tv = getView<TextView>(id)
        tv.setTextSize(unit, size)
        return tv
    }

    /**
     * 设置字体
     * [TextView.setTypeface]
     *
     * @param id 视图ID
     * @param tf 字体
     * @return [TextView]
     */
    fun setTypeface(@IdRes id: Int, tf: Typeface?): TextView {
        val tv = getView<TextView>(id)
        tv.setTypeface(tf)
        return tv
    }

    /**
     * 设置字体
     * [TextView.setTypeface]
     *
     * @param id    视图ID
     * @param tf    字体
     * @param style 字体样式
     * @return [TextView]
     */
    fun setTypeface(@IdRes id: Int, tf: Typeface?, style: Int): TextView {
        val tv = getView<TextView>(id)
        tv.setTypeface(tf, style)
        return tv
    }

    /**
     * 添加链接
     * [Linkify.addLinks]
     *
     * @param id   视图ID
     * @param mask 连接掩码；如：[Linkify.ALL]
     * @return [TextView]
     */
    @JvmOverloads
    fun addLinks(@IdRes id: Int, mask: Int = Linkify.ALL): TextView {
        val tv = getView<TextView>(id)
        Linkify.addLinks(tv, mask)
        return tv
    }

    /**
     * 添加链接
     * [Linkify.addLinks]
     *
     * @param id      视图ID
     * @param pattern 正则表达式模式
     * @param scheme  方案
     * @return [TextView]
     */
    fun addLinks(@IdRes id: Int, pattern: Pattern, scheme: String?): TextView {
        val tv = getView<TextView>(id)
        Linkify.addLinks(tv, pattern, scheme)
        return tv
    }

    /**
     * 根据Drawable资源ID设置图像
     * [ImageView.setImageResource]
     *
     * @param id    视图ID
     * @param resId Drawable资源ID
     * @return [ImageView]
     */
    fun setImageResource(@IdRes id: Int, @DrawableRes resId: Int): ImageView {
        val iv = getView<ImageView>(id)
        iv.setImageResource(resId)
        return iv
    }

    /**
     * 根据位图设置图像
     * [ImageView.setImageBitmap]
     *
     * @param id     视图ID
     * @param bitmap 位图
     * @return [ImageView]
     */
    fun setImageBitmap(@IdRes id: Int, bitmap: Bitmap?): ImageView {
        val iv = getView<ImageView>(id)
        iv.setImageBitmap(bitmap)
        return iv
    }

    /**
     * 根据 [Drawable] 设置图像
     * [ImageView.setImageResource]
     *
     * @param id       视图ID
     * @param drawable [Drawable]
     * @return [ImageView]
     */
    fun setImageDrawable(@IdRes id: Int, drawable: Drawable?): ImageView {
        val iv = getView<ImageView>(id)
        iv.setImageDrawable(drawable)
        return iv
    }

    /**
     * 设置是否选中
     * [CompoundButton.setChecked]
     *
     * @param id        视图ID
     * @param isChecked 是否选中
     * @return [CompoundButton]
     */
    fun setChecked(@IdRes id: Int, isChecked: Boolean): CompoundButton {
        val cb = getView<CompoundButton>(id)
        cb.isChecked = isChecked
        return cb
    }

    /**
     * 是否选中
     * [CompoundButton.isChecked]
     *
     * @param id 视图ID
     * @return `true` or `false`
     */
    fun isChecked(@IdRes id: Int): Boolean {
        val cb = getView<CompoundButton>(id)
        return cb.isChecked
    }

    /**
     * 切换
     * [CompoundButton.toggle]
     *
     * @param id 视图ID
     * @return [CompoundButton]
     */
    fun toggle(@IdRes id: Int): CompoundButton {
        val cb = getView<CompoundButton>(id)
        cb.toggle()
        return cb
    }

    /**
     * 设置进度值
     * [ProgressBar.setProgress]
     *
     * @param id       视图ID
     * @param progress 进度
     * @return [ProgressBar]
     */
    fun setProgress(@IdRes id: Int, progress: Int): ProgressBar {
        val progressBar = getView<ProgressBar>(id)
        progressBar.progress = progress
        return progressBar
    }

    /**
     * 设置最大进度值
     * [ProgressBar.setMax]
     *
     * @param id  视图ID
     * @param max 最大进度值
     * @return [ProgressBar]
     */
    fun setMax(@IdRes id: Int, max: Int): ProgressBar {
        val progressBar = getView<ProgressBar>(id)
        progressBar.max = max
        return progressBar
    }

    /**
     * 设置评分
     * [RatingBar.setRating]
     *
     * @param id     视图ID
     * @param rating 评分
     * @return [RatingBar]
     */
    fun setRating(@IdRes id: Int, rating: Float): RatingBar {
        val ratingBar = getView<RatingBar>(id)
        ratingBar.rating = rating
        return ratingBar
    }

    /**
     * 设置评分和最大评分值
     * [RatingBar.setRating] and [RatingBar.setMax]
     *
     * @param id     视图ID
     * @param rating 评分
     * @param max    最大评分值
     * @return [RatingBar]
     */
    fun setRating(@IdRes id: Int, rating: Float, max: Int): RatingBar {
        val ratingBar = getView<RatingBar>(id)
        ratingBar.rating = rating
        ratingBar.max = max
        return ratingBar
    }

    /**
     * 设置星星数量
     * [RatingBar.setNumStars]
     *
     * @param id       视图ID
     * @param numStars 星星数量
     * @return [RatingBar]
     */
    fun setNumStars(@IdRes id: Int, numStars: Int): RatingBar {
        val ratingBar = getView<RatingBar>(id)
        ratingBar.numStars = numStars
        return ratingBar
    }

    /**
     * 设置是否选择
     * [View.setSelected]
     *
     * @param id       视图ID
     * @param selected 是否选择
     * @return [View]
     */
    fun setSelected(@IdRes id: Int, selected: Boolean): View {
        val view = getView<View>(id)
        view.isSelected = selected
        return view
    }

    /**
     * 是否选择
     * [View.isSelected]
     *
     * @param id 视图ID
     * @return `true` or `false`
     */
    fun isSelected(@IdRes id: Int): Boolean {
        return getView<View>(id).isSelected
    }

    /**
     * 设置是否启用
     * [View.setEnabled]
     *
     * @param id      视图ID
     * @param enabled 是否启用
     * @return [View]
     */
    fun setEnabled(@IdRes id: Int, enabled: Boolean): View {
        val view = getView<View>(id)
        view.isEnabled = enabled
        return view
    }

    /**
     * 是否启用
     * [View.isEnabled]
     *
     * @param id 视图ID
     * @return `true` or `false`
     */
    fun isEnabled(@IdRes id: Int): Boolean {
        return getView<View>(id).isEnabled
    }

    //---------------------- 监听事件
    /**
     * 设置点击监听事
     * [View.setOnClickListener]
     *
     * @param id              视图ID
     * @param onClickListener [View.OnClickListener]
     */
    fun setOnClickListener(@IdRes id: Int, onClickListener: View.OnClickListener?) {
        getView<View>(id).setOnClickListener(onClickListener)
    }

    /**
     * 设置触摸监听
     * [View.setOnTouchListener]
     *
     * @param id              视图ID
     * @param onTouchListener [View.OnTouchListener]
     */
    fun setOnTouchListener(@IdRes id: Int, onTouchListener: OnTouchListener?) {
        getView<View>(id).setOnTouchListener(onTouchListener)
    }

    /**
     * 设置长按监听
     * [View.setOnLongClickListener]
     *
     * @param id                  视图ID
     * @param onLongClickListener [View.OnLongClickListener]
     */
    fun setOnLongClickListener(@IdRes id: Int, onLongClickListener: OnLongClickListener?) {
        getView<View>(id).setOnLongClickListener(onLongClickListener)
    }

    /**
     * 设置按键监听
     * [View.setOnKeyListener]
     *
     * @param id            视图ID
     * @param onKeyListener [View.OnKeyListener]
     */
    fun setOnKeyListener(@IdRes id: Int, onKeyListener: View.OnKeyListener?) {
        getView<View>(id).setOnKeyListener(onKeyListener)
    }
}
