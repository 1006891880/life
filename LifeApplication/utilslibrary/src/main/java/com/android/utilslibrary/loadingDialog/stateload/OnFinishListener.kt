package com.android.utilslibrary.loadingDialog.stateload

import android.view.View

interface OnFinishListener {
    /**
     * 分发绘制完成事件
     * @param v 绘制完成的View
     */
    fun dispatchFinishEvent(v: View?)
}