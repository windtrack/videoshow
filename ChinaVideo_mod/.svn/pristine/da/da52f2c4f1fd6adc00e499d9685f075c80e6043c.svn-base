package com.zhipu.chinavideo.roundperson;

import com.zhipu.chinavideo.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

/**
 * @author: zhongxf
 * @Description: 显示底部条件的Pop
 * @date: 2016年4月15日
 */
public class SelectPopWindow extends PopupWindow {
	private Button btn_cancel;  
    private View mMenuView;  
    
    private Button anchorBtn,playerBtn,allBtn,clearInfoBtn;
    private int isSteath = 0;
  
    public SelectPopWindow(Activity context,OnClickListener itemsOnClick,int isSteath) {  
        super(context);
        this.isSteath = isSteath;
        LayoutInflater inflater = (LayoutInflater) context  
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        mMenuView = inflater.inflate(R.layout.round_person_choose, null);  
        btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);  
        //取消按钮  
        btn_cancel.setOnClickListener(new OnClickListener() {  
  
            public void onClick(View v) {  
                //销毁弹出框  
                dismiss();  
            }  
        });
        
        anchorBtn = (Button) mMenuView.findViewById(R.id.only_zhubo);
        anchorBtn.setOnClickListener(itemsOnClick);
        playerBtn = (Button) mMenuView.findViewById(R.id.only_player);
        playerBtn.setOnClickListener(itemsOnClick);
        allBtn = (Button) mMenuView.findViewById(R.id.only_all);
        allBtn.setOnClickListener(itemsOnClick);
        clearInfoBtn = (Button) mMenuView.findViewById(R.id.clear_location);
        clearInfoBtn.setOnClickListener(itemsOnClick);
        if(this.isSteath==1){
        	clearInfoBtn.setText("退出");
        }else{
        	clearInfoBtn.setText("清除位置信息并退出");
        }
        //设置按钮监听  
        //设置SelectPicPopupWindow的View  
        this.setContentView(mMenuView);  
        //设置SelectPicPopupWindow弹出窗体的宽  
        this.setWidth(LayoutParams.MATCH_PARENT);  
        //设置SelectPicPopupWindow弹出窗体的高  
        this.setHeight(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击  
        this.setFocusable(true);  
        //设置SelectPicPopupWindow弹出窗体动画效果  
        this.setAnimationStyle(R.style.loading_dialog);  
        //实例化一个ColorDrawable颜色为半透明  
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.black_50)); 
        //设置SelectPicPopupWindow弹出窗体的背景  
        this.setBackgroundDrawable(dw);  
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框  
        mMenuView.setOnTouchListener(new OnTouchListener() {  
            public boolean onTouch(View v, MotionEvent event) {  
                int height = mMenuView.findViewById(R.id.pop_layout).getTop();  
                int y=(int) event.getY();  
                if(event.getAction()==MotionEvent.ACTION_UP){  
                    if(y<height){  
                        dismiss();  
                    }  
                }                 
                return true;  
            }  
        });  
  
    }  
}
