package com.zhipu.chinavideo.wxapi;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.zhipu.chinavideo.util.ThreadPoolWrap;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	private String access_token = "";
	private String openid = "";
	public static final String action = "action";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.weixin);
		handleIntent(getIntent());
	}

	@Override
	public void onReq(BaseReq arg0) {

	}

	@Override
	public void onResp(BaseResp resp) {
		
       
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		SendAuth.Resp resp = new SendAuth.Resp(intent.getExtras());
		if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
			// 用户同意
			String code = ((SendAuth.Resp) resp).code;
			Log.i("lvjian", "code======================>" + code);
			String dizhi = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxe3b6571489e8a96d&secret=9c02e9ea17fd0cf6ca73640a970ee7ef&code="
					+ code + "&grant_type=authorization_code";
			GETHTTP(dizhi);
		} else {
			// 用户不同意
		}
	}

	private void GETHTTP(final String url) {
		Runnable canclefollowrun = new Runnable() {
			@Override
			public void run() {
				String result = get(url);
				Log.i("lvjian", "result00000000000000===>" + result);
				try {
					JSONObject obj = new JSONObject(result);
					openid = obj.getString("openid");
					access_token = obj.getString("access_token");
					handler.sendEmptyMessage(1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		ThreadPoolWrap.getThreadPool().executeTask(canclefollowrun);
	}

	private String get(String url) {
		String result = null;
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (ClientProtocolException e) {
			Log.i("lvjian", "----------异常一---------");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("lvjian", "----------异常二---------");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Intent intent = new Intent(action);
				intent.putExtra("openid", openid);
				intent.putExtra("access_token", access_token);
				sendBroadcast(intent);
				finish();
				break;
			case 2:
				finish();
				break;
			default:
				break;
			}
		};
	};
}