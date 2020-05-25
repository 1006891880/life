package com.android.webviewlibrary.utils

import android.util.Log

object X5LogUtils {
    private const val TAG = "X5LogUtils"
    private var isLog = true

    /**
     * 设置是否开启日志
     * @param isLog                 是否开启日志
     */
    fun setIsLog(isLog: Boolean) {
        X5LogUtils.isLog = isLog
    }

    fun d(message: String?) {
        if (isLog) {
            Log.d(TAG, message)
        }
    }

    fun i(message: String?) {
        if (isLog) {
            Log.i(TAG, message)
        }
    }

    fun e(message: String?, throwable: Throwable?) {
        if (isLog) {
            Log.e(TAG, message, throwable)
        }
    }
}