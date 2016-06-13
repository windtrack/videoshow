package com.zhipu.chinavideo.manager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;

import java.util.Queue;

import com.zhipu.chinavideo.entity.ChatMessage;
import com.zhipu.chinavideo.util.RunTextView;

public class RunwayManager {
	public static RunwayManager instance = null;
	private Lock queueLock = null;// 消息锁
	private Queue<ChatMessage> runwayQueue = null;// 消息队列
	private RunTextView sTextView = null;// 跑马灯的TextView
//	private ChatMessage tempMesage = null;// 消息
	private int queueSize = 200;
	private String runMessage = "";

	private int duration = 10000;
	// private SmilyParse smilyparse;
	private RunwayManager() {
		runwayQueue = null;
		queueLock = null;
		sTextView = null;
//		context = null;
//		tempMesage = null;
		runwayQueue = new ArrayBlockingQueue(queueSize);
		queueLock = new ReentrantLock();
	}

	private void add(ChatMessage paramChatMessage) {
		this.queueLock.lock();
//		if (this.runwayQueue != null) {
//			// if (!this.runwayQueue.offer(paramChatMessage)) {
//			// this.runwayQueue.poll();
//			// this.runwayQueue.offer(paramChatMessage);
//			// }
//		} 
		
		if(this.runwayQueue==null){
			this.runwayQueue = new ArrayBlockingQueue<ChatMessage>(queueSize);
		}
		this.runwayQueue.offer(paramChatMessage);
		if (paramChatMessage.getTid() == 14) {
			this.runwayQueue.offer(paramChatMessage);
			this.runwayQueue.offer(paramChatMessage);
		}
		this.queueLock.unlock();
	}

	private void display(SpannableStringBuilder paramSpannableStringBuilder) {
		String msg = paramSpannableStringBuilder.toString();
		boolean isCanGoon = true;
		if (msg != null) {
			if ("".equals(msg.trim())) {
				this.sTextView.setVisibility(View.GONE);
				isCanGoon = false;
			} else {
				this.sTextView.setVisibility(View.VISIBLE);
			}
		} else {
			this.sTextView.setVisibility(View.GONE);
			isCanGoon = false;
		}
		this.sTextView.setText("");
		this.sTextView.pauseScroll();
		this.sTextView.append(paramSpannableStringBuilder);
		// int duration = 10000*this.runwayQueue.size();
		this.sTextView.setDuration(duration);
		if (isCanGoon) {
			this.sTextView.startScroll();
		}
	}

	public static RunwayManager getInstance() {
		if (instance == null)
			instance = new RunwayManager();
		return instance;
	}

	public static void release() {
		if (instance != null)
			instance = null;
	}

	public void handleMessage(ChatMessage paramChatMessage) {
		if (paramChatMessage != null) {
			add(paramChatMessage);
			this.sTextView.pauseScroll();
			if("".equals(runMessage)){
				this.sTextView.setDuration(2000);
				this.sTextView.startScroll();
			}else{
				this.sTextView.resumeScroll();
			}
			this.sTextView.setVisibility(View.VISIBLE);
		}
	}

	public void init(RunTextView paramRunTextView, Context paramContext) {
		this.sTextView = paramRunTextView;
//		this.context = paramContext;
		this.sTextView.init(new RunTextView.OnCompleteListener() {
			public void onComplete() {
				RunwayManager.this.show();
			}
		});
	}
	public void show() {
		this.queueLock.lock();
		SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(
				"");
		// for (int i = 0; i < this.runwayQueue.size(); i++) {
		// final ChatMessage localChatMessage;
		// localChatMessage = (ChatMessage) this.runwayQueue.poll();//
		// 从队列中取出message
		// if (localChatMessage == null) {
		// break;
		// }
		// if(localChatMessage.getTid()==14){
		// textMsg = localChatMessage;
		// }
		// if ((this.tempMesage == localChatMessage) && (i != 0))
		// break;
		// if((localChatMessage.getTid()==14 && textMsg.time>2)){
		// textMsg=null;
		// break;
		// }
		// if(localChatMessage.getTid()==14){
		// textMsg.time ++;
		// }
		// this.tempMesage = localChatMessage;
		// this.runwayQueue.offer(localChatMessage);// 加入message
		// String s = localChatMessage.getSay();
		// localSpannableStringBuilder = new SpannableStringBuilder(s);
		// }
		if (this.runwayQueue.size() > 0) {
			ChatMessage message = this.runwayQueue.poll();
			localSpannableStringBuilder = new SpannableStringBuilder(message.getSay());
			runMessage = message.getSay();
		}else{
			runMessage = "";
		}
		this.queueLock.unlock();
		display(localSpannableStringBuilder);
		return;
	}
	public void recyle() {
		this.runwayQueue = null;
	}
}