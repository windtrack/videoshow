package com.zhipu.chinavideo.util;
import android.R;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

public class RunTextView extends TextView {
	// 持续时间 6秒
	private int duration =6000;
	private OnCompleteListener listener;
	private boolean mPaused = true;
	private int mRndDuration = 200;
	private Scroller mSlr;
	private int mXPaused = 0;

	public RunTextView(Context paramContext) {
		this(paramContext, null);
	}

	public RunTextView(Context paramContext, AttributeSet paramAttributeSet) {
		this(paramContext, paramAttributeSet, 16842884);
	}

	public RunTextView(Context paramContext, AttributeSet paramAttributeSet,
			int paramInt) {
		super(paramContext, paramAttributeSet, paramInt);
		setSingleLine();
		setEllipsize(null);
		setVisibility(4);//初始化的时候，是隐藏的
		setClickable(false);
		setFocusable(false);
		setTextColor(paramContext.getResources().getColor(R.color.holo_orange_dark));
	}

	private int calculateScrollingLen() {
		TextPaint localTextPaint = getPaint();
		Rect localRect = new Rect();
		String str = getText().toString();
		localTextPaint.getTextBounds(str, 0, str.length(), localRect);
		return localRect.width() + getWidth();
	}

	public void computeScroll() {
		super.computeScroll();
		if (this.mSlr == null)
			return;

		if ((this.mSlr.isFinished()) && (!this.mPaused)) {
			startScroll();
			if (this.listener != null)
				this.listener.onComplete();
		}
	}
	public int getDuration() {
		return this.duration;
	}
	public int getRndDuration() {
		return this.mRndDuration;
	}

	public void init(OnCompleteListener paramOnCompleteListener) {
		this.listener = paramOnCompleteListener;
	}

	public boolean isPaused() {
		return this.mPaused;
	}

	public void pauseScroll() {
		if (this.mSlr == null)
			return;

		if (!this.mPaused) {
			this.mPaused = true;
			this.mXPaused = this.mSlr.getCurrX();
			this.mSlr.abortAnimation();
		}
	}

	public void resumeScroll() {
		if (!this.mPaused)
			return;
		setHorizontallyScrolling(true);
		this.mSlr = new Scroller(getContext(), new LinearInterpolator());
		setScroller(this.mSlr);
		int i = calculateScrollingLen() - (getWidth() + this.mXPaused);
		setVisibility(0);//显示出跑马的TextView
		this.mSlr.startScroll(this.mXPaused, 0, i, 0, this.duration);
		this.mPaused = false;
	}

	public void setDuration(int paramInt) {
		this.duration = paramInt;
	}

	public void setRndDuration(int paramInt) {
		this.mRndDuration = paramInt;
	}

	public void startScroll() {
		this.mXPaused = (-1 * getWidth());
		this.mPaused = true;
		resumeScroll();
	}
	public static abstract interface OnCompleteListener {
		public abstract void onComplete();
	}

}
