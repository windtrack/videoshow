package com.zhipu.chinavideo.util;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;


/**
 *
 * @author sunjinfang
 * @version 2016年5月24日
 */
public class UMengShare {

	/**
	 * 
	 * 描述：
	 * @param act
	 * @param title 标题
	 * @param text 内容
	 * @param imgurl 显示的图片地址
	 * @param touchUrl 链接地址
	 */
	public static void callShare(final Activity act,String title,String text,String imgurl,String touchUrl){
		    UMShareListener umShareListener = new UMShareListener() {
		        @Override
		        public void onResult(SHARE_MEDIA platform) {
		            Log.d("plat","platform"+platform);
		            if(platform.name().equals("WEIXIN_FAVORITE")){
		                Toast.makeText(act,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
		            }else{
		                Toast.makeText(act, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
		            }
		        }
		        	
		        @Override
		        public void onError(SHARE_MEDIA platform, Throwable t) {
		            Toast.makeText(act,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
		        }

		        @Override
		        public void onCancel(SHARE_MEDIA platform) {
		            Toast.makeText(act,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
		        }
		    };
		
		 UMImage image = new UMImage(act,imgurl);
		
		new ShareAction(act).setDisplayList(SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE)
	    .withText(text)
	    .withMedia(image)
	    .withTitle(title)
	    .withTargetUrl(touchUrl)
	    .setCallback(umShareListener)
	    .open();
	}
	
	public static void initShare(){
	    //各个平台的配置，建议放在全局Application或者程序入口
	    {
	        //微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
//	        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
	        PlatformConfig.setWeixin("wxe3b6571489e8a96d", "9c02e9ea17fd0cf6ca73640a970ee7ef&code");
	        //豆瓣RENREN平台目前只能在服务器端配置
	        //新浪微博
//	        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
	        //易信
//	        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
	        PlatformConfig.setQQZone("100448250", "2eeaababbb58ae4e01ba5fedc164c46b");
//	        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
//	        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
//	        PlatformConfig.setAlipay("2015111700822536");
//	        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
//	        PlatformConfig.setPinterest("1439206");
	        
	    }}
}


