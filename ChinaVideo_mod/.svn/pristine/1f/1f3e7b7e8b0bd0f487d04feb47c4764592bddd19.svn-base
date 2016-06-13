package ctv.sdk;

import org.json.JSONObject;





import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.db.HandlerCmd;
import com.zhipu.chinavideo.rpc.RpcEvent;
import com.zhipu.chinavideo.rpc.RpcRoutine;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class YD_chongzhi extends Activity implements OnClickListener {
	// // 计费信息

	private ProgressDialog mProgressDialog;

	private Context context;

	private int[] tvIds = { R.id.yd_ck_1, R.id.yd_ck_2, R.id.yd_ck_3,
			R.id.yd_ck_4, R.id.yd_ck_5, R.id.yd_ck_6 };
	private TextView[] tv = new TextView[this.tvIds.length];
	private TextView yd_recharge_sure;
	private ImageView back;
	private TextView yd_recharge_beans, yd_recharge_name;
	private SharedPreferences preferences;
	private String room_id = "";
	private String user_id;
	private String secret;
	private Dialog dialog;
	private String user_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zgyd_activity);
		for (int j = 0; j < tvIds.length; j++) {
			this.tv[j] = ((TextView) findViewById(this.tvIds[j]));
			tv[j].setOnClickListener(this);
		}
		yd_recharge_sure = (TextView) findViewById(R.id.yd_recharge_sure);
		back = (ImageView) findViewById(R.id.title_back);
		back.setOnClickListener(this);
		yd_recharge_sure.setOnClickListener(this);
		preferences = getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
		user_id = preferences.getString(APP.USER_ID, "");
		secret = preferences.getString(APP.SECRET, "");
		user_name = preferences.getString(APP.NICKNAME, "");
		setinfo();
		mProgressDialog = new ProgressDialog(YD_chongzhi.this);
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setMessage("请稍候...");
		this.dialog = Utils.showProgressDialog(this, "获取订单中", true);
//		context = YD_chongzhi.this;
//		GameBilling.getInstance().init(YD_chongzhi.this);
	}

	public void dismissProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("Demo1 resume");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		System.out.println("Demo1 onRestart");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("Demo1 onPause");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("Demo1 onStop");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void showProgressDialog() {
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(YD_chongzhi.this);
			mProgressDialog.setIndeterminate(true);
			mProgressDialog.setMessage("请稍候.....");
		}
		if (!mProgressDialog.isShowing()) {
			mProgressDialog.show();
		}
	}

	/**
	 * 选中充值类型
	 */
	private void changeTvBg(int type) {
		for (int i = 0; i < tvIds.length; i++) {
			if (type == tvIds[i]) {
				tv[i].setBackgroundResource(R.drawable.bg_recharge_selected);
			} else {
				tv[i].setBackgroundResource(R.drawable.white_cor_bg);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.yd_ck_1:
			changeTvBg(R.id.yd_ck_1);
			break;
		case R.id.yd_ck_2:
			changeTvBg(R.id.yd_ck_2);
			break;
		case R.id.yd_ck_3:
			changeTvBg(R.id.yd_ck_3);
			break;
		case R.id.yd_ck_4:
			changeTvBg(R.id.yd_ck_4);
			break;
		case R.id.yd_ck_5:
			changeTvBg(R.id.yd_ck_5);
			break;
		case R.id.yd_ck_6:
			changeTvBg(R.id.yd_ck_6);
			break;
		case R.id.yd_recharge_sure:
			GetOrder();
			break;
		case R.id.title_back:
			finish();
			break;
		default:
			break;
		}
	}
	
	private void setinfo() {
		yd_recharge_name = (TextView) findViewById(R.id.yd_recharge_name);
		yd_recharge_beans = (TextView) findViewById(R.id.yd_recharge_beans);
		String nickname = preferences.getString(APP.NICKNAME, "");
		String money = preferences.getString(APP.BEANS, "");
		yd_recharge_name.setText(nickname);
		yd_recharge_beans.setText(money);
	}

	/**
	 * 支付宝充值
	 */
	private void GetOrder() {
		dialog.show();
		GameBilling.getInstance().getOrderID(context,handler,user_id,"2",room_id) ;
	
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HandlerCmd.HandlerCmd_Get3rdOrderId:
				dialog.dismiss();
				String yd_order = msg.obj.toString();
				/**
				 * 商品购买接口。
				 */
//				GameBilling.sendBilling(YD_chongzhi.this, user_id, user_name,
//						yd_order, "111", "移动充值", "2");

				break;
			case HandlerCmd.HandlerCmd_Get3rdOrderIdError:
				dialog.dismiss();
				Utils.showToast(context, "获取订单号异常！");
				break;

			default:
				break;
			}
		};
	};
}
