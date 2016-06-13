package com.zhipu.chinavideo.fragment;


import java.util.HashMap;

import com.zhipu.chinavideo.LiveRoomActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.ItemMoney;
import com.zhipu.chinavideo.manager.SoundManager;
import com.zhipu.chinavideo.util.DvAppUtil;
import com.zhipu.chinavideo.util.Tools;
import com.zhipu.chinavideo.util.dConfig;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MommonView extends View{

	private static final int MAX_ITEMONEY_COUNT = 120 ;//最大金币数量
	
	public static final int MAX_SHOWTIEM = 7000 ;//财神总时间微秒 前 5秒倒计时
	
	private static final int INTERVAL_TIME = 100;//间隔多久创建一个金币
	
	private ItemMoney[] mAllItemMoney ; //
	
	

	
	static Bitmap[] bitmapMapMoney = new Bitmap[7];//7张金币动画
	
	DisplayMetrics metrics ;
	
	java.util.Random random=new java.util.Random();
	
	private long mCurTime ;
	private long mPreTime ;
	
	private long mCurTime2 ;
	private long mPreTime2 ;
	
	boolean isPause ;
	
	private static final int[] ID_MONEY ={
		R.drawable.cs_jinbi1,
		R.drawable.cs_jinbi2,
		R.drawable.cs_jinbi3,
		R.drawable.cs_jinbi4,
		R.drawable.cs_jinbi5,
		R.drawable.cs_jinbi6,
		R.drawable.cs_jinbi7,
	};
	
	public ValueAnimator animator = ValueAnimator.ofFloat(0, 1);

	LiveRoomActivity mContent;
	@SuppressLint("NewApi") public MommonView(LiveRoomActivity context) {
		super(context);
		mContent = context ;
		isPause= false ;
		initAllItemMoney();
		
		 animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	            @Override
	            public void onAnimationUpdate(ValueAnimator arg0) {
	            	if(!isPause){
	            	 	doCreatMoney();

		            	mCurTime2 = System.currentTimeMillis() ;
		            	
		            	if(mCurTime2-mPreTime2>=33){
		            		mPreTime2 = mCurTime2 ;
		            		invalidate();
		            	}
	            	}
	            }
	        });
	        animator.setRepeatCount(ValueAnimator.INFINITE);
	        animator.setDuration(3000);
//	        animator.start();
	}

	
	@SuppressLint("NewApi")
	public void doStart(){
		 resetAllItemMoney() ;
		 if(animator!=null){
			 animator.start();
		 }
		
	}
	
	@SuppressLint("WrongCall") @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		for(int i=0; i<mAllItemMoney.length; i++){
			if(!mAllItemMoney[i].mWillDead &&mAllItemMoney[i].isShow() ){
				mAllItemMoney[i].onDraw(canvas);
			}
		}
		
		for(int j=0 ;j<mAllStr.length; j++){
			if(mAllStr[j].isShow){
				mAllStr[j].draw(canvas);
			}
		}
		
	}
	
	
	private void doCreatMoney(){
		mCurTime = System.currentTimeMillis() ;
		if(mCurTime-mPreTime>=INTERVAL_TIME){
			mPreTime = mCurTime ;
			creatOneMoney() ;
		}
	}
	
	/**
	 * 初始化数据
	 */
	public void initAllItemMoney(){
		 metrics = DvAppUtil.getDisplayMetrics(mContent);
   
		for(int i=0; i<bitmapMapMoney.length; i++){
			if(bitmapMapMoney[i]==null){
				bitmapMapMoney[i] = ((BitmapDrawable)getResources().getDrawable(ID_MONEY[i])).getBitmap() ;
			}
		}
		if(mAllItemMoney == null){
			mAllItemMoney = new ItemMoney[MAX_ITEMONEY_COUNT];
			for(int i=0; i<mAllItemMoney.length; i++){
				float startX =  135/2+(random.nextInt(metrics.widthPixels-135)-135/2);
				mAllItemMoney[i] = ItemMoney.creatItemMoney(bitmapMapMoney,startX,-96-48,startX,metrics.heightPixels+135) ;
			}
		}
		
		initStrMoney() ;
	}
	/**
	 * 重置数据
	 */
	public void resetAllItemMoney(){
		ItemMoney.ITEM_ID = 0 ;
		for(int i=0; i<mAllItemMoney.length; i++){
			mAllItemMoney[i].mIsShow = false ;
			mAllItemMoney[i].mWillDead = false ;
		}
		cleanStr() ;
	}
	/**
	 * 生成一个金币
	 */
	public void creatOneMoney(){
		for(int i=0; i<mAllItemMoney.length; i++){
			if(!mAllItemMoney[i].isShow() && !mAllItemMoney[i].mWillDead){
				float startX =  135/2+(random.nextInt(metrics.widthPixels-135)-135/2);
				mAllItemMoney[i].reSetItem(startX,-135,startX,metrics.heightPixels+135);
				mAllItemMoney[i].mIsShow = true ;
				mAllItemMoney[i].mWillDead = false ;
				return ;
			}
		}	
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		 if(MotionEvent.ACTION_DOWN == event.getAction()){
			 
			float px = event.getX();
			float py = event.getY();
			checkChickMoney(px,py) ;
	     }
		
		return super.onTouchEvent(event);
	}
	
	private void checkChickMoney(float px, float py){
		for(int i=0; i<mAllItemMoney.length; i++){
			if(mAllItemMoney[i].isShow()){
				//触摸检测
				if(mAllItemMoney[i].mCurRect.left<px 
						&& mAllItemMoney[i].mCurRect.right>px
						&& mAllItemMoney[i].mCurRect.top<py
						&& mAllItemMoney[i].mCurRect.bottom> py
						){
					mAllItemMoney[i].mWillDead = true ;
					mContent.SocketMommonClickMoney(mAllItemMoney[i].id);
					SoundManager.getIntance().playSound(SoundManager.SFX_ID_SHAKE);
					return ;
				}
			}
		}
	}
	
	
    @SuppressLint("NewApi")
	public void pause() {
    	isPause = true ;
        // Make sure the animator's not spinning in the background when the activity is paused.
    	if(animator!=null && animator.isRunning()){
    		animator.cancel();
//    		animator.clone()
    	}
        
    }

	@SuppressLint("NewApi")
	public void resume() {
		isPause = false ;
		if(animator!=null){
			animator.start();
    	}
    }
	
	@SuppressLint("NewApi")
	public void doStop(){
		if(animator!=null){
    		animator.cancel();
//    		animator =null ;
    	}
	}
	@SuppressLint("NewApi")
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Cancel animator in case it was already running
        animator.cancel();
        animator.start();
        isPause = false ;
    }
	
	
	public void doAddMoneyOnCanvans(int money ,int id){
		
		for(int i=0; i<mAllItemMoney.length; i++){
			if(mAllItemMoney[i].id == id){
				
				for(int j=0 ;j<mAllStr.length; j++){
					if(!mAllStr[j].isShow){
						mAllStr[j].isShow = true ;
						mAllStr[j].str = money+"" ;
						mAllStr[j].x = mAllItemMoney[i].getmCurX() ;
						mAllStr[j].y = mAllItemMoney[i].getmCurY() ;
						mAllItemMoney[i].setShow(false);
						mAllItemMoney[i].mWillDead = false ;
						return ;
					}
				}
				return ;
			}
		}
		
		Log.i("sjf", "没有找到金币并显示");
		
		
		
	}

	private void cleanStr(){
		for(int i=0 ;i<mAllStr.length; i++){
			mAllStr[i].reset() ;
		}
	}
	
	public void initStrMoney(){
		for(int i=0 ;i<mAllStr.length; i++){
			if(mAllStr[i]==null){
				mAllStr[i] = new StrMoney();
			}
			mAllStr[i].reset() ;
		}
	}
	
	StrMoney[] mAllStr = new StrMoney[30];
	
	
	class StrMoney{
		boolean isShow;
		String str;
		float x ;
		float y ;
		Paint paint = new Paint();
		int alpah = 255 ;
		void draw(Canvas canvas){
			
			if(!isShow){
				return ;
			}
			
			Tools.drawText("+"+str, canvas, paint, x, y, 0xfff53505, 0xffffffff, (int)(64*dConfig.curScale), 255);
			y-=2 ;
			
			alpah-=10;
			if(alpah<=0){
				alpah = 0 ;
				reset() ;
			}
		}
		
		void reset(){
			isShow = false ;
			alpah = 255 ;
		}
		
		
	}
}
