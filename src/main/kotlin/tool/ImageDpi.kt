
import java.io.UnsupportedEncodingException
import java.nio.ByteBuffer

/*
 * @Author: zhuxiaoyao
 * @Date: 2019/8/7 - 23 : 31
 * @LastEditors: zhuxiaoyao
 * @LastEditTime: 2020-03-17 15:44:35
 * @Description:Base
 * @Email: hasiy.jj@gmail.com

 */
object ImageDpi {
    /*
    * https://blog.csdn.net/u011715726/article/details/83143301
    * 设置PNG 图片dpi
    * 步骤：获取bitmap的数据流，向其字节数组中添加pHys数据，保存，bingo！
    */
    fun setDpi(imageData: ByteArray, dpi: Int): ByteArray {
        var mDpi = dpi
        val imageDataCopy = ByteArray(imageData.size + 21)
        System.arraycopy(imageData, 0, imageDataCopy, 0, 33)
        System.arraycopy(imageData, 33, imageDataCopy, 33 + 21, imageData.size - 33)
        val pHYs = intArrayOf(0, 0, 0, 9, 112, 72, 89, 115, 0, 0, 23, 18, 0, 0, 23, 18, 1, 103, 159, 210, 82)

        for (i in 0..20) {
            imageDataCopy[i + 33] = (pHYs[i] and 0xff).toByte()
        }

        mDpi = (mDpi / 0.0254).toInt()
        imageDataCopy[41] = (mDpi shr 24).toByte()
        imageDataCopy[42] = (mDpi shr 16).toByte()
        imageDataCopy[43] = (mDpi shr 8).toByte()
        imageDataCopy[44] = (mDpi and 0xff).toByte()

        imageDataCopy[45] = (mDpi shr 24).toByte()
        imageDataCopy[46] = (mDpi shr 16).toByte()
        imageDataCopy[47] = (mDpi shr 8).toByte()
        imageDataCopy[48] = (mDpi and 0xff).toByte()
        //        for (int i = 0; i < 30; i++) {
        //            String line = "";
        //            for (int j = 0; j < 16; j++) {
        //                int temp = imageDataCopy[16 * i + j] & 0xFF;
        //                line += Integer.toHexString(temp) + " ";
        //            }
        //            Log.e(i + "", line);
        //        }
        return imageDataCopy
    }

    /**
     * 密度单位
     * 00：无单位; 宽：高像素宽高比 = XDensity：YDensity
     * 01：像素每英寸（2.54厘米）
     * 02：像素每厘米
     */
    private enum class DensityUnit(private val value: Byte) {
        UNDEFINED(0.toByte()),
        PIXELS_PER_INCH(1.toByte()),
        PIXELS_PER_CENTIMETER(2.toByte());

        internal fun toByte(): Byte {
            return this.value
        }
    }

    /**
     * 设置JPEG 图片dpi
     * @param org 元图片数据格式字节数组
     */
    fun jpegJFIFChange(org: ByteArray, dpi: Int): ByteArray {

        if (org[2] == 0xff.toByte() && org[3] == 0xe0.toByte()) {
            org[13] = 1.toByte()
            org[14] = (dpi shr 8).toByte()
            org[15] = dpi.toByte()
            org[16] = (dpi shr 8).toByte()
            org[17] = dpi.toByte()
            return org
        } else {
            // 文件格式见
            // https://en.wikipedia.org/wiki/JPEG_File_Interchange_Format
            val app0Marker = byteArrayOf(0xFF.toByte(), 0xE0.toByte())
            val identifier: ByteArray = try {
                "JFIF\u0000".toByteArray(charset("US-ASCII"))
            } catch (e: UnsupportedEncodingException) {
                byteArrayOf(0x4A.toByte(), 0x46.toByte(), 0x49.toByte(), 0x46.toByte(), 0.toByte())
            }

            val jfifVersionMajor: Byte = 1
            val jfifVersionMinor: Byte = 1

            val densityUnit = DensityUnit.PIXELS_PER_INCH
            val densityX = dpi.toShort()
            val densityY = dpi.toShort()

            // 默认大端
            val buffer = ByteBuffer.allocate(18)
            buffer.put(app0Marker)
            buffer.putShort(16.toShort()) // 本段除去 Marker 后的总长度
            buffer.put(identifier)
            buffer.put(jfifVersionMajor)
            buffer.put(jfifVersionMinor)
            buffer.put(densityUnit.toByte())
            buffer.putShort(densityX)
            buffer.putShort(densityY)
            buffer.put(0.toByte()) // 缩略图宽度
            buffer.put(0.toByte()) // 缩略图高度
            // 这里还有个部分，是缩略图 RGB 三个通道的数据
            // 因为上面指定宽高都是 0，所以这里没有数据
            val rst = ByteArray(org.size + 18)
            rst[0] = org[0]
            rst[1] = org[1]
            for (i in 2..19) {
                rst[i] = buffer.array()[i - 2]
            }
            System.arraycopy(org, 2, rst, 20, org.size - 2)
            return rst
        }
    }
}
