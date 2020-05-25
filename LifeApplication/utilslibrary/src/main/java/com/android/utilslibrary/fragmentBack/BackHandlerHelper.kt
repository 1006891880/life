package com.android.utilslibrary.fragmentBack

import androidx.fragment.app.*
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

object BackHandlerHelper {

    /**
     * 将back事件分发给 FragmentManager 中管理的子Fragment，
     * 如果该 FragmentManager 中的所有Fragment都没有处理back事件，则尝试 FragmentManager.popBackStack()
     * @return 如果处理了back键则返回 **true**
     * @see .handleBackPress
     * @see .handleBackPress
     */
    fun handleBackPress(fragmentManager: FragmentManager): Boolean {
        val fragments: List<Fragment> = fragmentManager.getFragments() ?: return false
        for (i in fragments.indices.reversed()) {
            val child: Fragment = fragments[i]
            if (isFragmentBackHandled(child)) {
                return true
            }
        }
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack()
            return true
        }
        return false
    }

    /**
     * 将back事件分发给Fragment中的子Fragment,
     * 该方法调用了 [.handleBackPress]
     *
     * @return 如果处理了back键则返回 **true**
     */
    fun handleBackPress(fragment: Fragment): Boolean {
        return handleBackPress(fragment.getChildFragmentManager())
    }

    /**
     * 将back事件分发给Activity中的子Fragment,
     * 该方法调用了 [.handleBackPress]
     *
     * @return 如果处理了back键则返回 **true**
     */
    fun handleBackPress(fragmentActivity: FragmentActivity): Boolean {
        return handleBackPress(fragmentActivity.getSupportFragmentManager())
    }

    /**
     * 将back事件分发给ViewPager中的Fragment,[.handleBackPress] 已经实现了对ViewPager的支持，所以自行决定是否使用该方法
     *
     * @return 如果处理了back键则返回 **true**
     * @see .handleBackPress
     * @see .handleBackPress
     * @see .handleBackPress
     */
    fun handleBackPress(viewPager: ViewPager?): Boolean {
        if (viewPager == null) return false
        val adapter: PagerAdapter = viewPager.getAdapter() ?: return false
        val currentItem: Int = viewPager.getCurrentItem()
        val fragment: Fragment?
        fragment = if (adapter is FragmentPagerAdapter) {
            (adapter as FragmentPagerAdapter).getItem(currentItem)
        } else if (adapter is FragmentStatePagerAdapter) {
            (adapter as FragmentStatePagerAdapter).getItem(currentItem)
        } else {
            null
        }
        return isFragmentBackHandled(fragment)
    }

    /**
     * 判断Fragment是否处理了Back键
     * @return 如果处理了back键则返回 **true**
     */
    private fun isFragmentBackHandled(fragment: Fragment?): Boolean {
        return (fragment != null && fragment.isVisible()
                && fragment.getUserVisibleHint() //for ViewPager
                && fragment is FragmentBackHandler
                && (fragment as FragmentBackHandler).onBackPressed())
    }
}