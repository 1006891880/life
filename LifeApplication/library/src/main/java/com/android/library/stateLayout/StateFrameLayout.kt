package com.android.library.stateLayout

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.View
import android.widget.FrameLayout


class StateFrameLayout : FrameLayout {

    constructor(context: Context): this(context,null){
        Log.e("yyy","constructor  first  ")
    }
    constructor(context: Context,attributeSet: AttributeSet?):this(context,attributeSet,0){
        Log.e("yyy","constructor  two  ")
    }
    constructor(context: Context,attributeSet: AttributeSet?,defStyleAtt:Int):super(context,attributeSet,defStyleAtt){
        Log.e("yyy","constructor  three  ")
    }

    init {
        Log.e("yyy","constructor  init  ")
    }

    companion object{
        //loading  id
       const val LAYOUT_LOADING_ID = 1
        //content  id
       const val LAYOUT_CONTENT_ID = 2
        //exception id
       const val LAYOUT_ERROR_ID = 3
        //network error  id
        const val LAYOUT_NETWORK_ERROR_ID = 4
        //empty id
        const val LAYOUT_EMPTY_DATA_ID = 5

        //save list view
        var layoutSparseArray = SparseArray<View>()

    }

}