package com.zhipu.chinavideo.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * @author: zhongxf
 * @Description: 点歌和已点的适配器
 * @date: 2016年4月8日
 */
public class ChooseSongAdapter extends PagerAdapter {

	private String[] TITLES = { "点歌", "已点歌曲" };

	private List<View> viewLists;

	public ChooseSongAdapter(List<View> viewList) {
		this.viewLists = viewList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return TITLES.length;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}


	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return TITLES[position];
	}

	public String[] getTITLES() {
		return TITLES;
	}

	@Override
	public void destroyItem(View view, int position, Object object) { // 销毁Item
		((ViewPager) view).removeView(viewLists.get(position));
	}

	@Override
	public Object instantiateItem(View view, int position) { // 实例化Item
		((ViewPager) view).addView(viewLists.get(position), 0);
		return viewLists.get(position);
	}

}
