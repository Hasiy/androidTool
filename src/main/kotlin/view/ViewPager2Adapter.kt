
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @Author: Hasiy
 * @Date: 2020/2/26 - 10 : 01
 * @Description: Base
 * @Email: hasiy.jj@gmail.com
 */

class ViewPager2Adapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    private val fragments: MutableList<Fragment> = mutableListOf()

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun addFragment(fragment: Fragment) {
        fragments.add(fragment)
        notifyDataSetChanged()
    }

    fun addFirstFragment(fragment: Fragment) {
        val list: MutableList<Fragment> = mutableListOf()
        list.add(fragment)
        fragments.clear()
        fragments.addAll(list)
        LogUtil.e("on fragments:${fragments.first()}")
        notifyDataSetChanged()
    }
}
