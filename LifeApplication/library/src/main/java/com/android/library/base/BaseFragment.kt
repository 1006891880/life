package com.android.library.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment <T : BasePresenter> : Fragment() {

    protected var presenter : BasePresenter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getContentView(),container,false)
    }
    protected abstract fun getContentView() : Int

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter?.subscribe()
        initView()
        initListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.unSubscribe()
        initLeakCanary()
    }

    private fun initLeakCanary(){
        val refWatcher = LibApplication.getLeakCanary()
        refWatcher.watch(this)
    }

    protected abstract fun initData()

    protected abstract fun initListener()

    protected abstract fun initView()


    /**
     * 通过Class跳转界面
     */
    open fun startActivity(cls: Class<*>?) {
        startActivity(cls, null)
    }

    /**
     * 通过Class跳转界面
     */
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
        intent.setClass(activity!!, cls!!)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivityForResult(intent, requestCode)
    }

    /**
     * 含有Bundle通过Class跳转界面
     */
    open fun startActivity(cls: Class<*>?, bundle: Bundle?) {
        val intent = Intent()
        intent.setClass(activity!!, cls!!)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
    }

}