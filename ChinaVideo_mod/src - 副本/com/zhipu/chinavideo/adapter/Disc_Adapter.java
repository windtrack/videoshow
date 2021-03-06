package com.zhipu.chinavideo.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.FirstPayActivity;
import com.zhipu.chinavideo.HuoDongActivity;
import com.zhipu.chinavideo.HuoDongActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.adapter.SearchAdapter.ViewHolder;
import com.zhipu.chinavideo.entity.News;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Disc_Adapter extends BaseAdapter {
	private List<News> news;
	private Context context;
	public DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;

	public Disc_Adapter(List<News> news, Context context) {
		super();
		this.news = news;
		this.context = context;
	}

	public Disc_Adapter() {
		super();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return news.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return news.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.disc_item, null);
			holder.icon = (ImageView) convertView
					.findViewById(R.id.disc_item_icon);
			holder.title = (TextView) convertView
					.findViewById(R.id.disc_item_title);
			// holder.description = (TextView) convertView
			// .findViewById(R.id.disc_item_description);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String str1 = APP.DISC_PATH + news.get(position).getIcon();
//		Log.i("lvjian", "图片地址：" + str1);
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.discovery).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		mImageLoader.displayImage(str1, holder.icon, mOptions);
		holder.title.setText(news.get(position).getTitle());
		// holder.description.setText(news.get(position).getDescription());
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if ("喜迎双十一狂欢盛典".equals(news.get(position).getTitle())) {
					// Intent intent1 = new Intent(context,
					// DoubleHuoDongActivity.class);
					// context.startActivity(intent1);
				} else if(news.get(position).getAct().equals("100")){
					Intent intent = new Intent(context, FirstPayActivity.class);
					context.startActivity(intent);
				}else {
					Intent intent = new Intent(context, HuoDongActivity.class);
					intent.putExtra("url", news.get(position).getUrl());
					intent.putExtra("title", news.get(position).getTitle());
					context.startActivity(intent);
				}
			}
		});
		return convertView;
	}

	class ViewHolder {
		ImageView icon;
		TextView title;
		TextView description;

	}

}
