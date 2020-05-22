package com.android.utilslibrary.blurview

import android.app.Activity
import android.graphics.Bitmap
import android.util.LruCache

class BlurBehind  {

    private enum class State {
        READY,EXECUTING
    }
    companion object{
        private const val CACHE_BLURRED_IMAGE = "CACHE_BLURRED_IMAGE"
        private const val BLUR_RADIUS = 12
        private const val DEFAULT_ALPHA = 100
        private val blur= BlurBehind()
        fun getBlurBehind() = blur
        private val mImageCache: LruCache<String, Bitmap> = LruCache(1)

    }

    private var mAlpha: Int = DEFAULT_ALPHA
    private var mFilterColor = -1
    private var mState = State.READY

    fun withAlpha(alpha: Int): BlurBehind {
        mAlpha = alpha
        return this
    }

    fun withFilterColor(filterColor: Int): BlurBehind {
        mFilterColor = filterColor
        return this
    }

    fun setBackground(activity: Activity){

        if (mImageCache.size() != 0){


            mImageCache.remove(CACHE_BLURRED_IMAGE)
        }

    }

    interface OnBlurListener {
        fun onBlurComplete()
    }

}