package com.zhipu.chinavideo.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class MyProgress extends ProgressBar {
	String text;
	Paint mPaint;

	public MyProgress(Context context) {
		super(context);
		initText();
	}

	public MyProgress(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initText();
	}

	public MyProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		initText();
	}

	@Override
	public synchronized void setProgress(int progress) {
		setText(progress);
		super.setProgress(progress);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// this.setText();
		Rect rect = new Rect();
		this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
		int x = (getWidth() / 2) - rect.centerX();

		int y = (getHeight() / 2) - rect.centerY();
		canvas.drawText(this.text, x + 10, y, this.mPaint);
	}

	// 初始化，画笔
	private void initText() {
		this.mPaint = new Paint();
		this.mPaint.setColor(Color.GREEN);
		this.mPaint.setTextSize(25);
	}

	// 设置文字内容
	private void setText(int progress) {
		int i = this.getMax();
		this.text = progress + "/" + i;
	}
}