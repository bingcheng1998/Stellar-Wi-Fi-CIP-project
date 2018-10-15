package com.sansi.stellarWiFi.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.sansi.stellar.StellarApplication;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import coolniu.encrypt.utils.MyLog;


/**
 * @author lei
 *
 */
public class TimeLoader {
	

	public static TimeLoader mInstance;
	/*
	 * threadPool
	 */
	private ExecutorService mThreadPool;
	private static int DEFAULT_THREAD_COUNT = 1;
	/*
	 * 队列调度方式
	 */
	private Type mType = Type.LIFO;

	public enum Type {
		FIFO, LIFO;
	}

	/*
	 * 任务队列
	 */
	private LinkedList<Runnable> mTaskQueue;
	/*
	 * 轮询线程
	 */
	private Thread mPoolThread;
	private Handler mPoolThreadHandler;
	// 信号量
	/*
	 * 轮询
	 */
	private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
	/*
	 * 线程池
	 */
	private Semaphore mSemaphoreThreadPool;

	private Handler mUIHandler;
	
	private StellarApplication mAPP;
	
	private TimeLoader(int threadCount, Type type) {
		init(threadCount, type);
	}
	    
	private void init(int threadCount, Type type) {

		//mAPP = (StellarApplication)this.getApplication();
		/*
		 * 后台轮询线程
		 */
		mPoolThread = new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				mPoolThreadHandler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						mThreadPool.execute(getTask());
						try {
							mSemaphoreThreadPool.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				};
				mSemaphorePoolThreadHandler.release();
				Looper.loop();
			}
		};
		mPoolThread.start();
		
		/*
		 * create threadPool
		 */
		mThreadPool = Executors.newFixedThreadPool(threadCount);
		mTaskQueue = new LinkedList<Runnable>();
		mType = type;
		mSemaphoreThreadPool = new Semaphore(threadCount);
	}

	public static TimeLoader getInstance(){
        if (mInstance == null){
            synchronized (TimeLoader.class){
                if (mInstance == null){
                    mInstance = new TimeLoader(DEFAULT_THREAD_COUNT, Type.LIFO);
                }
            }
        }
        return mInstance;
    }
	
	
	
	public void showImage(final String mac,final ImageView img) {
		img.setTag(mac);
		 if (mUIHandler == null){
	            mUIHandler = new Handler(){
	            	@Override
					public void handleMessage(Message msg) {
	                    //设置图片
	                    if (img.getTag().toString().equals(mac)){
	                    	img.setVisibility(View.VISIBLE);
	                    }else{
	                    	img.setVisibility(View.GONE);
	                    }

	                }
	            };
	        }
		
		addTask(new Runnable() {
			@Override
			public void run() {
				//耗时操作
				
			
				mUIHandler.sendEmptyMessage(1);
				MyLog.i("lei", "释放信号量");
				//释放信号量
				mSemaphoreThreadPool.release();
			}
		});
	}
	
	
	 private synchronized void addTask(Runnable runnable) {
	        mTaskQueue.add(runnable);
	        try {
	            if (mPoolThreadHandler == null)
	            mSemaphorePoolThreadHandler.acquire();
	        } catch (InterruptedException e) {

	        }
	        mPoolThreadHandler.sendEmptyMessage(0x110);
	  }
	 
	 
	 private Runnable getTask(){
		 
		 if (mType == Type.LIFO){
			 return mTaskQueue.removeLast();
		 }else {
			 return mTaskQueue.removeFirst();
		 }
	 }
	 
	
}
