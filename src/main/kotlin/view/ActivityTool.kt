
import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment

/*
 * @Author: Hasiy
 * @Date: 2020/3/16 - 10 : 33
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 */

// use :    val intent = intent<T>()
inline fun <reified T : Activity> Activity.intent(): Intent {
    return Intent(this, T::class.java)
}

inline fun <reified T : Activity> Fragment.intent(): Intent {
    return Intent(requireContext(), T::class.java)
}

//    Use:  startActivity<XXXActivity>()
inline fun <reified T : Activity> Activity.startActivity() {
    startActivity(intent<T>())
}

inline fun <reified T : Activity> Fragment.startActivity() {
    val intent = intent<T>()
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
}
