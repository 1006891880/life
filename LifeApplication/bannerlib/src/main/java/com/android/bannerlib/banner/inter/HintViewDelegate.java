package com.android.bannerlib.banner.inter;


public interface HintViewDelegate {

    /**
     * 设置索引位置
     * @param position              索引
     * @param hintView              HintView
     */
    void setCurrentPosition(int position, BaseHintView hintView);

    /**
     * 初始化view
     * @param length                length
     * @param gravity               位置
     * @param hintView              HintView
     */
    void initView(int length, int gravity, BaseHintView hintView);

}
