package com.android.life.testbannerlib;


import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.android.baseadapterlibrary.adapter.BaseAdapter;
import com.android.baseadapterlibrary.adapter.BaseViewHolder;
import com.android.life.R;

public class Snap2AdapterBannerBar extends BaseAdapter<Integer> {


    Snap2AdapterBannerBar(Context context) {
        super(context, R.layout.item_snap2_banner);
    }


    @Override
    protected void bindData(BaseViewHolder holder, Integer data) {
        Integer integer = getData().get(data % getData().size());
        ImageView imageView = holder.getView(R.id.iv_image);
        imageView.setBackgroundResource(integer);
    }

    @Override
    public int getItemCount() {
        if (getData().size() != 1) {
            Log.e("getItemCount","getItemCount---------");
            return Integer.MAX_VALUE; // 无限轮播
        } else {
            Log.e("getItemCount","getItemCount++++----");
            return getData().size();
        }
    }


}
