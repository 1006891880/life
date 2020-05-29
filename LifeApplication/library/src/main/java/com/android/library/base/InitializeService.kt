package com.android.library.base

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.android.webviewlibrary.utils.X5WebUtils
import com.blankj.utilcode.util.LogUtils

class InitializeService :IntentService("initializeService") {

    companion object{
        const val ACTION_INIT = "initApplication"
        fun start (context: Context) {
            val intent = Intent(context, InitializeService::class.java)
            intent.action = ACTION_INIT
            context.startService(intent)
        }

    }



    override fun onHandleIntent(intent: Intent?) {
        if (intent?.action.equals(ACTION_INIT)){
            initApplication()
        }
    }

    private fun initApplication() {
        //初始化 腾讯x5 的内核
        X5WebUtils.init(LibApplication.getLibApplication())
        initUtils()
    }

    private fun initUtils() {
        val config = LogUtils.getConfig()
        //边框开关，设置打开
        config.setBorderSwitch(true)
        //logcat 是否打印，设置打印
        config.setConsoleSwitch(true)
        //设置打印日志总开关，线上时关闭
        config.setLogSwitch(true)
    }
}