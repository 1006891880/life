package com.android.utilslibrary.slidelayout

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Scroller


/**
 * 自定义可以滑动的RelativeLayout, 类似于IOS的滑动删除页面效果，当我们要使用
 * 此功能的时候，需要将该Activity的顶层布局设置为SlideFinishLayout，
 */
class SlideFinishLayout : RelativeLayout {
    private val TAG = SlideFinishLayout::class.java.name
    /**
     * SlideFinishLayout布局的父布局
     */
    private var mParentView: ViewGroup? = null
    /**
     * 滑动的最小距离
     */
    private var mTouchSlop = 0
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
    private var mScroller: Scroller? = null
    /**
     * SlideFinishLayout的宽度
     */
    private var viewWidth = 0
    /**
     * 记录是否正在滑动
     */
    private var isSlide = false
    /**
     * 是否开启左侧切换事件
     */
    private var enableLeftSlideEvent = true
    /**
     * 是否开启右侧切换事件
     */
    private var enableRightSlideEvent = true
    /**
     * 按下时范围(处于这个范围内就启用切换事件，目的是使当用户从左右边界点击时才响应)
     */
    private var size = 0
    /**
     * 是否拦截触摸事件
     */
    private var isIntercept = false
    /**
     * 是否可切换
     */
    private var canSwitch = false
    /**
     * 左侧切换
     */
    private var isSwitchFromLeft = false
    /**
     * 右侧侧切换
     */
    private var isSwitchFromRight = false

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs, 0) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
        init(context)
    }

    private fun init(context: Context) {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        Log.i(TAG, "设备的最小滑动距离:$mTouchSlop")
        mScroller = Scroller(context)
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        super.onLayout(changed, l, t, r, b)
        if (changed) { // 获取SlideFinishLayout所在布局的父布局
            mParentView = this.parent as ViewGroup
            viewWidth = this.width
            size = viewWidth
        }
        Log.i(TAG, "viewWidth=$viewWidth")
    }

    fun setEnableLeftSlideEvent(enableLeftSlideEvent: Boolean) {
        this.enableLeftSlideEvent = enableLeftSlideEvent
    }

    fun setEnableRightSlideEvent(enableRightSlideEvent: Boolean) {
        this.enableRightSlideEvent = enableRightSlideEvent
    }

    /**
     * 设置OnSlideFinishListener, 在onSlideFinish()方法中finish Activity
     * @param onSlideFinishListener         onSlideFinishListener
     */
    private var onSlideFinishListener: OnSlideFinishListener? = null

    fun setOnSlideFinishListener(onSlideFinishListener: OnSlideFinishListener?) {
        this.onSlideFinishListener = onSlideFinishListener
    }

    /**
     * 是否拦截事件，如果不拦截事件，对于有滚动的控件的界面将出现问题(相冲突)
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val downX = ev.rawX
        Log.i(TAG, "downX =$downX,viewWidth=$viewWidth")
        if (enableLeftSlideEvent && downX < size) {
            Log.e(TAG, "downX 在左侧范围内 ,拦截事件")
            isIntercept = true
            isSwitchFromLeft = true
            isSwitchFromRight = false
            return false
        } else if (enableRightSlideEvent && downX > viewWidth - size) {
            Log.i(TAG, "downX 在右侧范围内 ,拦截事件")
            isIntercept = true
            isSwitchFromRight = true
            isSwitchFromLeft = false
            return true
        } else {
            Log.i(TAG, "downX 不在范围内 ,不拦截事件")
            isIntercept = false
            isSwitchFromLeft = false
            isSwitchFromRight = false
        }
        return super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean { //不拦截事件时 不处理
        if (!isIntercept) {
            Log.d(TAG, "false------------")
            return false
        }
        Log.d(TAG, "true-----------")
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                run {
                    tempX = event.rawX.toInt()
                    downX = tempX
                }
                downY = event.rawY.toInt()
                Log.d(TAG, "downX---" + downX + "downY---" + downY)
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = event.rawX.toInt()
                val deltaX = tempX - moveX
                tempX = moveX
                if (Math.abs(moveX - downX) > mTouchSlop && Math.abs(event.rawY.toInt() - downY) < mTouchSlop) {
                    isSlide = true
                }
                Log.e(TAG, "scroll deltaX=$deltaX")
                //左侧滑动
                if (enableLeftSlideEvent) {
                    if (moveX - downX >= 0 && isSlide) {
                        mParentView!!.scrollBy(deltaX, 0)
                    }
                }
                //右侧滑动
                if (enableRightSlideEvent) {
                    if (moveX - downX <= 0 && isSlide) {
                        mParentView!!.scrollBy(deltaX, 0)
                    }
                }
                Log.i(
                    "$TAG/onTouchEvent",
                    "mParentView.getScrollX()=" + mParentView!!.scrollX
                )
            }
            MotionEvent.ACTION_UP -> {
                isSlide = false
                //mParentView.getScrollX() <= -viewWidth / 2  ==>指左侧滑动
//mParentView.getScrollX() >= viewWidth / 2   ==>指右侧滑动
                if (mParentView!!.scrollX <= -viewWidth / 2 || mParentView!!.scrollX >= viewWidth / 2) {
                    canSwitch = true
                    if (isSwitchFromLeft) {
                        scrollToRight()
                    }
                    if (isSwitchFromRight) {
                        scrollToLeft()
                    }
                } else {
                    scrollOrigin()
                    canSwitch = false
                }
            }
            else -> {
            }
        }
        return true
    }

    /**
     * 滚动出界面至右侧
     */
    private fun scrollToRight() {
        val delta = viewWidth + mParentView!!.scrollX
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        mScroller!!.startScroll(mParentView!!.scrollX, 0, -delta + 1, 0, Math.abs(delta))
        postInvalidate()
    }

    /**
     * 滚动出界面至左侧
     */
    private fun scrollToLeft() {
        val delta = viewWidth - mParentView!!.scrollX
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
//此处就不可用+1，也不卡直接用delta
        mScroller!!.startScroll(mParentView!!.scrollX, 0, delta - 1, 0, Math.abs(delta))
        postInvalidate()
    }

    /**
     * 滚动到起始位置
     */
    private fun scrollOrigin() {
        val delta = mParentView!!.scrollX
        mScroller!!.startScroll(mParentView!!.scrollX, 0, -delta, 0, Math.abs(delta))
        postInvalidate()
    }

    override fun computeScroll() { // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (mScroller!!.computeScrollOffset()) {
            mParentView!!.scrollTo(mScroller!!.currX, mScroller!!.currY)
            postInvalidate()
            if (mScroller!!.isFinished) {
                if (onSlideFinishListener != null && canSwitch) { //回调，左侧切换事件
                    if (isSwitchFromLeft) {
                        onSlideFinishListener!!.onSlideBack()
                    }
                    //右侧切换事件
                    if (isSwitchFromRight) {
                        onSlideFinishListener!!.onSlideForward()
                    }
                }
            }
        }
    }

    interface OnSlideFinishListener {
        fun onSlideBack()
        fun onSlideForward()
    }
}