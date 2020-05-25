package com.android.webviewlibrary.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import androidx.annotation.IntDef
import com.android.webviewlibrary.utils.X5WebUtils.ErrorMode.Companion.NO_NET
import com.tencent.smtt.sdk.CookieManager
import com.tencent.smtt.sdk.CookieSyncManager
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.*

object X5WebUtils {

    /**
     * 不能直接new，否则抛个异常
     */
    private fun X5WebUtils() {
        throw UnsupportedOperationException("u can't instantiate me...")
    }

    /**
     * 初始化腾讯x5浏览器webView，建议在application初始化
     * @param context                       上下文
     */
    fun init(context: Context?) {
        if (context is Application) { //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
            val cb: PreInitCallback = object : PreInitCallback {
                override fun onViewInitFinished(arg0: Boolean) { //x5內核初始化完成的回调，为true表示x5内核加载成功
//否则表示x5内核加载失败，会自动切换到系统内核。
                    Log.d("app", " onViewInitFinished is $arg0")
                }

                override fun onCoreInitFinished() {
                    Log.d("app", " onCoreInitFinished ")
                }
            }
            //x5内核初始化接口
            QbSdk.initX5Environment(context, cb)
        } else {
            throw UnsupportedOperationException("context must be application...")
        }
    }


    /**
     * 判断网络是否连接
     *
     * 需添加权限
     * `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`
     *
     * @return `true`: 是<br></br>`false`: 否
     */
    fun isConnected(context: Context?): Boolean {
        if (context == null) {
            return false
        }
        val info =
            getActiveNetworkInfo(
                context
            )
        return info != null && info.isConnected
    }

    /**
     * 获取活动网络信息
     *
     * 需添加权限
     * `<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />`
     *
     * @return NetworkInfo
     */
    @SuppressLint("MissingPermission")
    private fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        val manager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                ?: return null
        return manager.activeNetworkInfo
    }

    /**
     * 同步cookie
     * 建议调用webView.loadUrl(url)之前一句调用此方法就可以给WebView设置Cookie
     * @param url                   地址
     * @param cookieList            需要添加的Cookie值,以键值对的方式:key=value
     */
    fun syncCookie(
        context: Context?,
        url: String?,
        cookieList: ArrayList<String?>?
    ) { //初始化
        CookieSyncManager.createInstance(context)
        //获取对象
        val cookieManager =
            CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        //移除
        cookieManager.removeSessionCookie()
        //添加cookie操作
        if (cookieList != null && cookieList.size > 0) {
            for (cookie in cookieList) {
                cookieManager.setCookie(url, cookie)
            }
        }
        val cookies = cookieManager.getCookie(url)
        X5LogUtils.d("cookies-------$cookies")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.flush()
        } else {
            CookieSyncManager.getInstance().sync()
        }
    }

    /**
     * 清除cookie操作
     * @param context               上下文
     */
    fun removeCookie(context: Context?) {
        CookieSyncManager.createInstance(context)
        CookieSyncManager.getInstance().startSync()
        CookieManager.getInstance().removeSessionCookie()
    }

    /**
     * Return whether the activity is alive.
     *
     * @param context The context.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isActivityAlive(context: Context?): Boolean {
        return isActivityAlive(
            getActivityByContext(
                context
            )
        )
    }

    /**
     * Return the activity by context.
     *
     * @param context The context.
     * @return the activity by context.
     */
    fun getActivityByContext(context: Context?): Activity? {
        var context = context
        if (context is Activity) {
            return context
        }
        while (context is ContextWrapper) {
            if (context is Activity) {
                return context
            }
            context = context.baseContext
        }
        return null
    }


    /**
     * Return whether the activity is alive.
     *
     * @param activity The activity.
     * @return `true`: yes<br></br>`false`: no
     */
    fun isActivityAlive(activity: Activity?): Boolean {
        return (activity != null && !activity.isFinishing
                && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !activity.isDestroyed))
    }


    /**
     * 注解限定符
     */
    @IntDef(value = [NO_NET, ErrorMode.STATE_404, ErrorMode.RECEIVED_ERROR, ErrorMode.SSL_ERROR])
    @Retention(RetentionPolicy.SOURCE)
    annotation class ErrorType

    /**
     * 异常状态模式
     * NO_NET                       没有网络
     * STATE_404                    404，网页无法打开
     * RECEIVED_ERROR               onReceivedError，请求网络出现error
     * SSL_ERROR                    在加载资源时通知主机应用程序发生SSL错误
     */
    @Retention(RetentionPolicy.SOURCE)
    annotation class ErrorMode {
        companion object {
            const val NO_NET = 1001
            const val STATE_404 = 1002
            const val RECEIVED_ERROR = 1003
            const val SSL_ERROR = 1004
        }
    }
}