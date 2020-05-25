package com.android.utilslibrary.blurview

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.renderscript.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import androidx.annotation.RequiresApi
import com.android.utilslibrary.R

class RealTimeBlurView : View {

    companion object{
        init {
            try{
                RealTimeBlurView::class.java.classLoader!!.loadClass("android.support.v8.renderscript.RenderScript")
            }catch(ex:Exception){
                throw RuntimeException("RenderScript support not enabled. Add \"android { defaultConfig { renderscriptSupportModeEnabled true }}\" in your build.gradle")
            }
        }
        var RENDERING_COUNT = 0
        private class StopException : RuntimeException()
        private val STOP_EXCEPTION = StopException()
    }

    /**
     * 默认4
     */
    var mDownsampleFactor = 0f
    /**
     * 默认 #aaffffff
     */
    var mOverlayColor = 0
    /**
     * 默认 10dp (0 < r <= 25)
     */
    var mBlurRadius = 0f

    var mDirty = false
    var mBitmapToBlur: Bitmap? = null
    private var mBlurredBitmap:Bitmap? = null
    var mBlurringCanvas: Canvas? = null
    var mRenderScript: RenderScript? = null
    var mBlurScript: ScriptIntrinsicBlur? = null
    var mBlurInput: Allocation? = null
    private  var mBlurOutput:Allocation? = null
    var mIsRendering = false
    val mRectSrc = Rect()
    private  val mRectDst:Rect? = Rect()
    var mDecorView: View? = null
    var mDifferentRoot = false


    constructor(context: Context, attrs: AttributeSet?):super(context, attrs) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.RealTimeBlurView)
        mBlurRadius = a.getDimension(
            R.styleable.RealTimeBlurView_realtimeBlurRadius,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                10f,
                context.resources.displayMetrics
            )
        )
        mDownsampleFactor = a.getFloat(R.styleable.RealTimeBlurView_realtimeDownsampleFactor, 4f)
        mOverlayColor = a.getColor(R.styleable.RealTimeBlurView_realtimeOverlayColor, -0x55000001)
        a.recycle()
    }


    fun setBlurRadius(radius: Float) {
        if (mBlurRadius != radius) {
            mBlurRadius = radius
            mDirty = true
            invalidate()
        }
    }


    fun setDownSampleFactor(factor: Float) {
        require(factor > 0) { "Downsample factor must be greater than 0." }
        if (mDownsampleFactor != factor) {
            mDownsampleFactor = factor
            // may also change blur radius
            mDirty = true
            releaseBitmap()
            invalidate()
        }
    }


    fun setOverlayColor(color: Int) {
        if (mOverlayColor != color) {
            mOverlayColor = color
            invalidate()
        }
    }


    open fun releaseBitmap() {
        if (mBlurInput != null) {
            mBlurInput!!.destroy()
            mBlurInput = null
        }
        if (mBlurOutput != null) {
            mBlurOutput!!.destroy()
            mBlurOutput = null
        }
        if (mBitmapToBlur != null) {
            mBitmapToBlur!!.recycle()
            mBitmapToBlur = null
        }
        if (mBlurredBitmap != null) {
            mBlurredBitmap!!.recycle()
            mBlurredBitmap = null
        }
    }


    open fun releaseScript() {
        if (mRenderScript != null) {
            mRenderScript!!.destroy()
            mRenderScript = null
        }
        if (mBlurScript != null) {
            mBlurScript!!.destroy()
            mBlurScript = null
        }
    }

    fun release() {
        releaseBitmap()
        releaseScript()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun prepare(): Boolean {
        if (mBlurRadius == 0f) {
            release()
            return false
        }
        var downsampleFactor = mDownsampleFactor
        if (mDirty || mRenderScript == null) {
            if (mRenderScript == null) {
                try {
                    mRenderScript = RenderScript.create(getContext())
                    mBlurScript = ScriptIntrinsicBlur.create(
                        mRenderScript,
                        Element.U8_4(mRenderScript)
                    )
                } catch (e: RSRuntimeException) {
                    return if (isDebug(getContext())) {
                        if (e.message != null && e.message!!.startsWith("Error loading RS jni library: java.lang.UnsatisfiedLinkError:")) {
                            throw RuntimeException("Error loading RS jni library, Upgrade buildToolsVersion=\"24.0.2\" or higher may solve this issue")
                        } else {
                            throw e
                        }
                    } else { // In release mode, just ignore
                        releaseScript()
                        false
                    }
                }
            }
            mDirty = false
            var radius = mBlurRadius / downsampleFactor
            if (radius > 25) {
                downsampleFactor = downsampleFactor * radius / 25
                radius = 25f
            }
            mBlurScript!!.setRadius(radius)
        }
        val width: Int = getWidth()
        val height: Int = getHeight()
        val scaledWidth = Math.max(1, (width / downsampleFactor).toInt())
        val scaledHeight = Math.max(1, (height / downsampleFactor).toInt())
        if (mBlurringCanvas == null || mBlurredBitmap == null || mBlurredBitmap?.getWidth() != scaledWidth || mBlurredBitmap?.getHeight() != scaledHeight
        ) {
            releaseBitmap()
            var r = false
            try {
                mBitmapToBlur =
                    Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
                if (mBitmapToBlur == null) {
                    return false
                }
                mBlurringCanvas = Canvas(mBitmapToBlur!!)
                mBlurInput = Allocation.createFromBitmap(
                    mRenderScript,
                    mBitmapToBlur,
                    Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT
                )
                mBlurOutput =
                    Allocation.createTyped(mRenderScript, mBlurInput?.getType())
                mBlurredBitmap =
                    Bitmap.createBitmap(scaledWidth, scaledHeight, Bitmap.Config.ARGB_8888)
                if (mBlurredBitmap == null) {
                    return false
                }
                r = true
            } catch (e: OutOfMemoryError) { // Bitmap.createBitmap() may cause OOM error
// Simply ignore and fallback
            } finally {
                if (!r) {
                    releaseBitmap()
                    return false
                }
            }
        }
        return true
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun blur(bitmapToBlur: Bitmap?, blurredBitmap: Bitmap?) {
        mBlurInput!!.copyFrom(bitmapToBlur)
        mBlurScript!!.setInput(mBlurInput)
        mBlurScript!!.forEach(mBlurOutput)
        mBlurOutput!!.copyTo(blurredBitmap)
    }

    val preDrawListener =
        ViewTreeObserver.OnPreDrawListener {
            val locations = IntArray(2)
            var oldBmp: Bitmap? = mBlurredBitmap
            val decor = mDecorView
            if (decor != null && isShown() && prepare()) {
                val redrawBitmap = mBlurredBitmap != oldBmp
                oldBmp = null
                decor.getLocationOnScreen(locations)
                var x = -locations[0]
                var y = -locations[1]
                getLocationOnScreen(locations)
                x += locations[0]
                y += locations[1]
                // just erase transparent
                mBitmapToBlur!!.eraseColor(mOverlayColor and 0xffffff)
                val rc = mBlurringCanvas!!.save()
                mIsRendering = true
                RENDERING_COUNT++
                try {
                    mBlurringCanvas!!.scale(
                        1f * mBitmapToBlur!!.width / getWidth(),
                        1f * mBitmapToBlur!!.height / getHeight()
                    )
                    mBlurringCanvas!!.translate(-x.toFloat(), -y.toFloat())
                    if (decor.background != null) {
                        decor.background.draw(mBlurringCanvas!!)
                    }
                    decor.draw(mBlurringCanvas)
                } catch (e: StopException) {
                } finally {
                    mIsRendering = false
                    RENDERING_COUNT--
                    mBlurringCanvas!!.restoreToCount(rc)
                }
                blur(mBitmapToBlur, mBlurredBitmap)
                if (redrawBitmap || mDifferentRoot) {
                    invalidate()
                }
            }
            true
        }

    fun getActivityDecorView(): View? {
        var ctx: Context? = getContext()
        var i = 0
        while (i < 4 && ctx != null && ctx !is Activity && ctx is ContextWrapper) {
            ctx = ctx.baseContext
            i++
        }
        return if (ctx is Activity) {
            ctx.window.decorView
        } else {
            null
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mDecorView = getActivityDecorView()
        if (mDecorView != null) {
            mDecorView!!.viewTreeObserver.addOnPreDrawListener(preDrawListener)
            mDifferentRoot = mDecorView!!.rootView !== getRootView()
            if (mDifferentRoot) {
                mDecorView!!.postInvalidate()
            }
        } else {
            mDifferentRoot = false
        }
    }

    override fun onDetachedFromWindow() {
        if (mDecorView != null) {
            mDecorView!!.viewTreeObserver.removeOnPreDrawListener(preDrawListener)
        }
        release()
        super.onDetachedFromWindow()
    }

    override fun draw(canvas: Canvas?) {
        if (mIsRendering) { // Quit here, don't draw views above me
            throw STOP_EXCEPTION
        } else if (RENDERING_COUNT > 0) { // Doesn't support blurview overlap on another blurview
        } else {
            super.draw(canvas)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawBlurredBitmap(canvas!!, mBlurredBitmap, mOverlayColor)
    }

    /**
     * Custom draw the blurred bitmap and color to define your own shape
     */
    fun drawBlurredBitmap(
        canvas: Canvas,
        blurredBitmap: Bitmap?,
        overlayColor: Int
    ) {
        if (blurredBitmap != null) {
            mRectSrc.right = blurredBitmap.width
            mRectSrc.bottom = blurredBitmap.height
            mRectDst!!.right = getWidth()
            mRectDst.bottom = getHeight()
            canvas.drawBitmap(blurredBitmap, mRectSrc, mRectDst!!, null)
        }
        canvas.drawColor(overlayColor)
    }

    // android:debuggable="true" in AndroidManifest.xml (auto set by build tool)
    var DEBUG: Boolean? = null

    fun isDebug(ctx: Context?): Boolean {
        if (DEBUG == null && ctx != null) {
            DEBUG = ctx.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        }
        return DEBUG === java.lang.Boolean.TRUE
    }
}