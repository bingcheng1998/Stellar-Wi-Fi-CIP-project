package com.sansi.stellarWiFi.api;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/8/22 13:17
 *          类说明
 */
public interface Callback2<T> extends Callback {
    void onResponse(Call<T> call, T t);

    /**
     * Invoked when a network exception occurred talking to the server or when an unexpected
     * exception occurred creating the request or processing the response.
     */
    void onFailure(Call<T> call, Throwable t);
}
