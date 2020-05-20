package com.android.library.base

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.alibaba.android.arouter.launcher.ARouter
import com.bumptech.glide.Glide
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import io.realm.Realm
import io.realm.RealmConfiguration

class LibApplication : MultiDexApplication() {

    /**
     * 类的拓展，外部类直接访问，不需要对象指针
     */
    companion object{
       private lateinit var mLeakCanary : RefWatcher
       fun getLeakCanary():RefWatcher = mLeakCanary

       private lateinit var instance : LibApplication
       fun getLibApplication() : LibApplication = instance

        const val TAG = "LibApplication"
    }

    override fun onCreate() {
        super.onCreate()
        printLog("onCreate")
        initLeakCanary(this)
        initRealm(this)
        instance = this
        AppConfig.INSTANCE.initConfig(this)
        InitializeService.start(this)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)

    }

    private fun initRealm(application: LibApplication) {
        Realm.init(application)
        val realmConfig = RealmConfiguration.Builder()
            .name(Constant.REALM_NAME)
            .schemaVersion(Constant.REALM_VERSION)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.getInstance(realmConfig)
        Realm.setDefaultConfiguration(realmConfig)
    }

    private fun initLeakCanary(application: Application) {
        if (LeakCanary.isInAnalyzerProcess(application)) return;
        mLeakCanary = LeakCanary.install(application);
    }

    //程序终止是 执行
    override fun onTerminate() {
        printLog("onTerminate")
        super.onTerminate()
        //关闭 realm 数据库
        Realm.getDefaultInstance().close()
        //关闭线程池
        AppConfig.INSTANCE.closeThreadPool()
        //关闭路由
        ARouter.getInstance().destroy()
    }

    //home 键 点击执行 削减内存
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        printLog("onTrimMemory")
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
            Glide.get(this).clearMemory()
        }
        Glide.get(this).trimMemory(level)
    }

    //低内存
    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }

    private fun printLog(msg:String){
        Log.d(TAG,msg)
    }

}