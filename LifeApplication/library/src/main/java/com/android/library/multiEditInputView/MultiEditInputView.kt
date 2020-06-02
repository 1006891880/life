package com.android.library.multiEditInputView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.android.library.R

/**
 * 意见反馈界面
 */
class MultiEditInputView  : LinearLayout{

    constructor( context: Context,
                 attrs: AttributeSet? = null,
                 defStyleAttr: Int = 0):super(context,attrs,defStyleAttr){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MultiEditInputView)
        MAX_COUNT = typedArray.getInteger(R.styleable.MultiEditInputView_maxCount, DEFAULT_MAX_COUNT)
        ignoreCnOrEn = typedArray.getBoolean(R.styleable.MultiEditInputView_IgnoreCnOrEn, true)
        hintText = typedArray.getString(R.styleable.MultiEditInputView_hintText)
        contentText = typedArray.getString(R.styleable.MultiEditInputView_contentText)
        contentHeight = typedArray.getDimension(
            R.styleable.MultiEditInputView_contentHeight,
            DEFAULT_CONTENT_HEIGHT.toFloat()
        )
        typedArray.recycle()
        init(context)
    }

    private var id_et_input: EditText? = null
    private var id_tv_input: TextView? = null
    private var MAX_COUNT: Int = 0
    private var hintText: String?
    private var ignoreCnOrEn: Boolean = false
    private var contentText: String?
    private val contentHeight: Float
    var id_ll_multi: LinearLayout? = null
    private fun init( context: Context) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.view_multi_edit_input, this)
        id_ll_multi = view.findViewById<View>(R.id.id_ll_multi) as LinearLayout
        id_ll_multi!!.setBackgroundResource(R.drawable.view_selector_edit_text_multi)
        id_et_input = view.findViewById<View>(R.id.id_et_input) as EditText
        id_tv_input = view.findViewById<View>(R.id.id_tv_input) as TextView
        id_et_input!!.addTextChangedListener(mTextWatcher)
        id_et_input!!.hint = hintText
        id_et_input!!.setText(contentText)
        id_et_input!!.height = contentHeight.toInt()
        /**
         * 配合 id_tv_input xml的
         * android:focusable="true"
         * android:focusableInTouchMode="true"
         * 在id_et_input设置完文本后不给id_et_input 焦点
         */
        id_tv_input!!.requestFocus()
        //初始化
        configCount()
        id_et_input!!.setSelection(id_et_input!!.length()) // 将光标移动最后一个字符后面
        /**
         * focus后给背景设置Selected
         */
        id_et_input!!.onFocusChangeListener = OnFocusChangeListener { _, b -> id_ll_multi!!.isSelected = b }
    }

    private val mTextWatcher: TextWatcher = object : TextWatcher {
        private var editStart = 0
        private var editEnd = 0
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

        override fun afterTextChanged(editable: Editable) {
            editStart = id_et_input!!.selectionStart
            editEnd = id_et_input!!.selectionEnd
            // 先去掉监听器，否则会出现栈溢出
            id_et_input!!.removeTextChangedListener(this)
            if (ignoreCnOrEn) { //当输入字符个数超过限制的大小时，进行截断操作
                while (calculateLengthIgnoreCnOrEn(editable.toString()) > MAX_COUNT) {
                    editable.delete(editStart - 1, editEnd)
                    editStart--
                    editEnd--
                }
            } else { // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
                while (calculateLength(editable.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
                    editable.delete(editStart - 1, editEnd)
                    editStart--
                    editEnd--
                }
            }
            id_et_input!!.setSelection(editStart)
            // 恢复监听器
            id_et_input!!.addTextChangedListener(this)
            //update
            configCount()
        }
    }

    private fun calculateLength(c: CharSequence): Long {
        var len = 0.0
        for (i in 0 until c.length) {
            val tmp = c[i].toInt()
            if (tmp > 0 && tmp < 127) {
                len += 0.5
            } else {
                len++
            }
        }
        return Math.round(len)
    }

    private fun calculateLengthIgnoreCnOrEn(c: CharSequence): Int {
        var len = 0
        for (i in 0 until c.length) {
            len++
        }
        return len
    }

    private fun configCount() {
        if (ignoreCnOrEn) {
            val nowCount = calculateLengthIgnoreCnOrEn(id_et_input!!.text.toString())
            id_tv_input!!.text = (MAX_COUNT - nowCount).toString() + "/" + MAX_COUNT
        } else {
            val nowCount = calculateLength(id_et_input!!.text.toString())
            id_tv_input!!.text = (MAX_COUNT - nowCount).toString() + "/" + MAX_COUNT
        }
    }

    fun setContentText(content: String?) {
        contentText = content
        if (id_et_input == null) {
            return
        }
        id_et_input!!.setText(contentText)
    }

    fun getContentText(): String? {
        if (id_et_input != null) {
            contentText =
                if (id_et_input!!.text == null) "" else id_et_input!!.text.toString()
        }
        return contentText
    }

    fun setHintText(hintText: String?) {
        this.hintText = hintText
        id_et_input!!.hint = hintText
    }

    fun getHintText(): String? {
        if (id_et_input != null) {
            hintText = if (id_et_input!!.hint == null) "" else id_et_input!!.hint.toString()
        }
        return hintText
    }

    companion object {
        private const val DEFAULT_MAX_COUNT = 240
        private const val DEFAULT_CONTENT_HEIGHT = 140
        fun dp2px(context: Context, dp: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dp * scale + 0.5f).toInt()
        }
    }
}