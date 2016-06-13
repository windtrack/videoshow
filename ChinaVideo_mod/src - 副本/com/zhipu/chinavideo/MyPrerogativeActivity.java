package com.zhipu.chinavideo;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.zhipu.chinavideo.adapter.MyspinnerAdapter;
import com.zhipu.chinavideo.rechargecenter.RechargeActivity;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

public class MyPrerogativeActivity extends Activity implements OnClickListener {
	private ImageView title_back;
	private TextView title_tv;
	private ArrayList<String> list;
	private ImageView imgView;
	private TextView textView;
	private LinearLayout layout;
	private ListView listView;
	private MyspinnerAdapter adapter;
	private PopupWindow popupWindow;
	private LinearLayout vip_dengji;
	private TextView vip_tv1;
	private TextView vip_jine_tv;
	private LinearLayout vip_jine;
	private MyspinnerAdapter adapter_msg;
	private List<String> msg_name;
	private TextView vip_text;
	private List<String> vipcontext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myprerogative);
		title_back = (ImageView) findViewById(R.id.title_back);
		title_tv = (TextView) findViewById(R.id.title_text);
		title_back.setOnClickListener(this);
		title_tv.setText("我的特权");
		vip_dengji = (LinearLayout) findViewById(R.id.vip_dengji);
		vip_dengji.setOnClickListener(this);
		vip_tv1 = (TextView) findViewById(R.id.vip_tv1);
		
		
		vip_jine_tv=(TextView) findViewById(R.id.vip_jine_tv);
		vip_jine=(LinearLayout) findViewById(R.id.vip_jine);
		vip_jine.setOnClickListener(this);
		list = new ArrayList<String>();
		msg_name=new ArrayList<String>();
		list.add("VIP一级");
		list.add("VIP二级");
		list.add("VIP三级");
		list.add("VIP四级");
		list.add("VIP五级");
		list.add("VIP六级");
		list.add("VIP七级");
		list.add("VIP八级");
		list.add("VIP九级");
		msg_name.add("");
		adapter = new MyspinnerAdapter(this, list);
		vip_text = (TextView) findViewById(R.id.vip_text);
		vipcontext = new ArrayList<String>();
		vipcontext.add("★ VIP勋章：VIP一级勋章\n★ 可进入已达观众人数上限的房间\n★ 防踢：VIP七级以下、房间管理和一级守护的用户\n★ 防禁言：VIP三级以下和一级守护的用户\n★ 排名：非VIP用户的前面\n★ 专属表情：柏夫表情\n★ 可同时打开两个房间");
		vipcontext.add("★ VIP勋章：VIP二级勋章\n★ 可进入已达观众人数上限的房间\n★ 防踢：VIP七级以下、房间管理和一级守护的用户\n★ 防禁言：VIP四级以下和一级守护的用户\n★ 可享有5次免费广播\n★ 排名：VIP一级用户的前面\n★ 专属表情：公主桃表情\n★ 可同时打开三个房间");
		vipcontext.add("★ VIP勋章：VIP三级勋章\n★ 可进入已达观众人数上限的房间\n★ 防踢：VIP七级以下、房间管理和一级守护的用户\n★ 防禁言：VIP五级以下和一级守护的用户\n★ 禁言：7富及以下的用户\n★ 可享有10次免费广播\n★ 排名：VIP二级用户的前面\n★ 专属表情：猫耳茉莉表情\n★ 可同时打开四个房间");
		vipcontext.add("★ VIP勋章：VIP四级勋章\n★ 可进入已达观众人数上限的房间\n★ 防踢：VIP七级以下、房间管理和一级守护的用户\n★ 防禁言：VIP六级以下、房间管理和一级守护的用户\n★ 禁言：9富及以下和VIP一级的用户\n★ 座驾：拖拉机座驾\n★ 可享有25次免费广播\n★ 排名：VIP三级用户的前面\n★ 专属表情：潘达达表情\n★ 可同时打开五个房间");
		vipcontext.add("★ VIP勋章：VIP五级勋章\n★ 可进入已达观众人数上限的房间\n★ 防踢：VIP七级以下、房间管理和一级守护的用户\n★ 防禁言：VIP七级以下、房间管理和一级守护的用户\n★ 禁言：子爵及以下、VIP二级及以下和房间管理的用户\n★ 座驾：吉普座驾\n★ 可享有50次免费广播\n★ 排名：VIP四级用户的前面\n★ 专属表情：洋葱头表情\n★ 可同时打开六个房间");
		vipcontext.add("★ VIP勋章：VIP六级勋章\n★ 可进入已达观众人数上限的房间\n★ 防踢：VIP七级以下、房间管理和二级守护的用户\n★ 防禁言：VIP七级以下、房间管理和二级守护的用户\n★ 禁言：公爵及以下、VIP三级及以下和房间管理的用户\n★ 座驾：悍马座驾\n★ 可享有80次免费广播\n★ 排名：VIP五级用户的前面\n★ 可对所有人隐身\n★ 专属表情：小鸡表情\n★ 可同时打开七个房间");
		vipcontext.add("★ VIP勋章：VIP七级勋章\n★ 可进入已达观众人数上限的房间\n★ 防踢：五冠以下主播、房间管理和三级守护的用户\n★ 防禁言：五冠以下主播、房间管理和三级守护的用户\n★ 踢人：VIP三级以下和房间管理的用户3次/日\n★ 禁言：王爵及以下、VIP四级及以下、房间管理和一级守护的用户\n★ 座驾：坦克座驾\n★ 可享有150次免费广播\n★ 排名：VIP六级的前面\n★ 可对所有人隐身\n★ 专属表情：丘比龙表情\n★ 可同时打开八个房间");
		vipcontext.add("★ VIP勋章：VIP八级勋章\n★ 可进入已达观众人数上限的房间\n★ 防踢：十冠以上主播和四级守护除外的用户\n★ 防禁言：十冠以上主播和官方除外的用户\n★ 踢人：VIP三级以下和房间管理的用户3次/日\n★ 禁言：国王及以下、VIP五级及以下、房间管理和二级守护的用户\n★ 座驾：擎天柱座驾\n★ 可享有200次免费广播\n★ 排名：VIP七级的前面面\n★ 可对所有人隐身\n★ 专属表情：丘比龙表情\n★ 可同时打开九个房间");
		vipcontext.add("★ VIP勋章：VIP九级勋章\n★ 可进入已达观众人数上限的房间\n★ 防踢：十冠以上主播和四级守护除外的用户\n★ 防禁言：十冠以上主播和官方之外的用户\n★ 踢人：VIP三级以下和房间管理的用户3次/日\n★ 禁言：国王及以下、VIP五级及以下、房间管理和二级守护的用户\n★ 座驾：航母座驾\n★ 可享有200次免费广播\n★ 排名：VIP八级的前面\n★ 可对所有人隐身\n★ 专属表情：丘比龙表情\n★ 可同时打开十个房间");
		vip_text.setText(vipcontext.get(0));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.vip_dengji:
			showWindow(vip_dengji, vip_tv1);
			break;
		case R.id.vip_jine:
			Intent intent =new Intent(MyPrerogativeActivity.this,RechargeActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void showWindow(View position, final TextView txt) {

		layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.myspinner_dropdown, null);
		listView = (ListView) layout.findViewById(R.id.listView);
		listView.setAdapter(adapter);
		popupWindow = new PopupWindow(position);
		// 设置弹框的宽度为布局文件的宽
		popupWindow.setWidth(vip_dengji.getWidth());
		popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置一个透明的背景，不然无法实现点击弹框外，弹框消失
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击弹框外部，弹框消失
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setContentView(layout);
		// 设置弹框出现的位置，在v的正下方横轴偏移textview的宽度，为了对齐~纵轴不偏移
		popupWindow.showAsDropDown(position, 0, 0);
		// listView的item点击事件
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				txt.setText(list.get(arg2));// 设置所选的item作为下拉框的标题
				if(list.get(arg2).equals("VIP一级")){
					vip_jine_tv.setText("(升级)50万乐币");
				}
				else if(list.get(arg2).equals("VIP二级")){
					vip_jine_tv.setText("(升级)300万乐币");
				}
				else if(list.get(arg2).equals("VIP三级")){
					vip_jine_tv.setText("(升级)1000万乐币");
				}
				else if(list.get(arg2).equals("VIP四级")){
					vip_jine_tv.setText("(升级)2000万乐币");
				}
				else if(list.get(arg2).equals("VIP五级")){
					vip_jine_tv.setText("(升级)5000万乐币");
				}
				else if(list.get(arg2).equals("VIP六级")){
					vip_jine_tv.setText("(升级)10000万乐币");
				}
				else if(list.get(arg2).equals("VIP七级")){
					vip_jine_tv.setText("(升级)30000万乐币");
				}
				else if(list.get(arg2).equals("VIP八级")){
					vip_jine_tv.setText("(升级)60000万乐币");
				}else{
					vip_jine_tv.setText("(升级)120000万乐币");
				}
				vip_text.setText(vipcontext.get(arg2));
				// 弹框消失
				popupWindow.dismiss();
				popupWindow = null;
			}
		});

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
