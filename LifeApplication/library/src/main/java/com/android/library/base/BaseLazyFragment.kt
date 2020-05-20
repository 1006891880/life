package com.android.library.base

import android.os.Bundle

abstract class BaseLazyFragment :BaseFragment<BasePresenter>() {

    //是否已经加载过
    private var isLazyLoaded = false
    // view 是否已经记载完毕
    private var isPrepared =false

    //执行 onActivityCreated view 已经加载完毕
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isPrepared = true
        lazyLoad()
    }

    // 此方法在 onCreateView 之前 执行
    // ViewPage中 fragment 改变可见的状态 也会调用； Fragment 从可见到不可见
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        lazyLoad()
    }

    //在lazyLoad()方法中进行双重标记判断,通过后即可进行数据加载
    private fun lazyLoad() {
        if (userVisibleHint && !isLazyLoaded && isPrepared){
            onLazyLoad()
            isLazyLoaded = true
        }

    }

    abstract fun onLazyLoad()

}