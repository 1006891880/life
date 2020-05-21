package com.android.threadpoollib.wrapper

import com.android.threadpoollib.callback.NormalCallback
import com.android.threadpoollib.config.ThreadConfigs
import com.android.threadpoollib.utils.ThreadUtils
import java.lang.Exception
import java.util.concurrent.Callable

class RunnableWrapper :Runnable {

    var name:String
    var normalCallback:NormalCallback
    var runnable :Runnable?= null
//    var callable :Callable<T>?= null
    constructor(configs: ThreadConfigs){
        this.name = configs.threadName!!
        this.normalCallback = NormalCallback(configs.threadCallback!!,configs.executor!!,configs.asyncCallback!!)
    }

    public fun setRunnable(run:Runnable):RunnableWrapper{
        this.runnable = run
        return this
    }
/*    public fun setCallable(run:Callable<T>):RunnableWrapper<T>{
        this.callable = run
        return this
    }*/

    override fun run() {
        ThreadUtils.resetThread(Thread.currentThread(), name, normalCallback)
        normalCallback.onStart(name)

        if (runnable != null){
            runnable!!.run()
        }/*else if (callable != null){
            try {
               var obj =  callable!!.call()
                normalCallback.success(obj)
            }catch (ex:Exception){
                normalCallback.onError(name,ex)
            }
        }*/

        normalCallback.onCompleted(name)
    }
}