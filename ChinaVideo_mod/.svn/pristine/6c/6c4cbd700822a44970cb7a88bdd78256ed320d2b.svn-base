package com.zhipu.chinavideo.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
 
public class KeyboardLayout extends RelativeLayout {
 
    private onSizeChangedListener mChangedListener;
    private boolean mShowKeyboard = false;
     
    public KeyboardLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
 
    public KeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
 
    public KeyboardLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
     
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
 
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);
    }
     
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        if (null != mChangedListener && 0 != oldw && 0 != oldh) {
            if (h < oldh) {
                mShowKeyboard = true;
            } else {
                mShowKeyboard = false;
            }
            mChangedListener.onChanged(mShowKeyboard);
        }
    }
     
    public void setOnSizeChangedListener(onSizeChangedListener listener) {
        mChangedListener = listener;
    }
     
    public interface onSizeChangedListener{
         
        void onChanged(boolean showKeyboard);
    }
     
}