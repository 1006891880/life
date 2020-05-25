package com.android.webviewlibrary

/**
 * 自定义接口，处理消息回调逻辑
 */
interface BridgeHandler {
    /**
     * 处理消息
     * @param data                        消息内容
     * @param function                    回调
     */
    fun handler(data: String?, function: CallBackFunction?)
}