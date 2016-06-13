package com.zhipu.chinavideo.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.Car;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ConsumpUtil;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCarAdapter extends BaseAdapter {
	private Context context;
	private List<Car> cars;
	private String user_id;
	private String secret;
	private Handler handler;
	public static DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;
	private Dialog dialog;
	private SharedPreferences sharedPreferences;

	public MyCarAdapter(Context context, List<Car> cars, String user_id,
			String secret, Handler handler, Dialog dialog) {
		super();
		this.context = context;
		this.cars = cars;
		this.user_id = user_id;
		this.secret = secret;
		this.handler = handler;
		this.dialog = dialog;
		sharedPreferences = context.getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
	}

	public MyCarAdapter() {
		super();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cars.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cars.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_mycar, null);
			holder.mycar_item_tv1 = (TextView) convertView
					.findViewById(R.id.mycar_item_tv1);
			holder.mycar_item_tv3 = (TextView) convertView
					.findViewById(R.id.mycar_item_tv3);
			holder.mycar_item_icon = (ImageView) convertView
					.findViewById(R.id.mycar_item_icon);
			holder.end_time = (TextView) convertView
					.findViewById(R.id.end_time);
			holder.mycar_guoqi = (ImageView) convertView
					.findViewById(R.id.mycar_guoqi_lala);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int p = Integer.parseInt(cars.get(position).getPrice());
		String price = "";
		if (p > 10000) {
			price = (p / 10000) + "万";
		} else {
			price = cars.get(position).getPrice();
		}
		holder.mycar_item_tv1.setText("价格：" + price + "/月");
		String str1 = APP.CAR_PATH + cars.get(position).getImage();
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading_img).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		mImageLoader.displayImage(str1, holder.mycar_item_icon, mOptions);
		final String status=cars.get(position).getStatus();
		holder.mycar_item_tv3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if("2".equals(status)){
					//点击使用
					ChangeTheCar(cars.get(position).getId());
				}else{
					// 续费
					String beans = sharedPreferences.getString(APP.BEANS, "");
					if (!Utils.isEmpty(beans)&& !Utils.isEmpty(cars.get(position).getPrice())) {
						if(ConsumpUtil.compare(beans, String.valueOf(cars.get(position).getPrice()))){
//						if (Integer.parseInt(beans) > Integer.parseInt(cars.get(position).getPrice())) {
							BuyCar(cars.get(position).getId());
						} else {
							Utils.Moneynotenough(context, "余额不足！","");
						}
					}
				}
			}
		});

		holder.mycar_guoqi.setVisibility(0);
		if("0".equals(status)){
			holder.mycar_guoqi.setImageResource(R.drawable.yiguoqi);
			holder.end_time.setText("已过期");
			holder.mycar_item_tv3.setText("续费");
			holder.mycar_item_tv3.setBackgroundColor(context.getResources().getColor(R.color.second));
		}else if("1".equals(status)){
			holder.end_time.setText("还剩"+cars.get(position).getRemain_days()+"天");
			holder.mycar_guoqi.setImageResource(R.drawable.zhengzaishiyong);
			holder.mycar_item_tv3.setText("续费");
			holder.mycar_item_tv3.setBackgroundColor(context.getResources().getColor(R.color.second));
		}else {
			holder.mycar_item_tv3.setText("点击使用");
			holder.mycar_guoqi.setImageResource(R.drawable.weishiyong);
			holder.end_time.setText("还剩"+cars.get(position).getRemain_days()+"天");
			holder.mycar_item_tv3.setBackgroundColor(context.getResources().getColor(R.color.title_bg));
		}
		
//		if ("0".equals(cars.get(position).getStatus())) {
//			holder.mycar_guoqi.setVisibility(0);
//		} else {
//			holder.mycar_guoqi.setVisibility(8);
//		}
		return convertView;
	}

	class ViewHolder {
		TextView mycar_item_tv1;
		TextView mycar_item_tv3;
		ImageView mycar_item_icon;
		TextView end_time;
		ImageView mycar_guoqi;
	}

	/**
	 * 购买座驾接口
	 */
	private void BuyCar(final String mount_id) {
		dialog.show();
		// TODO Auto-generated method stub
		Runnable buycarrun = new Runnable() {
			public void run() {
				String s = Utils.BuyTheCar(user_id, secret, mount_id);
//				Log.i("lvjian", "s---购买座驾----------------》" + s);
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
	 * 切换座驾
	 */
	private void ChangeTheCar(final String mount_id) {
		dialog.show();
		// TODO Auto-generated method stub
		Runnable buycarrun = new Runnable() {
			public void run() {
				String s = Utils.ChangeCar(user_id, secret, mount_id);
//				Log.i("lvjian", "s---切换座驾----------------》" + s);
				try {
					JSONObject obj = new JSONObject(s);
					int a = obj.getInt("s");
					if (a == 1) {
						handler.sendEmptyMessage(7);
					} else {
						Message msg = new Message();
						msg.what = 8;
						msg.obj = "切换失败";
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 8;
					msg.obj = "接口异常！";
					handler.sendMessage(msg);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(buycarrun);

	}
}
