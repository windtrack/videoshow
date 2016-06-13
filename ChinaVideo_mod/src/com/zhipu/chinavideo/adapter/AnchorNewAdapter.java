package com.zhipu.chinavideo.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhipu.chinavideo.LoginActivity;
import com.zhipu.chinavideo.MyPhotoAlbumActivity;
import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.db.GlobalData;
import com.zhipu.chinavideo.entity.AnchorNews;
import com.zhipu.chinavideo.entity.OnePhoto;
import com.zhipu.chinavideo.rpc.RpcEvent;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import ctv.sdk.Config;
import ctv.sdk.GameBilling;

/**
 * 主播离线动态适配器
 * @author sunjinfang
 *
 */
public class AnchorNewAdapter   extends ArrayAdapter<AnchorNews>implements OnClickListener {

	
	private DisplayImageOptions mOptions ;
	private DisplayImageOptions mOptionsheand ;
	private ImageLoader mImageLoader ;
	
	TextView m_tv_likeCount; //点赞数量
	ImageView likeView ;//点赞图片
	Context mContext ;
	String mAnchorID ;//主播id

	Map<String, Drawable> m_allshowMap;
	
	@SuppressLint("NewApi")
	public AnchorNewAdapter(Context context, int textViewResourceId, List<AnchorNews> pathList) {
		super(context, textViewResourceId, pathList);
		mContext = context ;
		mOptions = new DisplayImageOptions.Builder().showStubImage(R.drawable.empty_photo).cacheInMemory().cacheOnDisc().build();
		mOptionsheand = new DisplayImageOptions.Builder().showStubImage(R.drawable.icon).cacheInMemory().cacheOnDisc().build();
		
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		
		m_allshowMap = new HashMap<String, Drawable>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(R.layout.item_anchor_news, null);
		} else {
			view = convertView;
		}
		view.setTag(position);
		final AnchorNews news = getItem(position);
		ImageView headView = (ImageView)view.findViewById(R.id.imageView_head);
		ImageView photoView = (ImageView)view.findViewById(R.id.imageView_photo);
		photoView.setOnClickListener(this);
		likeView = (ImageView)view.findViewById(R.id.imgview_liked);
		likeView.setOnClickListener(this);
		
		final int id = position ;
		likeView.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				doLikePhot(id) ;
			}
		});
		
		
		
		m_tv_likeCount = (TextView)view.findViewById(R.id.textView_likedcount);
		headView.setImageResource(R.drawable.icon);
		mImageLoader.displayImage(news.getmHeadIconUrl(), headView, mOptionsheand,null);
		
		
		LinearLayout hasPhoto = (LinearLayout)view.findViewById(R.id.LinearLayout_hasPhoto);
		LinearLayout noPhoto = (LinearLayout)view.findViewById(R.id.LinearLayout_noPhoto);
		if(news.getmPhotoUrl().equals("null")){
			hasPhoto.setVisibility(View.GONE);
			noPhoto.setVisibility(View.VISIBLE);
			
		}else{
			
			hasPhoto.setVisibility(View.VISIBLE);
			noPhoto.setVisibility(View.GONE);
			mImageLoader.displayImage(news.getmPhotoUrl(), photoView, mOptions,null);
			updateLike(news) ;
		}
		
		
		return view;
	}
	
	
	public String getmAnchorID() {
		return mAnchorID;
	}

	public void setmAnchorID(String mAnchorID) {
		this.mAnchorID = mAnchorID;
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgview_liked:
			//点赞
//			doLikePhot(curPhoto) ;
			break;
		case R.id.imageView_photo:
		{
			//打开相册
			Intent in = new Intent(mContext, MyPhotoAlbumActivity.class);
			in.putExtra("anchorId", mAnchorID);
			mContext.startActivity(in);
		}
			break;

		default:
			break;
		}
	}
	
	private void doLikePhot(final int id){
		
		if(!GlobalData.getInstance(mContext).checkLogin()){
			if (Config.Use_3rd_LogIn) {
//				GameBilling.getInstance().Login(mContext, handler);
			} else {
				Intent intent = new Intent(mContext, LoginActivity.class);
				mContext.startActivity(intent);
			}
			return ;
		}
		
		final AnchorNews curPhoto = getItem(id);
		if(curPhoto!=null){
			if(curPhoto.ismHasLiked() ){
				return ;
			}
			
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						GlobalData gd = GlobalData.getInstance(mContext);
						String reslut = Utils.addRpcEvent(RpcEvent.CallLikePhoto,
								gd.getSharedPreferences()
										.getString(APP.USER_ID, ""), gd.getSharedPreferences()
										.getString(APP.SECRET, ""),mAnchorID, curPhoto.getmId());
						JSONObject obj = new JSONObject(reslut);

						int s = obj.getInt("s");
						
						if (s == 1) {
							Message msg = new Message() ;
							msg.arg1 = id;
							msg.what = M_Handler_DoLikeSuccess ;
							handler.sendMessage(msg);
						} else {
							handler.sendEmptyMessage(M_Handler_DoLikeFailed);
						}

					} catch (Exception e) {
						e.printStackTrace();
						handler.sendEmptyMessage(M_Handler_DoLikeFailed);
					}
					ThreadPoolWrap.getThreadPool().removeTask(this);
				}
			};
			ThreadPoolWrap.getThreadPool().executeTask(runnable);
			
			
		}
	}
	
	private static final int M_Handler_DoLikeSuccess = 1 ;
	private static final int M_Handler_DoLikeFailed = 2 ;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case M_Handler_DoLikeSuccess:
			{
				int id = msg.arg1 ;
				AnchorNews photo = getItem(id) ;
				if(photo.ismHasLiked()){
					photo.setmHasLiked(false);
					photo.setmLikeCount(photo.getmLikeCount()-1);
				}else{
					photo.setmHasLiked(true);;
					photo.setmLikeCount(photo.getmLikeCount()+1);
				}
				updateLike(photo) ;
			}
			
				break;
			case M_Handler_DoLikeFailed:
				
				break;
					
			default:
				break;
			}
		}
		
		
	} ;
	
	
	private void updateLike(AnchorNews photo){
		if(photo!=null){
			if(photo.ismHasLiked()){
				likeView.setImageResource(R.drawable.img_photo_dianzan_select);
			}else{
				likeView.setImageResource(R.drawable.img_photo_dianzan_news_normal);
			}
			m_tv_likeCount.setText(photo.getmLikeCount()+"");
			notifyDataSetChanged();
		}
	}
	
	
	/**
	 * 将处理的图片加入内存
	 * @param view
	 * @param url
	 * @param img
	 */
	private void addBitMapInWindow(View view,String url, Bitmap img){
		ImageView v = (ImageView)view;
		if(m_allshowMap.containsKey(url)){
			v.setImageDrawable(m_allshowMap.get(url));
		
		}else{
			Bitmap map = Utils.centerSquareScaleBitmap(img,v.getWidth(),v.getHeight());
			if(map!=null){
				Drawable drawable = new BitmapDrawable(map);
				m_allshowMap.put(url, drawable);
				v.setImageDrawable(m_allshowMap.get(url));
				
			}else{
				
			}
			notifyDataSetChanged();//防止被覆盖
		}
	}
	
	
}
