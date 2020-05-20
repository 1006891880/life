package com.android.library.stateLayout

import android.view.View

interface OnShowHideViewListener {
    /**
     * show
     * @param view                  view
     * @param id                    view对应id
     */
    fun onShowView(view: View?, id: Int)

    /**
     * hide
     * @param view                  view
     * @param id                    view对应id
     */
    fun onHideView(view: View?, id: Int)
}