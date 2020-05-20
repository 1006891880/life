package com.android.library.stateLayout

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes


class StateFrameLayout : FrameLayout {

    constructor(context: Context): this(context,null){
        Log.e("yyy","constructor  first  ")
    }
    constructor(context: Context,attributeSet: AttributeSet?):this(context,attributeSet,0){
        Log.e("yyy","constructor  two  ")
    }
    constructor(context: Context,attributeSet: AttributeSet?,defStyleAtt:Int):super(context,attributeSet,defStyleAtt){
        Log.e("yyy","constructor  three  ")
    }

    init {
        Log.e("yyy","constructor  init  ")
    }

    /**
     * loading 加载id
     */
    val LAYOUT_LOADING_ID = 1

    /**
     * 内容id
     */
    val LAYOUT_CONTENT_ID = 2

    /**
     * 异常id
     */
    val LAYOUT_ERROR_ID = 3

    /**
     * 网络异常id
     */
    val LAYOUT_NETWORK_ERROR_ID = 4

    /**
     * 空数据id
     */
    val LAYOUT_EMPTY_DATA_ID = 5

    /**
     * 存放布局集合
     */
    private val layoutSparseArray = SparseArray<View?>()

    /**
     * 布局管理器
     */
    private var mStatusLayoutManager: StateLayoutManager? = null


    /**
     * 设置状态管理者manager
     * 该manager主要的操作是设置不同状态view以及设置属性
     * 而该帧布局主要操作是显示和隐藏视图，用帧布局可以较少一层视图层级
     * 这样操作是利用了面向对象中封装类尽量保持类的单一职责，一个类尽量只做一件事情
     * @param statusLayoutManager               manager
     */
    fun setStatusLayoutManager(statusLayoutManager: StateLayoutManager?) {
        mStatusLayoutManager = statusLayoutManager
        //添加所有的布局到帧布局
        addAllLayoutToRootLayout()
    }

    /**
     * 添加所有不同状态布局到帧布局中
     */
    private fun addAllLayoutToRootLayout() {
        if (mStatusLayoutManager!!.contentLayoutResId !== 0) {
            addLayoutResId(
                mStatusLayoutManager!!.contentLayoutResId,
                LAYOUT_CONTENT_ID
            )
        }
        if (mStatusLayoutManager!!.loadingLayoutResId !== 0) {
            addLayoutResId(
                mStatusLayoutManager!!.loadingLayoutResId,
                LAYOUT_LOADING_ID
            )
        }
        if (mStatusLayoutManager!!.emptyDataVs != null) {
            addView(mStatusLayoutManager!!.emptyDataVs)
        }
        if (mStatusLayoutManager!!.errorVs != null) {
            addView(mStatusLayoutManager!!.errorVs)
        }
        if (mStatusLayoutManager!!.netWorkErrorVs != null) {
            addView(mStatusLayoutManager!!.netWorkErrorVs)
        }
    }

    private fun addLayoutResId(@LayoutRes layoutResId: Int, id: Int) {
        val resView =
            LayoutInflater.from(mStatusLayoutManager!!.context).inflate(layoutResId, null)
        layoutSparseArray.put(id, resView)
        addView(resView)
    }

    /**
     * 关闭showLoading
     * @return
     */
    fun goneLoading(): Boolean {
        return if (layoutSparseArray[LAYOUT_LOADING_ID] != null) {
            val view = layoutSparseArray[LAYOUT_LOADING_ID]
            if (view!!.visibility == View.VISIBLE) {
                view.visibility = View.GONE
                true
            } else {
                false
            }
        } else {
            false
        }
    }


    /**
     * 判断是否正在loading中
     * @return                      true 表示loading正在加载中
     */
    fun isLoading(): Boolean {
        val view = layoutSparseArray[LAYOUT_LOADING_ID]
        return if (view != null) {
            view.visibility == View.VISIBLE
        } else {
            false
        }
    }

    /**
     * 显示loading
     */
    fun showLoading() {
        if (layoutSparseArray[LAYOUT_LOADING_ID] != null) {
            showHideViewById(LAYOUT_LOADING_ID)
        }
    }

    /**
     * 显示内容
     */
    fun showContent() {
        if (layoutSparseArray[LAYOUT_CONTENT_ID] != null) {
            showHideViewById(LAYOUT_CONTENT_ID)
        }
    }

    /**
     * 显示空数据
     */
    fun showEmptyData(iconImage: Int, textTip: String) {
        if (inflateLayout(LAYOUT_EMPTY_DATA_ID)) {
            showHideViewById(LAYOUT_EMPTY_DATA_ID)
            emptyDataViewAddData(iconImage, textTip)
        }
    }

    /**
     * 显示网络异常
     */
    fun showNetWorkError() {
        if (inflateLayout(LAYOUT_NETWORK_ERROR_ID)) {
            showHideViewById(LAYOUT_NETWORK_ERROR_ID)
        }
    }

    /**
     * 显示异常
     */
    fun showError(iconImage: Int, textTip: String) {
        if (inflateLayout(LAYOUT_ERROR_ID)) {
            showHideViewById(LAYOUT_ERROR_ID)
            errorViewAddData(iconImage, textTip)
        }
    }

    /**
     * 空数据并且设置页面简单数据
     * @param iconImage                 空页面图片
     * @param textTip                   文字
     */
    private fun emptyDataViewAddData(iconImage: Int, textTip: String) {
        if (iconImage == 0 && TextUtils.isEmpty(textTip)) {
            return
        }
        val emptyDataView = layoutSparseArray[LAYOUT_EMPTY_DATA_ID]
        val iconImageView =
            emptyDataView!!.findViewById<View>(mStatusLayoutManager!!.emptyDataIconImageId)
        val textView =
            emptyDataView.findViewById<View>(mStatusLayoutManager!!.emptyDataTextTipId)
        if (iconImageView != null && iconImageView is ImageView) {
            iconImageView.setImageResource(iconImage)
        }
        if (textView != null && textView is TextView) {
            textView.text = textTip
        }
    }

    /**
     * 展示空页面
     * @param objects                   object
     */
    fun showLayoutEmptyData(vararg objects: Any?) {
        if (inflateLayout(LAYOUT_EMPTY_DATA_ID)) {
            showHideViewById(LAYOUT_EMPTY_DATA_ID)
            val emptyDataLayout = mStatusLayoutManager!!.emptyDataLayout
            emptyDataLayout?.setData(objects)
        }
    }

    private fun errorViewAddData(iconImage: Int, textTip: String) {
        if (iconImage == 0 && TextUtils.isEmpty(textTip)) {
            return
        }
        val errorView = layoutSparseArray[LAYOUT_ERROR_ID]
        val iconImageView =
            errorView!!.findViewById<View>(mStatusLayoutManager!!.emptyDataIconImageId)
        val textView =
            errorView.findViewById<View>(mStatusLayoutManager!!.emptyDataTextTipId)
        if (iconImageView != null && iconImageView is ImageView) {
            iconImageView.setImageResource(iconImage)
        }
        if (textView != null && textView is TextView) {
            textView.text = textTip
        }
    }

    /**
     * 展示错误
     * @param objects
     */
    fun showLayoutError(vararg objects: Any?) {
        if (inflateLayout(LAYOUT_ERROR_ID)) {
            showHideViewById(LAYOUT_ERROR_ID)
            val errorLayout = mStatusLayoutManager!!.errorLayout
            errorLayout?.setData(objects)
        }
    }

    /**
     * 根据ID显示隐藏布局
     * @param id                    id值
     */
    private fun showHideViewById(id: Int) { //这个需要遍历集合中数据，然后切换显示和隐藏
        for (i in 0 until layoutSparseArray.size()) {
            val key = layoutSparseArray.keyAt(i)
            val valueView = layoutSparseArray.valueAt(i)
            //显示该view
            if (key == id) { //显示该视图
                valueView!!.visibility = View.VISIBLE
                if (mStatusLayoutManager!!.onShowHideViewListener != null) {
                    mStatusLayoutManager!!.onShowHideViewListener!!.onShowView(valueView, key)
                }
            } else { //隐藏该视图
                if (valueView!!.visibility != View.GONE) {
                    valueView.visibility = View.GONE
                    if (mStatusLayoutManager!!.onShowHideViewListener != null) {
                        mStatusLayoutManager!!.onShowHideViewListener!!.onHideView(valueView, key)
                    }
                }
            }
        }
    }


    /**
     * 主要是显示ViewStub布局，比如网络异常，加载异常以及空数据等页面
     * 注意该方法中只有当切换到这些页面的时候，才会将ViewStub视图给inflate出来，之后才会走视图绘制的三大流程
     * 方法里面通过id判断来执行不同的代码，首先判断ViewStub是否为空，如果为空就代表没有添加这个View就返回false，
     * 不为空就加载View并且添加到集合当中，然后调用showHideViewById方法显示隐藏View，
     * retryLoad方法是给重试按钮添加事件
     * @param id                        布局id
     * @return                          是否inflate出视图
     */
    private fun inflateLayout(id: Int): Boolean {
        var isShow = true
        if (layoutSparseArray[id] != null) {
            return isShow
        }
        when (id) {
            LAYOUT_NETWORK_ERROR_ID -> isShow =
                if (mStatusLayoutManager!!.netWorkErrorVs != null) { //只有当展示的时候，才将ViewStub视图给inflate出来
                    val view = mStatusLayoutManager!!.netWorkErrorVs!!.inflate()
                    view.setOnClickListener {
                        Log.i("重试加载", "网络异常")
                        mStatusLayoutManager!!.onNetworkListener!!.onNetwork()
                    }
                    layoutSparseArray.put(id, view)
                    true
                } else {
                    false
                }
            LAYOUT_ERROR_ID -> isShow =
                if (mStatusLayoutManager!!.errorVs != null) { //只有当展示的时候，才将ViewStub视图给inflate出来
                    val view = mStatusLayoutManager!!.errorVs!!.inflate()
                    if (mStatusLayoutManager!!.errorLayout != null) {
                        mStatusLayoutManager!!.errorLayout!!.setView(view)
                    }
                    view.setOnClickListener { mStatusLayoutManager!!.onRetryListener!!.onRetry() }
                    layoutSparseArray.put(id, view)
                    true
                } else {
                    false
                }
            LAYOUT_EMPTY_DATA_ID -> isShow =
                if (mStatusLayoutManager!!.emptyDataVs != null) { //只有当展示的时候，才将ViewStub视图给inflate出来
                    val view = mStatusLayoutManager!!.emptyDataVs!!.inflate()
                    if (mStatusLayoutManager!!.emptyDataLayout != null) {
                        mStatusLayoutManager!!.emptyDataLayout!!.setView(view)
                    }
                    view.setOnClickListener { mStatusLayoutManager!!.onRetryListener!!.onRetry() }
                    layoutSparseArray.put(id, view)
                    true
                } else {
                    false
                }
            else -> {
            }
        }
        return isShow
    }

}