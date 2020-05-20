package com.android.library.stateLayout

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import androidx.annotation.LayoutRes

// 管理状态的管理器
class StateLayoutManager {

    var context: Context? = null
    var netWorkErrorVs: ViewStub? = null
    var netWorkErrorRetryViewId = 0
    var emptyDataVs: ViewStub? = null
    var emptyDataRetryViewId = 0
    var errorVs: ViewStub? = null
    var errorRetryViewId = 0
    var loadingLayoutResId = 0
    var contentLayoutResId = 0
    var retryViewId = 0
    var emptyDataIconImageId = 0
    var emptyDataTextTipId = 0
    var errorIconImageId = 0
    var errorTextTipId = 0

    var errorLayout: AbsViewStubLayout? = null
    var emptyDataLayout: AbsViewStubLayout? = null

    var onShowHideViewListener: OnShowHideViewListener? = null
    var onRetryListener: OnRetryListener? = null
    var onNetworkListener: OnNetworkListener? = null
    private var rootFrameLayout: StateFrameLayout? = null

    fun newBuilder(context: Context?): Builder? {
        return Builder(context)
    }

    fun newBuilder(context: Context?, wrapContent: Boolean): Builder? {
        return Builder(context, wrapContent)
    }

    /**
     * 复杂构造对象，使用builder模式
     * @param builder                       builder
     * @param wrapContent                   是否包裹内容
     */
    constructor(builder: Builder, wrapContent: Boolean) {
        context = builder.context
        loadingLayoutResId = builder.loadingLayoutResId
        netWorkErrorVs = builder.netWorkErrorVs
        netWorkErrorRetryViewId = builder.netWorkErrorRetryViewId
        emptyDataVs = builder.emptyDataVs
        emptyDataRetryViewId = builder.emptyDataRetryViewId
        errorVs = builder.errorVs
        errorRetryViewId = builder.errorRetryViewId
        contentLayoutResId = builder.contentLayoutResId
        onShowHideViewListener = builder.onShowHideViewListener
        retryViewId = builder.retryViewId
        onRetryListener = builder.onRetryListener
        onNetworkListener = builder.onNetworkListener
        emptyDataIconImageId = builder.emptyDataIconImageId
        emptyDataTextTipId = builder.emptyDataTextTipId
        errorIconImageId = builder.errorIconImageId
        errorTextTipId = builder.errorTextTipId
        errorLayout = builder.errorLayout
        emptyDataLayout = builder.emptyDataLayout
        //创建帧布局
        rootFrameLayout = StateFrameLayout(context!!)
        //是否包裹内容
        val layoutParams: ViewGroup.LayoutParams = if (wrapContent) {
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
        } else {
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        rootFrameLayout?.layoutParams = layoutParams
        //设置为白色
        rootFrameLayout?.setBackgroundColor(Color.WHITE)
        //设置状态管理器
        rootFrameLayout?.setStatusLayoutManager(this)
    }

    /**
     * 判断是否在showLoading中
     * @return
     */
    fun isShowLoading(): Boolean {
        return rootFrameLayout!!.isLoading()
    }

    /**
     * 显示loading
     */
    fun showLoading() {
        if (!isShowLoading()) {
            rootFrameLayout!!.showLoading()
        }
    }

    /**
     * 隐藏loading
     */
    fun goneLoading() {
        rootFrameLayout!!.goneLoading()
    }

    /**
     * 显示内容
     */
    fun showContent() {
        rootFrameLayout!!.showContent()
    }

    /**
     * 显示空数据
     */
    fun showEmptyData(iconImage: Int, textTip: String?) {
        rootFrameLayout!!.showEmptyData(iconImage, textTip!!)
    }


    /**
     * 显示空数据
     */
    fun showEmptyData() {
        showEmptyData(0, "")
    }

    /**
     * 显示空数据
     */
    fun showLayoutEmptyData(vararg objects: Any?) {
        rootFrameLayout!!.showLayoutEmptyData(objects)
    }

    /**
     * 显示网络异常
     */
    fun showNetWorkError() {
        rootFrameLayout!!.showNetWorkError()
    }

    /**
     * 显示异常
     */
    fun showError(iconImage: Int, textTip: String?) {
        rootFrameLayout!!.showError(iconImage, textTip!!)
    }

    /**
     * 显示异常
     */
    fun showError() {
        showError(0, "")
    }

    /**
     * 显示异常
     * @param objects               objects
     */
    fun showLayoutError(vararg objects: Any?) {
        rootFrameLayout!!.showLayoutError(objects)
    }

    /**
     * 得到root 布局
     */
    fun getRootLayout(): View? {
        return rootFrameLayout
    }

    class Builder {
         var context: Context?
        /**
         * StateFrameLayout布局创建时，是否包裹内容，默认是MATCH_PARENT
         */
         var wrapContent = false
         var loadingLayoutResId = 0
         var contentLayoutResId = 0
         var netWorkErrorVs: ViewStub? = null
         var netWorkErrorRetryViewId = 0
         var emptyDataVs: ViewStub? = null
         var emptyDataRetryViewId = 0
         var errorVs: ViewStub? = null
         var errorRetryViewId = 0
         var retryViewId = 0
         var emptyDataIconImageId = 0
         var emptyDataTextTipId = 0
         var errorIconImageId = 0
         var errorTextTipId = 0
         var errorLayout: AbsViewStubLayout? = null
         var emptyDataLayout: AbsViewStubLayout? = null
         var onShowHideViewListener: OnShowHideViewListener? = null
         var onRetryListener: OnRetryListener? = null
         var onNetworkListener: OnNetworkListener? = null
        //internal 只能在当前 model 使用
        internal constructor(context: Context?) {
            this.context = context
        }

        internal constructor(context: Context?, wrapContent: Boolean) {
            this.context = context
            this.wrapContent = wrapContent
        }

        /**
         * 自定义加载布局
         */
        fun loadingView(@LayoutRes loadingLayoutResId: Int): Builder {
            this.loadingLayoutResId = loadingLayoutResId
            return this
        }

        /**
         * 自定义网络错误布局
         */
        fun netWorkErrorView(@LayoutRes newWorkErrorId: Int): Builder {
            netWorkErrorVs = ViewStub(context)
            netWorkErrorVs!!.layoutResource = newWorkErrorId
            return this
        }

        /**
         * 自定义加载空数据布局
         */
        fun emptyDataView(@LayoutRes noDataViewId: Int): Builder {
            emptyDataVs = ViewStub(context)
            emptyDataVs!!.layoutResource = noDataViewId
            return this
        }

        /**
         * 自定义加载错误布局
         */
        fun errorView(@LayoutRes errorViewId: Int): Builder {
            errorVs = ViewStub(context)
            errorVs!!.layoutResource = errorViewId
            return this
        }

        /**
         * 自定义加载内容正常布局
         */
        fun contentView(@LayoutRes contentLayoutResId: Int): Builder {
            this.contentLayoutResId = contentLayoutResId
            return this
        }

        /**
         * 自定义异常布局
         * @param errorLayout                   error
         * @return
         */
        fun errorLayout(errorLayout: AbsViewStubLayout): Builder {
            this.errorLayout = errorLayout
            errorVs = errorLayout.getLayoutVs()
            return this
        }

        /**
         * 自定义空数据布局
         * @param emptyDataLayout              emptyDataLayout
         * @return
         */
        fun emptyDataLayout(emptyDataLayout: AbsViewStubLayout): Builder {
            this.emptyDataLayout = emptyDataLayout
            emptyDataVs = emptyDataLayout.getLayoutVs()
            return this
        }

        /**
         * 自定义网络异常布局
         * @param netWorkErrorRetryViewId       layoutId
         * @return
         */
        fun netWorkErrorRetryViewId(@LayoutRes netWorkErrorRetryViewId: Int): Builder {
            this.netWorkErrorRetryViewId = netWorkErrorRetryViewId
            return this
        }

        /**
         * 自定义空数据异常布局
         * @param emptyDataRetryViewId          layoutId
         * @return
         */
        fun emptyDataRetryViewId(@LayoutRes emptyDataRetryViewId: Int): Builder {
            this.emptyDataRetryViewId = emptyDataRetryViewId
            return this
        }

        /**
         * 自定义重新刷新布局
         * @param errorRetryViewId              layoutId
         * @return
         */
        fun errorRetryViewId(@LayoutRes errorRetryViewId: Int): Builder {
            this.errorRetryViewId = errorRetryViewId
            return this
        }

        fun retryViewId(@LayoutRes retryViewId: Int): Builder {
            this.retryViewId = retryViewId
            return this
        }

        fun emptyDataIconImageId(@LayoutRes emptyDataIconImageId: Int): Builder {
            this.emptyDataIconImageId = emptyDataIconImageId
            return this
        }

        fun emptyDataTextTipId(@LayoutRes emptyDataTextTipId: Int): Builder {
            this.emptyDataTextTipId = emptyDataTextTipId
            return this
        }

        fun errorIconImageId(@LayoutRes errorIconImageId: Int): Builder {
            this.errorIconImageId = errorIconImageId
            return this
        }

        fun errorTextTipId(@LayoutRes errorTextTipId: Int): Builder {
            this.errorTextTipId = errorTextTipId
            return this
        }

        /**
         * 为状态View显示隐藏监听事件
         * @param listener                  listener
         * @return
         */
        fun onShowHideViewListener(listener: OnShowHideViewListener?): Builder {
            onShowHideViewListener = listener
            return this
        }

        /**
         * 为重试加载按钮的监听事件
         * @param onRetryListener           listener
         * @return
         */
        fun onRetryListener(onRetryListener: OnRetryListener?): Builder {
            this.onRetryListener = onRetryListener
            return this
        }

        /**
         * 为重试加载按钮的监听事件
         * @param onNetworkListener           listener
         * @return
         */
        fun onNetworkListener(onNetworkListener: OnNetworkListener?): Builder {
            this.onNetworkListener = onNetworkListener
            return this
        }

        /**
         * 创建对象
         * @return
         */
        fun build(): StateLayoutManager {
            return StateLayoutManager(this, wrapContent)
        }
    }

}