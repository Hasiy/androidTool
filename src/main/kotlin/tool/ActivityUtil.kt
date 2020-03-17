
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Created by 84449 on 2018/2/5.
 * Fragment 加载
 */
object ActivityUtil {
    fun addFragment(fm: FragmentManager?, fragment: Fragment?, frameId: Int) {
        checkNotNull(fm)
        checkNotNull(fragment)
        val transaction = fm.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.commitAllowingStateLoss()
    }

    fun addDialogFragment(fm: FragmentManager?, fragment: Fragment?, tag: String) {
        checkNotNull(fm)
        checkNotNull(fragment)
        val transaction = fm.beginTransaction()
        transaction.add(fragment, tag)
        transaction.commitAllowingStateLoss()
    }
}
