package com.android.threadpoollib.callback

//线程的回调
interface ThreadCallback {

    //线程发生错误
    fun onError(threadName:String ,throwable: Throwable)

    //通知用户线程执行完毕
    fun onCompleted(threadName: String)

    //线程任务开始执行
    fun onStart(threadName: String)

}