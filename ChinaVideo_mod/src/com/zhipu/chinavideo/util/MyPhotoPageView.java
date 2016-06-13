package com.zhipu.chinavideo.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyPhotoPageView extends ViewPager {
	private boolean scrollble;
	private int curPage;
	private boolean isRight;
	private boolean needCheck;
	private boolean isLeft;

	public MyPhotoPageView(Context context) {
		super(context);
	}

	public MyPhotoPageView(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
	}

	@Override
	public void scrollTo(int x, int y) {

		super.scrollTo(x, y);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (needCheck && curPage == 1 && isRight) {
			return false;
		}
		return super.onTouchEvent(arg0);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (needCheck && curPage == 1 && isRight) {
			return false;
		}
		return super.onInterceptTouchEvent(arg0);
	}

	public boolean isScrollble() {
		return scrollble;
	}

	public void setScrollble(boolean scrollble) {
		this.scrollble = scrollble;
	}

	// 是否需要检测向右滑动
	public void setNeedCheck(boolean needCheck) {
		this.needCheck = needCheck;
	}

	public boolean isRight() {
		return isRight;
	}

	public void setRight(boolean isRight) {
		this.isRight = isRight;
	}

	public boolean isLeft() {
		return isLeft;
	}

	public void setLeft(boolean isLeft) {
		this.isLeft = isLeft;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
}