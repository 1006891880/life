package com.android.baseadapterlibrary.adapter;

/**
 * 定义一个接口，判断返回数据类型
 */
public interface MultiTypeSupport<T> {
    int getLayoutId(T item, int position);
}
