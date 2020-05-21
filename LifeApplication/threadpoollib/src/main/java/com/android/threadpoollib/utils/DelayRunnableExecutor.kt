package com.android.threadpoollib.utils

import com.android.threadpoollib.factory.MyThreadFactory
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class DelayRunnableExecutor {

    companion object{
        private var delayRunnableExecutor:DelayRunnableExecutor= DelayRunnableExecutor()
        fun getInstance(): DelayRunnableExecutor = delayRunnableExecutor
    }
    var dispatcher : ScheduledExecutorService =
        Executors.newScheduledThreadPool(1,MyThreadFactory(Thread.MAX_PRIORITY,"Delay-Task-Dispatcher"))

    fun postDelay(delay:Long,  pool :ExecutorService,runnable: Runnable){
        if (delay == 0L){
            runnable.run()
            return
        }
        dispatcher.schedule({ pool.execute(runnable) },delay,TimeUnit.MILLISECONDS)
    }


}