package com.android.webviewlibrary.webview

import android.content.Context
import android.util.AttributeSet
import com.android.webviewlibrary.CallBackFunction
import com.android.webviewlibrary.WebViewJavascriptBridge
import com.tencent.smtt.sdk.WebView

class BridgeWebView : WebView, WebViewJavascriptBridge {

    constructor(context: Context):this(context,null)
    constructor(context: Context,attributeSet: AttributeSet?): this(context,attributeSet,0)
    constructor(context: Context,attributeSet: AttributeSet?,defStyle:Int): super(context,attributeSet){
        initView()
    }

    private fun initView() {

    }

    override fun send(data: String?) {

    }

    override fun send(data: String?, responseCallback: CallBackFunction?) {

    }
}