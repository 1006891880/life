package com.android.utilslibrary.scrollview

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.core.widget.NestedScrollView

/**
 * ScrollView屏蔽 滑动事件
 */
class NoNestedScrollview : NestedScrollView {
    private var downX = 0
    private var downY = 0
    private var mTouchSlop: Int

    constructor(context: Context?) : super(context!!) {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs ) {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        val action = e.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                downX = e.rawX.toInt()
                downY = e.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val moveY = e.rawY.toInt()
                if (Math.abs(moveY - downY) > mTouchSlop) {
                    return true
                }
            }
            else -> {
            }
        }
        return super.onInterceptTouchEvent(e)
    }

    private var scrollViewListener: NestedScrollViewListener? = null
    fun setScrollViewListener(scrollViewListener: NestedScrollViewListener?) {
        this.scrollViewListener = scrollViewListener
    }

    protected override fun onScrollChanged(x: Int, y: Int, oldX: Int, oldY: Int) {
        super.onScrollChanged(x, y, oldX, oldY)
        if (scrollViewListener != null) {
            scrollViewListener!!.onScrollChanged(this, x, y, oldX, oldY)
        }
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scrollViewListener != null) {
            scrollViewListener!!.onScroll(scrollY)
        }
    }
}
