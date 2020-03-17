
import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.util.TypedValue
import android.view.Surface
import android.view.WindowManager

/**
 * 屏幕工具类
 */
object ScreenUtil {

    //val intValue: Int = resource.dpToPx(64)
    //val floatValue: Float = resource.dpToPx(64)
    inline fun <reified T> Resources.dpToPx(value: Int): T {
        val result = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), displayMetrics)

        return when (T::class) {
            Float::class -> result as T
            Int::class -> result.toInt() as T
            else -> throw IllegalStateException("Type not supported")
        }
    }

    fun dip2px(context: Context, dpValue: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue.toFloat(), context.resources.displayMetrics).toInt()
    }

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        val display = windowManager.defaultDisplay
        try {
            display.getRealSize(size)
        } catch (err: NoSuchMethodError) {
            display.getSize(size)
        }
        return size.x
    }

    fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val size = Point()
        val display = windowManager.defaultDisplay
        try {
            display.getRealSize(size)
        } catch (err: NoSuchMethodError) {
            display.getSize(size)
        }
        return size.y
    }

    /**
     * 获取屏幕旋转角度
     * @return 屏幕旋转角度
     */
    fun getDisplayRotation(mContext: Context): Int {
        val wm = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        when (wm.defaultDisplay.rotation) {
            Surface.ROTATION_0 -> return 0
            Surface.ROTATION_90 -> return 90
            Surface.ROTATION_180 -> return 180
            Surface.ROTATION_270 -> return 270
        }
        return 0
    }

    /**
     * 获得屏幕尺寸
     * @return 屏幕尺寸比例
     */
    fun getScreenRate(context: Context): Float {
        val p = getScreenMetrics(context)
        val height = p.y.toFloat()
        val width = p.x.toFloat()
        return height / width
    }

    private fun getScreenMetrics(context: Context): Point {
        val dm = context.resources.displayMetrics
        val screenWidth = dm.widthPixels
        val screenHeight = dm.heightPixels

        return Point(screenWidth, screenHeight)
    }
}
