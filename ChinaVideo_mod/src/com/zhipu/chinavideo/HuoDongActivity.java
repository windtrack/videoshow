package com.zhipu.chinavideo;

import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.util.APP;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HuoDongActivity extends Activity implements OnClickListener {
	private ImageView title_back;
	private WebView webview;
	private Handler mHandler = new Handler();
	private String url = "";
	private String title = "愚人节活动";
	private HuoDongActivity instance;
	private LinearLayout pb;
	private TextView title_text;
	private SharedPreferences preferences;
	String id = "";
	String secret = "";

	@SuppressLint({ "SetJavaScriptEnabled", "AddJavascriptInterface" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.instance = this;
		setContentView(R.layout.huodongactivity);
		url = getIntent().getExtras().getString("url");
		title = getIntent().getExtras().getString("title");
		title_back = (ImageView) findViewById(R.id.title_back);
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText(title);
		pb = (LinearLayout) findViewById(R.id.pb);
		title_back.setOnClickListener(this);
		webview = (WebView) findViewById(R.id.webview);
		preferences = getSharedPreferences(APP.MY_SP, Context.MODE_PRIVATE);
		id = preferences.getString(APP.USER_ID, "");
		secret = preferences.getString(APP.SECRET, "");
		// 设置WebView支持JavaScript
//		webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);// 不使用缓存
		
		webview.getSettings().setJavaScriptEnabled(true);
		webview.loadUrl(url);
		webview.setWebChromeClient(new myWebChromeClient());
		webview.setWebViewClient(new myWebViewClient());
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			if (webview.canGoBack()) {
				webview.goBack();
			} else {
				finish();
			}
			break;
		default:
			break;
		}
	}

	class myWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view,
				String paramAnonymousString) {
			// TODO Auto-generated method stub
			view.loadUrl(paramAnonymousString);
			return true;
		}
	}

	private class myWebChromeClient extends WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {

				pb.setVisibility(View.GONE);
				String canshu = id + "," + secret;
				Log.i("lvjian", "canshu-------------->" + canshu);
				webview.loadUrl("javascript:showInfoFromJava('" + canshu + "')");
			}
			super.onProgressChanged(view, newProgress);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			if (webview.canGoBack()) {
				webview.goBack();
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
