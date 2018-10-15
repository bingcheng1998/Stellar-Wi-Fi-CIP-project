package com.sansi.stellarWiFi.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.stellarwifilibrary.BuildConfig;


/**
 * Log统一管理类
 * 
 * @author lql
 * 
 */
public class L {
	
	private static Toast toast;

	public static boolean isDebuggable() {
		return BuildConfig.DEBUG;
	}
	
	public static void i(String msg) {
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.i(sElement.getFileName(), createLog(sElement, msg));
	}

	

	public static void d(String msg) {
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.d(sElement.getFileName(), createLog(sElement, msg));
	}

	public static void dformat(String format,Object ...args) {
		if(!isDebuggable())return;
		String msg=String.format(format,args);
		StackTraceElement sElement = getStackTrace();
		Log.d(sElement.getFileName(), createLog(sElement, msg));
	}

	public static void eformat(String format,Object ...args) {
		if(!isDebuggable())return;
		String msg=String.format(format,args);
		StackTraceElement sElement = getStackTrace();
		Log.e(sElement.getFileName(), createLog(sElement, msg));
	}

	public static void e(String msg) {
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.e(sElement.getFileName(), createLog(sElement, msg));
	}
	
	public static void w(String msg) {
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.w(sElement.getFileName(), createLog(sElement, msg));
		
	}
	public static void e(String msg,int hierarchy) {
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace(hierarchy);
		Log.e(sElement.getFileName(), createLog(sElement, msg));
	}

	public static void e(String tag,String msg,int hierarchy) {
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace(hierarchy);
		Log.e(tag, createLog(sElement, msg));
	}


	public static void v(String msg) {
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.v(sElement.getFileName(), createLog(sElement, msg));
	}
	
	//下面是传入类名打印log
	public static void ii(String msg){
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.i(sElement.getClassName(), createLog(sElement, msg));
	}
	public static void dd(String msg){
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.d(sElement.getClassName(), createLog(sElement, msg));
	}
	public static void ee(String msg){
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.e(sElement.getClassName(), createLog(sElement, msg));
	}
	public static void vv(String msg){
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.v(sElement.getClassName(), createLog(sElement, msg));
	}
	public static void ww(String msg) {
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.w(sElement.getClassName(), createLog(sElement, msg));
		
	}
	
	// 下面是传入自定义tag的函数
	public static void i(String tag, String msg) {
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.i(tag, createLog(sElement, msg));
	}

	public static void d(String tag, String msg) {
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.d(tag, createLog(sElement, msg));
	}

	public static void e(String tag, String msg) {
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.e(tag, createLog(sElement, msg));
	}

	public static void v(String tag, String msg) {
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.v(tag, createLog(sElement, msg));
	}


	public static void w(String tag,String msg) {
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace();
		Log.w(tag, createLog(sElement, msg));
		
	}
	public static void w(String tag,String msg,int hierarchy) {
		if(!isDebuggable())return;
		StackTraceElement sElement = getStackTrace(hierarchy);
		Log.w(tag, createLog(sElement, msg));
		
	}

	public static void e(Throwable throwable, String message) {
		message = buildMessage(throwable, message);
		e(message, 3);
	}

	public static void e(String tag,Throwable throwable, String message) {
		message = buildMessage(throwable, message);
		e(tag,message,3);
	}
	public static void w(String tag,Throwable throwable){
		w(tag, buildMessage(throwable, null),3);
	}
	public static void w(String tag,Throwable throwable,String message){
		w(tag, buildMessage(throwable, message),3);
	}

	private static String buildMessage(Throwable throwable, String message) {
		if (throwable != null && message != null) {
			message += " : " + throwable.toString();
		}
		if (throwable != null && message == null) {
			message = throwable.toString();
		}
		if (message == null) {
			message = "No message/exception is set";
		}
		return message;
	}
	
	private static StackTraceElement getStackTrace() {
		StackTraceElement sElement = new Throwable().getStackTrace()[2];
		return sElement;
	}
	private static StackTraceElement getStackTrace(int hierarchy) {
		StackTraceElement sElement = new Throwable().getStackTrace()[hierarchy];
		return sElement;
	}
	private static String createLog(StackTraceElement sElement,String log ) {

		StringBuffer buffer = new StringBuffer();
		buffer.append("[T:");
		buffer.append(Thread.currentThread().getName());
		buffer.append(" M:");
		buffer.append(sElement.getMethodName());
		buffer.append(" L:");
		buffer.append(sElement.getLineNumber());
		buffer.append("] ");
		buffer.append(log);
		return buffer.toString();
	}

		/**
		 * 短时间显示Toast
		 * 
		 * @param context
		 * @param message
		 */
		public static void showLog(Context context, CharSequence message) {
			if(!isDebuggable())return;
			if (null == toast) {
				toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
				// toast.setGravity(Gravity.CENTER, 0, 0);
			} else {
				toast.setText(message);
			}
			toast.show();
		}
		
}
