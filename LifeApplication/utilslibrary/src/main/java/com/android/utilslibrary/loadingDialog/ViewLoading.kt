package com.android.utilslibrary.loadingDialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.StyleRes
import com.android.utilslibrary.R

abstract class ViewLoading: Dialog{

    constructor(@NonNull context: Context, @StyleRes type: Int, content: String): super(context, R.style.Loading){

        if (type == 1) { // 加载布局
            setContentView(R.layout.layout_dialog_loading)
            val message = findViewById<View>(R.id.message) as TextView
            if (content != null && content.length > 0) {
                message.text = content
            } else {
                message.text = "加载中"
            }
        } else { // 加载布局
            setContentView(R.layout.layout_dialog_loaded)
        }
        val progressImageView =
            findViewById<View>(R.id.iv_image) as ImageView
        //创建旋转动画
        val animation: Animation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        animation.duration = 2000
        //动画的重复次数
        animation.repeatCount = 10
        //设置为true，动画转化结束后被应用
        animation.fillAfter = true
        //开始动画
        progressImageView.startAnimation(animation)
        // 设置Dialog参数
        val window = window
        if (window != null) {
            val params = window.attributes
            params.gravity = Gravity.CENTER
            window.attributes = params
        }
    }

    /**
     * 封装Dialog消失的回调
     */
    override fun onBackPressed() { //回调
        loadCancel()
        //关闭Loading
        dismiss()
    }

    /**
     * 抽象方法，子类继承实现
     * 处理消失后的逻辑
     */
    protected abstract fun loadCancel()
}