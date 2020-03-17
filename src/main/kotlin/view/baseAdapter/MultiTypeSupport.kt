
/**
 * @Author: Hasiy
 * @Date: 2019/10/15 - 16 : 53  .
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 */

interface MultiTypeSupport<T> {
    fun getLayoutId(item: T, position: Int): Int
}
