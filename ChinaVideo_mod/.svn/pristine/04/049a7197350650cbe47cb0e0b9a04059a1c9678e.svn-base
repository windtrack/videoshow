package com.zhipu.chinavideo.util;

import com.zhipu.chinavideo.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author: zhongxf
 * @Description: 显示错误信息对话框
 * @date: 2016年4月8日
 */
public class ShowMsgDialog {
	
	private Dialog dialog;
	private TextView content;// 内容
	private Button sureBtn;// 确定按钮
	private OnClickListener sureListen;// 确定按钮监听
	private Button cancleBtn;// 取消按钮
	private OnClickListener cancleListen;// 取消按钮监听
	private ImageView errorImg;//错误的红叉ImageView
	private String sureText;
	private String cancleText;
	/**
	 * 显示一个消息提示框
	 */
	public void show(Context context,String msg) {
		creatDialog(context, msg,false);
	}
	
	/**
	 * 显示一个消息提示框
	 */
	public void show(Context context,String msg,boolean isShowError) {
		creatDialog(context, msg,isShowError);
	}
	
	public ShowMsgDialog setSureBtnText(String arg0){
		sureText = arg0;
		if(sureBtn!=null){
			sureBtn.setText(arg0);
		}
		return this;
	}
	public ShowMsgDialog setCancleBtnText(String arg0){
		cancleText = arg0;
		if(cancleBtn!=null){
			cancleBtn.setText(arg0);
		}
		return this;
	}

	public ShowMsgDialog setSureBtn(OnClickListener surListen) {
		this.sureListen = surListen;
		return this;
	}


	public ShowMsgDialog setCancleBtn(OnClickListener cancleListen) {
		this.cancleListen = cancleListen;
		return this;
	}

	private void creatDialog(Context context,String msg,boolean isShowError) {
		dialog = new Dialog(context, R.style.show_dialog);// 创建自定义样式dialog
		View main = LayoutInflater.from(context).inflate(R.layout.show_msg_dialog,
				null);
		
		content = (TextView) main.findViewById(R.id.dia_msg_content);
		content.setText(msg);
		
		errorImg = (ImageView) main.findViewById(R.id.error_icon);
		if(isShowError){
			errorImg.setVisibility(View.VISIBLE);
		}else{
			errorImg.setVisibility(View.GONE);
		}
		
		
		sureBtn = (Button) main.findViewById(R.id.dia_msg_sure_btn);
		cancleBtn = (Button) main.findViewById(R.id.dia_msg_cancle_btn);
		if (sureListen == null) {
			sureBtn.setVisibility(View.GONE);
		} else {
			sureBtn.setVisibility(View.VISIBLE);
			sureBtn.setOnClickListener(sureListen);
		}
		if (cancleListen == null) {
			cancleBtn.setVisibility(View.GONE);
		} else {
			cancleBtn.setVisibility(View.VISIBLE);
			cancleBtn.setOnClickListener(cancleListen);
		}
		
		if(sureText!=null && !"".equals(sureText)){
			sureBtn.setText(sureText);
		}
		if(cancleText!=null && !"".equals(cancleText)){
			cancleBtn.setText(cancleText);
		}
		
		LinearLayout.LayoutParams fill_parent = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
		dialog.setContentView(main, fill_parent);// 设置布局
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	/**
	 * 关闭消息提示框
	 * */
	public void closeDialog() {
		if (dialog!=null){
			dialog.dismiss();
		}
	}
	
}
