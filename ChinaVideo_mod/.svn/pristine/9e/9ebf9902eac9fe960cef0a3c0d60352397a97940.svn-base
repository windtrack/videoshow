package com.zhipu.chinavideo.fragment;


import com.zhipu.chinavideo.LiveRoomActivity;
import com.zhipu.chinavideo.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

@SuppressLint({ "NewApi", "ValidFragment" })
public  class MommonCountFragment extends Fragment {
	private View mCurView;
	private TextView mTextMoney;

	private int mAllMoney ;
	private LiveRoomActivity mContenxt ;

	public MommonCountFragment(LiveRoomActivity context) {
		mContenxt = context ;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(mCurView ==null){
			mCurView = inflater.inflate(R.layout.fragment_mommoncount,container, false);
			mCurView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// Toast.makeText(mContenxt, "哈哈哈哈哈哈", 100).show();
				}
			});
		}
	
		
		
		mTextMoney = (TextView) mCurView.findViewById(R.id.text_showmoney);
		mTextMoney.setText("乐币："+mAllMoney);
		doShoutDown() ;

		return mCurView;
	}
	public void doShoutDown(){
		new CountDownTimer(3000, 3000) {
			@Override
			public void onFinish() {
				mContenxt.showMommonCountView(false);
			}

			@Override
			public void onTick(long arg0) {
			
				
			}

		}.start();
	}
	
	public void onDestroyView() {
		super.onDestroyView();
//		ViewGroup localViewGroup = (ViewGroup) this.mCurView.getParent();
//		if (localViewGroup != null)
//			localViewGroup.removeView(this.mCurView);
	}
	public void setCurMoney(int money){
		mAllMoney = money ;
//		mTextMoney.setText("乐币："+mAllMoney);
	}

	
}