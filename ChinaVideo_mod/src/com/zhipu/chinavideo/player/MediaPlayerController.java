package com.zhipu.chinavideo.player;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener;
import tv.danmaku.ijk.media.widget.HideDelegate;
import tv.danmaku.ijk.media.widget.VideoView;


/**
 * @author: zhongxf
 * @Description: 播放器控制类
 * @date: 2016年4月26日
 */
public class MediaPlayerController {
	private LoadStreamInterface loadIf;// 加载视频的回调
	private VideoView view;// 调用视频
	private ViewGroup con;// 视频播放器的容器
	private Context cxt;// 上下文
	private boolean isInit = false;// 是否初始化

	public MediaPlayerController(ViewGroup con, Context cxt, LoadStreamInterface lsif) {
		this.con = con;
		this.cxt = cxt;
		init(lsif);// 初始化视频播放器
	}

	// 初始化
	private void init(LoadStreamInterface lsif) {
		if (view == null) {
			view = new VideoView(this.cxt);
		}
		this.loadIf = lsif;
		view.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(IMediaPlayer mp) {
				// TODO Auto-generated method stub
				loadIf.loadSuccess();
			}
		});
		view.setOnErrorListener(new OnErrorListener() {// 视频加载失败监听
			@Override
			public boolean onError(IMediaPlayer arg0, int arg1, int arg2) {
				loadIf.loadError();
				return false;
			}
		});
		view.setHideDelegate(new HideDelegate() {
			@Override
			public void hideVideoButtons() {
			}
		});
//		try {
//		this.setSize(720, 500);
//	}
//	catch (NotRelativeLayoutException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
		this.con.addView(view, 0);
		isInit = true;// 初始化完成 设置标志位为true
		

	}

	// 获取播放器
	public VideoView getMediaPlayer() {
		return this.view;
	}

	// 设置播放器的大小尺寸
	public void setSize(int width, int height) throws NotRelativeLayoutException {
		if (con != null && con.getClass().getName().equals("android.widget.RelativeLayout")) {// 判断父类容器是否是RelativeLayout。如果不是在获取LayoutParams的时候会报错
			if (view != null) {
				view.setSize(width, height);
			}
		}
		else {
			throw new NotRelativeLayoutException();
		}

	}

	// 设置播放器的上下左右的间距的值
	public void setMargin(int left, int top, int right, int bottom)
			throws NotRelativeLayoutException {

		if (con != null && con.getClass().getName().equals("android.widget.RelativeLayout")) {// 判断父类容器是否是RelativeLayout。如果不是在获取LayoutParams的时候会报错
			if (view != null) {
				view.setMargin(left, top, right, bottom);
			}
		}
		else {
			throw new NotRelativeLayoutException();
		}
	}

	// 按照比例设置播放器尺寸(比例为：宽度/高度)
	public void setProportionSize(int height) throws NotRelativeLayoutException {
		int width = height * 4 / 3;// 4/3是应用中视频的比例
		setSize(width, height);
	}

	// 开始播放
	public void startPlay(String path) throws MediaPlayerException {
		if (isInit && view != null) {
			view.setVideoPath(path);
			this.con.setVisibility(View.VISIBLE);
			view.start();
		}
		else {
			throw new MediaPlayerException();
		}
	}

	// 暂停播放
	public void pause() {
		if (view != null) {
			view.pause();
		}
	}

	// 停止播放
	public void stop() {
		if (view != null) {
			view.stopPlayback();
		}
	}

	// 重新播放
	public void resume() {
		if (view != null) {
			view.resume();
		}
	}
}
