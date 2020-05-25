package com.android.utilslibrary.scrollview

import androidx.core.widget.NestedScrollView

//ScrollView滑动监听事件接口
interface NestedScrollViewListener {
    /*** 在滑动的时候调用 */
    fun onScrollChanged(
        scrollView: NestedScrollView?,
        x: Int,
        y: Int,
        oldx: Int,
        oldy: Int
    )

    /*** 在滑动的时候调用，scrollY为已滑动的距离 */
    fun onScroll(scrollY: Int)
}