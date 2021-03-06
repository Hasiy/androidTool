
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


/**
 * @Author: Hasiy
 * @Date: 2019/10/15 - 16 : 53  .
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 */

open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mViews: SparseArray<View> = SparseArray()

    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(viewId: Int): T {
        var view: View? = mViews.get(viewId)
        if (view == null) {
            view = itemView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T
    }

    /**
     * 设置文本
     */
    fun setText(viewId: Int, text: CharSequence): BaseViewHolder {
        val tv = getView<TextView>(viewId)
        tv.text = text
        return this
    }

    /**
     * 设置图片
     */
    fun setImage(viewId: Int, url: String): BaseViewHolder {
        val imageView = getView<ImageView>(viewId)
        Glide.with(imageView.context)
            .load(url)
            .into(imageView)
        return this
    }
}
