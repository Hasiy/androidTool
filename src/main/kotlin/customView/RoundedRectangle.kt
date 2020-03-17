
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * @Author: Hasiy
 * @Date: 2019/10/15 - 16 : 53  .
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 * 圆角矩形 背景色块
 */

class RoundedRectangle @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr) {

    private var cornerRadius: Int = 5
    private var fillet = dip2px(context, cornerRadius).toFloat()
    private var color: SpecColorBean? = null

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (color == null) {
            return
        }
        if (color!!.start_color == 16777215) {
            // 白色画灰边
            val grayColor = SpecColorBean()
            grayColor.start_color = 10857149
            grayColor.enc_color = 10857149
            val grayBack = DoodleUtil.createGradientDrawable(grayColor)
            grayBack.cornerRadius = dip2px(context, cornerRadius + 1).toFloat()
            grayBack.setBounds(0, 0, width, height)
            grayBack.draw(canvas!!)
            val interval = dip2px(context, 1)
            val backColor = DoodleUtil.createGradientDrawable(color!!)
            backColor.cornerRadius = fillet
            backColor.setBounds(interval, interval, width - interval, height - interval)
            backColor.draw(canvas)
        } else {
            val backColor = DoodleUtil.createGradientDrawable(color!!)
            backColor.cornerRadius = fillet
            backColor.setBounds(0, 0, width, height)
            backColor.draw(canvas!!)
        }
    }

    fun setColor(color: SpecColorBean) {
        this.color = color
        invalidate()
    }

    fun setFillet(cornerRadius: Int) {
        this.cornerRadius = cornerRadius
    }
}
