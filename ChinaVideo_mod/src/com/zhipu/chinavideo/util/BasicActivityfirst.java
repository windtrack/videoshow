package com.zhipu.chinavideo.util;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.zhipu.chinavideo.R;

public class BasicActivityfirst extends FragmentActivity {
	// private int guideResourceId = 0;// 引导页图片资源id

	@Override
	protected void onStart() {
		super.onStart();
		addGuideImage();// 添加引导页

	}

	/**
	 * 添加引导图片
	 */
	public void addGuideImage() {
		View view = getWindow().getDecorView().findViewById(
				R.id.my_content_view1);// 查找通过setContentView上的根布局
		if (view == null) {
			return;
		}
		if (MyPreferences.activityIsGuided(this, this.getClass().getName())) {
			// 引导过了
			return;
		}
		ViewParent viewParent = view.getParent();
		if (viewParent instanceof FrameLayout) {
			final FrameLayout frameLayout = (FrameLayout) viewParent;
			final View guardview;
			guardview = this.getLayoutInflater()
					.inflate(R.layout.guard_4, null);
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.FILL_PARENT);
			guardview.setLayoutParams(params);
			guardview.getBackground().setAlpha(180);
			guardview.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					frameLayout.removeView(guardview);

					 MyPreferences.setIsGuided(getApplicationContext(),
							 BasicActivityfirst.this.getClass().getName());// 设为已引导
				}
			});
			frameLayout.addView(guardview);// 添加引导图片

		}
	}

}
