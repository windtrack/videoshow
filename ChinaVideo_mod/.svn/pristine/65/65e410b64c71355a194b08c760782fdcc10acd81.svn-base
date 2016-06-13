package com.zhipu.chinavideo.util;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class ThreadPoolWrap {
	private static final int DEFAULT_COREPOOLSIZE = 2;//核心线程数
	private static final long DEFAULT_KEEP_ALIVE_TIME = 30L;//允许的空闲时间
	private static final int DEFAULT_MAXIMUM_POOLSIZE = 30;//线程池的最大允许线程数（20160504将线程池最大尺寸从20更改到30）
	private static ThreadPoolWrap instance;//本类的单例
	private BlockingQueue<Runnable> bq;//队列
	private ThreadPoolExecutor executor ;//线程池对象、
	private static final int QUEUE_SIZE = 50;//队列最大允许数为50

	private ThreadPoolWrap() {
		executor = null;
		bq = new ArrayBlockingQueue(QUEUE_SIZE);
		executor = new ThreadPoolExecutor(DEFAULT_COREPOOLSIZE, DEFAULT_MAXIMUM_POOLSIZE, DEFAULT_KEEP_ALIVE_TIME, TimeUnit.SECONDS, bq);
	}

	public static ThreadPoolWrap getThreadPool() {
		if (instance == null)
			instance = new ThreadPoolWrap();
		return instance;
	}

	public void executeTask(Runnable paramRunnable) {
		try {
			this.executor.execute(paramRunnable);
		}catch (Exception e) {//当线程池执行线程的时候，有可能会超出最大线程数,如果不 catch的话，那么就会出现问题
			Log.e(APP.TAG, "Performing tasks appear abnormal, you can try again");
		}
	}

	public boolean isThreadPoolActive() {
		boolean flag = true;
		if (executor.getActiveCount() < 1)
			flag = false;
		return flag;
	}

	public void removeTask(Runnable paramRunnable) {
		this.executor.remove(paramRunnable);
	}

	public void shutdown() {
		this.executor.shutdown();
		instance = null;
	}
}
