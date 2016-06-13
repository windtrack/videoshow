package com.zhipu.chinavideo.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.Gift;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GiftAdapter extends BaseAdapter {
	private List<Gift> gifts;
	private Context context;
	public DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;
	private int location = -1;
	private int type = 0;

	public GiftAdapter() {
		super();
	}

	public GiftAdapter(List<Gift> gifts, Context context, int type) {
		super();
		this.gifts = gifts;
		this.context = context;
		this.type = type;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return gifts.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return gifts.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	ViewHolder holder = null;

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.gift_item, null);
		
			holder = new ViewHolder();
			holder.gift_name = (TextView) convertView
					.findViewById(R.id.gift_name);
			holder.gift_price = (TextView) convertView
					.findViewById(R.id.gift_price);
			holder.gift_img = (ImageView) convertView
					.findViewById(R.id.gift_img);
			holder.canku_top_icon = (ImageView) convertView
					.findViewById(R.id.canku_top_icon);
			convertView.setTag(holder);
	
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
	
		
		if (type == 9) {
			holder.canku_top_icon.setVisibility(0);
		} else {
			holder.canku_top_icon.setVisibility(8);
		}
		holder.gift_name.setText(gifts.get(position).getName());

		String price = gifts.get(position).getPrice();
		if (!Utils.isEmpty(price)) {
			int p = Integer.parseInt(price);
			if (p > 10000) {
				holder.gift_price.setText((p / 10000) + "万");
			} else {
				holder.gift_price.setText(price);
			}
		}
		

		if (mOptions == null) {
			mOptions = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.icon_liwu_on).cacheInMemory()
					.cacheOnDisc().build();
			mImageLoader = ImageLoader.getInstance();
			mImageLoader.init(ImageLoaderConfiguration.createDefault(context));

		}
		String str1 = APP.GIFT_PATH + gifts.get(position).getIcon();
		mImageLoader.displayImage(str1, holder.gift_img, mOptions);

		if (location == position) {
			convertView.setBackgroundResource(R.drawable.gift_bg_shape);
		} else {
			convertView.setBackgroundColor(context.getResources().getColor(
					R.color.gifttab_bg));
		}
//		if (convertView == null) {
//			convertView = LayoutInflater.from(context).inflate(
//					R.layout.gift_item, null);
//			
//			
//			holder = new ViewHolder();
//			
//			holder.gift_name = (TextView) convertView
//					.findViewById(R.id.gift_name);
//			holder.gift_price = (TextView) convertView
//					.findViewById(R.id.gift_price);
//			holder.gift_img = (ImageView) convertView
//					.findViewById(R.id.gift_img);
//			holder.canku_top_icon = (ImageView) convertView
//					.findViewById(R.id.canku_top_icon);
//			convertView.setTag(holder);
//			
//			if (type == 9) {
//				holder.canku_top_icon.setVisibility(0);
//			} else {
//				holder.canku_top_icon.setVisibility(8);
//			}
//			holder.gift_name.setText(gifts.get(position).getName());
//			
//			String price = gifts.get(position).getPrice();
//			if (!Utils.isEmpty(price)) {
//				int p = Integer.parseInt(price);
//				if (p > 10000) {
//					holder.gift_price.setText((p / 10000) + "万");
//				} else {
//					holder.gift_price.setText(price);
//				}
//			}
//		} else {
//			holder = (ViewHolder) convertView.getTag();
//		}
//		
//		if (mOptions == null) {
//			mOptions = new DisplayImageOptions.Builder()
//			.showStubImage(R.drawable.icon_liwu_on).cacheInMemory()
//			.cacheOnDisc().build();
//			mImageLoader = ImageLoader.getInstance();
//			mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
//			
//		}
//		String str1 = APP.GIFT_PATH + gifts.get(position).getIcon();
//		mImageLoader.displayImage(str1, holder.gift_img, mOptions);
//		
//		if (location == position) {
//			convertView.setBackgroundResource(R.drawable.gift_bg_shape);
//		} else {
//			convertView.setBackgroundColor(context.getResources().getColor(
//					R.color.gifttab_bg));
//		}
		return convertView;
	}

	class ViewHolder {
		TextView gift_name;
		TextView gift_price;
		ImageView gift_img;
		ImageView canku_top_icon;
	}

	class ViewHolder1 {
		TextView gift_name1;
		TextView gift_price1;
		ImageView gift_img1;
		TextView num;
	}

	public void setLocation(int location) {
		this.location = location;
	}
}
