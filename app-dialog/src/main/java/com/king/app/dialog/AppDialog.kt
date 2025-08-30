package com.king.app.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.king.app.dialog.fragment.AppDialogFragment

/**
 * AppDialog：是一个通用的 Dialog 和 DialogFragment 组件库，支持任意样式和交互的个性化实现，帮助开发者快速构建灵活的提示对话框。
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
object AppDialog {

    /**
     * 默认对话框宽度比例（基于屏幕的宽度）
     */
    const val DEFAULT_WIDTH_RATIO = 0.85f

    /**
     * 对话框
     */
    @JvmStatic
    private var dialog: Dialog? = null

    /**
     * 标签
     */
    @JvmStatic
    private var tag: String? = null

    //-------------------------------------------

    /**
     * 点击监听器 - 解散对话框
     */
    @JvmStatic
    val onClickDismissDialog = View.OnClickListener {
        dismissDialog()
    }

    //-------------------------------------------

    /**
     * [DialogFragment.dismiss]
     *
     * @param fragmentManager [FragmentManager]
     */
    @JvmStatic
    fun dismissDialogFragment(fragmentManager: FragmentManager) {
        val dialogFragment = fragmentManager.findFragmentByTag(tag) as? DialogFragment
        dismissDialogFragment(dialogFragment)
        tag = null
    }

    /**
     * [DialogFragment.dismiss]
     *
     * @param fragmentManager [FragmentManager]
     * @param tag dialogFragment对应的tag
     */
    @JvmStatic
    fun dismissDialogFragment(fragmentManager: FragmentManager, tag: String) {
        val dialogFragment = fragmentManager.findFragmentByTag(tag) as? DialogFragment
        dismissDialogFragment(dialogFragment)
    }

    /**
     * [DialogFragment.dismiss]
     *
     * @param dialogFragment [DialogFragment]
     */
    @JvmStatic
    fun dismissDialogFragment(dialogFragment: DialogFragment?) {
        dialogFragment?.dismiss()
    }

    //-------------------------------------------

    /**
     * 显示DialogFragment
     *
     * @param fragmentManager [FragmentManager]
     * @param config [BaseDialogConfig]
     * @return dialogFragment对应的tag
     */
    @JvmStatic
    fun showDialogFragment(fragmentManager: FragmentManager, config: BaseDialogConfig): String {
        val dialogFragment = AppDialogFragment.newInstance(config)
        return showDialogFragment(fragmentManager, dialogFragment)
    }

    /**
     * 显示DialogFragment
     *
     * @param fragmentManager [FragmentManager]
     * @param dialogFragment [DialogFragment]
     * @return dialogFragment对应的tag
     */
    @JvmStatic
    fun showDialogFragment(
        fragmentManager: FragmentManager,
        dialogFragment: DialogFragment
    ): String {
        val tag = dialogFragment.tag ?: dialogFragment.javaClass.simpleName
        showDialogFragment(fragmentManager, dialogFragment, tag)
        this.tag = tag
        return tag
    }

    /**
     * 显示DialogFragment
     *
     * @param fragmentManager [FragmentManager]
     * @param dialogFragment [DialogFragment]
     * @param tag dialogFragment对应的tag
     * @return
     */
    @JvmStatic
    fun showDialogFragment(
        fragmentManager: FragmentManager,
        dialogFragment: DialogFragment,
        tag: String
    ) {
        dismissDialogFragment(fragmentManager)
        dialogFragment.show(fragmentManager, tag)
        this.tag = tag
    }

    //-------------------------------------------

    /**
     * 显示对话框
     *
     * @param config   对话框配置 [AppDialogConfig]
     * @param cancelable 是否可取消（默认为true，false则拦截back键）
     */
    @JvmOverloads
    @JvmStatic
    fun showDialog(config: AppDialogConfig, cancelable: Boolean = true) {
        showDialog(
            context = config.context,
            contentView = config.buildAppDialogView(),
            styleId = config.styleId,
            gravity = config.gravity,
            widthRatio = config.widthRatio,
            x = config.x,
            y = config.y,
            horizontalMargin = config.horizontalMargin,
            verticalMargin = config.verticalMargin,
            horizontalWeight = config.horizontalWeight,
            verticalWeight = config.verticalWeight,
            cancelable = cancelable
        )
    }

    /**
     * 显示对话框
     *
     * @param config   对话框配置 [AppDialogConfig]
     * @param cancelable 是否可取消（默认为true，false则拦截back键）
     */
    @JvmOverloads
    @JvmStatic
    fun showDialog(context: Context, config: AppDialogConfig, cancelable: Boolean = true) {
        showDialog(
            context = context,
            contentView = config.buildAppDialogView(),
            styleId = config.styleId,
            gravity = config.gravity,
            widthRatio = config.widthRatio,
            x = config.x,
            y = config.y,
            horizontalMargin = config.horizontalMargin,
            verticalMargin = config.verticalMargin,
            horizontalWeight = config.horizontalWeight,
            verticalWeight = config.verticalWeight,
            cancelable = cancelable
        )
    }

    /**
     * 显示对话框
     *
     * @param context          上下文
     * @param contentView      对话框内容视图
     * @param styleId          Dialog样式
     * @param gravity          Dialog的对齐方式
     * @param widthRatio       宽度比例，根据屏幕宽度计算得来
     * @param x                x轴偏移量，需与 [gravity] 结合使用
     * @param y                y轴偏移量，需与 [gravity] 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin   垂直方向边距
     * @param horizontalWeight 水平方向权重
     * @param verticalWeight   垂直方向权重
     * @param cancelable       是否可取消（默认为true，false则拦截back键）
     */
    @JvmOverloads
    @JvmStatic
    fun showDialog(
        context: Context,
        contentView: View,
        @StyleRes styleId: Int = R.style.app_dialog,
        gravity: Int = Gravity.NO_GRAVITY,
        widthRatio: Float = DEFAULT_WIDTH_RATIO,
        x: Int = 0,
        y: Int = 0,
        horizontalMargin: Float = 0f,
        verticalMargin: Float = 0f,
        horizontalWeight: Float = 0f,
        verticalWeight: Float = 0f,
        cancelable: Boolean = true
    ) {
        dismissDialog()
        dialog = createDialog(
            context = context,
            contentView = contentView,
            styleId = styleId,
            gravity = gravity,
            widthRatio = widthRatio,
            x = x,
            y = y,
            horizontalMargin = horizontalMargin,
            verticalMargin = verticalMargin,
            horizontalWeight = horizontalWeight,
            verticalWeight = verticalWeight,
            cancelable = cancelable
        )
        dialog?.show()
    }

    /**
     * 设置对话框窗口配置
     * @param context          上下文
     * @param dialog           Dialog对话框
     * @param gravity          Dialog的对齐方式
     * @param widthRatio       宽度比例，根据屏幕宽度计算得来
     * @param x                x轴偏移量，需与 [gravity] 结合使用
     * @param y                y轴偏移量，需与 [gravity] 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin   垂直方向边距
     * @param horizontalWeight 水平方向权重
     * @param verticalWeight   垂直方向权重
     */
    @JvmStatic
    private fun setDialogWindow(
        context: Context,
        dialog: Dialog,
        gravity: Int = Gravity.NO_GRAVITY,
        widthRatio: Float = DEFAULT_WIDTH_RATIO,
        x: Int = 0,
        y: Int = 0,
        horizontalMargin: Float = 0f,
        verticalMargin: Float = 0f,
        horizontalWeight: Float = 0f,
        verticalWeight: Float = 0f
    ) {
        val window = dialog.window
        val lp = window?.attributes
        lp?.width = (context.resources.displayMetrics.widthPixels * widthRatio).toInt()
        lp?.gravity = gravity
        lp?.x = x
        lp?.y = y
        lp?.horizontalMargin = horizontalMargin
        lp?.verticalMargin = verticalMargin
        lp?.horizontalWeight = horizontalWeight
        lp?.verticalWeight = verticalWeight
        window?.attributes = lp
    }

    /**
     * 创建对话框
     * @param config   对话框配置 [AppDialogConfig]
     * @param cancelable 是否可取消（默认为true，false则拦截back键）
     */
    @JvmOverloads
    @JvmStatic
    fun createDialog(
        config: AppDialogConfig,
        cancelable: Boolean = true
    ): Dialog {
        return createDialog(
            context = config.context,
            contentView = config.buildAppDialogView(),
            styleId = config.styleId,
            gravity = config.gravity,
            widthRatio = config.widthRatio,
            x = config.x,
            y = config.y,
            horizontalMargin = config.horizontalMargin,
            verticalMargin = config.verticalMargin,
            horizontalWeight = config.horizontalWeight,
            verticalWeight = config.verticalWeight,
            cancelable = cancelable
        )
    }

    /**
     * 创建对话框
     * @param context          上下文
     * @param contentView      对话框内容视图
     * @param styleId          Dialog样式
     * @param gravity          Dialog的对齐方式
     * @param widthRatio       宽度比例，根据屏幕宽度计算得来
     * @param x                x轴偏移量，需与 [gravity] 结合使用
     * @param y                y轴偏移量，需与 [gravity] 结合使用
     * @param horizontalMargin 水平方向边距
     * @param verticalMargin   垂直方向边距
     * @param horizontalWeight 水平方向权重
     * @param verticalWeight   垂直方向权重
     * @param cancelable       是否可取消（默认为true，false则拦截back键）
     */
    @JvmOverloads
    fun createDialog(
        context: Context,
        contentView: View,
        @StyleRes styleId: Int = R.style.app_dialog,
        gravity: Int = Gravity.NO_GRAVITY,
        widthRatio: Float = DEFAULT_WIDTH_RATIO,
        x: Int = 0,
        y: Int = 0,
        horizontalMargin: Float = 0f,
        verticalMargin: Float = 0f,
        horizontalWeight: Float = 0f,
        verticalWeight: Float = 0f,
        cancelable: Boolean = true
    ): Dialog {
        val dialog = Dialog(context, styleId)
        dialog.setContentView(contentView)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener { _: DialogInterface, keyCode: Int, event: KeyEvent ->
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (cancelable) {
                    dismissDialog()
                }
                return@setOnKeyListener true
            }
            false
        }
        setDialogWindow(
            context = context,
            dialog = dialog,
            gravity = gravity,
            widthRatio = widthRatio,
            x = x,
            y = y,
            horizontalMargin = horizontalMargin,
            verticalMargin = verticalMargin,
            horizontalWeight = horizontalWeight,
            verticalWeight = verticalWeight
        )
        return dialog
    }

    /**
     * 获取Dialog
     *
     * @return [dialog]
     */
    @JvmStatic
    fun getDialog(): Dialog? {
        return dialog
    }

    /**
     * [Dialog.dismiss]
     */
    @JvmStatic
    fun dismissDialog() {
        dismissDialog(dialog)
        dialog = null
    }

    /**
     * [Dialog.dismiss]
     *
     * @param dialog [Dialog]
     */
    @JvmStatic
    fun dismissDialog(dialog: Dialog?) {
        dialog?.dismiss()
    }

}
