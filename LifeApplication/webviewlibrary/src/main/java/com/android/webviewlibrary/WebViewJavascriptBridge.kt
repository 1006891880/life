package com.android.webviewlibrary

interface WebViewJavascriptBridge {
    fun send(data: String?)
    fun send(data: String?, responseCallback: CallBackFunction?)
}