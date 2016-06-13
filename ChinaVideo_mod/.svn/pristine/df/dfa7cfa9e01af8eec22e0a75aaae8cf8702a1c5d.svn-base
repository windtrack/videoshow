package com.zhipu.chinavideo.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.Guard;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GuardAdapter extends BaseAdapter {
	public static DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;
	private List<Guard> guards;
	private Context context;

	public GuardAdapter(List<Guard> guards, Context context) {
		super();
		this.guards = guards;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return guards.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return guards.get(position);
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
					R.layout.guard_item_layout, null);
			holder.guard_item_icon = (CircularImage) convertView
					.findViewById(R.id.guard_item_icon);
			holder.guard_item_name = (TextView) convertView
					.findViewById(R.id.guard_item_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.guard_item_name.setText(guards.get(position).getNickname());
		if("1".equals(guards.get(position).getOl())){
			holder.guard_item_name.setTextColor(context.getResources().getColor(R.color.guard_color_1));
		}else{
			holder.guard_item_name.setTextColor(context.getResources().getColor(R.color.guard_color_0));
		}
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading_img).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		String str = APP.USER_BIG_LOGO_ROOT + guards.get(position).getIcon();
		mImageLoader.displayImage(str, holder.guard_item_icon, mOptions);
		return convertView;
	}

	class ViewHolder {
		CircularImage guard_item_icon;
		TextView guard_item_name;

	}

}
