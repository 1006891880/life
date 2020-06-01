package com.android.life.testbannerlib;


import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.baseadapterlibrary.adapter.BaseAdapter;
import com.android.baseadapterlibrary.adapter.BaseViewHolder;
import com.android.life.R;

public class Snap2Adapter extends BaseAdapter<Integer> {


    Snap2Adapter(Context context) {
        super(context, R.layout.item_snap2);
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
