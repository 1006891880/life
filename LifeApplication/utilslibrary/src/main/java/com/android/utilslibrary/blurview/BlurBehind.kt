package com.android.utilslibrary.blurview

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.util.LruCache
import android.view.View

class BlurBehind {

    companion object {
        private val CACHE_BLURRED_IMAGE = "CACHE_BLURRED_IMAGE"
        private val BLUR_RADIUS = 12
        private val DEFAULT_ALPHA = 100
        private val mImageCache: LruCache<String, Bitmap> = LruCache(1)

        private enum class State {
            READY, EXECUTING
        }

        private var mState = State.READY
    }

    private var cacheBlurExecuteTask: CacheBlurExecuteTask? = null
    private var mAlpha = DEFAULT_ALPHA
    private var mFilterColor = -1
    private var mInstance: BlurBehind? = null

    fun getInstance(): BlurBehind? {
        if (mInstance == null) {
            mInstance = BlurBehind()
        }
        return mInstance
    }

    fun execute(activity: Activity?, onBlurListener: OnBlurListener) {
        if (mState == State.READY) {
            mState = State.EXECUTING
            cacheBlurExecuteTask = CacheBlurExecuteTask(activity, onBlurListener)
            cacheBlurExecuteTask!!.execute()
        }
    }

    fun withAlpha(alpha: Int): BlurBehind? {
        mAlpha = alpha
        return this
    }

    fun withFilterColor(filterColor: Int): BlurBehind? {
        mFilterColor = filterColor
        return this
    }

    fun setBackground(activity: Activity) {
        if (mImageCache.size() !== 0) {
            val bd =
                BitmapDrawable(activity.resources, mImageCache[CACHE_BLURRED_IMAGE])
            bd.alpha = mAlpha
            if (mFilterColor != -1) {
                bd.setColorFilter(mFilterColor, PorterDuff.Mode.DST_ATOP)
            }
            activity.window.setBackgroundDrawable(bd)
            mImageCache.remove(CACHE_BLURRED_IMAGE)
            cacheBlurExecuteTask = null
        }
    }

    class CacheBlurExecuteTask : AsyncTask<Void, Void, Void> {
        private var activity: Activity? = null
        private var onBlurListener: OnBlurListener? = null
        private var decorView: View? = null
        private var image: Bitmap? = null

        constructor(activity: Activity?, onBlurListener: OnBlurListener) {
            this.activity = activity
            this.onBlurListener = onBlurListener
        }

        override fun onPreExecute() {
            super.onPreExecute()
            decorView = activity!!.window.decorView
            decorView?.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW)
            decorView?.setDrawingCacheEnabled(true)
            decorView?.buildDrawingCache()
            image = decorView?.drawingCache
        }

        override fun doInBackground(vararg params: Void?): Void? {
            val blurredBitmap = CustomBlur.apply(activity, image!!, BLUR_RADIUS)
            mImageCache.put(CACHE_BLURRED_IMAGE, blurredBitmap)
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            decorView!!.destroyDrawingCache()
            decorView!!.isDrawingCacheEnabled = false
            activity = null
            onBlurListener!!.onBlurComplete()
            mState = State.READY
        }

    }

    interface OnBlurListener {
        fun onBlurComplete()
    }

}