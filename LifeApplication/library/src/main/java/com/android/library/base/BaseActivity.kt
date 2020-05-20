package com.android.library.base

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import cn.ycbjie.ycstatusbarlib.bar.StateAppBar
import com.android.library.R
import com.blankj.utilcode.util.NetworkUtils
import com.blankj.utilcode.util.ToastUtils

abstract class BaseActivity<T :BasePresenter> :AppCompatActivity() {

    protected var presenter: BasePresenter?= null
    @SuppressWarnings
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        StateAppBar.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorTheme))
//        requestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        presenter?.subscribe()
        initView()
        initListener()
        initData()
        if (!NetworkUtils.isConnected()) {
            ToastUtils.showShort("请检查网络是否连接")
        }
    }

    abstract fun initData()

    abstract fun initListener()

    abstract fun initView()

    abstract fun getContentView(): View

    override fun onDestroy() {
        super.onDestroy()
        presenter?.unSubscribe()
        initLeakCanary()
    }

    private fun initLeakCanary(){
        val refWatcher = LibApplication.getLeakCanary()
        refWatcher.watch(this)
    }

    protected fun startActivity(clazz: Class<*>){
        startActivity(clazz,null)
    }

    private fun startActivity(clazz: Class<*> ,bundle: Bundle?){
        val intent = Intent()
        intent.setClass(this, clazz)
        intent.putExtras(bundle!!)
        startActivity(intent)
    }

    open fun startActivityForResult(cls: Class<*>?, requestCode: Int) {
        startActivityForResult(cls, null, requestCode)
    }

    /**
     * 含有Bundle通过Class跳转界面
     */
    open fun startActivityForResult(
        cls: Class<*>?,
        bundle: Bundle?,
        requestCode: Int
    ) {
        val intent = Intent()
        intent.setClass(this, cls!!)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

}