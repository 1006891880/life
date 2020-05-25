package com.android.utilslibrary.loadingDialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.android.utilslibrary.R
import kotlinx.android.synthetic.main.layout_progress_loading.*

//自定义加载框
class LoadDialog(context: Context) : Dialog(context) {

    private var canNotCancel :Boolean = false
    private var tipMsg :String? = null
    constructor(context: Context,canNotCancel:Boolean,showMsg:Boolean,tipMsg:String):this(context){
        this.canNotCancel = canNotCancel
        this.tipMsg =tipMsg
        context.setTheme(android.R.style.Theme_InputMethod)
        setContentView(R.layout.layout_progress_loading)

        if (showMsg){
            show_message.visibility = View.VISIBLE
            show_message.text = tipMsg
        }else{
            show_message.visibility = View.GONE
        }
        var params :WindowManager.LayoutParams =window!!.attributes
        params.flags =WindowManager.LayoutParams.FLAG_DIM_BEHIND // 背后的都会变暗
        params.dimAmount = 0.5f
        window?.attributes = params
        window?.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (canNotCancel){
                Toast.makeText(context, tipMsg, Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object{
        var loadDialog: LoadDialog? = null

        fun show(context: Context) {
            show(context, null, false, false)
        }


        fun show(context: Context, message: String) {
            show(context, message, false, false)
        }

        /**
         * 展示加载窗
         * @param context               上下文
         * @param message               内容
         * @param showMsg               是否展示文字
         * @param isCancel              是否可以取消
         */
        fun show(
            context: Context,
            message: String?,
            showMsg: Boolean,
            isCancel: Boolean
        ) {
            if (context is Activity) {
                if (context.isFinishing) {
                    return
                }
            }
            if (loadDialog != null && loadDialog!!.isShowing) {
                return
            }
            loadDialog = LoadDialog(context, isCancel, showMsg, message!!)
            loadDialog!!.show()
        }

        fun dismiss(context :Context){
            try {
                if (context is Activity){
                    if (context.isFinishing){
                        loadDialog = null
                        return
                    }
                }
                if (loadDialog != null && loadDialog!!.isShowing) {
                    val loadContext = loadDialog!!.context
                    if (loadContext is Activity) {
                        if (loadContext.isFinishing) {
                            loadDialog = null
                            return
                        }
                    }
                    loadDialog!!.dismiss()
                    loadDialog = null
                }

            }catch (ex:Exception){
                ex.printStackTrace()
                loadDialog = null
            }
        }
    }

}