package com.sansi.stellarWiFi.api;


/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/1 21:40
 *          类说明
 */
public class RealCall<T> implements Call<T> {
    // Guarded by this.
    protected boolean executed = false;
    volatile boolean canceled = false;
    @Override
    public T execute() throws Exception {
        return null;
    }

    @Override
    public void enqueue(Callback<T> callback) {

    }

    @Override
    public void cancel() {
        canceled = true;
    }

    @Override
    public boolean isExecuted() {
        return executed;
    }

    @Override
    public boolean isCanceled() {
        return canceled;
    }
}
