package com.zhipu.chinavideo;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.cityseleced.ProvinceListActivity;
import com.zhipu.chinavideo.fragment.PersonalCenterFragment;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyInformationActivity extends Activity implements OnClickListener {
	private Dialog dialog;
	private TextView title;
	private ImageView title_back;
	private SharedPreferences sharedPreferences;
	private RelativeLayout touxiang_rl;
	private CircularImage touxiang;
	public static ImageLoader mImageLoader = null;
	public static DisplayImageOptions mOptions;
	private TextView nicheng;
	private RelativeLayout nicheng_rl;
	private RelativeLayout xingbie_rl;
	private TextView xingbie;
	private String sex = "";
	private RelativeLayout diqu_rl;
	private TextView diqu;
	private ImageView cfdj;
	private ImageView cfdj2;
	private TextView cost_cha;
	private TextView recevel_cha;
	private ProgressBar cost_progressbar;
	private ProgressBar recevel_progressbar;
	private ImageView mxdj;
	private ImageView mxdj2;
	private RelativeLayout zhanghao_rl;
	private TextView zhanghao;
	private TextView id;
	private Uri photoUri;
	private static final int IMAGE_REQUEST_CODE = 0;
	private static final int CAMERA_REQUEST_CODE = 1;
	private static final int RESULT_REQUEST_CODE = 2;
	private String user_id;
	private String secret;
	private String diqu1 = "未知";
	private String inputurl = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinformation);

		sharedPreferences = getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
		user_id = sharedPreferences.getString(APP.USER_ID, "");
		secret = sharedPreferences.getString(APP.SECRET, "");
		title = (TextView) findViewById(R.id.title_text);
		title.setText("我的资料");
		title_back = (ImageView) findViewById(R.id.title_back);
		title_back.setOnClickListener(this);
		touxiang_rl = (RelativeLayout) findViewById(R.id.touxiang_rl);
		touxiang_rl.setOnClickListener(this);
		touxiang = (CircularImage) findViewById(R.id.touxiang);
		ImageLoaderConfiguration localImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(
				this).threadPoolSize(1).memoryCache(new WeakMemoryCache())
				.build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(localImageLoaderConfiguration);
		mOptions = new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565)
				.showStubImage(R.drawable.loading_img)
				.showImageForEmptyUri(R.drawable.loading_img)
				.showImageOnFail(R.drawable.loading_img)
				.showImageOnLoading(R.drawable.loading_img).cacheOnDisc()
				.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		mImageLoader.displayImage(
				sharedPreferences.getString(APP.USER_ICON, ""), touxiang,
				mOptions, null);
		nicheng_rl = (RelativeLayout) findViewById(R.id.nicheng_rl);
		nicheng_rl.setOnClickListener(this);
		nicheng = (TextView) findViewById(R.id.nicheng);
		nicheng.setText(sharedPreferences.getString(APP.NICKNAME, ""));
		xingbie_rl = (RelativeLayout) findViewById(R.id.xingbie_rl);
		xingbie_rl.setOnClickListener(this);
		xingbie = (TextView) findViewById(R.id.xingbie);
		if (sharedPreferences.getString(APP.GENDER, "").equals("0")) {
			sex = "男";
		} else if (sharedPreferences.getString(APP.GENDER, "").equals("1")) {
			sex = "女";
		} else {
			sex = "未知";
		}
		xingbie.setText(sex);
		diqu_rl = (RelativeLayout) findViewById(R.id.diqu_rl);
		diqu_rl.setOnClickListener(this);
		diqu = (TextView) findViewById(R.id.diqu);
		diqu.setText(sharedPreferences.getString(APP.POS, ""));
		zhanghao = (TextView) findViewById(R.id.zhanghao);
		zhanghao.setText(sharedPreferences.getString(APP.USER, ""));
		cfdj = (ImageView) findViewById(R.id.cfdj);
		cfdj2 = (ImageView) findViewById(R.id.cfdj2);
		cost_cha = (TextView) findViewById(R.id.cost_cha);
		recevel_cha = (TextView) findViewById(R.id.recevel_cha);
		cost_progressbar = (ProgressBar) findViewById(R.id.cost_progressbar);
		recevel_progressbar = (ProgressBar) findViewById(R.id.recevel_progressbar);
		mxdj = (ImageView) findViewById(R.id.mxdj);
		mxdj2 = (ImageView) findViewById(R.id.mxdj2);
		if (!Utils.isEmpty(sharedPreferences.getString(APP.USER_RLEVEL, ""))) {
			APP.setReceived_level(
					sharedPreferences.getString(APP.USER_RLEVEL, ""), mxdj,
					MyInformationActivity.this);
			String r = sharedPreferences.getString(APP.USER_RLEVEL, "");
			String rr = (Integer.parseInt(r) + 1) + "";
			APP.setReceived_level(rr, mxdj2, MyInformationActivity.this);
			Long recevelbeans = Long.parseLong(sharedPreferences.getString(
					APP.RECEIVED_BEANS, ""));
			Long currrecevel = APP.parsereceivenum(rr);
			Long frontrecevel = APP.parsereceivenum(r);
			int b = (int) (((recevelbeans - frontrecevel) * 100) / (currrecevel - frontrecevel));
			recevel_progressbar.setMax(100);
			recevel_progressbar.setProgress(b);
			String tt = (currrecevel - recevelbeans) + "";
			recevel_cha.setText("还差" + tt + "升级");
		}

		if (!Utils.isEmpty(sharedPreferences.getString(APP.USER_CLEVEL, ""))) {
			APP.setCost_level(sharedPreferences.getString(APP.USER_CLEVEL, ""),
					cfdj, MyInformationActivity.this);
			String s = sharedPreferences.getString(APP.USER_CLEVEL, "");
			String ss = (Integer.parseInt(s) + 1) + "";
			APP.setCost_level(ss, cfdj2, MyInformationActivity.this);
			cost_progressbar.setMax(100);
			Long costbeans = Long.parseLong(sharedPreferences.getString(
					APP.COST_BEANS, ""));
			Long currLong = APP.parseCostnum(ss);
			Long front = APP.parseCostnum(s);
			int a = (int) (((costbeans - front) * 100) / (currLong - front));
			String t = (currLong - costbeans) + "";
			cost_cha.setText("还差" + t + "升级");
			cost_progressbar.setProgress(a);
		}

		id = (TextView) findViewById(R.id.user_id);
		id.setText(sharedPreferences.getString(APP.USER_ID, ""));
		String openid = sharedPreferences.getString(APP.OPEN_ID, "");
		String openkey = sharedPreferences.getString(APP.OPENKEY, "");
		String timestamp = sharedPreferences.getString(APP.TIMESTAMP, "");
		inputurl = "http://picture.0058.com/useravator.php?type=45&openid="
				+ openid + "&openkey=" + openkey + "&timestamp=" + timestamp;
		dialog = Utils.showProgressDialog(this, "正在上传", true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		// 头像
		case R.id.touxiang_rl:
			uploadHeadIcon();
			break;
		// 昵称
		case R.id.nicheng_rl:
			Intent intent = new Intent(MyInformationActivity.this,
					ResetinformationActivity.class);
			intent.putExtra("title", sharedPreferences.getString(APP.NICKNAME, ""));
			startActivityForResult(intent, 102);
			break;
		// 性别
		case R.id.xingbie_rl:
			Intent inten = new Intent(MyInformationActivity.this,
					ResetSexActivity.class);
			inten.putExtra("select_sex", xingbie.getText().toString().trim());
			startActivityForResult(inten, 100);
			break;
		// 地区
		case R.id.diqu_rl:
			Intent diqu = new Intent(MyInformationActivity.this,
					ProvinceListActivity.class);
			startActivityForResult(diqu, 101);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 20) {
			String sex = data.getStringExtra("sex");
			xingbie.setText(sex);
		}
		if (resultCode == 21) {
			diqu1 = data.getStringExtra("cityname");
			diqu.setText(diqu1);
			StringBuffer sb1 = new StringBuffer("");
			sb1.append("{\"pos\":\"");
			sb1.append(diqu1);
			sb1.append("\"");
			sb1.append("}");
			resetpos(sb1.toString());
		}
		if (resultCode == 22) {
			String nc = sharedPreferences.getString(APP.NICKNAME, "");
			nicheng.setText(nc);
		}
		// 结果码不等于取消时候
		Log.i("lvjian", "-resultCode------------->" + resultCode);
		if (resultCode != RESULT_CANCELED) {
			// 本地上传
			if (requestCode == IMAGE_REQUEST_CODE) {
				Log.i("lvjian", "--------------REQUEST_本地上传----------------");
				if ((resultCode == -1) && (data != null)) {
					doCropPhoto(data.getData());
				}
			}
			// 图片处理结果
			if (requestCode == this.RESULT_REQUEST_CODE) {
				Log.i("lvjian",
						"--------------REQUEST_PICKED处理结果----------------");
				String openid = sharedPreferences.getString(APP.OPEN_ID, "");
				String openkey = sharedPreferences.getString(APP.OPENKEY, "");
				String timestamp = sharedPreferences.getString(APP.TIMESTAMP,
						"");
				final String url = "http://picture.0058.com/useravator.php?type=45&openid="
						+ openid
						+ "&openkey="
						+ openkey
						+ "&timestamp="
						+ timestamp;
				if (data != null) {
					getImageToView(data);
				}
				final Bitmap localBitmap = (Bitmap) data
						.getParcelableExtra("data");
				dialog.show();
				saveMyBitmap(localBitmap);

				// /**
				// * 用来做图片上传滴
				// */
				// new Handler().post(new Runnable() {
				// public void run() {
				// String str = null;
				// if (localBitmap != null) {
				// str = Utils.bitmapToBase64(localBitmap);
				// localBitmap.recycle();
				// }
				//
				// if (str != null) {
				//
				// }
				//
				// }
				// });

			}
			// 相机拍照
			if (requestCode == CAMERA_REQUEST_CODE) {
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
				doCropPhoto(localUri);
			}
		}
	}

	protected void doCropPhoto(Uri paramUri) {
		startActivityForResult(getCropImageIntent(paramUri),
				this.RESULT_REQUEST_CODE);
	}

	private Intent getCropImageIntent(Uri paramUri) {
		Intent localIntent = new Intent("com.android.camera.action.CROP");
		localIntent.setDataAndType(paramUri, "image/*");
		localIntent.putExtra("crop", "true");
		localIntent.putExtra("aspectX", 1);
		localIntent.putExtra("aspectY", 1);
		localIntent.putExtra("outputX", 120);
		localIntent.putExtra("outputY", 120);
		localIntent.putExtra("return-data", true);
		return localIntent;
	}

	private void uploadHeadIcon() {
		String[] arrayOfString = new String[3];
		arrayOfString[0] = "拍照";
		arrayOfString[1] = "本地上传";
		arrayOfString[2] = "取消";
		final AlertDialog localAlertDialog = new AlertDialog.Builder(this)
				.create();
		localAlertDialog.show();
		Window localWindow = localAlertDialog.getWindow();
		View localView = getLayoutInflater().inflate(R.layout.shop_poplayout,
				null);
		localWindow.setContentView(localView);
		((TextView) localView.findViewById(R.id.vipName)).setText("上传头像");
		ListView localListView = (ListView) localView
				.findViewById(R.id.shop_pop_listview);
		localListView.setAdapter(new PopAdapter(this, arrayOfString));
		localListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(
							AdapterView<?> paramAnonymousAdapterView,
							View paramAnonymousView, int paramAnonymousInt,
							long paramAnonymousLong) {
						if (paramAnonymousInt == 0) {
							if (Environment.getExternalStorageState().equals(
									"mounted")) {

								MyInformationActivity.this.takePhoto();
								localAlertDialog.dismiss();
							}
						} else if (paramAnonymousInt == 1) {
							if (Environment.getExternalStorageState().equals(
									"mounted")) {
								MyInformationActivity.this.invokePhoto();
								localAlertDialog.dismiss();
							}
						} else {
							localAlertDialog.dismiss();
						}
					}
				});
	}

	class PopAdapter extends BaseAdapter {
		String[] contents;
		Context context;

		public PopAdapter(Context paramArrayOfString, String[] arg3) {
			this.context = paramArrayOfString;
			Object localObject;
			this.contents = arg3;
		}

		public int getCount() {
			return this.contents.length;
		}

		public Object getItem(int paramInt) {
			return Integer.valueOf(this.contents.length);
		}

		public long getItemId(int paramInt) {
			return paramInt;
		}

		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			ViewHolder localViewHolder;
			if (paramView == null) {
				localViewHolder = new ViewHolder();
				paramView = LayoutInflater.from(this.context).inflate(
						R.layout.edit_popitem, null);
				localViewHolder.textView = ((TextView) paramView
						.findViewById(R.id.shop_mounth));
				paramView.setTag(localViewHolder);
			} else {
				localViewHolder = (ViewHolder) paramView.getTag();
			}
			localViewHolder.textView.setText(this.contents[paramInt]);
			return paramView;
		}

		class ViewHolder {
			TextView textView;

			ViewHolder() {
			}
		}
	}

	protected void takePhoto() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		SimpleDateFormat timeStampFormat = new SimpleDateFormat(
				"yyyy_MM_dd_HH_mm_ss");
		String filename = timeStampFormat.format(new Date());
		ContentValues values = new ContentValues();
		values.put(Media.TITLE, filename);
		photoUri = getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		startActivityForResult(intent, this.CAMERA_REQUEST_CODE);
	}

	protected void invokePhoto() {
		Intent localIntent = new Intent("android.intent.action.PICK", null);
		localIntent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(localIntent, this.IMAGE_REQUEST_CODE);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void getImageToView(Intent data) {
		Bundle extras = data.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			touxiang.setImageDrawable(drawable);
		}
	}

	/**
	 * 修改地区
	 */
	private void resetpos(final String data) {
		Runnable resetposrun = new Runnable() {
			public void run() {
				String s = Utils.update_personmsg(user_id, secret, data);
				try {
					JSONObject jsonObject = new JSONObject(s);
					int a = jsonObject.getInt("s");
					if (a == 1) {
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
		ThreadPoolWrap.getThreadPool().executeTask(resetposrun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Editor editor = sharedPreferences.edit();
				editor.putString(APP.POS, diqu1);
				editor.commit();
				break;
			case 2:
				Log.i("lvjian", "----------修改位置信息异常--------");
				break;
			case 3:
				final String fileurl = msg.obj.toString();
				Thread t1 = new Thread(new Runnable() {
					@Override
					public void run() {
						uploadFile(fileurl);
					};
				});
				t1.start();
				Thread t2 = new Thread(new Runnable() {
					@Override
					public void run() {
						uploadFile200(fileurl);
					};
				});
				t2.start();

				break;
			case 4:
				dialog.dismiss();
				String icon = msg.obj.toString();
				Editor editor1 = sharedPreferences.edit();
				editor1.putString(APP.USER_ICON, APP.USER_LOGO_ROOT + icon);
				editor1.commit();
				PersonalCenterFragment.reseticon = true;
				Utils.showToast(MyInformationActivity.this, "上传成功！");
				break;
			case 5:
				dialog.dismiss();
				Utils.showToast(MyInformationActivity.this, "上传失败！");
				break;
			default:
				break;
			}
		};
	};

	public void saveMyBitmap(Bitmap mBitmap) {

		String sddizhi = "/sdcard/" + System.currentTimeMillis() + ".jpg";
		File f = new File(sddizhi);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
			Message msg = new Message();
			msg.what = 3;
			msg.obj = sddizhi;
			handler.sendMessage(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* 上传文件至Server的方法 */
	private void uploadFile(String uploadFile) {
		try {
			URL url = new URL(inputurl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "application/octet-stream");
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			FileInputStream fStream = new FileInputStream(uploadFile);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			/* close streams */
			fStream.close();
			ds.flush();
			/* 取得Response内容 */
			int res = con.getResponseCode();
			if (res == 200) {
				InputStream is = con.getInputStream();
				int ch;
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				Log.i("lvjian", "返回结果-------------------》" + b.toString());
				Message msg = new Message();
				String result = b.toString();
				String icon = result.substring((result.indexOf("|") + 1),
						result.length());
				msg.obj = icon;
				msg.what = 4;
				handler.sendMessage(msg);
			} else {
				handler.sendEmptyMessage(5);
			}
			ds.close();
		} catch (Exception e) {
			handler.sendEmptyMessage(5);
		}
	}

	/* 上传文件至Server的方法 */
	private void uploadFile200(String uploadFile) {
		String openid = sharedPreferences.getString(APP.OPEN_ID, "");
		String openkey = sharedPreferences.getString(APP.OPENKEY, "");
		String timestamp = sharedPreferences.getString(APP.TIMESTAMP, "");
		String dizhi = "http://picture.0058.com/useravator.php?type=200&openid="
				+ openid + "&openkey=" + openkey + "&timestamp=" + timestamp;
		try {
			URL url = new URL(dizhi);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setRequestMethod("POST");
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type", "application/octet-stream");
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			FileInputStream fStream = new FileInputStream(uploadFile);
			/* 设置每次写入1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* 从文件读取数据至缓冲区 */
			while ((length = fStream.read(buffer)) != -1) {
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
			}
			/* close streams */
			fStream.close();
			ds.flush();
			/* 取得Response内容 */
			int res = con.getResponseCode();
			if (res == 200) {
				InputStream is = con.getInputStream();
				int ch;
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				Log.i("lvjian", "返回结果--------200-----------》" + b.toString());

			} else {
			}
			ds.close();
		} catch (Exception e) {
		}
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
}