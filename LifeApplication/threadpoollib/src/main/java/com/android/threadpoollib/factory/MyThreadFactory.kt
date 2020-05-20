package com.android.threadpoollib.factory

import java.util.concurrent.ThreadFactory

class MyThreadFactory : ThreadFactory{

    var priority :Int = 0
    constructor(priority :Int){
        this.priority = priority
    }
    override fun newThread(r: Runnable): Thread {
        var thread : Thread = Thread(r)
        thread.priority = priority
        return thread
    }
}