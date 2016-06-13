
package com.zhipu.chinavideo.socket;

import android.os.Handler;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class BaseClient {
	public static final int CHAT_MESSAGE = 80000;
	public static final int CHAT_SERVER_CONNECT = 80001;
	ClientBootstrap bootstrap;
	Channel ch;
	ChannelFuture channelFuture = null;

	public void disconnect() {
		try {
			Channel localChannel = this.channelFuture.awaitUninterruptibly()
					.getChannel();
			if (this.ch != null) {
				this.ch.disconnect();
				this.ch.close();
				this.ch = null;
			}
			if (localChannel != null)
				localChannel.close().awaitUninterruptibly();
			if (this.bootstrap != null)
				this.bootstrap.releaseExternalResources();
			if (this.channelFuture != null)
				this.channelFuture.getChannel().close();
			return;
		} catch (Exception localException) {
			while (true)
				localException.printStackTrace();
		}
	}

	public boolean sendmsg(byte[] paramString) {
		if ((this.ch != null) && (this.ch.isConnected())) {
			this.ch.write(ChannelBuffers.wrappedBuffer(paramString));
		}
		for (boolean bool = true;; bool = false) {
			return bool;
		}
	}

	
	public void start(String paramString, int paramInt,
			final Handler paramHandler,final int msgid,final int errormsgid) throws Exception {
		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());
		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
			public ChannelPipeline getPipeline() {
				ChannelPipeline pipeline = Channels.pipeline();
				pipeline.addLast("encode", new StringEncoder());
				pipeline.addLast("decode", new DecoderUtils());
				pipeline.addLast("handler", new ClientHandler(paramHandler,msgid,errormsgid));
				return pipeline;
			}
		});
		bootstrap.setOption("tcpNoDelay", true);
		bootstrap.setOption("keepAlive", true);
		channelFuture = bootstrap.connect(new InetSocketAddress(paramString,paramInt));
		channelFuture.awaitUninterruptibly();
		ch = channelFuture.awaitUninterruptibly().getChannel();
	}
}