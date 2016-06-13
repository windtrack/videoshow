package com.zhipu.chinavideo.fragment;

import com.zhipu.chinavideo.R;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author: zhongxf
 * @Description: 填写寄语对话框
 * @date: 2016年4月8日
 */
public class ChooseSongTextDialog {
	private Dialog dialog;
	private Button sureBtn;// 确定按钮
	private Button cancleBtn;// 取消按钮
	private OnClickListener onSureClickListen;// 确定按钮的点击监听
	private OnClickListener onCancleClickListen;// 取消按钮的点击监听
	private EditText txtInput;// 文本输入框
	private TextView showWordsNum;// 显示文本输入的个数
	private int maxWordsNum = 25;// 最大允许输入数
	private CharSequence temp;// 监听前的文本
	/**
	 * 显示一个消息提示框
	 */
	public void show(Context context) {
		creatDialog(context);
	}

	public ChooseSongTextDialog setOnSureClickListen(
			OnClickListener onSureClickListen) {
		this.onSureClickListen = onSureClickListen;
		return this;
	}

	public ChooseSongTextDialog setOnCancleClickListen(
			OnClickListener onCancleClickListen) {
		this.onCancleClickListen = onCancleClickListen;
		return this;
	}

	// 寄语输入框的监听
	private TextWatcher wordsWatch = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			temp = arg0;
			showWordsNum.setText(arg0.length() + "/" + maxWordsNum);
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			/** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
			if (temp.length() > maxWordsNum) {// 输入的内容超出了允许的长度
				s.delete(maxWordsNum, temp.length());
				int tempSelection = maxWordsNum;
				txtInput.setText(s);
				txtInput.setSelection(tempSelection);
			}
		}
	};

	// 获取内容的输入框
	public EditText getTxtInput() {
		return txtInput;
	}

	private void creatDialog(Context context) {
		dialog = new Dialog(context, R.style.show_dialog);// 创建自定义样式dialog
		View main = LayoutInflater.from(context).inflate(
				R.layout.choose_song_text_dia, null);
		txtInput = (EditText) main.findViewById(R.id.choose_song_txt_input);
		txtInput.addTextChangedListener(wordsWatch);// 为EditText添加输入监听
		showWordsNum = (TextView) main.findViewById(R.id.show_words_num);
		// 初始化TextView显示可输入的字数
		String words = txtInput.getText().toString();
		if (words != null) {
			showWordsNum.setText(words.length() + "/" + maxWordsNum);
		}
		sureBtn = (Button) main.findViewById(R.id.choose_song_txt__sure_btn);
		cancleBtn = (Button) main.findViewById(R.id.choose_song_txt_cancle_btn);

		if (onSureClickListen != null) {
			sureBtn.setOnClickListener(onSureClickListen);
		}
		if (onCancleClickListen != null) {
			cancleBtn.setOnClickListener(onCancleClickListen);
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
