package com.zhipu.chinavideo;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.common.SysOSUtil;
import com.google.gson.JsonArray;
import com.zhipu.chinavideo.adapter.PhotoWallAdapter;
import com.zhipu.chinavideo.db.GlobalData;
import com.zhipu.chinavideo.entity.OnePhoto;
import com.zhipu.chinavideo.fragment.PersonalCenterFragment;
import com.zhipu.chinavideo.rechargecenter.RechargeActivity;
import com.zhipu.chinavideo.rpc.RpcEvent;
import com.zhipu.chinavideo.rpc.RpcRoutine;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.MyJSONRPCHttpClient;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.UploadUtil;
import com.zhipu.chinavideo.util.UploadUtil.OnUploadProcessListener;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


/**
 * 我的照片列表
 * 
 * @author sunjinfang
 * 
 */
public class MyPhotoAlbumActivity extends Activity implements OnClickListener,
		OnUploadProcessListener {
	private static final int IMAGE_REQUEST_CODE = 0;

	private static final int M_Handler_Upload = 99;
	private static final int M_Handler_Delete = 1;
	private static final int M_Handler_ToUpload = 3;
	private static final int M_Handler_UploadSuccess = 4;
	private static final int M_Handler_UploadFialed = 5;

	private static final int M_Handler_GetPhotoSuccess = 6;
	private static final int M_Handler_GetPhotoFailed = 7;
	private static final int M_Handler_DeletPhotoSuccess = 8;
	private static final int M_Handler_DeletPhotoFiled = 9;

	public Vector<OnePhoto> imageThumbUrls; // 临时的存储
	/**
	 * 用于展示照片墙的GridView
	 */
	private GridView mPhotoWall;
	/**
	 * GridView的适配器
	 */
	private PhotoWallAdapter adapter;
	private int mImageThumbSize;
	private int mImageThumbSpacing;

	private TextView tv_select;// 选择

	private LinearLayout mLineDelete; // 删除界面

	private boolean isMine; // 是否我的界面

	private boolean isSelect; // 是否选择删除

	public Context context;

	private String inputurl = "";
	private Dialog dialog;
	private TextView dialogTx;
	private TextView m_tv_title;
	private Uri photoUri;
	/** 获取到的图片路径 */
	private String picPath;
	private String picNewPath;

	public String anchorId;// 选中的主播id

	private GlobalData gd;// 全局数据

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photoalbum);
		gd = GlobalData.getInstance(MyPhotoAlbumActivity.this);
		context = this;
		Intent intent = this.getIntent();
		anchorId = intent.getStringExtra("anchorId");
		getPhotoWallList(anchorId, 1);
		isSelect = false;
		isMine = anchorId.equals(gd.getUid());

		m_tv_title = (TextView)findViewById(R.id.title_text);

		// 返回监听
		ImageView back = (ImageView)findViewById(R.id.title_back);
		back.setOnClickListener(this);

		// 选择按钮监听
		tv_select = (TextView)findViewById(R.id.title_select);
		tv_select.setOnClickListener(this);

		// 删除界面
		mLineDelete = (LinearLayout)findViewById(R.id.line_delete);
		mLineDelete.setOnClickListener(this);

		// 非自己的界面 或者不是 主播 隐藏
		if (isMine) {
			tv_select.setVisibility(View.VISIBLE);
			m_tv_title.setText("我的相册");
		}
		else {
			tv_select.setVisibility(View.GONE);
			m_tv_title.setText("主播相册");
		}

		showDelete(false);

		mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
		mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

		dialog = Utils.showProgressDialog(this, "正在上传", true);
		dialogTx = (TextView)dialog.findViewById(R.id.tipTextView);
		dialog.setCancelable(false);

		gd.mPhotoAblumImgPath.clear();

		if (isMine) {
			OnePhoto one = new OnePhoto();
			one.id = 999999;
			one.url = "upload";
			gd.mPhotoAblumImgPath.add(one);
		}
		imageThumbUrls = gd.mPhotoAblumImgPath;

		mPhotoWall = (GridView)findViewById(R.id.gridview_photo_wall);
		adapter = new PhotoWallAdapter(context, 0, imageThumbUrls, mPhotoWall);
		mPhotoWall.setAdapter(adapter);
		mPhotoWall.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						final int numColumns = (int)Math.floor(mPhotoWall.getWidth()
								/ (mImageThumbSize + mImageThumbSpacing));
						if (numColumns > 0) {
							int columnWidth = (mPhotoWall.getWidth() / numColumns)
									- mImageThumbSpacing;
							adapter.setItemHeight(columnWidth);
							mPhotoWall.getViewTreeObserver().removeGlobalOnLayoutListener(this);
						}
					}
				});

		mPhotoWall.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				if (isSelect) {
					adapter.setLocation(arg2);
					adapter.notifyDataSetChanged();
				}
				else {
					if (arg2 == adapter.getUploadIndex()) {// 上传

						invokePhoto();
					}
					else {
						Intent intent = new Intent(context, ImageDetailsActivity.class);
						intent.putExtra("image_position", arg2);
						intent.putExtra("anchorID", anchorId);
						intent.putExtra("isMine", anchorId.equals(gd.getUid()));
						startActivity(intent);
					}
				}
			}
		});

		mPhotoWall.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					isMoveing = true;
				}
				else {
					isMoveing = false;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
					int totalItemCount) {
				// TODO Auto-generated method stub
				int id = 0;
				System.out.println("");

				m_startIndex = firstVisibleItem;
				m_endIndex = visibleItemCount + firstVisibleItem;

				adapter.cleanBitmaoOutOfWindow(m_startIndex, m_endIndex);
			}
		});

	}

	private boolean isMoveing;
	private int m_startIndex;
	private int m_endIndex;

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		adapter.doclean();
		imageThumbUrls.clear();
		setContentView(R.layout.activity_null);
		imageThumbUrls = null;
		mPhotoWall = null;
		adapter.clear();
		adapter = null;
		tv_select = null;
		mLineDelete = null;
		dialog = null;
		gd.mPhotoAblumImgPath.clear();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.title_back:
				finish();
				break;
			case R.id.title_select:

				isSelect = !isSelect;
				doSelect();

				break;
			case R.id.line_delete:
				if (adapter.canBeDelete()) {
					showDeleteTips();
				}
				break;
			default:
				break;
		}
	}

	public void doSelect() {

		if (!isSelect) {
			adapter.cleanSelect();
		}
		tv_select.setText(isSelect ? "取消" : "选择");
		showDelete(isSelect);
		adapter.setSelecting(isSelect);
		adapter.notifyDataSetChanged();
	}

	private void showDelete(boolean flag) {
		mLineDelete.setVisibility(flag ? View.VISIBLE : View.GONE);
		TranslateAnimation animation;
		if (flag) {
			animation = new TranslateAnimation(0.0f, 0, mLineDelete.getHeight() + 100, 0);
		}
		else {
			animation = new TranslateAnimation(0.0f, 0, 0, mLineDelete.getHeight());
		}
		mLineDelete.startAnimation(animation);
		animation.setDuration(200);
		animation.setRepeatCount(0);
		animation.setInterpolator(new AccelerateInterpolator());
		mLineDelete.startAnimation(animation);

	}

	private void doDelete() {
		adapter.doDelete();
		handle.sendEmptyMessage(M_Handler_Delete);
	}

	// 重置选择界面
	private void reSetAdapter() {
		isSelect = !isSelect;
		doSelect();
		imageThumbUrls = gd.mPhotoAblumImgPath;
		adapter.notifyDataSetChanged();
	}

	// private static final int M_Handler_Upload = 99 ;

	Handler handle = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);

			switch (msg.what) {
				case M_Handler_Upload: {
					int pro = (int)msg.arg1;
					String str = "正在上传" + pro + "%";
					dialogTx.setText(str);
				}
					break;
				case M_Handler_Delete:
					reSetAdapter();
					break;
				case M_Handler_ToUpload:
					Thread t1 = new Thread(new Runnable() {
						@Override
						public void run() {
							uploadFile(picNewPath);
						};
					});
					t1.start();
					break;
				case M_Handler_UploadSuccess: {

					File f = new File(picNewPath);
					if (f.exists()) {
						f.delete();
					}

					dialog.hide();
					Utils.showToast(MyPhotoAlbumActivity.this, "上传成功！");
					OnePhoto photoadd = (OnePhoto)msg.obj;
					gd.mPhotoAblumImgPath.add(photoadd);
					imageThumbUrls = gd.mPhotoAblumImgPath;
					adapter.notifyDataSetChanged();
				}

					break;
				case M_Handler_UploadFialed:
					dialog.hide();
					Utils.showToast(MyPhotoAlbumActivity.this, "上传失败！");
					break;
				case M_Handler_GetPhotoSuccess: {
					initViewData();
					// Utils.showToast(MyPhotoAlbumActivity.this, "获取照片墙成功！");
				}
					break;
				case M_Handler_GetPhotoFailed: {
					initViewData();
					// Utils.showToast(MyPhotoAlbumActivity.this, "获取照片失败！");
				}
					break;
				case M_Handler_DeletPhotoSuccess: {
					// Utils.showToast(MyPhotoAlbumActivity.this, "删除照片成功！");
				}
					break;
				case M_Handler_DeletPhotoFiled: {
					Utils.showToast(MyPhotoAlbumActivity.this, "删除照片失败！");
				}
					break;
				default:
					break;
			}
		}

	};

	private void initViewData() {
		imageThumbUrls = gd.mPhotoAblumImgPath;
		adapter.notifyDataSetChanged();
	}

	/**
	 * 上传图片
	 */
	protected void invokePhoto() {

		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, IMAGE_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			doPhoto(requestCode, data);
		}

	}

	public void saveMyBitmap(Bitmap mBitmap) {

		picNewPath = "/sdcard/ctv_upload_" + System.currentTimeMillis() + ".jpg";
		File f = new File(picNewPath);
		if (f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 25, fOut);
		try {
			fOut.flush();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		try {

			fOut.close();

			String openid = gd.getSharedPreferences().getString(APP.OPEN_ID, "");
			String openkey = gd.getSharedPreferences().getString(APP.OPENKEY, "");
			String timestamp = gd.getSharedPreferences().getString(APP.TIMESTAMP, "");
			inputurl = "http://picture.0058.com/photo.php?openid=" + openid + "&openkey=" + openkey
					+ "&timestamp=" + timestamp;

			String fileKey = "Filedata";
			UploadUtil uploadUtil = UploadUtil.getInstance();
			uploadUtil.setOnUploadProcessListener(this); // 设置监听器监听上传状态
			Map<String, String> params2 = new HashMap<String, String>();
			params2.put("name", "45454");
			uploadUtil.uploadFile(picNewPath, fileKey, inputurl, params2);

		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* 上传文件至Server的方法 */
	private void uploadFile(String uploadFile) {

		HttpURLConnection con = null;
		try {
			String openid = gd.getSharedPreferences().getString(APP.OPEN_ID, "");
			String openkey = gd.getSharedPreferences().getString(APP.OPENKEY, "");
			String timestamp = gd.getSharedPreferences().getString(APP.TIMESTAMP, "");
			inputurl = "http://picture.0058.com/photo.php?openid=" + openid + "&openkey=" + openkey
					+ "&timestamp=" + timestamp;

			URL url = new URL(inputurl);
			con = (HttpURLConnection)url.openConnection();
			// con.disconnect();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "application/octet-stream");
			/* 设置DataOutputStream */

			File file = new File(uploadFile);
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			FileInputStream fStream = new FileInputStream(uploadFile);
			long maxpsize = file.length();
			long curSize = 0;
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */

			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				curSize += length;
				ds.write(buffer, 0, length);
				// Message msg = new Message();
				// msg.arg1 = (int) (curSize*100/maxpsize);
				// msg.what = 99;
				// handle.sendMessage(msg);
			}
			/* close streams */
			fStream.close();

			ds.flush();
			ds.close();
			/* 取得Response内容 */
			con.connect();
			int res = con.getResponseCode();
			if (res == 200) {
				InputStream is = con.getInputStream();
				int ch;
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char)ch);
				}
				Message msg = new Message();
				String result = b.toString();
				String icon = result.substring((result.indexOf("|") + 1), result.length());
				msg.obj = icon;
				msg.what = M_Handler_UploadSuccess;
				handle.sendMessage(msg);
			}
			else {
				handle.sendEmptyMessage(M_Handler_UploadFialed);
			}
			// con.disconnect();
		}
		catch (Exception e) {
			if (con != null) {
				con.disconnect();
			}
			handle.sendEmptyMessage(M_Handler_UploadFialed);
		}
	}

	/**
	 * 选择图片后，获取图片的路径
	 * 
	 * @param requestCode
	 * @param data
	 */
	private void doPhoto(int requestCode, Intent data) {
		if (requestCode == IMAGE_REQUEST_CODE) // 从相册取图片，有些手机有异常情况，请注意
		{
			if (data == null) {
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
			photoUri = data.getData();
			if (photoUri == null) {
				Toast.makeText(this, "选择图片文件出错", Toast.LENGTH_LONG).show();
				return;
			}
		}

		Uri localUri = null;
		if ((data != null) && (data.getData() != null)) {
			localUri = data.getData();
		}
		// 一些机型无法从getData中获取uri，则需手动指定拍照后存储照片的Uri
		if (localUri == null) {
			if (photoUri != null) {
				localUri = photoUri;
			}
		}
		if (localUri != null) {
			// startCrop(localUri);
		}

		String[] pojo = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(photoUri, pojo, null, null, null);

		if (cursor != null) {
			int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
			cursor.moveToFirst();
			picPath = cursor.getString(columnIndex);
			try {
				// 4.0以上的版本会自动关闭 (4.0--14;; 4.0.3--15)
				if (Integer.parseInt(Build.VERSION.SDK) < 14) {
					cursor.close();
				}
			}
			catch (Exception e) {
			}
		}

		// Log.i(TAG, "imagePath = "+picPath);
		if (picPath != null
				&& (picPath.endsWith(".png") || picPath.endsWith(".PNG")
						|| picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
			dialog.show();
			Log.i("sjf", "path== " + picPath);
			Bitmap bm = BitmapFactory.decodeFile(picPath);

			saveMyBitmap(bm);

		}
		else {
			Toast.makeText(this, "选择图片文件不正确", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onUploadDone(int responseCode, String message) {

		Message msg = new Message();
		if (responseCode == UploadUtil.UPLOAD_SUCCESS_CODE) {

			try {
				JSONObject jb = new JSONObject(message);// 转换为JSONObject

				int id = jb.getInt("id");
				String path = jb.getString("url");
				String urlPath = jb.getString("name");
				OnePhoto photoadd = new OnePhoto();
				photoadd.id = id;
				photoadd.url = APP.POST_URL_HEAD + path + "big_" + urlPath;

				msg.what = M_Handler_UploadSuccess;
				msg.obj = photoadd;
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		else {
			msg.what = M_Handler_UploadFialed;
		}

		handle.sendMessage(msg);

	}

	@Override
	public void onUploadProcess(int uploadSize) {
		// TODO Auto-generated method stub
	}

	@Override
	public void initUpload(int fileSize) {
		// TODO Auto-generated method stub

	}

	public void getPhotoWallList(final String anchorid, final int pageid) {

		Runnable runnable = new Runnable() {
			public void run() {
				try {
					String msg = Utils.addRpcEvent(RpcEvent.GetPhotoWall, gd.getSharedPreferences()
							.getString(APP.USER_ID, ""),
							gd.getSharedPreferences().getString(APP.SECRET, ""), anchorid, 1);
					JSONObject obj = new JSONObject(msg);

					int s = obj.getInt("s");
					gd.mPhotoAblumImgPath.clear();

					if (isMine) {
						OnePhoto one = new OnePhoto();
						one.id = 999999;
						one.url = "upload";
						gd.mPhotoAblumImgPath.add(one);
					}

					if (s == 1) {
						JSONObject data = obj.getJSONObject("data");
						String path = data.getString("image_url");
						int count = data.getInt("count");

						JSONArray photoData = data.getJSONArray("photos");

						for (int i = 1; i <= photoData.length(); i++) {

							JSONObject photoInfo = photoData.getJSONObject(i - 1);
							OnePhoto onephoto = new OnePhoto();
							String urlPath = photoInfo.getString("uri");
							onephoto.url = APP.POST_URL_HEAD + path + "big_" + urlPath;
							onephoto.id = photoInfo.getInt("id");
							if (photoInfo.has("liked")) {
								if ((photoInfo.getInt("liked") == 1)) {
									onephoto.isLike = true;
								}
								else {
									onephoto.isLike = false;
								}
								onephoto.likeCount = photoInfo.getInt("like_num");
							}
							gd.mPhotoAblumImgPath.add(onephoto);
						}

						handle.sendEmptyMessage(M_Handler_GetPhotoSuccess);
					}
					else {
						handle.sendEmptyMessage(M_Handler_GetPhotoFailed);
					}

				}
				catch (Exception e) {
					e.printStackTrace();
					handle.sendEmptyMessage(M_Handler_GetPhotoFailed);
				}
				ThreadPoolWrap.getThreadPool().removeTask(this);
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(runnable);

	}

	public void callDeletePhoto(final int photoid) {
		Runnable runnable = new Runnable() {
			public void run() {
				try {
					String msg = Utils.addRpcEvent(RpcEvent.CallDeletePhoto, gd
							.getSharedPreferences().getString(APP.USER_ID, ""), gd
							.getSharedPreferences().getString(APP.SECRET, ""), photoid);
					JSONObject obj = new JSONObject(msg);

					int s = obj.getInt("s");

					if (s == 1) {

						handle.sendEmptyMessage(M_Handler_DeletPhotoSuccess);
					}
					else {
						handle.sendEmptyMessage(M_Handler_DeletPhotoFiled);
					}

				}
				catch (Exception e) {
					e.printStackTrace();
					handle.sendEmptyMessage(M_Handler_DeletPhotoFiled);
				}
				ThreadPoolWrap.getThreadPool().removeTask(this);
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(runnable);
	}

	private void showDeleteTips() {

		final AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
		localAlertDialog.show();
		Window localWindow = localAlertDialog.getWindow();
		View localView = getLayoutInflater().inflate(R.layout.dialog_deletphoto, null);

		LinearLayout queding = (LinearLayout)localView.findViewById(R.id.layout_sure);
		queding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				doDelete();
				localAlertDialog.dismiss();
			}
		});

		LinearLayout close = (LinearLayout)localView.findViewById(R.id.layout_cancel);
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				localAlertDialog.dismiss();
				isSelect = false;
				doSelect();
			}
		});

		localAlertDialog.setCancelable(false);
		localWindow.setContentView(localView);
	}

}