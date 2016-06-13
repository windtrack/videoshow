package com.zhipu.chinavideo.socket;

/**
 * 
 */


import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.zhipu.chinavideo.entity.ActivityMsg;

/**
 * @author lushouzhi
 * 
 */
public class DecoderUtils  extends FrameDecoder{
	
//	public static ActivityMsg decode(byte[] bytes) {
//		ByteBuffer in = ByteBuffer.wrap(bytes);
//		ActivityMsg msg = new ActivityMsg();
//		int start = in.position();
//		if (in.remaining() > 4) {
//			int length = in.getInt();
//			if (length < 0) {
//				in.position(start);
//				return null;
//			} else if (in.remaining() < length - 4) {
//				in.position(start);
//				return null;
//			}
//			int tid = in.getInt();
//			int bodyLen = in.getInt();
//			byte[] value = new byte[bodyLen];
//			in.get(value);
//			String text = BigEndianUtils.readUTF(value);
//			msg.setTid(tid);
//			msg.setMsg(text);
//		}
//		return msg;
//	}

	@Override
	protected Object decode(ChannelHandlerContext arg0, Channel arg1,
			ChannelBuffer in) throws Exception {
//		ByteBuffer in = ByteBuffer.wrap(bytes);
		ActivityMsg msg = new ActivityMsg();
		in.markReaderIndex();
		if (in.readableBytes() > 4) {
			int length = in.readInt();
			if (length < 0) {
				in.resetReaderIndex();
				return null;
			} else if (in.readableBytes() < length - 4) {
				in.resetReaderIndex();
				return null;
			}
			int tid = in.readInt();
			int bodyLen = in.readInt();
			byte[] value = new byte[bodyLen];
			in.readBytes(value);
			String text = BigEndianUtils.readUTF(value);
			msg.setTid(tid);
			msg.setMsg(text);
		}
		return msg;
	}
}
