
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

/**
 * @Author: Hasiy
 * @Date: 2019/12/10 - 16 : 12
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 */

class ProductImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    AppCompatImageView(context, attrs, defStyleAttr) {
    private var viewWidth: Int = 0
    private var viewHeight: Int = 0
    private var mSrcBitmap: Bitmap? = null
    private var mPxSize: List<String>? = null
    private var mMmSize: List<String>? = null

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (mSrcBitmap == null) return
        val maxHeight = viewHeight / 4 * 3
        val maxWidth = viewWidth / 6 * 5
        val width = mSrcBitmap!!.width
        val height = mSrcBitmap!!.height

        LogUtil.i("sfy::viewHeight:$viewHeight::viewWidth:$viewWidth::maxHeight$maxHeight::maxWidth:$maxWidth")
        val heightRatio = (height.toFloat() / maxHeight.toFloat())
        val widthRatio = (width.toFloat() / maxWidth.toFloat())
        val ratio = if (heightRatio > widthRatio) {
            heightRatio
        } else {
            widthRatio
        }
        // 对图片进行缩放
        val left = (viewWidth - width.toFloat() / ratio) / 2
        val top = (viewHeight - height.toFloat() / ratio) / 2
        val bitmapWith = width.toFloat() / ratio
        val bitmapHeight = height.toFloat() / ratio
        LogUtil.i("sfy::width:$width::height:$height::ratio$ratio")
        LogUtil.i("sfy::$left::$top")
        // 绘制外围半透明遮罩
        drawMask(canvas, left, top, left + bitmapWith, top + bitmapHeight)

        drawLineText(canvas, left, top, left + bitmapWith, top + bitmapHeight)
        val rect = Rect()
        rect.left = left.toInt()
        rect.top = top.toInt()
        rect.right = (left + bitmapWith).toInt()
        rect.bottom = (top + bitmapHeight).toInt()
        canvas!!.drawBitmap(mSrcBitmap!!, null, rect, null)
    }

    /**
     * 绘制半透明遮罩
     * left,top,right,bottom是bitmap放置位置
     */
    private fun drawMask(canvas: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        // 绘制半透明遮罩
        val paint = Paint()
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        paint.alpha = 60
        paint.style = Paint.Style.FILL

        val rectLeft = RectF(0f, 0f, left, viewHeight.toFloat())
        val rectTop = RectF(left, 0f, right, top)
        val rectRight = RectF(right, 0f, viewWidth.toFloat(), viewHeight.toFloat())
        val rectBottom = RectF(left, bottom, right, viewHeight.toFloat())
        canvas!!.drawRect(rectLeft, paint)
        canvas.drawRect(rectTop, paint)
        canvas.drawRect(rectRight, paint)
        canvas.drawRect(rectBottom, paint)
    }

    /**
     * 绘制边线及文字
     * left,top,right,bottom是bitmap放置位置
     */
    private fun drawLineText(canvas: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        val paint = Paint()
        paint.color = Color.argb(66, 255, 255, 255)
//        paint.color = Color.WHITE
        paint.isAntiAlias = true
        paint.isFilterBitmap = true
        paint.isDither = true
        paint.style = Paint.Style.FILL
        paint.textSize = ScreenUtil.dip2px(context, 13f).toFloat()
        paint.strokeWidth = ScreenUtil.dip2px(context, 1f).toFloat()

        // 边角线长度
        val ls = ScreenUtil.dip2px(context, 20f)
        // 边线距离图片距离
        val lls = ScreenUtil.dip2px(context, 17f)
        // 文字距离图片距离
        val lets = ScreenUtil.dip2px(context, 10f)
        // 预留文字空余尺寸
        val lts = ScreenUtil.dip2px(context, 40f)
        // 算出宽高中点（为兼容canvas放大情况不能直接用canvas中点）
        val withMid = (right - left) / 2 + left
        val heightMid = (bottom - top) / 2 + top
        // 先画四个角
        canvas!!.drawLine(right, top, right + ls, top, paint)
        canvas.drawLine(right, bottom, right + ls, bottom, paint)
        canvas.drawLine(left, top - ls, left, top, paint)
        canvas.drawLine(right, top - ls, right, top, paint)
//        canvas!!.drawLine(left - ls, top, left, top, paint)
//        canvas.drawLine(left - ls, bottom, left, bottom, paint)
//        canvas.drawLine(left, bottom, left, bottom + ls, paint)
//        canvas.drawLine(right, bottom, right, bottom + ls, paint)

        // 画边线
        LogUtil.d("left：$left  withMid - lts:${withMid - lts}")
        canvas.drawLine(left, top - lls, withMid - lts, top - lls, paint)
        canvas.drawLine(withMid + lts, top - lls, right, top - lls, paint)
        canvas.drawLine(right + lls, top, right + lls, heightMid - lts, paint)
        canvas.drawLine(right + lls, heightMid + lts, right + lls, bottom, paint)
//        canvas.drawLine(left, bottom + lls, withMid - lts, bottom + lls, paint)
//        canvas.drawLine(withMid + lts, bottom + lls, right, bottom + lls, paint)
//        canvas.drawLine(left - lls, top, left - lls, heightMid - lts, paint)
//        canvas.drawLine(left - lls, heightMid + lts, left - lls, bottom, paint)

        drawText(canvas, mPxSize!![0] + "px", withMid - lls, top - lets, paint, 0f)
        drawText(canvas, mPxSize!![1] + "px", right + lets * 2, heightMid + ls, paint, 270f)
//        drawText(canvas, mMmSize!![0] + "mm", withMid - lls, bottom + ls, paint, 0f)
//        drawText(canvas, mMmSize!![1] + "mm", left - lets, heightMid + ls, paint, 270f)
    }

    private fun drawText(canvas: Canvas, text: String, x: Float, y: Float, paint: Paint, angle: Float) {
        if (angle != 0f) {
            canvas.rotate(angle, x, y)
        }
        canvas.drawText(text, x, y, paint)
        if (angle != 0f) {
            canvas.rotate(-angle, x, y)
        }
    }

    fun setBitmap(bitmap: Bitmap) {
        if (mPxSize == null || mMmSize == null) return
        this.mSrcBitmap = bitmap
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        /**
         * 获得控件的宽高，默认MeasureSpec.EXACTLY （ match_parent , accurate ）
         * 并且布局文件中应该设置 控件的宽高相等
         */
        viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewHeight = MeasureSpec.getSize(heightMeasureSpec)
    }

    fun setSize(pxSize: List<String>?, mmSize: List<String>?) {
        mPxSize = pxSize
        mMmSize = mmSize
    }
}
