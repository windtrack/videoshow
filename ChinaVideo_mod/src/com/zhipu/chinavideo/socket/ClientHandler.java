
package com.zhipu.chinavideo.socket;

import android.os.Handler;
import android.util.Log;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.zhipu.chinavideo.entity.ActivityMsg;

public class ClientHandler extends SimpleChannelUpstreamHandler {
	private Handler mHandler;
	private int msgid;
	private int errormsgid;
	public ClientHandler(Handler paramHandler ,int sendMessageHandleId,int errorHandleId) {
		this.mHandler = paramHandler;
		this.msgid = sendMessageHandleId ;
		this.errormsgid = errorHandleId ;
	}

	public void channelConnected(
			ChannelHandlerContext paramChannelHandlerContext,
			ChannelStateEvent paramChannelStateEvent) throws Exception {
		Log.i("lvjian",
				"------------------------socket发送--------------------------");
		this.mHandler.sendMessage(this.mHandler.obtainMessage(80001, null));
	}

	public void exceptionCaught(
			ChannelHandlerContext paramChannelHandlerContext,
			ExceptionEvent paramExceptionEvent) throws Exception {
		Log.i("lvjian",
				"------------------------socket异常--------------------------");
		Channel localChannel = paramExceptionEvent.getChannel();
		this.mHandler.sendEmptyMessage(errormsgid);
		if (localChannel != null)
			localChannel.close();
	}
	
	public void messageReceived(
			ChannelHandlerContext paramChannelHandlerContext,
			MessageEvent paramMessageEvent) throws Exception {
		Object buffs = paramMessageEvent.getMessage();
		if (buffs instanceof ActivityMsg) {
			ActivityMsg msg = (ActivityMsg) buffs;
			this.mHandler.sendMessage(this.mHandler.obtainMessage(msgid, msg));
		}
	}

}