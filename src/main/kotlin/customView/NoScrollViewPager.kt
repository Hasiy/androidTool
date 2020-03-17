
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * @Author: Hasiy
 * @Date: 2019/12/10 - 16 : 12
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 */

class NoScrollViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPager(context, attrs) {
    private var isScroll = false

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        // return false;//可行,不拦截事件,
        // return true;//不行,孩子无法处理事件
        // return super.onInterceptTouchEvent(ev);//不行,会有细微移动
        return if (isScroll) {
            super.onInterceptTouchEvent(ev)
        } else {
            false
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        // return false;// 可行,不消费,传给父控件
        // return true;// 可行,消费,拦截事件
        // super.onTouchEvent(ev); //不行,
        // 虽然onInterceptTouchEvent中拦截了,
        // 但是如果viewpager里面子控件不是viewGroup,还是会调用这个方法.
        return if (isScroll) {
            super.onTouchEvent(ev)
        } else {
            true // 可行,消费,拦截事件
        }
    }

    fun setScroll(scroll: Boolean) {
        isScroll = scroll
    }
}
