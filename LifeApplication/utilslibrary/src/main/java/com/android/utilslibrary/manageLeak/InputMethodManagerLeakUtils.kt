package com.android.utilslibrary.manageLeak

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.reflect.Field

object InputMethodManagerLeakUtils {
    fun fixInputMethodManagerLeak(destContext: Context?) {
        if (destContext == null) {
            return
        }
        val inputMethodManager = destContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val viewArray = arrayOf("mCurRootView", "mServedView", "mNextServedView")
        var filed: Field
        var filedObject: Any?
        for (view in viewArray) {
            try {
                filed = inputMethodManager.javaClass.getDeclaredField(view)
                if (!filed.isAccessible) {
                    filed.isAccessible = true
                }
                filedObject = filed[inputMethodManager]
                if (filedObject != null && filedObject is View) {
                    // 被InputMethodManager持有引用的context是想要目标销毁的
                    if (filedObject.context === destContext) { // 置空，破坏掉path to gc节点
                        filed[inputMethodManager] = null
                    } else { // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }
}