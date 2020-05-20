package com.android.library.base.callback

import cn.ycbjie.ycthreadpoollib.callback.ThreadCallback
import com.blankj.utilcode.util.LogUtils

class LogCallback :ThreadCallback {
    private val TAG = "LogCallback"
    override fun onError(threadName: String?, t: Throwable?) {
        LogUtils.e("LogCallback" + "------onError")
        LogUtils.e(
            TAG,
            String.format(
                "[任务线程%s]/[回调线程%s]执行失败: %s",
                threadName,
                Thread.currentThread(),
                t!!.message
            ),
            t
        )
    }

    override fun onCompleted(threadName: String?) {
        LogUtils.e("LogCallback" + "------onCompleted")
        LogUtils.e(
            TAG,
            String.format("[任务线程%s]/[回调线程%s]执行完毕：", threadName, Thread.currentThread())
        )
    }

    override fun onStart(threadName: String?) {
        LogUtils.e("LogCallback" + "------onStart")
        LogUtils.e(
            TAG,
            String.format("[任务线程%s]/[回调线程%s]执行开始：", threadName, Thread.currentThread())
        )
    }
}