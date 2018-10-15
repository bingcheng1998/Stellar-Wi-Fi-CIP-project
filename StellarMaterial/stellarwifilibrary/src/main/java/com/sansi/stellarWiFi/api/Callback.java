package com.sansi.stellarWiFi.api;
/** 
* @author lql E-mail: 595308107@qq.com
* @version 0 创建时间：2017年3月1日 下午9:03:22 
* 类说明 
*/
public interface Callback<T> {
	  void onResponse(T t);

	  /**
	   * Invoked when a network exception occurred talking to the server or when an unexpected
	   * exception occurred creating the request or processing the response.
	   */
	  void onFailure(Throwable t);
}
