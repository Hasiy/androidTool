
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.GradientDrawable
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * @Author: zhuxiaoyao
 * @Date: 2018-12-5 13:05:28
 * @LastEditors: zhuxiaoyao
 * @LastEditTime: 2018-12-5 13:05:37
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 * 对图片绘制
 */

class DoodleUtil {

    companion object {

        private val MASK = floatArrayOf(
            0f, 0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f, 0f,
            0f, 0f, 0f, 0f, 0f,
            1f, 0f, 0f, 0f, 0f
        )

        fun composeBitmap(src: Bitmap, cover: Bitmap, backdrop: SpecColorBean): Bitmap {
            val normalPaint = Paint()
            val srcBitmap = Bitmap.createBitmap(src.width, src.height, Bitmap.Config.ARGB_8888)
            val srcCanvas = Canvas(srcBitmap)
            srcCanvas.drawBitmap(src, 0f, 0f, normalPaint)
            srcCanvas.drawBitmap(composeMask(cover, backdrop), 0f, 0f, normalPaint)
            return srcBitmap
        }

        fun composeMask(cover: Bitmap, backdrop: SpecColorBean): Bitmap {
            val alpha = convertToAlphaMask(cover)
            val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)

            val backBitmap = Bitmap.createBitmap(alpha.width, alpha.height, Bitmap.Config.ARGB_8888)
            val backCanvas = Canvas(backBitmap)

            val background = createGradientDrawable(backdrop)
            background.setBounds(0, 0, alpha.width, alpha.height)
            background.draw(backCanvas)
            backCanvas.drawBitmap(alpha, 0f, 0f, maskPaint)
            return backBitmap
        }

        fun composeBitmapBackground(cover: Bitmap, backColor: Bitmap): Bitmap {
            val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
            val backBitmap = Bitmap.createBitmap(cover.width, cover.height, Bitmap.Config.ARGB_8888)
            val backCanvas = Canvas(backBitmap)
            val backColorDrawable = BitmapDrawable(null, backColor)
            backColorDrawable.setBounds(0, 0, cover.width, cover.height)
            backColorDrawable.draw(backCanvas)
            backCanvas.drawBitmap(cover, 0f, 0f, maskPaint)
            return backBitmap
        }

        fun composeBackground(cover: Bitmap, backdrop: SpecColorBean): Bitmap {
            val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//            maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)
            val backBitmap = Bitmap.createBitmap(cover.width, cover.height, Bitmap.Config.ARGB_8888)
            val backCanvas = Canvas(backBitmap)
            val background = createGradientDrawable(backdrop)
            background.setBounds(0, 0, cover.width, cover.height)
            background.draw(backCanvas)
            backCanvas.drawBitmap(cover, 0f, 0f, maskPaint)
            return backBitmap
        }

        fun createGradientDrawable(backdrop: SpecColorBean): GradientDrawable {
            val colorStart = color2Hex(backdrop.start_color!!)
            val colorEnd = color2Hex(backdrop.enc_color!!)
            return GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(colorStart, colorEnd)
            )
        }

        fun drawable2Bitmap(width: Int, height: Int, drawable: GradientDrawable): Bitmap {
            val createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(createBitmap)
            drawable.setBounds(0, 0, width, height)
            drawable.draw(canvas)
            return createBitmap
        }

        fun color2Hex(backdrop: Int): Int {
            val hexString = Integer.toHexString(Integer.valueOf(backdrop))
            val stringBuilder = StringBuilder()
            if (hexString.length < 6) {
                val length = 6 - hexString.length
                for (j in 0 until length) {
                    stringBuilder.append("0")
                }
            }
            val start = "#$stringBuilder$hexString"
            return Color.parseColor(start.trim { it <= ' ' })
        }

        /**
         * 将ARGB_8888转成ALPHA_8  bitmap
         */
        fun convertToAlphaMask(grayScale: Bitmap): Bitmap {
            val alpha = Bitmap.createBitmap(grayScale.width, grayScale.height, Bitmap.Config.ALPHA_8)
            val canvas = Canvas(alpha)
            val paint = Paint()
            paint.colorFilter = ColorMatrixColorFilter(MASK)
            canvas.drawBitmap(grayScale, 0f, 0f, paint)
            return alpha
        }

        /**
         * 将ALPHA_8转成ARGB_8888  bitmap
         */
        fun convertToARGBBmp(alphaBmp: Bitmap): Bitmap {
            val bitmap = Bitmap.createBitmap(alphaBmp.width, alphaBmp.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            val paint = Paint()
            paint.color = Color.WHITE
            canvas.drawColor(Color.BLACK)
            canvas.drawBitmap(alphaBmp, 0f, 0f, paint)
            return bitmap
        }

        /**
         * bitmap转为base64
         *
         * @param bitmap
         * @return
         */
        fun bitmapToBase64(bitmap: Bitmap?): String? {
            var result: String? = null
            var baos: ByteArrayOutputStream? = null
            try {
                if (bitmap != null) {
                    baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                    baos.flush()
                    baos.close()

                    val bitmapBytes = baos.toByteArray()
                    result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    if (baos != null) {
                        baos.flush()
                        baos.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            return result
        }

        /**
         * base64转为bitmap
         *
         * @return
         */
        fun base64ToBitmap(base64Data: String): Bitmap {
            val bytes = Base64.decode(base64Data, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }

        fun drawBg4Bitmap(color: Int, orginBitmap: Bitmap): Bitmap {
            val paint = Paint()
            paint.color = color
            val bitmap = Bitmap.createBitmap(orginBitmap.width, orginBitmap.height, orginBitmap.config)
            val canvas = Canvas(bitmap)
            canvas.drawRect(0f, 0f, orginBitmap.width.toFloat(), orginBitmap.height.toFloat(), paint)
            canvas.drawBitmap(orginBitmap, 0f, 0f, paint)
            return bitmap
        }

        fun cropBitmap(srcBitmap: Bitmap, with: Int, height: Int, rect: Rect): Bitmap {
            val createBitmap = Bitmap.createBitmap(with, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(createBitmap)
            canvas.drawBitmap(srcBitmap, rect, Rect(0, 0, with, height), null)
            return createBitmap
        }
    }
}
