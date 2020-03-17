package com.leqi.shape.uiComponent.customView

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader

/**
 * @Author: Hasiy
 * @Date: 2020/3/10 - 17 : 24
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 * 画圆角Drawable
 */

//  image_rounded.setImageDrawable(RoundedDrawable(resource, ScreenUtil.dip2px(context, 8), RoundedDrawable.CORNER_ALL))
class RoundedDrawable constructor(bitmap: Bitmap, cornerRadius: Int, corners: Int) : Drawable() {
    private val cornerRadius: Float = cornerRadius.toFloat()  // 圆角大小px
    private val mRect = RectF()
    private val mBitmapRect: RectF
    private val bitmapShader: BitmapShader
    private val paint: Paint
    private var corners: Int = 0

    init {
        this.corners = corners
        bitmapShader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        mBitmapRect = RectF(0F, 0F, bitmap.width.toFloat(), bitmap.height.toFloat())
        paint = Paint()
        paint.isAntiAlias = true
        paint.shader = bitmapShader
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        //Drawable绘制位置以及绘制大小
        mRect[0f, 0f, bounds.width().toFloat()] = bounds.height().toFloat()
        val shaderMatrix = Matrix() //缩放图像
        shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.FILL)
        bitmapShader.setLocalMatrix(shaderMatrix)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawRoundRect(mRect, cornerRadius, cornerRadius, paint) //画完圆角矩形
        val notRoundedCorners = corners xor CORNER_ALL
        // 缺那个角再画上去
        if (notRoundedCorners and CORNER_TOP_LEFT != 0) {
            canvas.drawRect(0f, 0f, cornerRadius, cornerRadius, paint)
        }
        if (notRoundedCorners and CORNER_TOP_RIGHT != 0) {
            canvas.drawRect(mRect.right - cornerRadius, 0f, mRect.right, cornerRadius, paint)
        }
        if (notRoundedCorners and CORNER_BOTTOM_LEFT != 0) {
            canvas.drawRect(0f, mRect.bottom - cornerRadius, cornerRadius, mRect.bottom, paint)
        }
        if (notRoundedCorners and CORNER_BOTTOM_RIGHT != 0) {
            canvas.drawRect(mRect.right - cornerRadius, mRect.bottom - cornerRadius, mRect.right, mRect.bottom, paint)
        }
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
        //API级别29不推荐使用此方法。在图形优化中不再使用此方法。
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }

    companion object {
        const val CORNER_TOP_LEFT = 1
        const val CORNER_TOP_RIGHT = 1 shl 1
        const val CORNER_BOTTOM_LEFT = 1 shl 2
        const val CORNER_BOTTOM_RIGHT = 1 shl 3
        const val CORNER_ALL = CORNER_TOP_LEFT or CORNER_TOP_RIGHT or CORNER_BOTTOM_LEFT or CORNER_BOTTOM_RIGHT
    }
}
