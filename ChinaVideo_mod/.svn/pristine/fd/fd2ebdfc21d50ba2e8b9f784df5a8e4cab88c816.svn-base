package com.zhipu.chinavideo.util;

import com.zhipu.chinavideo.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * @author 曙光城邦 http://www.cnblogs.com/beenupper/
 * 
 */
public class BasicActivity extends FragmentActivity {
	private SharedPreferences sharedPreferences;
	int a = 0;

	@Override
	protected void onStart() {
		super.onStart();
		sharedPreferences = getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
		addGuideImage(1);// 添加引导页

		
	}

	/**
	 * 添加引导图片
	 */
	public void addGuideImage(final int guideResourceId) {
		View view = getWindow().getDecorView().findViewById(
				R.id.my_content_view);// 查找通过setContentView上的根布局
		if (view == null) {
			return;
		}

		if (MyPreferences.activityIsGuided(this, this.getClass().getName())
				&& "true".equals(sharedPreferences.getString(APP.HD_FIRST, ""))) {
			// 引导过了
			return;
		}
		if(MyPreferences.activityIsGuided(this, this.getClass().getName())&&guideResourceId==1){
			return;
		}
		
		ViewParent viewParent = view.getParent();
		if (viewParent instanceof FrameLayout) {
			final FrameLayout frameLayout = (FrameLayout) viewParent;
			if (guideResourceId != 0) {// 设置了引导图片
				final View guardview;

				if (guideResourceId == 1) {
					guardview = this.getLayoutInflater().inflate(
							R.layout.guard_1, null);
				} else if (guideResourceId == 2) {
					guardview = this.getLayoutInflater().inflate(
							R.layout.guard_3, null);
				} else if (guideResourceId == 3) {
					guardview = this.getLayoutInflater().inflate(
							R.layout.guard_2, null);
				} else {
					guardview = this.getLayoutInflater().inflate(
							R.layout.guard_2, null);
				}

				FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
						ViewGroup.LayoutParams.FILL_PARENT,
						ViewGroup.LayoutParams.FILL_PARENT);
				guardview.setLayoutParams(params);
				guardview.getBackground().setAlpha(180);
				guardview.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						frameLayout.removeView(guardview);
						if (a == 0&&guideResourceId!=3) {
							addGuideImage(2);// 添加引导页
							a = 1;
						}
						if (guideResourceId == 3) {
							a= 2;
							Editor editor = sharedPreferences.edit();
							editor.putString(APP.HD_FIRST, "true");
							editor.commit();
						} else {
							MyPreferences.setIsGuided(getApplicationContext(),
									BasicActivity.this.getClass().getName());// 设为已引导
						}

					}
				});
				frameLayout.addView(guardview);// 添加引导图片

			}
		}
	}

	// /**
	// * 子类在onCreate中调用，设置引导图片的资源id
	// 并在布局xml的根元素上设置android:id="@id/my_content_view"
	// *
	// * @param resId
	// */
	protected void setGuideResId(int resId) {
		addGuideImage(resId);
	}
}