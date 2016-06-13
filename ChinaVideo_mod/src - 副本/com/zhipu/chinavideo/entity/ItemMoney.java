package com.zhipu.chinavideo.entity;


import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class ItemMoney {

	public static int ITEM_ID =0 ;
	
	private Bitmap[] mImgMoney ;
	private Bitmap mCurBitMap ;
	private float mCurX ;
	private float mCurY ;
	private float mEndX ;
	private float mEndY ;
	private int mIndex ;
	public boolean mIsShow ;
	public boolean mWillDead ;
	
	long mCurTime ;
	long mPreTime ;
	
	public int id ;
	
	private float mAy ;
	private float mSpendY ;
	java.util.Random random=new java.util.Random();
	
	public Rect mCurRect = new Rect() ;
	
	public static ItemMoney creatItemMoney(Bitmap[] img,float x,float y,float endx,float endy){
		ItemMoney item = new ItemMoney() ;
		item.mImgMoney = img ;
		item.mIndex = 0 ;
		item.mCurBitMap = item.mImgMoney[item.mIndex] ;
		item.mIsShow = false ;
		item.mWillDead = false ;
		item.reSetItem(x,y,endx,endy) ;
		return item ;
	}
	Paint paint = new Paint() ;
	public void onDraw(Canvas canvas){

		if(!mIsShow){
			return ;
		}
		canvas.drawBitmap(mCurBitMap, mCurX, mCurY,null);
		
		mCurTime = System.currentTimeMillis() ;
		if(mCurTime - mPreTime >=100){
			mPreTime = mCurTime ;
			mIndex ++ ;
			if(mIndex >= mImgMoney.length){
				mIndex = 0 ;
			}
			mCurBitMap = mImgMoney[mIndex];
		}
		mCurY += (mAy+mSpendY) ;
		
		if(mCurY > mEndY){
			mIsShow = false ;
			mWillDead = false ;
		}
		mCurRect.left = (int) mCurX ;
		mCurRect.top = (int) mCurY;
		mCurRect.right = (int) (mCurBitMap.getWidth()+ mCurRect.left);
		mCurRect.bottom = (int) (mCurBitMap.getHeight() +mCurRect.top);
		

		
//		paint.setColor(Color.RED);// 设置灰色  
//		paint.setStyle(Paint.Style.STROKE);//设置填满  
//	    canvas.drawRect(mCurRect.left, mCurRect.top, mCurRect.right,mCurRect.bottom,paint);// 正方形 
		
	}
	
	
	public void reSetItem(float x,float y,float endx,float endy){
		
		mCurX = x ;
		mCurY = y ;
		mEndX = endx ;
		mEndY = endy ;
		
		mIndex = 0 ;
		mIsShow = false ;
		mWillDead = false ;
		mCurBitMap = mImgMoney[mIndex] ;
		mSpendY = 10 ;
		mAy =  0 ;
		
//		mSpendY = random.nextInt(8)+3 ;
//		mAy =  random.nextInt(2)+1 ;
		mPreTime = mCurTime ;
		id = ITEM_ID ;
		ITEM_ID++ ;
	}
	
	
	public float getmCurX() {
		return mCurX;
	}
	public void setmCurX(float mCurX) {
		this.mCurX = mCurX;
	}
	public float getmCurY() {
		return mCurY;
	}
	public void setmCurY(float mCurY) {
		this.mCurY = mCurY;
	}
	public float getmEndX() {
		return mEndX;
	}
	public void setmEndX(float mEndX) {
		this.mEndX = mEndX;
	}
	public float getmEndY() {
		return mEndY;
	}
	public void setmEndY(float mEndY) {
		this.mEndY = mEndY;
	}
	public int getmIndex() {
		return mIndex;
	}
	public void setmIndex(int mIndex) {
		this.mIndex = mIndex;
	}
	public boolean isShow() {
		return mIsShow;
	}
	public void setShow(boolean isShow) {
		this.mIsShow = isShow;
	}

}
