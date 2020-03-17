
import android.view.View


/**
 * @Author: Hasiy
 * @Date: 2019/10/15 - 16 : 53  .
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 */

interface OnItemClickListener<T> {
    fun onItemClick(view: View, position: Int, item: T)
}
