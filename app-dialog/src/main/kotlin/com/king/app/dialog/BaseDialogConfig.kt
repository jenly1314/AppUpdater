package com.king.app.dialog

import android.content.Context
import android.os.Parcelable
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import kotlinx.parcelize.Parcelize

/**
 * 对话框配置基类
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
@Parcelize
open class BaseDialogConfig @JvmOverloads constructor(
    @LayoutRes val layoutId: Int = R.layout.app_dialog
) : Parcelable {

    /**
     * 标题视图ID
     */
    @set:JvmSynthetic
    @IdRes
    var titleId: Int = R.id.tvDialogTitle

    /**
     * 内容视图ID
     */
    @set:JvmSynthetic
    @IdRes
    var contentId: Int = R.id.tvDialogContent

    /**
     * 取消视图ID（左边按钮）
     */
    @set:JvmSynthetic
    @IdRes
    var cancelId: Int = R.id.btnDialogCancel

    /**
     * 确定视图ID（右边按钮）
     */
    @set:JvmSynthetic
    @IdRes
    var confirmId: Int = R.id.btnDialogConfirm

    /**
     * 按钮中间分割线ID
     */
    @set:JvmSynthetic
    @IdRes
    var lineId: Int = R.id.line

    /**
     * 样式ID（仅对Dialog有效，如果使用的是DialogFragment，请使用[setAnimationStyleId]）
     */
    @set:JvmSynthetic
    @StyleRes
    var styleId: Int = R.style.app_dialog

    /**
     * 对话框动画样式ID（仅对DialogFragment有效，如果使用的是Dialog，请使用[styleId]）
     */
    @set:JvmSynthetic
    @StyleRes
    var animationStyleId: Int = R.style.app_dialog_scale_animation

    /**
     * 标题文本
     */
    @set:JvmSynthetic
    var title: CharSequence? = null

    /**
     * 内容文本
     */
    @set:JvmSynthetic
    var content: CharSequence? = null

    /**
     * 取消按钮文本
     */
    @set:JvmSynthetic
    var cancel: CharSequence? = null

    /**
     * 确定按钮文本
     */
    @set:JvmSynthetic
    var confirm: CharSequence? = null

    /**
     * 是否隐藏取消按钮，如果隐藏取消按钮，则底部只显示一个按钮
     */
    @set:JvmSynthetic
    var hideCancel: Boolean = false

    /**
     * 是否隐藏标题
     */
    @set:JvmSynthetic
    var hideTitle: Boolean = false

    /**
     * 宽度比例，根据屏幕宽度计算得来
     */
    @set:JvmSynthetic
    var widthRatio: Float = AppDialog.DEFAULT_WIDTH_RATIO

    /**
     * Dialog对齐方式 [WindowManager.LayoutParams.gravity]
     */
    @set:JvmSynthetic
    var gravity: Int = Gravity.NO_GRAVITY

    /**
     * [WindowManager.LayoutParams.x]
     */
    @set:JvmSynthetic
    var x: Int = 0

    /**
     * [WindowManager.LayoutParams.y]
     */
    @set:JvmSynthetic
    var y: Int = 0

    /**
     * [WindowManager.LayoutParams.verticalMargin]
     */
    @set:JvmSynthetic
    var verticalMargin: Float = 0f

    /**
     * [WindowManager.LayoutParams.horizontalMargin]
     */
    @set:JvmSynthetic
    var horizontalMargin: Float = 0f

    /**
     * [WindowManager.LayoutParams.horizontalWeight]
     */
    @set:JvmSynthetic
    var horizontalWeight: Float = 0f

    /**
     * [WindowManager.LayoutParams.verticalWeight]
     */
    @set:JvmSynthetic
    var verticalWeight: Float = 0f

    /**
     * 点击“取消”按钮监听器
     */
    @set:JvmSynthetic
    var onClickCancel: View.OnClickListener? = null

    /**
     * 点击“确定”按钮监听器
     */
    @set:JvmSynthetic
    var onClickConfirm: View.OnClickListener? = null

    /**
     * 设置标题视图ID
     *
     * @param titleId 视图ID
     * @return [BaseDialogConfig]
     */
    open fun setTitleId(@IdRes titleId: Int) = apply {
        this.titleId = titleId
    }

    /**
     * 设置Dialog样式ID(仅对Dialog有效，如果使用的是DialogFragment，请使用[.setAnimationStyleId])
     *
     * @param styleId
     * @return [BaseDialogConfig]
     */
    open fun setStyleId(@StyleRes styleId: Int) = apply {
        this.styleId = styleId
    }

    /**
     * 对话框动画样式ID (仅对DialogFragment有效，如果使用的是Dialog，请使用[.setStyleId])
     *
     * @param animationStyleId
     * @return [BaseDialogConfig]
     */
    open fun setAnimationStyleId(@StyleRes animationStyleId: Int) = apply {
        this.animationStyleId = animationStyleId
    }

    /**
     * 设置内容视图ID
     *
     * @param contentId 内容视图ID
     * @return [BaseDialogConfig]
     */
    open fun setContentId(@IdRes contentId: Int) = apply {
        this.contentId = contentId
    }

    /**
     * 设置取消按钮视图ID
     *
     * @param cancelId 取消按钮视图ID
     * @return [BaseDialogConfig]
     */
    open fun setCancelId(@IdRes cancelId: Int) = apply {
        this.cancelId = cancelId
    }

    /**
     * 设置确定按钮视图ID
     *
     * @param confirmId 确定按钮视图ID
     * @return [BaseDialogConfig]
     */
    open fun setConfirmId(@IdRes confirmId: Int) = apply {
        this.confirmId = confirmId
    }

    /**
     * 设置分割线视图ID
     *
     * @param lineId 分割线视图ID
     * @return [BaseDialogConfig]
     */
    open fun setLineId(@IdRes lineId: Int) = apply {
        this.lineId = lineId
    }

    /**
     * 设置标题
     *
     * @param title 标题
     * @return [BaseDialogConfig]
     */
    open fun setTitle(title: CharSequence?) = apply {
        this.title = title
    }

    /**
     * 设置标题
     *
     * @param context 上下文
     * @param resId   标题资源ID
     * @return [BaseDialogConfig]
     */
    open fun setTitle(context: Context, @StringRes resId: Int) = apply {
        this.title = context.getString(resId)
    }

    /**
     * 设置文本内容
     *
     * @param content 文本内容
     * @return [BaseDialogConfig]
     */
    open fun setContent(content: CharSequence?) = apply {
        this.content = content
    }

    /**
     * 设置取消按钮文本内容
     *
     * @param cancel 取消按钮文本内容
     * @return [BaseDialogConfig]
     */
    open fun setCancel(cancel: CharSequence?) = apply {
        this.cancel = cancel
    }

    /**
     * 设置取消按钮文本内容
     *
     * @param context 上下文
     * @param resId   取消按钮文本内容资源ID
     * @return [BaseDialogConfig]
     */
    open fun setCancel(context: Context, @StringRes resId: Int) = apply {
        this.cancel = context.getString(resId)
    }

    /**
     * 设置确定按钮文本内容
     *
     * @param confirm 确定按钮文本内容
     * @return [BaseDialogConfig]
     */
    open fun setConfirm(confirm: CharSequence?) = apply {
        this.confirm = confirm
    }

    /**
     * 设置确定按钮文本内容
     *
     * @param context 上下文
     * @param resId   确定按钮文本内容资源ID
     * @return [BaseDialogConfig]
     */
    open fun setConfirm(context: Context, @StringRes resId: Int) = apply {
        this.confirm = context.getString(resId)
    }

    /**
     * 设置是否隐藏取消按钮
     *
     * @param hideCancel 是否隐藏取消按钮
     * @return [BaseDialogConfig]
     */
    open fun setHideCancel(hideCancel: Boolean) = apply {
        this.hideCancel = hideCancel
    }

    /**
     * 设置是否隐藏标题
     *
     * @param hideTitle 是否隐藏标题
     * @return [BaseDialogConfig]
     */
    open fun setHideTitle(hideTitle: Boolean) = apply {
        this.hideTitle = hideTitle
    }

    /**
     * 设置Dialog的宽度比例，根据屏幕宽度计算得来
     *
     * @param widthRatio Dialog的宽度比例；默认值：[AppDialog.DEFAULT_WIDTH_RATIO]
     * @return [BaseDialogConfig]
     */
    open fun setWidthRatio(widthRatio: Float) = apply {
        this.widthRatio = widthRatio
    }

    /**
     * 设置Dialog的对齐方式  [WindowManager.LayoutParams.gravity]
     *
     * @param gravity Dialog的对齐方式
     * @return [BaseDialogConfig]
     */
    open fun setGravity(gravity: Int) = apply {
        this.gravity = gravity
    }

    /**
     * 设置“取消”按钮点击监听，不设置默认点击关闭对话框
     *
     * @param onClickCancel “取消”按钮点击监听
     * @return [BaseDialogConfig]
     */
    open fun setOnClickCancel(onClickCancel: View.OnClickListener?) = apply {
        this.onClickCancel = onClickCancel
    }

    /**
     * 设置“确定”按钮点击监听，不设置默认点击关闭对话框
     *
     * @param onClickConfirm “确定”按钮点击监听
     * @return [BaseDialogConfig]
     */
    open fun setOnClickConfirm(onClickConfirm: View.OnClickListener?) = apply {
        this.onClickConfirm = onClickConfirm
    }

    /**
     * [WindowManager.LayoutParams.x]
     *
     * @param x x轴坐标
     */
    open fun setX(x: Int) = apply {
        this.x = x
    }

    /**
     * [WindowManager.LayoutParams.y]
     *
     * @param y y轴坐标
     * @return [BaseDialogConfig]
     */
    open fun setY(y: Int) = apply {
        this.y = y
    }

    /**
     * [WindowManager.LayoutParams.verticalMargin]
     *
     * @param verticalMargin 垂直边距
     */
    open fun setVerticalMargin(verticalMargin: Float) = apply {
        this.verticalMargin = verticalMargin
    }

    /**
     * [WindowManager.LayoutParams.horizontalMargin]
     *
     * @param horizontalMargin 水平边距
     * @return [BaseDialogConfig]
     */
    open fun setHorizontalMargin(horizontalMargin: Float) = apply {
        this.horizontalMargin = horizontalMargin
    }

    /**
     * [WindowManager.LayoutParams.horizontalWeight]
     *
     * @param horizontalWeight 水平方向权重
     * @return [BaseDialogConfig]
     */
    open fun setHorizontalWeight(horizontalWeight: Float) = apply {
        this.horizontalWeight = horizontalWeight
    }

    /**
     * [WindowManager.LayoutParams.verticalWeight]
     *
     * @param verticalWeight 垂直方向权重
     * @return [BaseDialogConfig]
     */
    open fun setVerticalWeight(verticalWeight: Float) = apply {
        this.verticalWeight = verticalWeight
    }

}

/**
 * DSL
 */
@JvmSynthetic
fun baseDialogConfig(
    @LayoutRes layoutId: Int = R.layout.app_dialog,
    block: BaseDialogConfig.() -> Unit
): BaseDialogConfig {
    return BaseDialogConfig(layoutId).apply(block)
}
