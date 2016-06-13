package com.zhipu.chinavideo.adapter;

import java.util.List;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.AnchorCenterActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.AnchorInfo;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;
import com.zhipu.chinavideo.util.MyJSONRPCHttpClient;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;


/**
 * 作用：我的关注的适配器 作者：unfind 时间：2016年5月24日09:12:21
 */
public class MyAnchorAdapter extends BaseAdapter {

	private Context context;
	private List<AnchorInfo> list;
	public DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;

	private int SCREEN_WIDTH = 0;
	private int state = 1;
	private SharedPreferences sharedPreferences;
	private Handler handler;

	public MyAnchorAdapter(Context context, List<AnchorInfo> list, int state) {
		super();
		this.context = context;
		this.list = list;
		SCREEN_WIDTH = context.getResources().getDisplayMetrics().widthPixels;
		this.state = state;
		sharedPreferences = context.getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
		handler = new Handler();

	}

	public void setmOptions(DisplayImageOptions mOptions) {
		this.mOptions = mOptions;
	}

	public void setmImageLoader(ImageLoader mImageLoader) {
		this.mImageLoader = mImageLoader;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.my_anchor_list_item, null);
			holder.cir = (CircularImage)convertView.findViewById(R.id.anchor_icon);
			holder.name = (TextView)convertView.findViewById(R.id.anchor_name);
			holder.isplay = (ImageView)convertView.findViewById(R.id.isplay);
			holder.leftCon = (LinearLayout)convertView.findViewById(R.id.left_content_con);
			holder.delBtn = (Button)convertView.findViewById(R.id.cancle_btn);
			holder.scrollView = (HorizontalScrollView)convertView
					.findViewById(R.id.all_content_con);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder)convertView.getTag();
		}
		if (list.size() == 0) {
			return convertView;
		}
		if (list.get(position).getStatus().equals("1")) {
			holder.isplay.setVisibility(0);
		}
		else {
			holder.isplay.setVisibility(8);
		}
		holder.name.setText(list.get(position).getNickname());
		String str1 = APP.USER_LOGO_ROOT + list.get(position).getIcon();
		mOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.loading_img)
				.cacheInMemory().cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		mImageLoader.displayImage(str1, holder.cir, mOptions);

		LayoutParams lp = holder.leftCon.getLayoutParams();
		lp.width = SCREEN_WIDTH;

		holder.leftCon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(context, AnchorCenterActivity.class);
				in.putExtra("id", list.get(position).getAnchor_id());
				context.startActivity(in);
			}
		});
		holder.scrollView.scrollTo(0, 0);
		if (state == 1) {
			holder.delBtn.setText("取消关注");
			holder.scrollView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return false;
				}
			});
		}
		else if (state == 0) {
			holder.delBtn.setText("取消守护");
			holder.scrollView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return true;
				}
			});
		}
		else if (state == 2) {
			holder.delBtn.setText("取消订阅");
			holder.scrollView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return true;
				}
			});
		}
		else {
			holder.delBtn.setText("取消");
			holder.scrollView.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					return true;
				}
			});
		}

		holder.delBtn.setTag(position);
		holder.delBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int position = (Integer)v.getTag();
				cancle(position);
			}
		});

		return convertView;
	}

	// 取消关注
	private void cancle(final int position) {
		final AnchorInfo anchor = list.get(position);
		if (state == 0) {
			handler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, "操作成功", Toast.LENGTH_LONG).show();
					list.remove(position);
					notifyDataSetChanged();
				}
			});
		}
		else if (state == 1) {// 取消关注
			Runnable cancle = new Runnable() {
				public void run() {
					try {
						String result = Utils.cancelfollow(
								sharedPreferences.getString(APP.USER_ID, ""),
								sharedPreferences.getString(APP.SECRET, ""), anchor.getAnchor_id());
						JSONObject obj = new JSONObject(result);
						int s = obj.getInt("s");
						if (s == 1) {
							handler.post(new Runnable() {

								@Override
								public void run() {
									Toast.makeText(context, "操作成功", Toast.LENGTH_LONG).show();
									list.remove(position);
									notifyDataSetChanged();
								}
							});
						}
						else {
							Toast.makeText(context, "操作失败", Toast.LENGTH_LONG).show();
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			ThreadPoolWrap.getThreadPool().executeTask(cancle);
		}
		else {

			handler.post(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, "操作成功", Toast.LENGTH_LONG).show();
					list.remove(position);
					notifyDataSetChanged();
				}
			});

		}
	}

	public void setRushData(List<AnchorInfo> list) {
		this.list = list;
		Log.i("sumao", "setRushData");
		notifyDataSetChanged();
	}

	class ViewHolder {
		CircularImage cir;
		TextView name;
		ImageView isplay;
		Button delBtn;// 取消按钮
		LinearLayout leftCon;// 左边内容区
		HorizontalScrollView scrollView;
	}

}
