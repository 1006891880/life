package com.android.utilslibrary.activitymanager

import android.app.Activity
import android.os.Process
import java.util.*
import kotlin.system.exitProcess

class AppManager {
    /**
     * 栈：也就是stack
     */
    private var activityStack: Stack<Activity?>? = null


    private constructor() {}

    /**
     * 单一实例
     */
    companion object{
        @Volatile
        private var instance: AppManager? = null
        fun getAppManager(): AppManager?{
            if (instance == null) {
                synchronized(AppManager::class.java) {
                    if (instance == null) {
                        instance = AppManager()
                    }
                }
            }
            return instance
        }
    }


    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity?) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity? {
        return if (activityStack == null || activityStack!!.size == 0) {
            null
        } else activityStack!!.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        if (activityStack != null && activityStack!!.size > 0) {
            val activity = activityStack!!.lastElement()
            finishActivity(activity)
        }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity( activity: Activity?) {
        if (activityStack == null) {
            return
        }
        if (activity != null && !activity.isFinishing) {
            activityStack!!.remove(activity)
            activity.finish()
        }
    }

    /**
     * 移除指定的Activity
     * @param activity
     */
    fun removeActivity(activity: Activity?) {
        if (activityStack == null) {
            return
        }
        if (activity != null) {
            activityStack!!.remove(activity)
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        if (activityStack == null) {
            return
        }
        for (activity in activityStack!!) {
            if (activity!!::class == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        if (activityStack == null) {
            return
        }
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!![i]) {
                activityStack!![i]?.finish()
            }
            i++
        }
        activityStack!!.clear()
    }

    /**
     * 退出应用程序
     */
    fun appExit(isBackground: Boolean?) {
        try { //finish所有activity
            finishAllActivity()
            //杀死进程
            Process.killProcess(Process.myPid())
        } catch (ignored: Exception) {
        } finally {
            if (!isBackground!!) {
                exitProcess(0)
            }
        }
    }
}