
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

/**
 * @Author: Hasiy
 * @Date: 2019/1/12 - 17 : 06
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 * 跑马灯文字 横向滚动文字
 */

class MarqueeTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    // 焦点
    override fun isFocused(): Boolean {
        return true
    }
}
