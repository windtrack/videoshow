package com.zhipu.chinavideo.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.zhipu.chinavideo.MainActivity;
import com.zhipu.chinavideo.MainTabActivity;
import com.zhipu.chinavideo.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class UpdateManager {
	private MainTabActivity mContext;
	// 提示语
	private String updateMsg = "有最新的软件包哦，亲快下载吧~,如果更新遇到问题,请从官网下载最新版本,卸载旧版本后,重新安装";
	// 返回的安装包url
	private String apkUrl = "http://down.0058.com/0058_Video.apk";
	private Dialog noticeDialog;
	private Dialog downloadDialog;
	/* 下载包安装路径 */
	private String savePath = "/updatechinavideo/";
	private String saveFileName = "0058_Video.apk";
	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private int progress;
	private Thread downLoadThread;
	private boolean interceptFlag = false;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				installApk();
				mContext.doFinish();
				break;
			default:
				break;
			}
		};
	};

	public UpdateManager(MainTabActivity context, String aurl) {
		this.mContext = context;
		this.apkUrl = aurl;
	}

	// 外部接口让主Activity调用
	public void checkUpdateInfo() {
		showNoticeDialog();
	}
	private Dialog dialog_re;
	
	public static String str_version;
	public static String str_updateInfo;
//	public static String str_updateInfo = "1、千呼万唤始出来，财神系统终于更新啦，以后手机也可以抢财神了&2、新手引导让你玩high中视秀场&3、经过程序猿哥哥们通宵熬夜修复BUG若干" ;;
	
	private void showNoticeDialog() {
//		str_updateInfo  = "1、千呼万唤始出来，财神系统终于更新啦，以后手机也可以抢财神了&2、新手引导让你玩high中视秀场&3、经过程序猿哥哥们通宵熬夜修复BUG若干" ;;
//		String realStrbc = Tools.ToSBC(str_updateInfo) ;
		String realStr = str_updateInfo.replace("&", "\n");
		realStr+="\n";
		dialog_re = new AlertDialog.Builder(mContext).create();
		dialog_re.show();
		Window localWindow = dialog_re.getWindow();
		localWindow.setContentView(LayoutInflater.from(mContext).inflate(R.layout.dialog_update, null));

		TextView myTextTitle = (TextView)localWindow.findViewById(R.id.text_updatetitle);
		TextView myTextView = (TextView)localWindow.findViewById(R.id.text_updateinfo);
		
		
		myTextTitle.setText(str_version);
		myTextView.setText(realStr);
		LinearLayout bt_download = (LinearLayout) localWindow.findViewById(R.id.layout_update_download);
		bt_download.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDownloadDialog() ;
			}
		});
		
		
		LinearLayout bt_canacel = (LinearLayout) localWindow.findViewById(R.id.layout_update_cancel);
		
		bt_canacel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog_re.dismiss();
				mContext.doFinish();
			}
		});
		 
		
		dialog_re.setCancelable(false);
	}



	
	private void showDownloadDialog() {
		AlertDialog.Builder builder = new Builder(mContext);
		builder.setTitle("软件版本更新");
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		builder.setView(v);
//		builder.setNegativeButton("取消", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				interceptFlag = true;
//			}
//		});
		downloadDialog = builder.create();
		downloadDialog.show();
		downloadDialog.setCancelable(false);
		downloadApk();
		dialog_re.dismiss();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				File sdcardDir = Environment.getExternalStorageDirectory();
				savePath = sdcardDir + savePath;
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = savePath + saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				int count = 0;
				byte buf[] = new byte[4096];
				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	/**
	 * 下载apk
	 * 
	 * @param url
	 */

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		saveFileName = savePath + saveFileName;
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		mContext.startActivity(i);

	}
}
