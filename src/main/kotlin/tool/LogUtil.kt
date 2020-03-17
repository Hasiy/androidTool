
import android.os.Looper
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import top.hasiy.toasts.Toasts
import top.hasiy.toasts.Toasts.LENGTH_LONG

/**
 * Created by lzc on 2018/1/26.
 * 日志工具类
 */
object LogUtil {

    private const val tag = "APPAPP"

    fun init() {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false) // 是否显示线程信息，默认为true
            .methodCount(3) // 显示的方法行数，默认为2
            .methodOffset(7) // 隐藏内部方法调用到偏移量，默认为5
            // .logStrategy { priority, tag, message -> } // 更改要打印的日志策略。
            .tag(tag) // 每个日志的全局标记。默认PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }

    @JvmStatic
    fun v(msg: String) {
        Logger.v(msg)
    }

    @JvmStatic
    fun d(msg: String) {
        if (BuildConfig.DEBUG) {
            Logger.d(msg)
        }
    }

    @JvmStatic
    fun i(msg: String) {
        if (BuildConfig.DEBUG) {
            Logger.i(msg)
        }
    }

    @JvmStatic
    fun w(msg: String) {
        Logger.w(msg)
    }

    @JvmStatic
    fun e(msg: String) {
        Logger.e(msg)
    }

    /************************toast*********************/

    fun toast(msg: String) {
        if (Thread.currentThread() != Looper.getMainLooper().thread) return
        Toasts.normal(IDApplication.appContext.get()!!, msg).show()
    }

    fun toastSuccess(msg: String) {
        if (Thread.currentThread() != Looper.getMainLooper().thread) return
        Toasts.success(IDApplication.appContext.get()!!, msg, LENGTH_LONG).show()
    }

    fun toastSuccess(msg: CharSequence) {
        if (Thread.currentThread() != Looper.getMainLooper().thread) return
        Toasts.success(IDApplication.appContext.get()!!, msg, LENGTH_LONG).show()
    }

    fun toastError(msg: String) {
        if (Thread.currentThread() != Looper.getMainLooper().thread) return
        Toasts.error(IDApplication.appContext.get()!!, msg, LENGTH_LONG).show()
    }

    fun toastWarning(msg: String) {
        if (Thread.currentThread() != Looper.getMainLooper().thread) return
        Toasts.warning(IDApplication.appContext.get()!!, msg, LENGTH_LONG).show()
    }

    fun toastInfo(msg: String) {
        if (Thread.currentThread() != Looper.getMainLooper().thread) return
        Toasts.info(IDApplication.appContext.get()!!, msg).show()
    }
}
