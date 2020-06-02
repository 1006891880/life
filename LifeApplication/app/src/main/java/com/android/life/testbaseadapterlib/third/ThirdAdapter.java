package com.android.life.testbaseadapterlib.third;

import android.content.Context;

import com.android.baseadapterlibrary.adapter.BaseAdapter;
import com.android.baseadapterlibrary.adapter.BaseViewHolder;
import com.android.baseadapterlibrary.adapter.MultiTypeSupport;
import com.android.life.R;


public class ThirdAdapter extends BaseAdapter<ThirdBean> implements MultiTypeSupport<ThirdBean> {


    public ThirdAdapter(Context context) {
        super(context, R.layout.main_chat_from_msgbasedapter);
        //这句话一点要添加
        this.multiTypeSupport = this;
    }

    @Override
    protected void bindData(BaseViewHolder holder, ThirdBean s) {
        holder.setText(R.id.tv_title,s.getTitle());
        //TextView view = holder.getView(R.id.tv_title);
        //view.setText(s);
    }

    @Override
    public int getLayoutId(ThirdBean item, int position) {
        if (item.getLocation()==1) {
            return R.layout.main_chat_from_msgbasedapter;
        }
        return R.layout.main_chat_send_msgbasedapter;
    }
}
