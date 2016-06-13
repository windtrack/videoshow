package com.zhipu.chinavideo.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.fragment.FaceFragment;
import com.zhipu.chinavideo.util.SmilyParse;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FaceManager implements View.OnClickListener {
	private boolean isShow = false;
	private View faceview;
	private static FaceManager instance;
	private Context context;
	private ViewStub viewstub_face;
	private ViewPager face_viewpager;
	private FragmentActivity localActivity;
	private FaceAdapter adapter;
	private ImageView[] imageViews;
	private ImageView imageView;
	private static SmilyParse sp;

	public static FaceManager getInstance() {
		if (instance == null)
			instance = new FaceManager();
		return instance;
	}

	public void exit() {
		instance = null;
	}

	public void initFaceManager(Context context, ViewStub viewstub_face) {
		this.context = context;
		this.viewstub_face = viewstub_face;
		sp = new SmilyParse(context);
	}

	public void showfaceView() {
		if (faceview == null) {
			this.viewstub_face.setLayoutResource(R.layout.face_layout);
			this.faceview = this.viewstub_face.inflate();
			findviewbyid();
		} else {
			this.isShow = true;
			this.faceview.setVisibility(0);
		}
	}

	public boolean getIsShow() {
		return this.isShow;
	}

	public boolean goneFaceView() {
		boolean bool = false;
		if ((this.faceview != null) && (this.faceview.getVisibility() == 0)) {
			Animation animation = AnimationUtils.loadAnimation(context,
					R.anim.input_exit);
			faceview.startAnimation(animation);
			this.faceview.setVisibility(8);
			this.isShow = false;
			bool = true;
		}
		return bool;
	}

	private void findviewbyid() {
		// TODO Auto-generated method stub
		localActivity = (FragmentActivity) this.context;
		face_viewpager = (ViewPager) faceview.findViewById(R.id.face_viewpager);
		adapter = new FaceAdapter(localActivity.getSupportFragmentManager());
		face_viewpager.setAdapter(adapter);
		initCirclePoint();
		face_viewpager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				// 重新设置原点布局集合
				for (int i = 0; i < imageViews.length; i++) {
					imageViews[arg0].setBackgroundResource(R.drawable.dot_red);
					if (arg0 != i) {
						imageViews[i]
								.setBackgroundResource(R.drawable.dot_white);
					}
				}
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		default:
			break;
		}
	}

	class FaceAdapter extends FragmentPagerAdapter {

		public FaceAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return FaceFragment.getIntance(context, initfacedata(arg0));
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 5;
		}
	}

	private void initCirclePoint() {
		ViewGroup group = (ViewGroup) faceview
				.findViewById(R.id.face_viewGroup);
		imageViews = new ImageView[5];
		// 广告栏的小圆点图标
		for (int i = 0; i < 5; i++) {
			// 创建一个ImageView, 并设置宽高. 将该对象放入到数组中
			imageView = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			params.setMargins(5, 0, 5, 0);
			imageView.setLayoutParams(params);
			imageViews[i] = imageView;
			// 初始值, 默认第0个选中
			if (i == 0) {
				imageViews[i].setBackgroundResource(R.drawable.dot_red);
			} else {
				imageViews[i].setBackgroundResource(R.drawable.dot_white);
			}
			// 将小圆点放入到布局中
			group.addView(imageViews[i]);
		}
	}
	
	private List<Map<String, ?>> initfacedata(int page) {
		List<Map<String, ?>> frag_data = new ArrayList<Map<String, ?>>();
		for (int i = 0; i < sp.getData().size(); i++) {
			if (i >= (24 * page) && i < (24 * (page + 1))) {
				frag_data.add(sp.getData().get(i));
			}
		}
		return frag_data;
	}
}
