package com.zhipu.chinavideo.roundperson;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.zhipu.chinavideo.R;

/**
 * @author: zhongxf
 * @Description: 点击周边达人弹出的首次提示框
 * @date: 2016年4月18日
 */
public class LocationDialog {

	private Dialog dialog;
	private Button sureBtn;// 确定按钮
	private Button cancleBtn;// 取消按钮
	private OnClickListener onSureClickListen;// 确定按钮的点击监听
	private OnClickListener onCancleClickListen;// 取消按钮的点击监听
	
	private CheckBox checkBox;
	
	public CheckBox getCheckBox(){
		return checkBox;
	}
	
	/**
	 * 显示一个消息提示框
	 */
	public void show(Context context) {
		creatDialog(context);
	}

	public LocationDialog setOnSureClickListen(
			OnClickListener onSureClickListen) {
		this.onSureClickListen = onSureClickListen;
		return this;
	}

	public LocationDialog setOnCancleClickListen(
			OnClickListener onCancleClickListen) {
		this.onCancleClickListen = onCancleClickListen;
		return this;
	}

	private void creatDialog(Context context) {
		dialog = new Dialog(context, R.style.show_dialog);// 创建自定义样式dialog
		View main = LayoutInflater.from(context).inflate(
				R.layout.loc_dia, null);
		sureBtn = (Button) main.findViewById(R.id.loc_dia_sure);
		cancleBtn = (Button) main.findViewById(R.id.loc_dia_cancle);
		if (onSureClickListen != null) {
			sureBtn.setOnClickListener(onSureClickListen);
		}
		if (onCancleClickListen != null) {
			cancleBtn.setOnClickListener(onCancleClickListen);
		}
		
		checkBox = (CheckBox) main.findViewById(R.id.checkbox);
		
		LinearLayout.LayoutParams fill_parent = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		dialog.setContentView(main, fill_parent);// 设置布局
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	/**
	 * 关闭消息提示框
	 * */
	public void closeDialog() {
		if (dialog != null) {
			dialog.dismiss();
		}
	}


}
