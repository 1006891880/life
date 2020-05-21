package com.android.threadpoollib.utils

import android.util.Log
import com.android.threadpoollib.callback.ThreadCallback

object ThreadUtils {
    //静态代码块
    var isAndroid = false
    private const val TAG :String ="ThreadUtils"
    init {
        //判断是否是安卓 环境
        isAndroid = try {
            Class.forName("android.os.Build")
            Log.e(TAG,"init  ---- ")
            true
        }catch (ex: Exception){
            false
        }
    }
    @JvmStatic
     fun resetThread(thread:Thread,name:String,threadCallback: ThreadCallback){
        thread.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler(){ _:Thread, throwable:Throwable->
            run {
                threadCallback.onError(name, throwable)
            }
        }
        thread.name = name
    }
    @JvmStatic
    fun sleepThread(time: Long) {
        if (time <= 0) {
            return
        }
        try {
            Thread.sleep(time)
        } catch (e: InterruptedException) {
            throw RuntimeException("Thread has been interrupted", e)
        }
    }

}