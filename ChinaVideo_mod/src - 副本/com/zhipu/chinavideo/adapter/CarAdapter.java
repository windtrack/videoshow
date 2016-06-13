package com.zhipu.chinavideo.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.MyCarActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.Car;
import com.zhipu.chinavideo.util.APP;

public class CarAdapter extends BaseAdapter {
	private int location = -1;
	private Context context;
	private List<Car> cars;
	public static DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;

	public CarAdapter(Context context, List<Car> cars) {
		super();
		this.context = context;
		this.cars = cars;
	}

	public CarAdapter() {
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_car, null);
			holder.carlist_item_price = (TextView) convertView
					.findViewById(R.id.carlist_item_price);
			holder.carlist_item_name = (TextView) convertView
					.findViewById(R.id.carlist_item_name);
			holder.mylistcar_item_icon = (ImageView) convertView
					.findViewById(R.id.mylistcar_item_icon);
			holder.rl_name_price = (RelativeLayout) convertView
					.findViewById(R.id.rl_name_price);
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
		holder.carlist_item_price.setText(price + "/月");
		String str1 = APP.CAR_PATH1 + cars.get(position).getImage();
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.user_icon_default).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		mImageLoader.displayImage(str1, holder.mylistcar_item_icon, mOptions);
		holder.carlist_item_name.setText(cars.get(position).getName());
		if (location == position) {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.title_bg));
			holder.rl_name_price.setBackgroundColor(context.getResources()
					.getColor(R.color.title_bg));
			MyCarActivity.buycar_id = cars.get(position).getId();
			MyCarActivity.buycar_price = cars.get(position).getPrice();
		} else {
			convertView.setBackgroundColor(Color.WHITE);
			holder.rl_name_price.setBackgroundColor(context.getResources()
					.getColor(R.color.item_textcolor));
		}
		return convertView;
	}

	class ViewHolder {
		TextView carlist_item_price;
		TextView carlist_item_name;
		ImageView mylistcar_item_icon;
		RelativeLayout rl_name_price;
	}

	public void setLocation(int location) {
		this.location = location;
	}
}
