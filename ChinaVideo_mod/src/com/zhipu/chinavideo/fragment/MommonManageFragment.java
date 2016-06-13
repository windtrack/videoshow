package com.zhipu.chinavideo.fragment;



import com.zhipu.chinavideo.LiveRoomActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.manager.SoundManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint({ "NewApi", "ValidFragment" })
public  class MommonManageFragment extends Fragment {

	private ImageView mImgCaishen;
	private ImageView mImgLeft;
	private ImageView mImgRight;
	private ImageView mImgShoutDown;
	private MommonView mommonView;
	private TextView	mText_money ;
	private int mCount;
	boolean mFinish ;
	View mCurView;
	Animation rotateAnimation;
	LinearLayout container2;

	CountDownTimer shoutDown;
	
	DisplayMetrics dm;
	private float mViewHeight ;
	
	public int curCash;
	
	
	public boolean isShowMommonView ;
	public boolean isPause ;
	
	private LiveRoomActivity mContenxt ;
	private static final int[] SHOUTDOWN_IMG = { R.drawable.cs_3,
			R.drawable.cs_2, R.drawable.cs_1, R.drawable.cs_go, };

	@SuppressLint("ValidFragment")
	public MommonManageFragment(LiveRoomActivity context) {
		mContenxt = context ;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		if(mCurView == null){
			mCurView = inflater.inflate(R.layout.fragment_mommon, container,false);
			mCurView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// Toast.makeText(mContenxt, "哈哈哈哈哈哈", 100).show();
				}
			});
			getScreenHeight() ;
		}
		
		if(mommonView ==null){
			mommonView = new MommonView(mContenxt);
		}
		
		
			doStart() ;

		return mCurView;
	}

	public void doStart(){
		
		if(mCurView != null){
			curCash = 0 ;
			
			mFinish = false ;
			mImgCaishen = (ImageView) mCurView.findViewById(R.id.imageView_caishen);
			mImgLeft = (ImageView) mCurView.findViewById(R.id.imageView_mommonleft);
			mImgRight = (ImageView) mCurView.findViewById(R.id.imageView_mommonRight);
			mImgShoutDown = (ImageView) mCurView.findViewById(R.id.imageView_shoutDown);

			mText_money = (TextView) mCurView.findViewById(R.id.text_mommonCount);
			
			mText_money.setText(curCash+"");
			mImgCaishen.setImageResource(R.drawable.cs_cs1);
			
			mImgLeft.setVisibility(View.INVISIBLE);
			mImgRight.setVisibility(View.INVISIBLE);
			mImgCaishen.setVisibility(View.INVISIBLE);
			mImgShoutDown.setVisibility(View.INVISIBLE);

			doShoutDown();
		}

	}
	
	
	
	/**
	 * 先倒计时
	 */
	private void doShoutDown() {
		mCount = 0;
		mImgShoutDown.setImageResource(SHOUTDOWN_IMG[mCount]);
		mImgShoutDown.setVisibility(View.VISIBLE);
		shoutDown = new CountDownTimer(MommonView.MAX_SHOWTIEM, 1000) {
			@Override
			public void onFinish() {
				mImgShoutDown.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onTick(long arg0) {
				// 每1000毫秒回调的方法

				if (mCount < SHOUTDOWN_IMG.length) {
					mImgShoutDown.setImageResource(SHOUTDOWN_IMG[mCount]);
				}
				
				if (mCount == 3) {
				
					doMoveAction();
				}
				if (mCount == 4) {
					mImgShoutDown.setVisibility(View.INVISIBLE);
				}
				mCount++;
			}

		}.start();

	}

	public void doFinishTime() {
		if(shoutDown!=null){
			shoutDown.cancel();
		}
	
	}
	
	public void doGameOver(){
		
		if(!mFinish &&mCurView!=null){
			SoundManager.getIntance().stopDropMusic();
			mFinish = true ;
			doFinishTime();
			mImgShoutDown.setVisibility(View.INVISIBLE);
			if(mommonView!=null){
				mommonView.resetAllItemMoney();
				mommonView.doStop();
			}
			if(container2!=null){
				container2.removeAllViews();
			}
			
		}
	
	}

	/**
	 * 在掉下财神
	 */
	private void doMoveAction() {
		isShowMommonView = true ;
		if(!isPause &&!mContenxt.isPause){
			SoundManager.getIntance().playDropMusic(mContenxt);
		}
		
		TranslateAnimation animation = new TranslateAnimation(0.0f, 0,-mViewHeight, 0);
		
		animation.setDuration(600); 
		mImgCaishen.startAnimation(animation);
		mImgCaishen.setVisibility(View.VISIBLE);
		animation.setRepeatCount(0);
		
		animation.setInterpolator(new AccelerateInterpolator());
		
		
		mImgCaishen.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				TranslateAnimation animation2 = new TranslateAnimation(0.0f, 0,45, 0);
				animation2.setDuration(120); 
				animation2.setRepeatCount(0);
				animation2.setInterpolator(new AccelerateInterpolator());
				mImgCaishen.startAnimation(animation2);
				animation2.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationEnd(Animation arg0) {
						doRotateAction();
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
					}
					@Override
					public void onAnimationStart(Animation arg0) {
				
					}
				});
			}
		});
	}

	/**
	 * 再显示金币掉落
	 */
	private void doShowMoneyView() {
		
	
		mommonView.doStart();
		container2 = (LinearLayout) mCurView.findViewById(R.id.container_moneyView);
		container2.addView(mommonView);
		
		mommonView.setLayerType(View.LAYER_TYPE_NONE, null);
	}
	
	/**
	 * 帽子动
	 */
	private void doRotateAction() {

		mImgLeft.setVisibility(View.VISIBLE);
		mImgRight.setVisibility(View.VISIBLE);
		mImgCaishen.setImageResource(R.drawable.cs_cs2);

		// 帽子动
		doRotate(mImgLeft, 15, 35, mImgLeft.getWidth(), mImgLeft.getHeight()/2);
		doRotate(mImgRight, -15, -35, 0, mImgRight.getHeight()/2);
		// 金币层
		doShowMoneyView();
	}

	/**
	 * 帽子运动并监听重复
	 * @param img
	 * @param fromDegrees
	 * @param toDegrees
	 * @param pivotX
	 * @param pivotY
	 */
	private void doRotate(final ImageView img, final float fromDegrees,
			final float toDegrees, final float pivotX, final float pivotY) {

		RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees,
				toDegrees, pivotX, pivotY);
		rotateAnimation.setDuration(500);
		img.startAnimation(rotateAnimation);

		rotateAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				doRotate(img, toDegrees, fromDegrees, pivotX, pivotY);
			}
		});

	}

	public void onDestroyView() {
		super.onDestroyView();
//		ViewGroup localViewGroup = (ViewGroup) this.mCurView.getParent();
//		if (localViewGroup != null)
//			localViewGroup.removeView(this.mCurView);
	}
	
	/**
	 * 获取实际屏幕的宽高
	 */
	public void getScreenHeight() {
		dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		mViewHeight = dm.heightPixels;
	}
	
	public void showAddMOney(int money,int id){
		curCash+=money ;
		mText_money.setText(curCash+"");
		mommonView.doAddMoneyOnCanvans(money,id);
	}

	
	public void doPause(){
		isPause = true ;
		if(mommonView!=null){
			
			mommonView.pause();
		}
		SoundManager.getIntance().pauseDropMusic();
	}
	
	public void doResume(){
		isPause = false ;
		if(mommonView!=null){
			mommonView.resume();
		}
		if(isShowMommonView){
			SoundManager.getIntance().playDropMusic(mContenxt);
		}else{
			SoundManager.getIntance().resumeDropMusic();
		}
		
	}
	
}