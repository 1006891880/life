package com.android.threadpoollib.factory

import java.util.concurrent.ThreadFactory

class MyThreadFactory : ThreadFactory{

    var priority :Int = 0
    var threadName : String?= null
    constructor(priority :Int,threadName:String){
        this.priority = priority
        this.threadName = threadName
    }
    override fun newThread(r: Runnable): Thread {
        var thread : Thread = Thread(r)
        thread.name = threadName
        thread.priority = priority
        return thread
    }
}