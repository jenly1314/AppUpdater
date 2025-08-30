package com.king.app.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment

/**
 * App对话框配置
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
open class AppDialogConfig @JvmOverloads constructor(
    internal val context: Context,
    @LayoutRes layoutId: Int = R.layout.app_dialog
) : BaseDialogConfig(layoutId) {

    val viewHolder by lazy {
        ViewHolder(getDialogView())
    }

    private var dialogView: View? = null

    /**
     * 获取对话框视图
     *
     * @return 对话框视图
     */
    private fun getDialogView(): View {
        if (dialogView == null) {
            dialogView = LayoutInflater.from(context).inflate(layoutId, null)
        }
        return dialogView!!
    }

    /**
     * 根据视图ID查找对应的视图
     *
     * @param id  视图ID
     * @param <T> 对应的视图类
     * @return 视图ID对应的视图
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : View?> findView(@IdRes id: Int): T? {
        return viewHolder.getView<View?>(id) as? T
    }

    /**
     * 通过[AppDialogConfig] 创建一个视图
     *
     * @return [View]
     */
    fun buildAppDialogView(): View {
        if (title != null) {
            findView<TextView>(titleId)?.apply {
                text = title
                visibility = if (hideTitle) View.GONE else View.VISIBLE
            }
        }

        if (content != null) {
            findView<TextView>(contentId)?.apply {
                text = content
            }
        }

        findView<TextView>(cancelId)?.apply {
            if (cancel != null) {
                text = cancel
            }
            visibility = if (hideCancel) View.GONE else View.VISIBLE
            setOnClickListener(if (onClickCancel != null) onClickCancel else AppDialog.onClickDismissDialog)
        }

        findView<View>(lineId)?.apply {
            visibility = if (hideCancel) View.GONE else View.VISIBLE
        }

        findView<TextView>(confirmId)?.apply {
            if (confirm != null) {
                text = confirm
            }
            setOnClickListener(if (onClickConfirm != null) onClickConfirm else AppDialog.onClickDismissDialog)
        }

        return dialogView!!
    }

    /**
     * 设置标题视图ID
     *
     * @param titleId 视图ID
     * @return [BaseDialogConfig]
     */
    override fun setTitleId(@IdRes titleId: Int) = apply {
        this.titleId = titleId
    }

    /**
     * 设置Dialog样式ID(仅对Dialog有效，如果使用的是DialogFragment，请使用[.setAnimationStyleId])
     *
     * @param styleId
     * @return [BaseDialogConfig]
     */
    override fun setStyleId(@StyleRes styleId: Int) = apply {
        this.styleId = styleId
    }

    /**
     * 对话框动画样式ID (仅对DialogFragment有效，如果使用的是Dialog，请使用[.setStyleId])
     *
     * @param animationStyleId
     * @return [BaseDialogConfig]
     */
    override fun setAnimationStyleId(@StyleRes animationStyleId: Int) = apply {
        this.animationStyleId = animationStyleId
    }

    /**
     * 设置内容视图ID
     *
     * @param contentId 内容视图ID
     * @return [BaseDialogConfig]
     */
    override fun setContentId(@IdRes contentId: Int) = apply {
        this.contentId = contentId
    }

    /**
     * 设置取消按钮视图ID
     *
     * @param cancelId 取消按钮视图ID
     * @return [BaseDialogConfig]
     */
    override fun setCancelId(@IdRes cancelId: Int) = apply {
        this.cancelId = cancelId
    }

    /**
     * 设置确定按钮视图ID
     *
     * @param confirmId 确定按钮视图ID
     * @return [BaseDialogConfig]
     */
    override fun setConfirmId(@IdRes confirmId: Int) = apply {
        this.confirmId = confirmId
    }

    /**
     * 设置分割线视图ID
     *
     * @param lineId 分割线视图ID
     * @return [BaseDialogConfig]
     */
    override fun setLineId(@IdRes lineId: Int) = apply {
        this.lineId = lineId
    }

    /**
     * 设置标题
     *
     * @param title 标题
     * @return [BaseDialogConfig]
     */
    override fun setTitle(title: CharSequence?) = apply {
        this.title = title
    }

    /**
     * 设置标题
     *
     * @param context 上下文
     * @param resId   标题资源ID
     * @return [BaseDialogConfig]
     */
    override fun setTitle(context: Context, @StringRes resId: Int) = apply {
        this.title = context.getString(resId)
    }

    /**
     * 设置文本内容
     *
     * @param content 文本内容
     * @return [BaseDialogConfig]
     */
    override fun setContent(content: CharSequence?) = apply {
        this.content = content
    }

    /**
     * 设置取消按钮文本内容
     *
     * @param cancel 取消按钮文本内容
     * @return [BaseDialogConfig]
     */
    override fun setCancel(cancel: CharSequence?) = apply {
        this.cancel = cancel
    }

    /**
     * 设置取消按钮文本内容
     *
     * @param context 上下文
     * @param resId   取消按钮文本内容资源ID
     * @return [BaseDialogConfig]
     */
    override fun setCancel(context: Context, @StringRes resId: Int) = apply {
        this.cancel = context.getString(resId)
    }

    /**
     * 设置确定按钮文本内容
     *
     * @param confirm 确定按钮文本内容
     * @return [BaseDialogConfig]
     */
    override fun setConfirm(confirm: CharSequence?) = apply {
        this.confirm = confirm
    }

    /**
     * 设置确定按钮文本内容
     *
     * @param context 上下文
     * @param resId   确定按钮文本内容资源ID
     * @return [BaseDialogConfig]
     */
    override fun setConfirm(context: Context, @StringRes resId: Int) = apply {
        this.confirm = context.getString(resId)
    }

    /**
     * 设置是否隐藏取消按钮
     *
     * @param hideCancel 是否隐藏取消按钮
     * @return [BaseDialogConfig]
     */
    override fun setHideCancel(hideCancel: Boolean) = apply {
        this.hideCancel = hideCancel
    }

    /**
     * 设置是否隐藏标题
     *
     * @param hideTitle 是否隐藏标题
     * @return [BaseDialogConfig]
     */
    override fun setHideTitle(hideTitle: Boolean) = apply {
        this.hideTitle = hideTitle
    }

    /**
     * 设置Dialog的宽度比例，根据屏幕宽度计算得来
     *
     * @param widthRatio Dialog的宽度比例；默认值：[AppDialog.DEFAULT_WIDTH_RATIO]
     * @return [BaseDialogConfig]
     */
    override fun setWidthRatio(widthRatio: Float) = apply {
        this.widthRatio = widthRatio
    }

    /**
     * 设置Dialog的对齐方式  [WindowManager.LayoutParams.gravity]
     *
     * @param gravity Dialog的对齐方式
     * @return [BaseDialogConfig]
     */
    override fun setGravity(gravity: Int) = apply {
        this.gravity = gravity
    }

    /**
     * 设置“取消”按钮点击监听，不设置默认点击关闭对话框
     *
     * @param onClickCancel “取消”按钮点击监听
     * @return [BaseDialogConfig]
     */
    override fun setOnClickCancel(onClickCancel: View.OnClickListener?) = apply {
        this.onClickCancel = onClickCancel
    }

    /**
     * 设置“确定”按钮点击监听，不设置默认点击关闭对话框
     *
     * @param onClickConfirm “确定”按钮点击监听
     * @return [BaseDialogConfig]
     */
    override fun setOnClickConfirm(onClickConfirm: View.OnClickListener?) = apply {
        this.onClickConfirm = onClickConfirm
    }

    /**
     * [WindowManager.LayoutParams.x]
     *
     * @param x x轴坐标
     */
    override fun setX(x: Int) = apply {
        this.x = x
    }

    /**
     * [WindowManager.LayoutParams.y]
     *
     * @param y y轴坐标
     * @return [BaseDialogConfig]
     */
    override fun setY(y: Int) = apply {
        this.y = y
    }

    /**
     * [WindowManager.LayoutParams.verticalMargin]
     *
     * @param verticalMargin 垂直边距
     */
    override fun setVerticalMargin(verticalMargin: Float) = apply {
        this.verticalMargin = verticalMargin
    }

    /**
     * [WindowManager.LayoutParams.horizontalMargin]
     *
     * @param horizontalMargin 水平边距
     * @return [BaseDialogConfig]
     */
    override fun setHorizontalMargin(horizontalMargin: Float) = apply {
        this.horizontalMargin = horizontalMargin
    }

    /**
     * [WindowManager.LayoutParams.horizontalWeight]
     *
     * @param horizontalWeight 水平方向权重
     * @return [BaseDialogConfig]
     */
    override fun setHorizontalWeight(horizontalWeight: Float) = apply {
        this.horizontalWeight = horizontalWeight
    }

    /**
     * [WindowManager.LayoutParams.verticalWeight]
     *
     * @param verticalWeight 垂直方向权重
     * @return [BaseDialogConfig]
     */
    override fun setVerticalWeight(verticalWeight: Float) = apply {
        this.verticalWeight = verticalWeight
    }

}

/**
 * DSL
 */
@JvmSynthetic
fun appDialogConfig(
    context: Context,
    @LayoutRes layoutId: Int = R.layout.app_dialog,
    block: AppDialogConfig.() -> Unit
): AppDialogConfig {
    return AppDialogConfig(context, layoutId).apply(block)
}

/**
 * 扩展DSL
 */
@JvmName("appDialogConfigWithContext")
@JvmSynthetic
fun Context.appDialogConfig(
    @LayoutRes layoutId: Int = R.layout.app_dialog,
    block: AppDialogConfig.() -> Unit
): AppDialogConfig {
    return AppDialogConfig(this, layoutId).apply(block)
}

/**
 * 扩展DSL
 */
@JvmName("appDialogConfigWithContext")
@JvmSynthetic
fun Fragment.appDialogConfig(
    @LayoutRes layoutId: Int = R.layout.app_dialog,
    block: AppDialogConfig.() -> Unit
): AppDialogConfig {
    return AppDialogConfig(requireContext(), layoutId).apply(block)
}
