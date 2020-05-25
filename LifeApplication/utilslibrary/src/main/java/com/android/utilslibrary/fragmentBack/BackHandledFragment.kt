package com.android.utilslibrary.fragmentBack

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

//  处理Fragment返回键，需要继承该类
abstract class BackHandledFragment :Fragment() ,FragmentBackHandler {

    override fun onBackPressed(): Boolean {
        return interceptBackPressed() ||
                if (getBackHandleViewPager() == null)
            BackHandlerHelper.handleBackPress(this)
        else BackHandlerHelper.handleBackPress(getBackHandleViewPager())
    }

    /**
     * 子类可以继承，实现是否分发back事件
     * 默认返回false，表示子类不分发
     * @return
     */
    fun interceptBackPressed(): Boolean {
        return false
    }

    /**
     * 2.1 版本已经不在需要单独对ViewPager处理
     */
    @Deprecated("")
    fun getBackHandleViewPager(): ViewPager? {
        return null
    }

}