package com.zhipu.chinavideo.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.zhipu.chinavideo.R;
import com.zhipu.chinavideo.adapter.GiftAdapter;
import com.zhipu.chinavideo.entity.Gift;
import com.zhipu.chinavideo.manager.GiftManager;
import com.zhipu.chinavideo.util.APP;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import com.zhipu.chinavideo.util.Utils;

public class CanKuFragment extends Fragment {
	// 获取仓库礼物
	private GiftAdapter adapter;
	private Context context;
	private List<Gift> gifts = new ArrayList<Gift>();
	private List<Gift> Gs = new ArrayList<Gift>();;
	private GridView cankugift_gridview;
	private View rootView;
	private SharedPreferences preferences;
	private Editor editor;
	private int position = 0;
	private TextView canku_gift_tv;
	private static CanKuFragment cankufragment;

	public static CanKuFragment getIntance(Context context,List<Gift>list, int position) {
		if (cankufragment != null) {
			cankufragment = null;
		}
		cankufragment = new CanKuFragment();
		cankufragment.context = context;
		cankufragment.gifts = list;
		cankufragment.position = position;
		cankufragment.preferences = context.getSharedPreferences(APP.MY_SP,
				Context.MODE_PRIVATE);
		cankufragment.editor = cankufragment.preferences.edit();

		return cankufragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if (rootView == null) {
			this.rootView = inflater.inflate(R.layout.fragment_cankugift,
					container, false);
			cankugift_gridview = (GridView) rootView
					.findViewById(R.id.cankugift_gridview);
			canku_gift_tv = (TextView) rootView
					.findViewById(R.id.canku_gift_tv);
			for (int i = 0; i < gifts.size(); i++) {
				if (i >= (8 * position) && i < (8 * (position + 1))) {
					Gs.add(gifts.get(i));
				}
			}
			adapter = new GiftAdapter(Gs, context, position);
			cankugift_gridview.setAdapter(adapter);
			cankugift_gridview
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							String gift_id = Gs.get(arg2).getId();
							GiftManager.getInstance().changeGiftTypeShow(true);
							if ("700006".equals(gift_id)) {
								getcaifudengjigift("1");
							} else if ("700007".equals(gift_id)) {
								getcaifudengjigift("2");
							} else if ("700008".equals(gift_id)) {
								getcaifudengjigift("3");
							} else if ("700009".equals(gift_id)) {
								getcaifudengjigift("4");
							} else if ("700010".equals(gift_id)) {
								getcaifudengjigift("5");
							} else if ("700011".equals(gift_id)) {
								getcaifudengjigift("6");
							} else if ("700012".equals(gift_id)) {
								getcaifudengjigift("7");
							} else if ("700013".equals(gift_id)) {
								getcaifudengjigift("8");
							} else if ("700014".equals(gift_id)) {
								getcaifudengjigift("9");
							} else if ("700015".equals(gift_id)) {
								getcaifudengjigift("10");
							} else if ("700016".equals(gift_id)) {
								getcaifudengjigift("11");
							} else if ("700017".equals(gift_id)) {
								getcaifudengjigift("12");
							} else if ("700018".equals(gift_id)) {
								getcaifudengjigift("13");
							} else if ("700019".equals(gift_id)) {
								getcaifudengjigift("14");
							} else if ("700020".equals(gift_id)) {
								getcaifudengjigift("15");
							} else if ("700021".equals(gift_id)) {
								getcaifudengjigift("16");
							} else if ("700022".equals(gift_id)) {
								getcaifudengjigift("17");
							} else if ("700023".equals(gift_id)) {
								getcaifudengjigift("18");
							} else if ("700024".equals(gift_id)) {
								getcaifudengjigift("19");
							} else if ("700025".equals(gift_id)) {
								getcaifudengjigift("20");
							} else if ("700026".equals(gift_id)) {
								getcaifudengjigift("21");
							} else if ("700027".equals(gift_id)) {
								getcaifudengjigift("22");
							} else if ("700028".equals(gift_id)) {
								getcaifudengjigift("23");
							} else if ("700029".equals(gift_id)) {
								getcaifudengjigift("24");
							} else if ("700030".equals(gift_id)) {
								getcaifudengjigift("25");
							} else if ("700031".equals(gift_id)) {
								getcaifudengjigift("26");
							}else if("700042".equals(gift_id)){
								//51活动 礼包
//								adapter.getItem(arg0)
								GiftManager.getInstance().changeGiftTypeShow(false);
								adapter.setLocation(arg2);
								adapter.notifyDataSetChanged();
								// 保存赠送礼物是否是仓库中的礼物
								editor.putString(APP.GIFT_ID_CURRENT, gift_id);
								editor.putString(APP.GIFT_FROM_CURRENT, "1");
								editor.commit();
							} else {
								adapter.setLocation(arg2);
								adapter.notifyDataSetChanged();
								// 保存赠送礼物是否是仓库中的礼物
								editor.putString(APP.GIFT_ID_CURRENT, gift_id);
								
								editor.putString(APP.GIFT_FROM_CURRENT, "1");
								editor.commit();
							}

						}
					});

		}
		return rootView;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		ViewGroup localViewGroup = (ViewGroup) this.rootView.getParent();
		if (localViewGroup != null) {
			localViewGroup.removeView(this.rootView);
		}
	}

	/**
	 * 获取仓库礼物
	 * 
	 * @author lvjian
	 */
	private void getcankugift() {
		gifts = new ArrayList<Gift>();
		Runnable getcankurun = new Runnable() {
			@Override
			public void run() {
				try {
					String user_id = preferences.getString(APP.USER_ID, "");
					String secret = preferences.getString(APP.SECRET, "");
					String result = Utils.get_canku_list(user_id, secret);
					JSONObject obj = new JSONObject(result);
					int s = obj.getInt("s");
					if (s == 1) {
						JSONArray ja = obj.getJSONArray("data");
						for (int i = 0; i < ja.length(); i++) {
							JSONObject j = ja.getJSONObject(i);
							Gift gift = new Gift();
							gift.setIcon(j.getString("icon"));
							gift.setPrice(j.getString("num"));
							gift.setName(j.getString("name"));
							gift.setId(j.getString("gift_id"));
							gifts.add(gift);
						}
						handler.sendEmptyMessage(1);
					} else {
						handler.sendEmptyMessage(2);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(3);
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getcankurun);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				GiftManager.changeCanku(gifts);
				break;
			case 2:
				canku_gift_tv.setVisibility(0);
				break;
			case 3:
				break;
			case 4:
				String m = "";
				m = msg.obj.toString();
				repeatlogin_dialog(m);
				break;
			case 5:
				repeatlogin_dialog("领取财富等级礼包异常！");
				break;
			// 领取成功刷新仓库列表！
			case 6:
				getcankugift();
				break;
			default:
				break;
			}
		};
	};

/*	public static void rushcanku() {
		cankufragment.getcankugift();
	}
*/
	/**
	 * 领取财富等级礼包
	 * 
	 * @author lvjian
	 * 
	 */
	private void getcaifudengjigift(final String type) {
		Runnable getcaifudengjigift = new Runnable() {
			@Override
			public void run() {
				try {
					String user_id = preferences.getString(APP.USER_ID, "");
					String secret = preferences.getString(APP.SECRET, "");
					String result = Utils.getcaifudengjigift(user_id, secret,
							type);
					JSONObject obj = new JSONObject(result);
					String data = obj.getString("data");
					Message msg1 = new Message();
					msg1.what = 4;
					msg1.obj = data;
					handler.sendMessage(msg1);
					int s = obj.getInt("s");
					if (s == 1) {
						handler.sendEmptyMessage(6);
					} else {

					}
				} catch (JSONException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(5);

				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(getcaifudengjigift);
	}

	private void repeatlogin_dialog(String text) {
		final Dialog dialog_re = new AlertDialog.Builder(getActivity())
				.create();
		dialog_re.show();
		Window localWindow = dialog_re.getWindow();
		localWindow.setContentView(LayoutInflater.from(getActivity()).inflate(
				R.layout.repeatlogin_dialog, null));
		Button releat_login_queding = (Button) localWindow
				.findViewById(R.id.releat_login_queding);
		TextView moneynotenoth_title = (TextView) localWindow
				.findViewById(R.id.moneynotenoth_title);
		moneynotenoth_title.setText(text);
		releat_login_queding.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				dialog_re.cancel();
			}
		});
	}
}
