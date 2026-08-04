package com.king.app.dialog.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import com.king.app.dialog.AppDialog
import com.king.app.dialog.R
import com.king.app.dialog.ViewHolder

/**
 * DialogFragment基类
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
abstract class BaseDialogFragment : DialogFragment() {

    @set:JvmSynthetic
    var viewHolder: ViewHolder? = null
        internal set

    /**
     * 根视图
     */
    @set:JvmSynthetic
    var rootView: View? = null
        internal set

    /**
     * 点击监听器 - 解散对话框
     */
    val onClickDismissDialog: View.OnClickListener = View.OnClickListener { dismiss() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(getRootLayoutId(), container, false)
        this.rootView = rootView
        viewHolder = createViewHolder(rootView)
        init(rootView)
        return rootView
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCanceledOnTouchOutside(false)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        initDialogWindow(
            requireContext(),
            dialog,
            Gravity.NO_GRAVITY,
            AppDialog.DEFAULT_WIDTH_RATIO,
            0,
            0,
            0f,
            0f,
            0f,
            0f,
            R.style.app_dialog_scale_animation
        )
        return dialog
    }

    /**
     * 创建[ViewHolder]
     */
    protected open fun createViewHolder(rootView: View): ViewHolder {
        return ViewHolder(rootView)
    }

    /**
     * 初始化对话框视图
     *
     * @param context          上下文
     * @param dialog           对话框
     * @param gravity          对齐方式
     * @param widthRatio       宽度比例，根据屏幕宽度计算得来
     * @param x                x轴偏移量，需与 [gravity] 结合使用
     * @param y                y轴偏移量，需与 [gravity] 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin   垂直方向边距
     * @param horizontalWeight 水平方向权重
     * @param verticalWeight   垂直方向权重
     * @param animationStyleId 话框动画样式ID
     */
    protected open fun initDialogWindow(
        context: Context,
        dialog: Dialog,
        gravity: Int,
        widthRatio: Float,
        x: Int,
        y: Int,
        horizontalMargin: Float,
        verticalMargin: Float,
        horizontalWeight: Float,
        verticalWeight: Float,
        animationStyleId: Int
    ) {
        setDialogWindow(
            context,
            dialog,
            gravity,
            widthRatio,
            x,
            y,
            horizontalMargin,
            verticalMargin,
            horizontalWeight,
            verticalWeight,
            animationStyleId
        )
    }

    /**
     * 设置对话框窗口配置
     *
     * @param context
     * @param dialog
     * @param gravity          Dialog的对齐方式
     * @param widthRatio       宽度比例，根据屏幕宽度计算得来
     * @param x                x轴偏移量，需与 [gravity] 结合使用
     * @param y                y轴偏移量，需与 [gravity] 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin   垂直方向边距
     * @param horizontalWeight 水平方向权重
     * @param verticalWeight   垂直方向权重
     * @param animationStyleId 动画样式
     */
    @SuppressLint("UseKtx")
    private fun setDialogWindow(
        context: Context,
        dialog: Dialog,
        gravity: Int,
        widthRatio: Float,
        x: Int,
        y: Int,
        horizontalMargin: Float,
        verticalMargin: Float,
        horizontalWeight: Float,
        verticalWeight: Float,
        animationStyleId: Int
    ) {
        val window = dialog.window
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = window.attributes
        lp.windowAnimations = animationStyleId
        lp.width = (context.resources.displayMetrics.widthPixels * widthRatio).toInt()
        lp.gravity = gravity
        lp.x = x
        lp.y = y
        lp.horizontalMargin = horizontalMargin
        lp.verticalMargin = verticalMargin
        lp.horizontalWeight = horizontalWeight
        lp.verticalWeight = verticalWeight
        window.attributes = lp
    }

    /**
     * 根据视图ID获取对应的视图
     *
     * @param id  视图ID
     * @param <T> 对应的视图类
     * @return 视图ID对应的视图
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : View?> getView(@IdRes id: Int): T? {
        return viewHolder?.getView<View?>(id) as? T
    }

    /**
     * 获取根布局ID
     *
     * @return 根布局ID
     */
    abstract fun getRootLayoutId(): Int

    /**
     * 初始化
     *
     * @param rootView
     */
    abstract fun init(rootView: View)
}
