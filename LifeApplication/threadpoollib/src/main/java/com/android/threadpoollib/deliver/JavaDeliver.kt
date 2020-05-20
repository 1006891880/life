package com.android.threadpoollib.deliver

import java.util.concurrent.Executor

class JavaDeliver :Executor {

    companion object{
        private var javaDeliver : JavaDeliver= JavaDeliver()
        fun getJavaDeliver() = javaDeliver
    }

    override fun execute(command: Runnable) {
        command.run()
    }
}