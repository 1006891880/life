package com.android.library.stateLayout

import android.content.Context
import android.view.ViewStub

// 管理状态的管理器
class StateLayoutManager {

    var context: Context? = null
    var netWorkErrorVs: ViewStub? = null  //网络异常的 viewStub
    var netWorkErrorRetryViewId: Int = 0  //网络错误 重试
    var emptyDataVs: ViewStub? = null   //空数据 viewStub
    var emptyDataEetryViewId: Int = 0  //空数据  重试
    var errorVs: ViewStub? = null     //errorVS
    var errorRetryViewId: Int = 0   //error viewId
    var loadingLayoutResId: Int = 0   //loading view id
    var contentLayoutResId: Int = 0   //content view id
    var retryViewId : Int = 0         //retry  view id
    var emptyDataIconImageId : Int = 0  // empty data icon
    var emptyDataTextTipId : Int = 0   // empty data tip text
    var errorIconImageId : Int = 0    //error icon image
    var errorTextTipId : Int = 0      // error tip text


}