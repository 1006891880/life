package com.android.bannerlib.banner.adapter;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import java.util.ArrayList;



/**
 * desc  : 静态存储的Adapter,  view添加进去就不管了，View长在，内存不再
 *       revise: 如果是静态轮播图就用这个
 */
public abstract class AbsStaticPagerAdapter extends PagerAdapter {

    private ArrayList<View> mViewList = new ArrayList<>();

	@Override
	public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
		return arg0==arg1;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        Log.d("PagerAdapter","销毁的方法");
    }

	@Override
	public void notifyDataSetChanged() {
        mViewList.clear();
        super.notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(@NonNull Object object) {
		return POSITION_NONE;
	}

	@NonNull
    @Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = findViewByPosition(container,position);
        container.addView(itemView);
        onBind(itemView,position);
        Log.d("PagerAdapter","创建的方法");
		return itemView;
	}

    private View findViewByPosition(ViewGroup container, int position){
        for (View view : mViewList) {
            if (((int)view.getTag()) == position&&view.getParent()==null){
                return view;
            }
        }
        View view = getView(container,position);
        view.setTag(position);
        mViewList.add(view);
        return view;
    }


    public void onBind(View view, int position){}

	public abstract View getView(ViewGroup container, int position);

}
