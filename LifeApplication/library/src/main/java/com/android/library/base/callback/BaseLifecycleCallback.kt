package com.android.library.base.callback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.android.utilslibrary.activitymanager.AppManager

class BaseLifecycleCallback : Application.ActivityLifecycleCallbacks {

    companion object{
        fun getInstance():BaseLifecycleCallback = HolderClass.INSTANCE
        private object HolderClass {
             val INSTANCE = BaseLifecycleCallback()
        }
    }

//    constructor(){}

    public fun  init(application: Application){
        application.registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.e("Activity生命周期", "onActivityCreated")
        AppManager.getAppManager()?.addActivity(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.e("Activity生命周期", "onActivityDestroyed")
        AppManager.getAppManager()?.removeActivity(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        Log.e("Activity生命周期", "onActivityPaused")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.e("Activity生命周期", "onActivityStarted")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.e("Activity生命周期", "onActivitySaveInstanceState")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.e("Activity生命周期", "onActivityStopped")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.e("Activity生命周期", "onActivityResumed")
    }
}