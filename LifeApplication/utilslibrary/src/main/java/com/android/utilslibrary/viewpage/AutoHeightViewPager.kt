package com.android.utilslibrary.viewpage

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.viewpager.widget.ViewPager

/**
 * 自适应子View高度的viewPager
 */
class AutoHeightViewPager(
    context: Context,
    attrs: AttributeSet?
) :
    ViewPager(context, attrs) {
    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        var height = 0
        // 下面遍历所有child的高度
        for (i in 0 until getChildCount()) {
            val child: View = getChildAt(i)
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED))
            val h = child.measuredHeight
            // 采用最大的view的高度
            if (h > height) {
                height = h
            }
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}