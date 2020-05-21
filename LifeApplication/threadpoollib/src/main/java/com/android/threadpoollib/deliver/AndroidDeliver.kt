package com.android.threadpoollib.deliver

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

class AndroidDeliver : Executor{

    //创建 handler
    private var handler : Handler = Handler(Looper.getMainLooper())

    companion object{
        private var androidDeliver : AndroidDeliver= AndroidDeliver()
        fun getAndroidDeliver() : AndroidDeliver = androidDeliver
    }
    private constructor(){}
    override fun execute(command: Runnable) {
        //当前线程在 主线程
        if (Looper.myLooper() == Looper.getMainLooper()){
            command.run()
            return
        }
        handler.post{ command.run() }
    }

}