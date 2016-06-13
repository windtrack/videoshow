package com.zhipu.chinavideo;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.CircularImage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AnchorInformationActivity extends Activity implements
		OnClickListener {
	public DisplayImageOptions mOptions;
	private ImageLoader mImageLoader = null;
	private TextView title_text;
	private String icon = "";
	private String nickname = "";
	private String received_level = "";
	private String cost_level = "";
	private String id = "";
	private String username = "";
	// 主播信息
	private CircularImage file_anchor_touxiang;
	private TextView file_anchor_nicheng;
	private ImageView file_anchor_cfdj;
	private ImageView file_anchor_mxdj;
	private TextView file_anchor_zhanghao;
	private TextView file_anchor_user_id;
	private ImageView title_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anchorinformation);
		Intent intent = this.getIntent();
		icon = intent.getStringExtra("icon");
		nickname = intent.getStringExtra("nickname");
		received_level = intent.getStringExtra("received_level");
		cost_level = intent.getStringExtra("cost_level");
		id = intent.getStringExtra("id");
		username = intent.getStringExtra("username");
		initviews();
	}

	private void initviews() {
		title_back = (ImageView) findViewById(R.id.title_back);
		title_back.setOnClickListener(this);
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("主播资料");
		file_anchor_touxiang = (CircularImage) findViewById(R.id.file_anchor_touxiang);
		mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.loading_img).cacheInMemory()
				.cacheOnDisc().build();
		mImageLoader = ImageLoader.getInstance();
		mImageLoader.init(ImageLoaderConfiguration
				.createDefault(AnchorInformationActivity.this));
		mImageLoader.displayImage(APP.USER_LOGO_ROOT + icon,
				file_anchor_touxiang, mOptions);
		file_anchor_nicheng = (TextView) findViewById(R.id.file_anchor_nicheng);
		file_anchor_nicheng.setText(nickname);
		file_anchor_cfdj = (ImageView) findViewById(R.id.file_anchor_cfdj);
		file_anchor_mxdj = (ImageView) findViewById(R.id.file_anchor_mxdj);
		APP.setCost_level(cost_level, file_anchor_cfdj,
				AnchorInformationActivity.this);
		APP.setReceived_level(received_level, file_anchor_mxdj,
				AnchorInformationActivity.this);

		file_anchor_zhanghao = (TextView) findViewById(R.id.file_anchor_zhanghao);
		file_anchor_zhanghao.setText(username);
		file_anchor_user_id = (TextView) findViewById(R.id.file_anchor_user_id);
		file_anchor_user_id.setText(id);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;

		default:
			break;
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
