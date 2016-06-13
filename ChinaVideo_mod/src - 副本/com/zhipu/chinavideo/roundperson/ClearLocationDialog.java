package com.zhipu.chinavideo.roundperson;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zhipu.chinavideo.R;

/**
 * @author: zhongxf
 * @Description: 清除位置信息弹出的提示框
 * @date: 2016年4月18日
 */
public class ClearLocationDialog {

	private Dialog dialog;
	private Button sureBtn;// 确定按钮
	private OnClickListener onSureClickListen;// 确定按钮的点击监听
	/**
	 * 显示一个消息提示框
	 */
	public void show(Context context) {
		creatDialog(context);
	}

	public ClearLocationDialog setOnSureClickListen(
			OnClickListener onSureClickListen) {
		this.onSureClickListen = onSureClickListen;
		return this;
	}

	private void creatDialog(Context context) {
		dialog = new Dialog(context, R.style.show_dialog);// 创建自定义样式dialog
		View main = LayoutInflater.from(context).inflate(
				R.layout.clear_loc_dia, null);
		sureBtn = (Button) main.findViewById(R.id.clear_loc_dia_btn);
		if (onSureClickListen != null) {
			sureBtn.setOnClickListener(onSureClickListen);
		}
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
