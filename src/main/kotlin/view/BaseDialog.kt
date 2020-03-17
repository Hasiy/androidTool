
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import top.hasiy.spinkit.BuildConfig
import java.util.*

/**
 * @Author: Hasiy
 * @Date: 2019/1/12 - 17 : 06
 * @LastEditors: Hasiy
 * @LastEditTime: 2019/1/12 - 17 : 06
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 * Dialog基类
 */

abstract class BaseDialog : DialogFragment() {
    private var serviceCurrentMills = 0L
    private var loadingCompleted: Boolean = false
    private var dismissCompleted: Boolean = false
    protected var dialogIsBottom: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setStyle(STYLE_NO_TITLE, R.style.dialogStyles)
        serviceCurrentMills = System.currentTimeMillis()
        loadingCompleted = true
        dismissCompleted = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getViewId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        initEvent()
    }

    abstract fun getViewId(): Int
    abstract fun initUI(rootView: View)
    open fun initEvent() {}

    override fun onSaveInstanceState(outState: Bundle) {
        loadingCompleted = false
        super.onSaveInstanceState(outState)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        if (!loadingCompleted) {
            dismissCompleted = false
            try {
                val transaction = manager.beginTransaction()
                transaction.add(this, tag)
                transaction.commitAllowingStateLoss()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    override fun dismiss() {
        // 防止未完成显示就关闭,引发catch
        if (System.currentTimeMillis() - serviceCurrentMills > 100 && loadingCompleted && !dismissCompleted) {
            try {
                super.dismiss()
                loadingCompleted = false
                dismissCompleted = true
            } catch (e: Exception) {
                e.printStackTrace()
                LogUtil.e("BaseDialog:$e.toString()")
            }
        } else if (!dismissCompleted) {
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    if (BuildConfig.DEBUG) {
                        LogUtil.i("BaseDialog:dismiss :=: !dismissCompleted")
                    }
                    dismiss()
                }
            }, 90)
        }
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        val params = window!!.attributes
        when (dialogIsBottom) {
            true -> {
                params.gravity = Gravity.BOTTOM
                params.width = WindowManager.LayoutParams.MATCH_PARENT
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
                window.attributes = params
                window.setWindowAnimations(R.style.anim_bottom_dialog)
            }
            false -> {
                params.gravity = Gravity.CENTER
                params.width = WindowManager.LayoutParams.WRAP_CONTENT
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
                window.attributes = params
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun onDestroy() {
        dismissCompleted = true
        loadingCompleted = false
        super.onDestroy()
    }
}
