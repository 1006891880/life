package com.android.bannerlib.banner.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


/**
 * desc  : 动态管理的Adapter  每次都会创建新view，销毁旧View。节省内存消耗性能
 *     revise: 比如使用场景是启动引导页
 */
public abstract class AbsDynamicPagerAdapter extends PagerAdapter {

	@Override
	public boolean isViewFromObject(@NonNull View arg0, @NonNull Object arg1) {
		return arg0==arg1;
	}

	@Override
	public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
		container.removeView((View) object);
	}
	
	@Override
	public int getItemPosition(@NonNull Object object) {
		return super.getItemPosition(object);
	}

	@NonNull
	@Override
	public Object instantiateItem(@NonNull ViewGroup container, int position) {
		View itemView = getView(container,position);
		container.addView(itemView);
		return itemView;
	}

	/**
	 * 创建view
	 * @param container					container
	 * @param position					索引
	 * @return
	 */
	public abstract View getView(ViewGroup container, int position);

}
