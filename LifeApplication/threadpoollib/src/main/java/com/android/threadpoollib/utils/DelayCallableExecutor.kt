package com.android.threadpoollib.utils

import com.android.threadpoollib.factory.MyThreadFactory
import com.android.threadpoollib.wrapper.CallableWrapper
import java.util.concurrent.*

class DelayCallableExecutor<T> {

/*    companion object CREATE{
        private var delayRunnableExecutor = DelayCallableExecutor<T>()
        fun getInstance() = DelayCallableExecutor<T>()
    }*/

    var dispatcher : ScheduledExecutorService =
        Executors.newScheduledThreadPool(1,MyThreadFactory(Thread.MAX_PRIORITY,"Delay-Callable-Dispatcher"))

    fun postDelay(delay:Long, runnable: CallableWrapper<T>){
        if (delay == 0L){
            runnable.call()
            return
        }
        dispatcher.schedule({ runnable.call() },delay,TimeUnit.MILLISECONDS)
    }


}