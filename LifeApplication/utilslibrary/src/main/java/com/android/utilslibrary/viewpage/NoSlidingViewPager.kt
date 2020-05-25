package com.android.utilslibrary.viewpage

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * 禁止 viewpager 的左右滑动
 */
class NoSlidingViewPager(
    context: Context,
    attrs: AttributeSet?
) :
    ViewPager(context, attrs) {
    override fun onTouchEvent(ev: MotionEvent?): Boolean { //去掉ViewPager默认的滑动效果， 不消费事件
        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean { //不让拦截事件
        return false
    }
}