package com.android.library.countdownview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.android.library.R

/**
 * 倒计时自定义控件
 *
 *      <xxx.CountDownView
            android:id="@+id/cdv_time"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_alignParentRight="true"
            android:layout_marginTop="40dp"
            android:layout_marginRight="20dp"
            app:cd_animator_time="3"
            app:cd_animator_time_unit="秒"
            app:cd_arc_color="@color/blackText"
            app:cd_arc_width="2dp"
            app:cd_bg_color="@color/whiteBg"
            app:cd_circle_radius="20dp"
            app:cd_location="top"
            app:cd_retreat_type="forward"
            app:cd_text_color="@color/blackText"
            app:cd_text_size="9sp" />
 *
 *  private void initCountDownView() {
        cdvTime.setTime(5);
        cdvTime.start();
        cdvTime.setOnLoadingFinishListener(new CountDownView.OnLoadingFinishListener() {
        @Override
        public void finish() {
        Toast.makeText(MainActivity.this,"完成倒计时",Toast.LENGTH_SHORT).show();
        }
        });
        cdvTime.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        cdvTime.stop();
        }
        });
    }
 *
 */
class CountDownView  : View {
    private var mPaintBackGround : Paint? = null//背景画笔


    constructor( context: Context,
                 attrs: AttributeSet? = null,
                 defStyleAttr: Int = 0):super(context,attrs,defStyleAttr){
        val array = context.obtainStyledAttributes(attrs, R.styleable.CountDownView)
        mRetreatType = array.getInt(R.styleable.CountDownView_cd_retreat_type, 1)
        location = array.getInt(R.styleable.CountDownView_cd_location, 1)
        mCircleRadius = array.getDimension(
            R.styleable.CountDownView_cd_circle_radius,
            dip2px(context, 25f).toFloat()
        ).toInt() //默认25dp
        mPaintArcWidth = array.getDimension(
            R.styleable.CountDownView_cd_arc_width,
            dip2px(context, 3f).toFloat()
        ) //默认3dp
        mPaintArcColor = array.getColor(R.styleable.CountDownView_cd_arc_color, mPaintArcColor)
        mTextSize = array.getDimension(
            R.styleable.CountDownView_cd_text_size,
            dip2px(context, 14f).toFloat()
        ).toInt() //默认14sp
        mTextColor = array.getColor(R.styleable.CountDownView_cd_text_color, mTextColor)
        mPaintBackGroundColor =
            array.getColor(R.styleable.CountDownView_cd_bg_color, mPaintBackGroundColor)
        mLoadingTime = array.getInteger(R.styleable.CountDownView_cd_animator_time, 3) //默认3秒
        mLoadingTimeUnit = array.getString(R.styleable.CountDownView_cd_animator_time_unit) //时间单位
        if (TextUtils.isEmpty(mLoadingTimeUnit)) {
            mLoadingTimeUnit = ""
        }
        array.recycle()
        init(context)
    }

    private var mPaintArc : Paint? = null//圆弧画笔
    private var mPaintText: Paint? = null //文字画笔
    private val mRetreatType : Int //圆弧绘制方式（增加和减少）
    private val mPaintArcWidth : Float//最外层圆弧的宽度
    private val mCircleRadius : Int //圆圈的半径
    private var mPaintArcColor = Color.parseColor("#3C3F41") //初始值
    private var mPaintBackGroundColor = Color.parseColor("#55B2E5") //初始值
    private var mLoadingTime  : Int//时间，单位秒
    private var mLoadingTimeUnit: String? = "" //时间单位
    private var mTextColor = Color.BLACK //字体颜色
    private val mTextSize  : Int//字体大小
    private val location  : Int//从哪个位置开始
    private var startAngle = 0.0f //开始角度
    private var mmSweepAngleStart = 0.0f //起点
    private var mmSweepAngleEnd = 0.0f //终点
    private var mSweepAngle = 0.0f //扫过的角度
    private var mText = "" //要绘制的文字
    private var mWidth = 0
    private var mHeight = 0
    private var set: AnimatorSet? = null
    private var rectF :RectF= RectF()

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun init(context: Context) { //背景设为透明，然后造成圆形View的视觉错觉
        this.background = ContextCompat.getDrawable(context, android.R.color.transparent)

        mPaintBackGround = Paint()
        mPaintBackGround!!.style = Paint.Style.FILL
        mPaintBackGround!!.isAntiAlias = true
        mPaintBackGround!!.color = mPaintBackGroundColor
        mPaintArc = Paint()
        mPaintArc!!.style = Paint.Style.STROKE
        mPaintArc!!.isAntiAlias = true
        mPaintArc!!.color = mPaintArcColor
        mPaintArc!!.strokeWidth = mPaintArcWidth
        mPaintText = Paint()
        mPaintText!!.style = Paint.Style.STROKE
        mPaintText!!.isAntiAlias = true
        mPaintText!!.color = mTextColor
        mPaintText!!.textSize = mTextSize.toFloat()
        //如果时间为小于0，则默认倒计时时间为3秒
        if (mLoadingTime < 0) {
            mLoadingTime = 3
        }
        if (location == 1) { //默认从左侧开始
            startAngle = -180f
        } else if (location == 2) {
            startAngle = -90f
        } else if (location == 3) {
            startAngle = 0f
        } else if (location == 4) {
            startAngle = 90f
        }
        if (mRetreatType == 1) {
            mmSweepAngleStart = 0f
            mmSweepAngleEnd = 360f
        } else {
            mmSweepAngleStart = 360f
            mmSweepAngleEnd = 0f
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //获取view宽高
        mWidth = w
        mHeight = h
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //因为必须是圆形的view，所以在这里重新赋值
        setMeasuredDimension(mCircleRadius * 2, mCircleRadius * 2)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画北景园
        canvas.drawCircle(
            mWidth / 2.toFloat(),
            mHeight / 2.toFloat(),
            mWidth / 2 - mPaintArcWidth,
            mPaintBackGround!!
        )
        //画圆弧
        rectF.left = 0 + mPaintArcWidth / 2
        rectF.top = 0 + mPaintArcWidth / 2
        rectF.right = mWidth - mPaintArcWidth / 2
        rectF.bottom = mHeight - mPaintArcWidth / 2
        canvas.drawArc(rectF, startAngle, mSweepAngle, false, mPaintArc!!)
        //画文字
        val mTextWidth = mPaintText!!.measureText(mText, 0, mText.length)
        val dx = mWidth / 2 - mTextWidth / 2
        val fontMetricsInt = mPaintText!!.fontMetricsInt
        val dy =
            (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom.toFloat()
        val baseLine = mHeight / 2 + dy
        canvas.drawText(mText, dx, baseLine, mPaintText!!)
    }

    /**
     * 开始动态倒计时
     */
    fun start() {
        val animator = ValueAnimator.ofFloat(mmSweepAngleStart, mmSweepAngleEnd)
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { valueAnimator ->
            mSweepAngle = valueAnimator.animatedValue as Float
            //获取到需要绘制的角度，重新绘制
            invalidate()
        }
        //这里是时间获取和赋值
        val animator1 = ValueAnimator.ofInt(mLoadingTime, 0)
        animator1.interpolator = LinearInterpolator()
        animator1.addUpdateListener { valueAnimator ->
            val time = valueAnimator.animatedValue as Int
            mText = time.toString() + mLoadingTimeUnit
        }
        set = AnimatorSet()
        set!!.playTogether(animator, animator1)
        set!!.duration = mLoadingTime * 1000.toLong()
        set!!.interpolator = LinearInterpolator()
        set!!.start()
        set!!.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                clearAnimation()
                if (loadingFinishListener != null) {
                    loadingFinishListener!!.finish()
                }
            }
        })
    }

    /**
     * 停止动画
     */
    fun stop() {
        if (set != null && set!!.isRunning) {
            set!!.cancel()
        }
    }

    /**
     * 设置倒计时时间
     * @param time      时间，秒
     */
    fun setTime(time: Int) {
        mLoadingTime = time
    }

    var loadingFinishListener: OnLoadingFinishListener? =
        null

    fun setOnLoadingFinishListener(listener: OnLoadingFinishListener?) {
        loadingFinishListener = listener
    }

    interface OnLoadingFinishListener {
        fun finish()
    }

    companion object {
        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        fun dip2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }
}