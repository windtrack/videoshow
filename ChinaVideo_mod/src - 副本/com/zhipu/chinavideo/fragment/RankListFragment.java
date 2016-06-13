package com.zhipu.chinavideo.fragment;

import com.zhipu.chinavideo.MainTabActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.SearchActivity;
import com.zhipu.chinavideo.adapter.RankTypeChoiceAdapter;
import com.zhipu.chinavideo.util.PagerSlidingTabStrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RankListFragment extends Fragment implements OnClickListener {
	private View rootView;
	private TypePagerAdapter adapter;
	private ViewPager viewPager;
	private PagerSlidingTabStrip indicator;
	private RelativeLayout pop_btn;
	private Animation showAnim;
	private GridView typeChoiceGrid;
	private View typeChoiceView;
	private int popCheckedId = 0;
	private TextView chooseTypeText;
	private Animation goneAnim;
	private RankTypeChoiceAdapter choiceAdapter;
	private ViewStub typeChoicePop;
	private Animation xuanzhuandonghua;
	private String[] TITLES = { "明星", "人气", "点播", "富豪", "周星", "风云" };
	private TextView title;
	private ImageView livehall_pop_btn;
	private View gridview_bg;

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null)
			localViewGroup.removeView(this.rootView);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_myanchorread,
					container, false);
			title = (TextView) this.rootView.findViewById(R.id.title_tv);
			title.setText("排行");
			this.viewPager = ((ViewPager) this.rootView
					.findViewById(R.id.rank_viewPager));
			this.indicator = ((PagerSlidingTabStrip) this.rootView
					.findViewById(R.id.rank_indicator));
			this.pop_btn = (RelativeLayout) this.rootView
					.findViewById(R.id.rank_pop_rl);
			this.chooseTypeText = (TextView) this.rootView
					.findViewById(R.id.rank_choose_pop_txt);
			livehall_pop_btn = (ImageView) rootView
					.findViewById(R.id.rank_pop_btn);
			this.pop_btn.setOnClickListener(this);
			initview();
			initTitleBar();
		}
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rank_pop_rl:
			if (((MainTabActivity) getActivity()).isTypeChoicePopShown()) {
				outTypeChoicePopView();
			} else {
				inTypeChoicePopView();
			}
			break;
		default:
			break;
		}
	}

	private void initPop() {
		xuanzhuandonghua = AnimationUtils.loadAnimation(getActivity(),
				R.anim.xuanzhuan_titlebutton);
		livehall_pop_btn.startAnimation(xuanzhuandonghua);
		xuanzhuandonghua.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				livehall_pop_btn.setImageResource(R.drawable.down_top);
			}
		});
		if (this.typeChoiceView == null) {
			this.typeChoicePop = ((ViewStub) getActivity().findViewById(
					R.id.rank_choice_pop_stub));
			this.typeChoicePop.setLayoutResource(R.layout.type_choice_pop);
			View localView = this.typeChoicePop.inflate();
			this.typeChoiceView = localView.findViewById(R.id.typegrid_layout);
			this.gridview_bg = localView.findViewById(R.id.gridview_bg);
			this.typeChoiceGrid = ((GridView) localView
					.findViewById(R.id.typegird));
			this.choiceAdapter = new RankTypeChoiceAdapter(getActivity(),
					this.popCheckedId);
			this.typeChoiceGrid.setAdapter(this.choiceAdapter);
			this.typeChoiceView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramAnonymousView) {
					RankListFragment.this.outTypeChoicePopView();
				}
			});
		}
		this.typeChoiceGrid
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(
							AdapterView<?> paramAnonymousAdapterView,
							View paramAnonymousView, int paramAnonymousInt,
							long paramAnonymousLong) {
						RankListFragment.this.viewPager
								.setCurrentItem(paramAnonymousInt);
						RankListFragment.this.outTypeChoicePopView();
					}
				});
	}

	/**
	 * 显示下拉框
	 */
	private void inTypeChoicePopView() {
		// TODO Auto-generated method stub
		initPop();
		if (this.showAnim == null)
			this.showAnim = AnimationUtils.loadAnimation(getActivity(),
					R.anim.livehall_type_choice_pop_show);
		this.typeChoiceGrid.startAnimation(this.showAnim);
		this.gridview_bg.setVisibility(0);
		this.typeChoiceGrid.setVisibility(0);
		this.typeChoiceView.setVisibility(0);
		this.choiceAdapter.refreshGrid(this.popCheckedId);
		((MainTabActivity) getActivity()).setTypeChoicePopShown(true);
		this.chooseTypeText.setVisibility(0);
	}

	/**
	 * 关闭下拉显示框
	 */
	public void outTypeChoicePopView() {
		livehall_pop_btn.startAnimation(xuanzhuandonghua);
		xuanzhuandonghua.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				livehall_pop_btn.setImageResource(R.drawable.down);
			}
		});
		if (this.goneAnim == null) {
			this.goneAnim = AnimationUtils.loadAnimation(getActivity(),
					R.anim.livehall_type_choice_pop_close);
			this.goneAnim
					.setAnimationListener(new Animation.AnimationListener() {
						public void onAnimationEnd(
								Animation paramAnonymousAnimation) {
							RankListFragment.this.typeChoiceGrid
									.setVisibility(8);
						}

						public void onAnimationRepeat(
								Animation paramAnonymousAnimation) {
						}

						public void onAnimationStart(
								Animation paramAnonymousAnimation) {
							RankListFragment.this.typeChoiceGrid
									.setOnItemClickListener(null);
						}
					});
		}
		this.typeChoiceGrid.startAnimation(this.goneAnim);
		this.typeChoiceView.setVisibility(8);
		this.gridview_bg.setVisibility(8);
		((MainTabActivity) getActivity()).setTypeChoicePopShown(false);
		this.chooseTypeText.setVisibility(4);
	}

	private void initview() {
		adapter = new TypePagerAdapter(getChildFragmentManager());
		viewPager.setAdapter(adapter);
		indicator.setViewPager(viewPager);
		indicator.setSelectedTextColorResource(R.color.title_bg);
		indicator.setIndicatorColorResource(R.color.title_bg);
		indicator.setTextSize(getResources().getDimensionPixelSize(
				R.dimen.livehall_tab_textsize));
		indicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				RankListFragment.this.popCheckedId = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public class TypePagerAdapter extends FragmentStatePagerAdapter {
		public TypePagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int paramInt) {
			// TODO Auto-generated method stub
			Fragment localObject = null;
			if (paramInt > 3) {
				localObject = FenYunFragment.getIntance(TITLES[paramInt]);
			} else {
				localObject = RankTypeFragment.getIntance(TITLES[paramInt]);
			}
			return localObject;
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

	private void initTitleBar() {
		ImageView sousuoimg = (ImageView) this.rootView
				.findViewById(R.id.sousuo_img);
		sousuoimg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), SearchActivity.class);
				startActivity(intent);
			}
		});
		ImageView curren_anchor = (ImageView) this.rootView
				.findViewById(R.id.current_anchor);
		curren_anchor.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 弹出在线主播界面
				MainTabActivity.initanchoronline();
				Log.i("lvjian", "---------正在直播---------");
			}
		});

	}
}
