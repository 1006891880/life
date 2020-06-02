package com.android.life.testbaseadapterlib.second;

import android.content.Context;

import com.android.baseadapterlibrary.adapter.BaseAdapter;
import com.android.baseadapterlibrary.adapter.BaseViewHolder;
import com.android.life.R;


public class SecondAdapter extends BaseAdapter<SecondBean> {

    public SecondAdapter(Context context) {
        super(context, R.layout.item_firstbaseadapter);
    }


    @Override
    protected void bindData(BaseViewHolder holder, SecondBean s) {
        holder.setText(R.id.tv_title,s.getTitle());
        //TextView view = holder.getView(R.id.tv_title);
        //view.setText(s);
    }
}
