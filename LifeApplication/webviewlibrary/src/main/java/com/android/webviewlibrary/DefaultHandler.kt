package com.android.webviewlibrary

class DefaultHandler : BridgeHandler {
    override fun handler(data: String?, function: CallBackFunction?) {
        function?.onCallBack("DefaultHandler response data")
    }
}