package com.android.utilslibrary.loadingDialog.stateload


/**
 * 用于设置全局的loading样式
 */

class CustomStyleManager {


    constructor() {}

    constructor(
        open: Boolean, repeatTime: Int, speed: CustomLoadingDialog.Speed,
        contentSize: Int, textSize: Float, showTime: Long, interceptBack: Boolean,
        loadText: String, successText: String, failedText: String
    ) {
        isOpenAnim = open
        this.repeatTime = repeatTime
        this.speed = speed
        this.contentSize = contentSize
        this.textSize = textSize
        this.showTime = showTime
        isInterceptBack = interceptBack
        this.loadText = loadText
        this.successText = successText
        this.failedText = failedText
    }

    /**
     * 是否开启绘制
     */
    public var isOpenAnim = true


    /**
     * 重绘次数
     */
    public var repeatTime = 0

    private var speed: CustomLoadingDialog.Speed = CustomLoadingDialog.Speed.SPEED_TWO

    /**
     * 反馈的尺寸，单位px
     */
    public var contentSize = -1

    /**
     * 文字的尺寸，单位px
     */
    public var textSize = -1f

    /**
     * loading的反馈展示的时间，单位ms
     */
    public  var showTime: Long = -1

    public var isInterceptBack = true

    private var loadText = "加载中..."

    private var successText = "加载成功"

    private var failedText = "加载失败"

    fun getFailedText(): String? {
        return failedText
    }

    fun getSuccessText(): String? {
        return successText
    }

    fun getLoadText(): String? {
        return loadText
    }

    fun getSpeed(): CustomLoadingDialog.Speed? {
        return speed
    }

    /**
     * 是否开启动态绘制
     *
     * @param openAnim true开启，false关闭
     * @return this
     */
    fun Anim(openAnim: Boolean): CustomStyleManager? {
        isOpenAnim = openAnim
        return this
    }

    /**
     * 重复次数
     *
     * @param times 次数
     * @return this
     */
    fun repeatTime(times: Int): CustomStyleManager? {
        repeatTime = times
        return this
    }

    fun speed(s: CustomLoadingDialog.Speed): CustomStyleManager? {
        speed = s
        return this
    }

    /**
     * 设置loading的大小
     *
     * @param size 尺寸，单位px
     * @return this
     */
    fun contentSize(size: Int): CustomStyleManager? {
        contentSize = size
        return this
    }

    /**
     * 设置loading 文字的大小
     *
     * @param size 尺寸，单位px
     * @return this
     */
    fun textSize(size: Float): CustomStyleManager? {
        textSize = size
        return this
    }

    /**
     * 设置展示的事件，如果开启绘制则从绘制完毕开始计算
     *
     * @param showTime 事件
     * @return this
     */
    fun showTime(showTime: Long): CustomStyleManager? {
        this.showTime = showTime
        return this
    }

    /**
     * 设置是否拦截back，默认拦截
     *
     * @param interceptBack true拦截，false不拦截
     * @return this
     */
    fun intercept(interceptBack: Boolean): CustomStyleManager? {
        isInterceptBack = interceptBack
        return this
    }

    /**
     * 设置loading时的文字
     *
     * @param text 文字
     * @return this
     */
    fun loadText(text: String): CustomStyleManager? {
        loadText = text
        return this
    }

    /**
     * 设置success时的文字
     *
     * @param text 文字
     * @return this
     */
    fun successText(text: String): CustomStyleManager? {
        successText = text
        return this
    }

    /**
     * 设置failed时的文字
     * https://github.com/ForgetAll/LoadingDialog
     * @param text 文字
     * @return this
     */
    fun failedText(text: String): CustomStyleManager? {
        failedText = text
        return this
    }
}