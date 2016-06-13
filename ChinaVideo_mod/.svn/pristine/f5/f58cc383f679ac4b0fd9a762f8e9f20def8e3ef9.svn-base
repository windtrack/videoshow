package com.zhipu.chinavideo;

import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.fragment.MyAnchorFragment;
import com.zhipu.chinavideo.util.PagerSlidingTabStrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAnchor_Activity extends FragmentActivity implements
		OnClickListener {
	private TextView title;
	private ImageView title_back;
	private ViewPager viewPager;
	private PagerSlidingTabStrip indicator;
	private String[] TITLES = { "我的守护", "我的关注", "我的订阅" };
	private MyAnchorAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myanchor);
		title = (TextView) findViewById(R.id.title_text);
		title.setText("我的主播");
		title_back = (ImageView) findViewById(R.id.title_back);
		title_back.setOnClickListener(this);
		viewPager = ((ViewPager) findViewById(R.id.myanchor_viewpager));
		indicator = ((PagerSlidingTabStrip) findViewById(R.id.myanchor_indicator));
		adapter = new MyAnchorAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		indicator.setViewPager(viewPager);
		indicator.setSelectedTextColorResource(R.color.title_bg);
		indicator.setIndicatorColorResource(R.color.title_bg);
		indicator.setTextSize(getResources().getDimensionPixelSize(
				R.dimen.livehall_tab_textsize));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		default:
			break;
		}
	}

	public class MyAnchorAdapter extends FragmentStatePagerAdapter {

		public MyAnchorAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int paramInt) {
			// TODO Auto-generated method stub
			return MyAnchorFragment.getIntance(paramInt);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return TITLES.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return TITLES[position];
		}

		public String[] getTITLES() {
			return TITLES;
		}
	}
	/**
	 * 友盟统计
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
}