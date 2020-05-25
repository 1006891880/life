package com.android.utilslibrary.loadingDialog.stateload

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.android.utilslibrary.R
import java.util.*

/**
 * 加载等待的Dialog
 */
class CustomLoadingDialog(context: Context?) : OnFinishListener {
    private var mLoadingView: CircularRingView? = null
    private var mLoadingDialog: Dialog?
    private var layout: LinearLayout? = null
    private var loadingText: TextView? = null
    private var mSuccessView: CustomRightView? = null
    private var mFailedView: CustomWrongView? = null
    private var loadSuccessStr: String? = null
    private var loadFailedStr: String? = null
    private var viewList: MutableList<View?>? = null
    /**
     * 当前dialog是否拦截back事件
     *
     * @return 如果拦截返回true，反之false
     */
    private var interceptBack = true
    private var openSuccessAnim = true
    private var openFailedAnim = true
    /**
     * 返回当前绘制的速度
     *
     * @return 速度
     */
    private var speed = 1
    private var time: Long = 1000

    enum class Speed {
        SPEED_ONE, SPEED_TWO
    }

    private fun initView(view: View) {
        layout = view.findViewById<View>(R.id.dialog_view) as LinearLayout
        mLoadingView = view.findViewById<View>(R.id.lv_ring) as CircularRingView
        loadingText = view.findViewById<View>(R.id.loading_text) as TextView
        mSuccessView = view.findViewById<View>(R.id.crv_right) as CustomRightView
        mFailedView = view.findViewById<View>(R.id.cwv_wrong) as CustomWrongView
        initData()
    }

    private fun initData() {
        viewList = ArrayList()
        viewList!!.add(mLoadingView)
        viewList!!.add(mSuccessView)
        viewList!!.add(mFailedView)
        mSuccessView!!.setOnDrawFinishListener(this)
        mFailedView!!.setOnDrawFinishListener(this)
    }

    override fun dispatchFinishEvent(v: View?) {
        if (v is CustomWrongView) {
            h!!.sendEmptyMessageDelayed(2, time)
        } else {
            h!!.sendEmptyMessageDelayed(1, time)
        }
    }

    private fun hideAll() {
        for (v in viewList!!) {
            if (v!!.visibility != View.GONE) {
                v.visibility = View.GONE
            }
        }
    }

    private fun setParams(size: Int) {
        if (size < 0) {
            return
        }
        val successParams: ViewGroup.LayoutParams = mSuccessView!!.getLayoutParams()
        successParams.height = size
        successParams.width = size
        mSuccessView!!.layoutParams = successParams
        val failedParams: ViewGroup.LayoutParams = mFailedView!!.getLayoutParams()
        failedParams.height = size
        failedParams.width = size
        mFailedView!!.layoutParams = failedParams
        val loadingParams = mLoadingView!!.layoutParams
        loadingParams.height = size
        loadingParams.width = size
    }

    @SuppressLint("HandlerLeak")
    private var h: Handler? = object : Handler() {
        override fun handleMessage(msg: Message) {
            close()
        }
    }

    private fun initStyle() {
        if (s != null) {
            setInterceptBack(s!!.isInterceptBack)
            setRepeatCount(s!!.repeatTime)
            setParams(s!!.contentSize)
            setTextSize(s!!.textSize)
            setShowTime(s!!.showTime)
            if (!s!!.isOpenAnim) {
                closeFailedAnim()
                closeSuccessAnim()
            }
            setLoadingText(s!!.getLoadText())
            setSuccessText(s!!.getSuccessText())
            setFailedText(s!!.getFailedText())
        }
    }
    //----------------------------------对外提供的api------------------------------//
    /**
     * 请在最后调用show，因此show返回值为void会使链式api断开
     */
    fun show() {
        hideAll()
        mLoadingView!!.visibility = View.VISIBLE
        mLoadingDialog!!.show()
        mLoadingView!!.startAnim()
    }

    /**
     * 让这个dialog消失，在拦截back事件的情况下一定要调用这个方法！
     * 在调用了该方法之后如需再次使用loadingDialog，请新创建一个
     * LoadingDialog对象。
     */
    fun close() {
        viewList!!.clear()
        if (h != null) {
            h!!.removeCallbacksAndMessages(null)
            h = null
        }
        if (mLoadingDialog != null) {
            mLoadingView!!.stopAnim()
            mLoadingDialog!!.dismiss()
            mLoadingDialog = null
        }
    }

    /**
     * 设置加载时的文字提示
     *
     * @param msg 文字
     * @return 这个对象
     */
    fun setLoadingText(msg: String?): CustomLoadingDialog {
        if (msg != null && msg.length > 0) {
            loadingText!!.text = msg
        }
        return this
    }

    /**
     * 设置加载成功的文字提示
     *
     * @param msg 文字
     * @return 这个对象
     */
    fun setSuccessText(msg: String?): CustomLoadingDialog {
        if (msg != null && msg.length > 0) {
            loadSuccessStr = msg
        }
        return this
    }

    /**
     * 设置加载失败的文字提示
     *
     * @param msg 文字
     * @return 这个对象
     */
    fun setFailedText(msg: String?): CustomLoadingDialog {
        if (msg != null && msg.length > 0) {
            loadFailedStr = msg
        }
        return this
    }

    /**
     * 当你需要一个成功的反馈的时候，在加载成功的回调中调用此方法
     */
    fun loadSuccess() {
        mLoadingView!!.stopAnim()
        hideAll()
        mSuccessView?.setDrawDynamic(openSuccessAnim)
        mSuccessView?.setVisibility(View.VISIBLE)
        loadingText!!.text = loadSuccessStr
    }

    /**
     * 当你需要一个失败的反馈的时候，在加载失败的回调中调用此方法
     */
    fun loadFailed() {
        mLoadingView!!.stopAnim()
        hideAll()
        mFailedView?.setDrawDynamic(openFailedAnim)
        mFailedView?.setVisibility(View.VISIBLE)
        loadingText!!.text = loadFailedStr
    }

    /**
     * 关闭动态绘制
     */
    fun closeSuccessAnim(): CustomLoadingDialog {
        openSuccessAnim = false
        return this
    }

    /**
     * 关闭动态绘制
     */
    fun closeFailedAnim(): CustomLoadingDialog {
        openFailedAnim = false
        return this
    }

    /**
     * 设置是否拦截back，默认会拦截
     *
     * @param interceptBack true拦截back，false不拦截
     * @return 这个对象
     */
    fun setInterceptBack(interceptBack: Boolean): CustomLoadingDialog {
        this.interceptBack = interceptBack
        mLoadingDialog!!.setCancelable(!interceptBack)
        return this
    }

    /**
     * 使用该方法改变成功和失败绘制的速度
     *
     * @param speed 绘制速度
     * @return 这个对象
     */
    fun setLoadSpeed(speed: Speed): CustomLoadingDialog {
        if (speed == Speed.SPEED_ONE) {
            this.speed = 1
            mSuccessView?.setSpeed(1)
            mFailedView?.setSpeed(1)
        } else {
            this.speed = 2
            mSuccessView?.setSpeed(2)
            mFailedView?.setSpeed(2)
        }
        return this
    }

    /**
     * 此方法改变成功失败绘制的颜色
     */
    fun setDrawColor(@ColorInt color: Int): CustomLoadingDialog {
        mFailedView?.setDrawColor(color)
        mSuccessView?.setDrawColor(color)
        loadingText!!.setTextColor(color)
        mLoadingView!!.setColor(color)
        return this
    }

    /**
     * 设置中间弹框的尺寸
     *
     * @param size 尺寸，单位px
     * @return 这个对象
     */
    fun setSize(size: Int): CustomLoadingDialog {
        if (size <= 50) {
            return this
        }
        setParams(size)
        return this
    }

    /**
     * 设置重新绘制的次数，默认只绘制一次，如果你设置这个
     * 数值为1，那么在绘制一次过后，还会再次绘制一次。
     *
     * @param count 绘制次数
     * @return 这个对象
     */
    fun setRepeatCount(count: Int): CustomLoadingDialog {
        mFailedView?.setRepeatTime(count)
        mSuccessView?.setRepeatTime(count)
        return this
    }

    /**
     * 设置反馈展示时间
     *
     * @param time 时间
     * @return 这个对象
     */
    fun setShowTime(time: Long): CustomLoadingDialog {
        if (time < 0) {
            return this
        }
        this.time = time
        return this
    }

    /**
     * 设置加载字体大小
     *
     * @param size 尺寸，单位sp
     * 来将sp转换为对应的px值
     * @return 这个对象
     */
    fun setTextSize(size: Float): CustomLoadingDialog {
        if (size < 0) {
            return this
        }
        loadingText!!.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
        return this
    }

    companion object {
        private var s: CustomStyleManager? = CustomStyleManager(
            true, 0, Speed.SPEED_TWO, -1, -1f, 1000L,
            true, "加载中...", "加载成功", "加载失败"
        )

        fun initStyle(style: CustomStyleManager?) {
            if (style != null) {
                s = style
            }
        }
    }

    init {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.loading_dialog_view, null)
        initView(view)
        // 创建自定义样式的Dialog
        mLoadingDialog = object : Dialog(context!!, R.style.loading_dialog) {
            override fun onBackPressed() {
                if (interceptBack) {
                    return
                }
                close()
            }
        }
        // 设置返回键无效
        mLoadingDialog?.setCancelable(!interceptBack)
        mLoadingDialog?.setContentView(
            layout!!, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )
        initStyle()
    }
}