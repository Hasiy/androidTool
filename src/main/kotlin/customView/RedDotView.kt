
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TabWidget
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.tabs.TabLayout

/**
 * @Author: Hasiy
 * @Date: 2019/1/12 - 17 : 06
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 * 控件添加小红点
 */

//    private fun showRedDot(option: Int) {
//        val redDotView = RedDotView(this)
//        redDotView.setTargetView(manufacture_bar, option)
//        redDotView.setRedHotViewGravity(Gravity.CENTER)
//        redDotView.setBadgeView(10)
//        redDotView.setBadgeMargin(0, 0, 0, 0)
//        manufacture_bar.getTabAt(option)!!.select()
//    }

class RedDotView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.textViewStyle) :
    AppCompatTextView(context, attrs, defStyleAttr) {

    /**
     * 是否隐藏红点上的数字
     */
    var isHideOnNull = true
        private set

    /**
     * 重新父类setText方法
     *
     * @param text text
     * @param type type
     */
    override fun setText(text: CharSequence, type: BufferType) {
        visibility = if (isHideOnNull) {
            if (text == null || text.toString().equals(ZERO, ignoreCase = true)) {
                View.GONE
            } else {
                View.VISIBLE
            }
        } else {
            View.VISIBLE
        }
        super.setText(text, type)
    }

    /**
     * 初始化view
     * 1.设置布局属性
     */
    private fun initView() {
        setLayoutParams()
        setTextView()
        setDefaultValues()
    }

    private fun setLayoutParams() {
        if (layoutParams !is FrameLayout.LayoutParams) {
            val lParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
            layoutParams = lParams
        }
    }

    private fun setTextView() {
        setTextColor(Color.WHITE)
        typeface = Typeface.DEFAULT_BOLD
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)
        setPadding(dip2Px(1f), dip2Px(1f), dip2Px(1f), dip2Px(1f))
        setBackground(9, Color.parseColor("#f14850"))
        gravity = Gravity.CENTER
    }

    private fun setDefaultValues() {
        setHideNull(true)
        badgeCount = 0
    }

    /**
     * 设置背景颜色
     *
     * @param dipRadius  半径
     * @param badgeColor 颜色
     */
    private fun setBackground(dipRadius: Int, badgeColor: Int) {
        val radius = dip2Px(dipRadius.toFloat())
        val radiusArray = floatArrayOf(
            radius.toFloat(), radius.toFloat(), radius.toFloat(), radius.toFloat(),
            radius.toFloat(), radius.toFloat(), radius.toFloat(), radius.toFloat()
        )
        val roundRect = RoundRectShape(radiusArray, null, null)
        val bgDrawable = ShapeDrawable(roundRect)
        bgDrawable.paint.color = badgeColor
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            background = bgDrawable
        }
    }

    fun setHideNull(hideOnNull: Boolean) {
        isHideOnNull = hideOnNull
        text = text
    }

    private fun setBadgeCount(count: String) {
        text = count
    }

    /**
     * 设置小红点，不设置数字
     */
    fun setBadgeView(dipRadius: Int) {
        text = ""
        width = dip2Px(dipRadius.toFloat())
        height = dip2Px(dipRadius.toFloat())
        setBackground(9, Color.parseColor("#FF3B30"))
    }

    /**
     * 设置支持TabWidget控件
     *
     * @param target   TabWidget
     * @param tabIndex 索引
     */
    fun setTargetView(target: TabWidget, tabIndex: Int) {
        val tabView = target.getChildTabViewAt(tabIndex)
        setTargetView(tabView)
    }

    /**
     * 设置支持tabLayout控件
     *
     * @param target   TabLayout
     * @param tabIndex 索引
     */
    fun setTargetView(target: TabLayout, tabIndex: Int) {
        val tabAt = target.getTabAt(tabIndex)
        var customView: View? = null
        if (tabAt != null) {
            customView = tabAt.customView
        }
        setTargetView(customView)
    }

    /**
     * 设置红点依附的view
     *
     * @param view view
     */
    fun setTargetView(view: View?) {
        if (parent != null) {
            (parent as ViewGroup).removeView(this)
        }
        if (view == null) {
            return
        }
        if (view.parent is FrameLayout) {
            (view.parent as FrameLayout).addView(this)
        } else if (view.parent is ViewGroup) {
            val parentContainer = view.parent as ViewGroup
            val groupIndex = parentContainer.indexOfChild(view)
            parentContainer.removeView(view)
            val badgeContainer = FrameLayout(context)
            val parentLayoutParams = view.layoutParams
            badgeContainer.layoutParams = parentLayoutParams
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            parentContainer.addView(badgeContainer, groupIndex, parentLayoutParams)
            badgeContainer.addView(view)
            //            Drawable background = ContextCompat.getDrawable(getContext(), R.drawable.tab_background);
//            badgeContainer.setBackground(background);
            badgeContainer.addView(this)
        } else {
            Log.e(javaClass.simpleName, "ParentView is must needed")
        }
    }

    /**
     * 设置红点位置
     *
     * @param gravity 位置
     */
    fun setRedHotViewGravity(gravity: Int) {
        val params = layoutParams as FrameLayout.LayoutParams
        params.gravity = gravity
        layoutParams = params
    }

    /**
     * 设置红点属性
     *
     * @param dipMargin margin值
     */
    fun setBadgeMargin(dipMargin: Int) {
        setBadgeMargin(dipMargin, dipMargin, dipMargin, dipMargin)
    }

    /**
     * 设置红点的margin属性
     *
     * @param leftDipMargin   左边margin
     * @param topDipMargin    上边margin
     * @param rightDipMargin  右边margin
     * @param bottomDipMargin 下边margin
     */
    fun setBadgeMargin(
        leftDipMargin: Int, topDipMargin: Int,
        rightDipMargin: Int, bottomDipMargin: Int
    ) {
        val params = layoutParams as FrameLayout.LayoutParams
        params.leftMargin = dip2Px(leftDipMargin.toFloat())
        params.topMargin = dip2Px(topDipMargin.toFloat())
        params.rightMargin = dip2Px(rightDipMargin.toFloat())
        params.bottomMargin = dip2Px(bottomDipMargin.toFloat())
        layoutParams = params
    }

    /**
     * 获取小红点的数量
     *
     * @return 数量
     *///设置参数，超过99显示99+
    /**
     * 设置红点的数字
     *
     * @param count 数字
     */
    var badgeCount: Int
        get() {
            if (text == null) {
                return 0
            }
            val text = text.toString()
            return try {
                text.toInt()
            } catch (e: NumberFormatException) {
                0
            }
        }
        set(count) {
            //设置参数，超过99显示99+
            setBadgeCount(if (count > 99) "99+" else count.toString())
        }

    private fun dip2Px(dip: Float): Int {
        return (dip * context.resources.displayMetrics.density + 0.5f).toInt()
    }

    companion object {
        private const val ZERO = "0"
    }

    init {
        initView()
    }
}
