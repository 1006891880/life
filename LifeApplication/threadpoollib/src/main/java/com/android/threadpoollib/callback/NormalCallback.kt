package com.android.threadpoollib.callback

import java.util.concurrent.Executor

//回调的委托类
class NormalCallback :ThreadCallback,AsyncCallback{

    var threadCallback : ThreadCallback?= null
    var executor : Executor?= null
    var asyncCallback : AsyncCallback?= null
    constructor(threadCallback: ThreadCallback,executor : Executor,asyncCallback: AsyncCallback){
        this.threadCallback = threadCallback
        this.executor = executor
        this.asyncCallback = asyncCallback
    }

    //线程发生错误
    override fun onError(threadName: String, throwable: Throwable) {
        onFailed(throwable)
        if (threadCallback ==null){
            return
        }
        executor?.execute { threadCallback?.onError(threadName,throwable) }
    }

    //线程执行完毕
    override fun onCompleted(threadName: String) {
        if (threadCallback == null){
            return
        }
        executor?.execute { threadCallback?.onCompleted(threadName) }
    }

    //线程开始执行
    override fun onStart(threadName: String) {
        if (threadCallback ==null){
            return
        }
        executor?.execute { threadCallback?.onStart(threadName) }
    }

    //回调成功
    override fun<T> success(t: T) {
        if (asyncCallback == null){
            return
        }
        executor?.execute { asyncCallback?.success(t) }
    }
    //回调失败
    override fun onFailed(throwable: Throwable) {
        if (asyncCallback == null){
            return
        }
        executor?.execute { asyncCallback?.onFailed(throwable) }
    }

    //异步回调接口 start
    override fun start(threadName: String) {
        if (asyncCallback == null){
            return
        }
        executor?.execute { asyncCallback?.start(threadName) }
    }


}