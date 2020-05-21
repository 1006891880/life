package com.android.threadpoollib.callback


//异步回调接口
interface AsyncCallback {
    //成功
    fun<T> success(t:T)

    //失败
    fun onFailed(throwable: Throwable)

    //开始
    fun start(threadName :String)

}