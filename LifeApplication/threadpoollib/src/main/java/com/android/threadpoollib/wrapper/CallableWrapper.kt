package com.android.threadpoollib.wrapper

import com.android.threadpoollib.callback.NormalCallback
import com.android.threadpoollib.config.ThreadConfigs
import com.android.threadpoollib.utils.ThreadUtils
import java.lang.Exception
import java.util.concurrent.Callable

class CallableWrapper<T> : Callable<T> {

    private var name:String
    private var callback: NormalCallback
    private var proxy : Callable<T>

    constructor(config1:ThreadConfigs,proxy:Callable<T>){
        this.name = config1.threadName!!
        this.proxy = proxy
        this.callback = NormalCallback(config1.threadCallback!!,config1.executor!!,config1.asyncCallback!!)
    }

    override fun call(): T? {
        ThreadUtils.resetThread(Thread.currentThread(),name,callback)
        callback.start(name)
        var t :T?= null
        try {
            t = proxy.call()
            callback.success(t)
        }catch (ex:Exception){
            callback.onFailed(ex)
        }finally {
//            callback.onCompleted(name)
        }
        return t
    }
}