package com.zhipu.chinavideo;

import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.util.APP;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends Activity implements OnClickListener {
	private ImageView back;
	private TextView title_tv;
	private TextView localversion_tv;
	private Button textView_yingsi ;


	private Context mContext ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		back = (ImageView) findViewById(R.id.title_back);
		title_tv = (TextView) findViewById(R.id.title_text);
		localversion_tv = (TextView) findViewById(R.id.localversion_tv);
		title_tv.setText("关于中视");
		back.setOnClickListener(this);
		
		textView_yingsi = (Button) findViewById(R.id.text_yingsi);
		textView_yingsi.setOnClickListener(this);
		String localversion = getlocalverson();
		localversion_tv.setText(localversion);
		mContext = getApplicationContext() ;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.text_yingsi:
		{
			Intent intent = new Intent(this, HuoDongActivity.class);
			if(APP.IS_DEMO == 0){
				intent.putExtra("url", "http://60.174.249.98:8000/about/rules.html");
			}else{
				intent.putExtra("url", "http://www.0058.com/about/rules.html");
			}
			intent.putExtra("title", "中视APP服务使用条例");
			this.startActivity(intent);
		}
			break;
		default:
			break;
		}
	}

	private String getlocalverson() {
		PackageInfo packageInfo;
		try {
			packageInfo = getApplicationContext().getPackageManager()
					.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "版本号未知";
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
