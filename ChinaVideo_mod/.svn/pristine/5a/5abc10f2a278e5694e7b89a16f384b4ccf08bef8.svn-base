package com.zhipu.chinavideo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.adapter.CarAdapter;
import com.zhipu.chinavideo.adapter.MyCarAdapter;
import com.zhipu.chinavideo.entity.Car;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ConsumpUtil;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCarActivity extends Activity implements OnClickListener {
	private ImageView title_back;
	private TextView title_text;
	private SharedPreferences preferences;
	private String user_id = "";
	private String secret = "";
	private List<Car> cars;
	private ListView mycar_list;
	private MyCarAdapter adapter;
	private CarAdapter carlist_adapter;
	private GridView mycar_grid;
	private List<Car> car_list;
	private TextView mycar_tv;
	private TextView buy_car;
	private TextView more_car;
	public static String buycar_id;
	public static String buycar_price;
	private Dialog dialog;
	// 不显示进场动画
	private ImageView mycar_checkbox;
	private RelativeLayout nocar_rl;
	private String is_xs = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mycar);
		preferences = getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
		user_id = preferences.getString(APP.USER_ID, "");
		secret = preferences.getString(APP.SECRET, "");
		dialog = Utils.showProgressDialog(MyCarActivity.this, "进行中...", true);
		car_list = new ArrayList<Car>();
		initview();
		initmycar();
	}

	private void initmycar() {
		// TODO Auto-generated method stub
		cars = new ArrayList<Car>();
		Runnable getmycarrun = new Runnable() {
			@Override
			public void run() {
				try {
					String result = Utils.getmycar(user_id, secret);
					Log.i("lvjian", "我的座驾------------》" + result);
					JSONObject obj = new JSONObject(result);
					int a = obj.getInt("s");
					is_xs = obj.getString("ride_status");
					if (a == 1) {
						JSONArray data = obj.getJSONArray("data");
						Gson gson = new Gson();
						for (int i = 0; i < data.length(); i++) {
							Car car = new Car();
							car = gson.fromJson(data.getJSONObject(i)
									.toString(), Car.class);
							cars.add(car);
						}
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(2);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(2);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getmycarrun);
	}

	private void initview() {
		// TODO Auto-generated method stub
		title_back = (ImageView) findViewById(R.id.title_back);
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("我的座驾");
		title_back.setOnClickListener(this);
		mycar_list = (ListView) findViewById(R.id.mycar_list);
		mycar_grid = (GridView) findViewById(R.id.mycar_grid);
		mycar_checkbox = (ImageView) findViewById(R.id.mycar_checkbox);

		carlist_adapter = new CarAdapter(MyCarActivity.this, car_list);
		mycar_grid.setAdapter(carlist_adapter);
		mycar_tv = (TextView) findViewById(R.id.mycar_tv);
		buy_car = (TextView) findViewById(R.id.buy_car);
		more_car = (TextView) findViewById(R.id.more_car);
		nocar_rl = (RelativeLayout) findViewById(R.id.nocar_rl);
		more_car.setOnClickListener(this);
		buy_car.setOnClickListener(this);
		buycar_id = "";
		buycar_price = "";
		mycar_checkbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if ("1".equals(is_xs)) {
					nocarxianshi();
				} else {
					Utils.showToast(MyCarActivity.this, "请选择要使用的座驾");
				}

			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.buy_car:
			String beans = preferences.getString(APP.BEANS, "");
			if (Utils.isEmpty(buycar_id) || Utils.isEmpty(buycar_price)) {
				Utils.showToast(MyCarActivity.this, "请选择座驾");
			} else {
				
				
				if(ConsumpUtil.compare(beans, buycar_price)){
					BuyCar(buycar_id);
				} else {
					Utils.Moneynotenough(MyCarActivity.this, "余额不足！", "");
				}
			}
			break;
		case R.id.more_car:
			// 加载座驾列表
			title_text.setText("座驾商城");
			getcarlist();
			break;
		default:
			break;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (cars.size() > 0) {
					adapter = new MyCarAdapter(MyCarActivity.this, cars,
							user_id, secret, handler, dialog);
					mycar_list.setAdapter(adapter);
					mycar_list.setVisibility(0);
					nocar_rl.setVisibility(0);
					mycar_grid.setVisibility(8);
					more_car.setVisibility(0);
				} else {
					mycar_tv.setVisibility(0);
					buy_car.setVisibility(0);
					more_car.setVisibility(8);
					// 加载座驾列表
					getcarlist();
				}
				if ("1".equals(is_xs)) {
					mycar_checkbox.setImageResource(R.drawable.xuanzhexianshi);
				} else {
					mycar_checkbox.setImageResource(R.drawable.zuojiaduigou);
				}
				break;
			case 2:
				Utils.showToast(MyCarActivity.this, "获取我的座驾信息失败");
				break;
			case 3:
				dialog.dismiss();
				carlist_adapter.notifyDataSetChanged();
				mycar_list.setVisibility(8);
				nocar_rl.setVisibility(8);
				mycar_grid.setVisibility(0);
				more_car.setVisibility(8);
				buy_car.setVisibility(0);
				mycar_grid.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						carlist_adapter.setLocation(arg2);
						carlist_adapter.notifyDataSetChanged();
					}
				});
				break;
			case 4:
				dialog.dismiss();
				Utils.showToast(MyCarActivity.this, "获取座驾列表失败！");
				break;
			// 续费成功
			case 5:
				dialog.dismiss();
				Utils.showToast(MyCarActivity.this, "购买成功");
				// 续费成功后刷新一下界面
				initmycar();
				break;
			// 续费失败
			case 6:
				dialog.dismiss();
				String data = msg.obj.toString();
				Utils.showToast(MyCarActivity.this, data);
				break;
			case 7:
				dialog.dismiss();
				// 切换成功
				initmycar();
				mycar_checkbox.setImageResource(R.drawable.xuanzhexianshi);
				break;
			case 8:
				dialog.dismiss();
				String data1 = msg.obj.toString();
				Utils.showToast(MyCarActivity.this, data1);
				break;
			case 9:
				// 不显示进场动画成功
				is_xs = "0";
				initmycar();
				break;
			default:
				break;
			}
		};
	};

	private void getcarlist() {
		if (this.dialog != null) {
			this.dialog.show();
		}
		Runnable getcarlistrun = new Runnable() {
			public void run() {
				String s = Utils.getTheCarData();
				try {
					JSONObject obj = new JSONObject(s);
					int a = obj.getInt("s");
					if (a == 1) {
						JSONObject jb = obj.getJSONObject("data");
						JSONArray jo = jb.getJSONArray("data");
						Gson gson = new Gson();
						for (int i = 0; i < jo.length(); i++) {
							JSONObject json = jo.getJSONObject(i);
							car_list.add(gson.fromJson(json.toString(),
									Car.class));
						}
						handler.sendEmptyMessage(3);
					} else {
						handler.sendEmptyMessage(4);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					handler.sendEmptyMessage(4);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getcarlistrun);
	}

	/**
	 * 购买座驾接口
	 */
	private void BuyCar(final String mount_id) {
		dialog.show();
		Runnable buycarrun = new Runnable() {
			public void run() {
				String s = Utils.BuyTheCar(user_id, secret, mount_id);
				try {
					JSONObject obj = new JSONObject(s);
					int a = obj.getInt("s");
					if (a == 1) {
						handler.sendEmptyMessage(5);
					} else {
						String data = obj.getString("data");
						Message msg = new Message();
						msg.what = 6;
						msg.obj = data;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 6;
					msg.obj = "接口异常！";
					handler.sendMessage(msg);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(buycarrun);

	}

	/**
	 * 友盟统计
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 不显示进场动画
	 */
	private void nocarxianshi() {
		Runnable nocarrun = new Runnable() {
			public void run() {
				String s = Utils.nocar(user_id, secret);
				Log.i("lvjian", "-不显示进场动画------------->" + s);
				try {
					JSONObject obj = new JSONObject(s);
					int a = obj.getInt("s");
					if (a == 1) {
						handler.sendEmptyMessage(9);
					} else {

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(nocarrun);
	}
}
