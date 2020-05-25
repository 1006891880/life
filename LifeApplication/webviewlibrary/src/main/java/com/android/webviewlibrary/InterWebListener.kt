package com.android.webviewlibrary

import com.android.webviewlibrary.utils.X5WebUtils

interface InterWebListener {
    /**
     * 隐藏进度条
     */
    fun hindProgressBar()

    /**
     * 展示异常页面
     * @param type                           异常类型
     */
    fun showErrorView(@X5WebUtils.ErrorType type: Int)

    /**
     * 进度条变化时调用，这里添加注解限定符，必须是在0到100之间
     * @param newProgress                   进度0-100
     */
    fun startProgress(/*@IntRange(start = 0, endInclusive = 100) */newProgress: Int)

    /**
     * 获取加载网页的标题
     * @param title                         title标题
     */
    fun showTitle(title: String?)
}