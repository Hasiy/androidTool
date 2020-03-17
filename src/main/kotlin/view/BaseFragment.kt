
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment

/**
 * @Author: Hasiy
 * @Date: 2019/1/12 - 17 : 06
 * @LastEditors: Hasiy
 * @LastEditTime: 2019/1/12 - 17 : 06
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 * Fragment基类
 */

abstract class BaseFragment : Fragment() {
    protected var fatherActivity: BaseActivity? = null

    @NonNull
    open fun context(): Context? {
        return activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.e("onCreate: ${this.javaClass}")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getViewId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        initEvent()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fatherActivity = if (activity is BaseActivity) {
            activity as BaseActivity?
        } else {
            throw RuntimeException("fragment created in wrong activity")
        }
    }

    abstract fun getViewId(): Int

    abstract fun initUI(view: View)

    open fun initEvent() {}

    fun showBaseProgressBar() {
        fatherActivity?.let {
            fatherActivity!!.showBaseProgressBar()
        }
    }

    fun dismissBaseProgressBar() {
        fatherActivity?.let {
            fatherActivity!!.dismissBaseProgressBar()
        }
    }
}
