package com.android.webviewlibrary

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi

/**
 * 实现WebView的滑动监听和优雅处理回退栈问题
 */
class ScrollWebView : WebView {
    private var isTouchByUser = false

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    var onScrollWebListener: OnScrollWebListener? = null
        private set

    fun setScrollWebListener(onScrollWebListener: OnScrollWebListener?) {
        this.onScrollWebListener = onScrollWebListener
    }

    interface OnScrollWebListener {
        fun onScroll(dx: Int, dy: Int)
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (onScrollWebListener != null) {
            onScrollWebListener!!.onScroll(l - oldl, t - oldt)
        }
    }

    override fun loadUrl(
        url: String,
        additionalHttpHeaders: Map<String, String>
    ) {
        super.loadUrl(url, additionalHttpHeaders)
        resetAllStateInternal(url)
    }

    override fun loadUrl(url: String) {
        super.loadUrl(url)
        resetAllStateInternal(url)
    }

    override fun postUrl(url: String, postData: ByteArray) {
        super.postUrl(url, postData)
        resetAllStateInternal(url)
    }

    override fun loadData(data: String, mimeType: String?, encoding: String?) {
        super.loadData(data, mimeType, encoding)
        resetAllStateInternal(url)
    }

    override fun loadDataWithBaseURL(baseUrl: String?, data: String,
                                     mimeType: String?, encoding: String?, historyUrl: String?) {
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl)
        resetAllStateInternal(url)
    }

    override fun reload() {
        super.reload()
        resetAllStateInternal(url)
    }

    private fun resetAllStateInternal(url: String) {
        if (!TextUtils.isEmpty(url) && url.startsWith("javascript:")) {
            return
        }
        resetAllState()
    }

    /**
     * 加载url时重置touch状态
     */
    protected fun resetAllState() {
        isTouchByUser = false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN ->  //用户按下到下一个链接加载之前，置为true
                isTouchByUser = true
            else -> {
            }
        }
        return super.onTouchEvent(event)
    }

    override fun setWebViewClient(client: WebViewClient) {
        super.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                val handleByChild = null != client && client.shouldOverrideUrlLoading(view, url)
                return if (handleByChild) { // 开放client接口给上层业务调用，如果返回true，表示业务已处理。
                    true
                } else if (!isTouchByUser) { // 如果业务没有处理，并且在加载过程中用户没有再次触摸屏幕，认为是301/302事件，直接交由系统处理。
                    super.shouldOverrideUrlLoading(view, url)
                } else { //否则，属于二次加载某个链接的情况，为了解决拼接参数丢失问题，重新调用loadUrl方法添加固有参数。
                    loadUrl(url)
                    true
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val handleByChild = null != client && client.shouldOverrideUrlLoading(view, request)
                return if (handleByChild) {
                    true
                } else if (!isTouchByUser) {
                    super.shouldOverrideUrlLoading(view, request)
                } else {
                    loadUrl(request.url.toString())
                    true
                }
            }
        })
    }
}