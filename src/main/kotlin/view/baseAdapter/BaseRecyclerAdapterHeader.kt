
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


/**
 * @Author: Hasiy
 * @Date: 2019/10/15 - 16 : 53  .
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 */

abstract class BaseRecyclerAdapterHeader<T> : RecyclerView.Adapter<BaseViewHolder> {

    // 点击事件
    private var mOnItemClickListener: OnItemClickListener<T>? = null
    // 支持多种布局
    private var mMultiTypeSupport: MultiTypeSupport? = null
    // mInflater
    private var mInflater: LayoutInflater
    private var context: Context
    private var layoutId: Int
    private var data: List<T>

    interface MultiTypeSupport {
        fun getLayoutId(position: Int): Int
    }

    constructor(context: Context, layoutId: Int, data: List<T>) {
        this.context = context
        this.layoutId = layoutId
        this.data = data
        this.mInflater = LayoutInflater.from(context)
    }

    constructor(context: Context, data: List<T>, multiTypeSupport: MultiTypeSupport) : this(context, -1, data) {
        this.mMultiTypeSupport = multiTypeSupport
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        mMultiTypeSupport?.also {
            this.layoutId = viewType
        }
        val itemView = mInflater.inflate(layoutId, parent, false)
        return BaseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (mOnItemClickListener != null && position != 0) {
            holder.itemView.setOnClickListener {
                mOnItemClickListener?.apply { onItemClick(holder.itemView, position, data[position - 1]) }
            }
        }

        if (position == 0) {
            initHeader(holder, data.size)
        } else {
            convert(holder, position, data[position - 1], data.size)
        }
    }

    abstract fun initHeader(holder: BaseViewHolder, dataSize: Int)

    abstract fun convert(holder: BaseViewHolder, position: Int, item: T, dataSize: Int)

    override fun getItemViewType(position: Int): Int {
        // 多布局
        return if (mMultiTypeSupport != null) {
            mMultiTypeSupport!!.getLayoutId(position)
        } else position
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    /**
     * 设置itemView的点击事件
     */
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        this.mOnItemClickListener = onItemClickListener
    }

    fun setLayoutId(layoutId: Int) {
        this.layoutId = layoutId
    }
}
