package com.sansi.stellarWiFi.api;

/**
* @author lql E-mail: 595308107@qq.com
* @version 0 创建时间：2017年3月1日 下午9:02:29 
* 类说明 
*/
public interface Call<T> {
	T execute() throws Exception;
	void enqueue(Callback<T> callback);
	/** Cancels the request, if possible. Requests that are already complete cannot be canceled. */
	void cancel();

	boolean isExecuted();

	boolean isCanceled();
}
