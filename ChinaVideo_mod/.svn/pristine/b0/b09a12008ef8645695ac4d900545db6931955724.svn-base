package com.zhipu.chinavideo;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhipu.chinavideo.db.GlobalData;
import com.zhipu.chinavideo.db.HandlerCmd;
import com.zhipu.chinavideo.entity.ActivityMsg;
import com.zhipu.chinavideo.fragment.MommonCountFragment;
import com.zhipu.chinavideo.fragment.MommonManageFragment;
import com.zhipu.chinavideo.rechargecenter.RechargeActivity;
import com.zhipu.chinavideo.rpc.RpcEvent;
import com.zhipu.chinavideo.rpc.RpcRoutine;
import com.zhipu.chinavideo.util.APP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class FirstPayActivity extends Activity implements OnClickListener {

	public static final String[] TitleTips = { "恭喜您！", "提示", "提示", "提示", "提示", "提示", };
	// public static final String[] SuccessTips = {
	// "获得了价值350元的超值大礼包\n礼包内包含：VIP一级体验7天，草泥马座驾体验7天，新人银勋章",
	// "获得了价值1000元的超值大礼包\n礼包内包含：VIP二级体验7天，黄金草泥马座驾体验7天，新人金勋章",
	// "您账号尚未登录，请先登陆",
	// "亲，首次充值10元以上（含10元）才可以领哦 ",
	// "亲，首次充值50元以上（含50元）才可以领哦"
	// } ;
	//
	// public static final String[] TitleTips = {
	// "恭喜您！",
	// "恭喜您！",
	// "提示",
	// "提示",
	// "提示",
	// } ;

	public static final int Type_GetGift = 0;
	public static final int Type_Login = 1;
	public static final int Type_ToPay = 2;
	public static final int Type_WorngClick = 3;
	public static final int Type_HasGet = 4;
	public static final int Type_LittlePay = 5;// 钱不足

	boolean hasPay;

	boolean hasPay350;

	boolean hasPay1000;

	float curPay;

	Button button_350;
	Button button_1000;

	public int curType;

	Activity contenxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		contenxt = this;
		setContentView(R.layout.activity_firstpay);

		ImageView back = (ImageView)findViewById(R.id.title_back);

		back.setOnClickListener(this);

		button_350 = (Button)this.findViewById(R.id.bt_shouchong_350);

		button_350.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (GlobalData.getInstance().checkLogin()) {

					RpcRoutine.getInstance().addRpc(
							RpcEvent.GetShouChongGift,
							handler,
							GlobalData.getInstance().getSharedPreferences()
									.getString(APP.USER_ID, ""),
							GlobalData.getInstance().getSharedPreferences()
									.getString(APP.SECRET, ""), 1);
				}
				else {
					GlobalData.getInstance().mGetShouchongTips = "您账号尚未登录，请先登陆";
					showTips(Type_Login);
				}

			}
		});

		button_1000 = (Button)this.findViewById(R.id.bt_shouchong_1000);

		button_1000.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (GlobalData.getInstance().checkLogin()) {

					RpcRoutine.getInstance().addRpc(
							RpcEvent.GetShouChongGift,
							handler,
							GlobalData.getInstance().getSharedPreferences()
									.getString(APP.USER_ID, ""),
							GlobalData.getInstance().getSharedPreferences()
									.getString(APP.SECRET, ""), 2);
				}
				else {
					GlobalData.getInstance().mGetShouchongTips = "您账号尚未登录，请先登陆";
					showTips(Type_Login);
				}
			}
		});
	}

	private void updateButton() {
		if (hasPay350) {
			button_350.setBackgroundResource(R.drawable.sc_anniu3);
		}
		if (hasPay1000) {
			button_1000.setBackgroundResource(R.drawable.sc_anniu3);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_back:
				finish();
				break;
			default:
				break;
		}
	}

	private void showTips(final int type) {
		final AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
		localAlertDialog.show();
		Window localWindow = localAlertDialog.getWindow();
		View localView = getLayoutInflater().inflate(R.layout.dialog_shouchongtips, null);

		TextView textshow = (TextView)localView.findViewById(R.id.text_shouchongtips);
		TextView shouchongTitle = (TextView)localView.findViewById(R.id.shouchongTitle);

		textshow.setText(GlobalData.getInstance().mGetShouchongTips);
		// textshow.setText(SuccessTips[type]);
		shouchongTitle.setText(TitleTips[type]);

		Button queding = (Button)localView.findViewById(R.id.bt_shouchongsussessSure);
		queding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				localAlertDialog.dismiss();

				if (type == Type_GetGift) {

				}
				else if (type == Type_Login) {
					Intent intent = new Intent(contenxt, LoginActivity.class);
					startActivityForResult(intent, 100);
				}
				else if (type == Type_ToPay) {
					Intent intent = new Intent(contenxt, RechargeActivity.class);
					startActivityForResult(intent, 100);
				}
				else if (type == Type_WorngClick) {

				}
				else if (type == Type_HasGet) {

				}
			}
		});

		Button close = (Button)localView.findViewById(R.id.buttonclose);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				localAlertDialog.dismiss();
			}
		});

		localAlertDialog.setCancelable(false);
		localWindow.setContentView(localView);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case HandlerCmd.HandlerCmd_GetShowChongGiftSuccess: {
					showTips(Type_GetGift);
				}
					break;
				case HandlerCmd.HandlerCmd_GetShowChongGiftFiled_hasGet: {
					showTips(Type_HasGet);
				}
					break;
				case HandlerCmd.HandlerCmd_GetShowChongGiftFiled_worng: {
					showTips(Type_WorngClick);
				}
					break;
				case HandlerCmd.HandlerCmd_GetShowChongGiftFiled_ToPay: {
					showTips(Type_ToPay);
				}
					break;
				case HandlerCmd.HandlerCmd_GetShowChongGiftFiled_PayLittle: {
					showTips(Type_LittlePay);
				}
					break;
			}
		}
	};

}
