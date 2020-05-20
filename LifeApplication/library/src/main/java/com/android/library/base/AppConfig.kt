package com.android.library.base

import cn.ycbjie.ycthreadpoollib.PoolThread
import com.alibaba.android.arouter.launcher.ARouter
import com.android.library.base.callback.BaseLifecycleCallback
import com.android.library.base.callback.LogCallback
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.Utils
import com.pedaily.yc.ycdialoglib.toast.ToastUtils
import java.lang.Boolean

enum class AppConfig {

    INSTANCE;

    private var isLogin = false
    private var isShowListImg = false
    private var isShowGirlImg = false
    private var isProbabilityShowImg = false
    private var thumbnailQuality = 0
    private var bannerUrl: String? = null
    private var isNight = false
    private var executor: PoolThread? = null

    fun initConfig(application: LibApplication){

        Utils.init(application)
        initThreadPool()
        ToastUtils.init(application)
        BaseLifecycleCallback.getInstance().init(application)
        initARouter(application)
        //1.是否是登录状态
        isLogin = SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.KEY_IS_LOGIN, false)
        //2.列表是否显示图片
        isShowListImg = SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.KEY_IS_SHOW_LIST_IMG, true)
        //3.启动页是否是妹子图
        isShowGirlImg = SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.KEY_IS_SHOW_GIRL_IMG, false)
        //4.启动页是否是妹子图
        isProbabilityShowImg = SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.KEY_IS_SHOW_IMG_RANDOM, false)
        //5.缩略图质量 0：原图 1：默认 2：省流
        thumbnailQuality = SPUtils.getInstance(Constant.SP_NAME).getInt(Constant.KEY_THUMBNAIL_QUALITY, 1)
        //6.Banner URL 用于加载页显示
        bannerUrl = SPUtils.getInstance(Constant.SP_NAME).getString(Constant.KEY_BANNER_URL, "")
        //7.初始化夜间模式
        isNight = SPUtils.getInstance(Constant.SP_NAME).getBoolean(Constant.KEY_NIGHT_STATE)

    }

    private fun initARouter(application: LibApplication) {
        if (Boolean.parseBoolean("true")){
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(application)
    }

    private fun initThreadPool() {
        if (executor == null){
            executor = PoolThread.ThreadBuilder.createFixed(6).setPriority(Thread.MAX_PRIORITY).setCallback(LogCallback()).build()
        }
    }

    public fun getThreadPool() :PoolThread? {
        initThreadPool()
        return executor
    }

    public fun closeThreadPool(){
        //?.表示对象为空时就直接返回null，所以返回值的变量必须被声明为可空类型
        executor?.close()
        executor = null
    }

    open fun isLogin(): kotlin.Boolean {
        return isLogin
    }

    open fun setLogin(login: kotlin.Boolean) {
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_IS_LOGIN, login)
        isLogin = login
    }

    open fun isShowListImg(): kotlin.Boolean {
        return isShowListImg
    }

    open fun setShowListImg(showListImg: kotlin.Boolean) {
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_IS_SHOW_LIST_IMG, showListImg)
        isShowGirlImg = showListImg
    }

    open fun isShowGirlImg(): kotlin.Boolean {
        return isShowGirlImg
    }

    open fun setShowGirlImg(showGirlImg: kotlin.Boolean) {
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_IS_SHOW_GIRL_IMG, showGirlImg)
        isShowGirlImg = showGirlImg
    }

    open fun isProbabilityShowImg(): kotlin.Boolean {
        return isProbabilityShowImg
    }

    open fun setProbabilityShowImg(probabilityShowImg: kotlin.Boolean) {
        SPUtils.getInstance(Constant.SP_NAME)
            .put(Constant.KEY_IS_SHOW_IMG_RANDOM, probabilityShowImg)
        isProbabilityShowImg = probabilityShowImg
    }

    open fun getThumbnailQuality(): Int {
        return thumbnailQuality
    }

    open fun setThumbnailQuality(thumbnailQuality: Int) {
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_THUMBNAIL_QUALITY, thumbnailQuality)
        this.thumbnailQuality = thumbnailQuality
    }

    open fun getBannerUrl(): String? {
        return bannerUrl
    }

    open fun setBannerUrl(bannerUrl: String?) {
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_BANNER_URL, bannerUrl!!)
        this.bannerUrl = bannerUrl
    }

    open fun isNight(): kotlin.Boolean {
        return isNight
    }

    open fun setNight(night: kotlin.Boolean) {
        SPUtils.getInstance(Constant.SP_NAME).put(Constant.KEY_NIGHT_STATE, night)
        isNight = night
    }


}