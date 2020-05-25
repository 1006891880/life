package com.android.utilslibrary.slidelayout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.Scroller


/**
 * 自定义可以滑动的RelativeLayout, 类似于IOS的滑动删除页面效果，当我们要使用
 * 此功能的时候，需要将该Activity的顶层布局设置为SlitherFinishLayout，
 * 然后需要调用setTouchView()方法来设置需要滑动的View
 */
class SlitherFinishLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet?,
    defStyle: Int = 0
) :
    RelativeLayout(context, attrs, defStyle), OnTouchListener {
    /**
     * SlitherFinishLayout布局的父布局
     */
    private var mParentView: ViewGroup? = null
    /**
     * 处理滑动逻辑的View
     */
    private var touchView: View? = null
    /**
     * 滑动的最小距离
     */
    private val mTouchSlop: Int
    /**
     * 按下点的X坐标
     */
    private var downX = 0
    /**
     * 按下点的Y坐标
     */
    private var downY = 0
    /**
     * 临时存储X坐标
     */
    private var tempX = 0
    /**
     * 滑动类
     */
    private val mScroller: Scroller
    /**
     * SlitherFinishLayout的宽度
     */
    private var viewWidth = 0
    /**
     * 记录是否正在滑动
     */
    private var isSlither = false
    private var onSlitherFinishListener: OnSlitherFinishListener? = null
    private var isFinish = false
    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        super.onLayout(changed, l, t, r, b)
        if (changed) { // 获取SlitherFinishLayout所在布局的父布局
            mParentView = this.parent as ViewGroup
            viewWidth = this.width
        }
    }

    /**
     * 设置OnSlitherFinishListener, 在onSlitherFinish()方法中finish Activity
     * @param onSlitherFinishListener           listener
     */
    fun setOnSlitherFinishListener(onSlitherFinishListener: OnSlitherFinishListener?) {
        this.onSlitherFinishListener = onSlitherFinishListener
    }

    /**
     * 设置Touch的View
     * @param touchView                 触摸view
     */
    fun setTouchView(touchView: View) {
        this.touchView = touchView
        touchView.setOnTouchListener(this)
    }

    /**
     * 滚动出界面
     */
    private fun scrollRight() {
        val delta = viewWidth + mParentView!!.scrollX
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        mScroller.startScroll(mParentView!!.scrollX, 0, -delta + 1, 0, Math.abs(delta))
        postInvalidate()
    }

    /**
     * 滚动到起始位置
     */
    private fun scrollOrigin() {
        val delta = mParentView!!.scrollX
        mScroller.startScroll(mParentView!!.scrollX, 0, -delta, 0, Math.abs(delta))
        postInvalidate()
    }

    /**
     * touch的View是否是AbsListView， 例如ListView, GridView等其子类
     * @return
     */
    private val isTouchOnAbsListView: Boolean
        private get() = touchView is AbsListView

    /**
     * touch的view是否是ScrollView或者其子类
     * @return
     */
    private val isTouchOnScrollView: Boolean
        private get() {
            return touchView is ScrollView
        }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                run {
                    tempX = event.rawX.toInt()
                    downX = tempX
                }
                downY = event.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = event.rawX.toInt()
                val deltaX = tempX - moveX
                tempX = moveX
                if (Math.abs(moveX - downX) > mTouchSlop && Math.abs(event.rawY.toInt() - downY) < mTouchSlop) {
                    isSlither = true
                    // 若touchView是AbsListView，
// 则当手指滑动，取消item的点击事件，不然我们滑动也伴随着item点击事件的发生
                    if (isTouchOnAbsListView) {
                        val cancelEvent = MotionEvent.obtain(event)
                        cancelEvent.action = (MotionEvent.ACTION_CANCEL
                                or (event.actionIndex shl MotionEvent.ACTION_POINTER_INDEX_SHIFT))
                        v.onTouchEvent(cancelEvent)
                    }
                }
                if (moveX - downX >= 0 && isSlither) {
                    mParentView!!.scrollBy(deltaX, 0)
                    // 屏蔽在滑动过程中ListView ScrollView等自己的滑动事件
                    if (isTouchOnScrollView || isTouchOnAbsListView) {
                        return true
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                isSlither = false
                if (mParentView!!.scrollX <= -viewWidth / 2) { //当X轴滑动位移大于或者等于1/2屏幕宽度时，滚动出界面
                    isFinish = true
                    scrollRight()
                } else { //当X轴滑动位移小于1/2屏幕宽度时，滚动到起始位置
                    scrollOrigin()
                    isFinish = false
                }
            }
            else -> {
            }
        }
        // 假如touch的view是AbsListView或者ScrollView 我们处理完上面自己的逻辑之后
        // 再交给AbsListView, ScrollView自己处理其自己的逻辑
        return if (isTouchOnScrollView || isTouchOnAbsListView) {
            v.onTouchEvent(event)
        } else true
        // 其他的情况直接返回true
    }

    override fun computeScroll() { // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (mScroller.computeScrollOffset()) {
            mParentView!!.scrollTo(mScroller.currX, mScroller.currY)
            postInvalidate()
            if (mScroller.isFinished) {
                if (onSlitherFinishListener != null && isFinish) {
                    onSlitherFinishListener!!.onSlitherFinish()
                }
            }
        }
    }

    interface OnSlitherFinishListener {
        /**
         * 在滑动结束后调用
         */
        fun onSlitherFinish()
    }

    init {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mScroller = Scroller(context)
    }
}