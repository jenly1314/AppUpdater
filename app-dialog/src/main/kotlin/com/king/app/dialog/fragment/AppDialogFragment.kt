package com.king.app.dialog.fragment

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.king.app.dialog.BaseDialogConfig
import com.king.app.dialog.R

/**
 * App对话框：封装便捷的对话框API，使用时更简单
 *
 * @author <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * <p>
 * <a href="https://github.com/jenly1314">Follow me</a>
 */
open class AppDialogFragment : BaseDialogFragment() {

    /**
     * 对话框配置
     */
    private var config: BaseDialogConfig? = null

    override fun getRootLayoutId(): Int {
        return config?.layoutId ?: R.layout.app_dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            config = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.getParcelable(
                    BaseDialogConfig::class.java.simpleName,
                    BaseDialogConfig::class.java
                )
            } else {
                it.getParcelable(BaseDialogConfig::class.java.simpleName)
            }
        }
    }

    /**
     * 初始化
     *
     * @param rootView
     */
    override fun init(rootView: View) {
        config?.apply {
            if (title != null) {
                getView<TextView>(titleId)?.apply {
                    text = title
                    visibility = if (hideTitle) View.GONE else View.VISIBLE
                }
            }

            if (content != null) {
                getView<TextView>(contentId)?.apply {
                    text = content
                }
            }

            getView<TextView>(cancelId)?.apply {
                if (cancel != null) {
                    text = cancel
                }
                visibility = if (hideCancel) View.GONE else View.VISIBLE
                setOnClickListener(if (onClickCancel != null) onClickCancel else onClickDismissDialog)
            }

            getView<View>(lineId)?.apply {
                visibility = if (hideCancel) View.GONE else View.VISIBLE
            }

            getView<TextView>(confirmId)?.apply {
                if (confirm != null) {
                    text = confirm
                }
                setOnClickListener(if (onClickConfirm != null) onClickConfirm else onClickDismissDialog)
            }
        }
    }

    /**
     * 初始化对话框
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
    override fun initDialogWindow(
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
        config?.let {
            super.initDialogWindow(
                context,
                dialog,
                it.gravity,
                it.widthRatio,
                it.x,
                it.y,
                it.horizontalMargin,
                it.verticalMargin,
                it.horizontalWeight,
                it.verticalWeight,
                it.animationStyleId
            )
        } ?: run {
            super.initDialogWindow(
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
    }

    companion object {
        /**
         * 新建一个 [AppDialogFragment] 实例
         *
         * @param config
         * @return
         */
        fun newInstance(config: BaseDialogConfig): AppDialogFragment {
            val args = Bundle().apply {
                putParcelable(BaseDialogConfig::class.java.simpleName, config)
            }
            val fragment = AppDialogFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
