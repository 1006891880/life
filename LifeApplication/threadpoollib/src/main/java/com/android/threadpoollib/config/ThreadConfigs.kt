package com.android.threadpoollib.config

import com.android.threadpoollib.callback.AsyncCallback
import com.android.threadpoollib.callback.ThreadCallback
import java.util.concurrent.Executor

class ThreadConfigs {

    //thread name
    var threadName : String? = null

    //线程执行 延迟的时间
    var delayTime : Long = 0

    //线程的执行者
    var executor:Executor?= null

    //用户任务的状态 的回调监听
    var threadCallback : ThreadCallback ? = null

    //异步 回调 callback
    var asyncCallback : AsyncCallback? = null

}