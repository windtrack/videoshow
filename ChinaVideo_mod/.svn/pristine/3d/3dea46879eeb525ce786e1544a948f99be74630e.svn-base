package com.zhipu.chinavideo.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zhipu.chinavideo.LiveRoomActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.entity.ChatMessage;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.Utils;

public class ChatGiftAdapter extends BaseAdapter {
	private Context context;
	private List<ChatMessage> messages = new ArrayList<ChatMessage>();
	public DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;
	public static Animation push_right_in;
	private Map<Integer,Boolean>map;
	private int[] image;
	

	public ChatGiftAdapter(Context context, List<ChatMessage> messages ) {
		super();
		this.context = context;
		this.messages = messages;
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading_img).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		push_right_in = AnimationUtils.loadAnimation(context, R.anim.push_right_in);
		map = new HashMap<Integer, Boolean>();
		image = new int[]{R.drawable.user_gift_bg,R.drawable.user_gift_bg2,R.drawable.user_gift_bg3}; 
	}

	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return messages.get(position);
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
					R.layout.chat_message_item4, null);
			holder.chat_gift_say = (TextView) convertView
					.findViewById(R.id.chat_gift_say);
			holder.chat_gift_icon = (ImageView) convertView
					.findViewById(R.id.chat_gift_icon);
			holder.chat_gift_num = (TextView) convertView
					.findViewById(R.id.chat_gift_num);
			holder.chat_gift_bg =  (LinearLayout) 
					convertView.findViewById(R.id.chat_gift_bg);
			convertView.setTag(holder);
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (!Utils.isEmpty(messages.get(position).getSay())) {
			holder.chat_gift_say.setText(messages.get(position).getSname());
			String str1 = APP.GIFT_PATH + messages.get(position).getIcon();
			mImageLoader.displayImage(str1, holder.chat_gift_icon, mOptions);
			holder.chat_gift_num.setText("X" + messages.get(position).getSay());
			Typeface typeFace = Typeface.createFromAsset(context.getAssets(),
					"Effra_Heavy_Italic.ttf");
			holder.chat_gift_num.setTypeface(typeFace);
		}
		if (map.get(position) == null || map.get(position)) {
			int index = (int)(Math.random()*3);
			Log.i("sumao", "index"+index);
			holder.chat_gift_bg.setBackgroundResource(image[index]);
			convertView.startAnimation(push_right_in);
			map.put(position, false);
		}
		
		return convertView;
	}

	class ViewHolder {
		TextView chat_gift_say;
		ImageView chat_gift_icon;
		TextView chat_gift_num;
		LinearLayout chat_gift_bg;
	}

	public void AppearAnimPosition(View view) {
		if(view!=null){
			view.startAnimation(push_right_in);
		}
	}
	public void clearMap(){
		if(map!=null){
			map.clear();
		}
	}
	

}
