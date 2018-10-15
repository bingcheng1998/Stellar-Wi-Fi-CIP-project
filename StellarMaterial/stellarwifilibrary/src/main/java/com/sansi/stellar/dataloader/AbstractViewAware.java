package com.sansi.stellar.dataloader;

import android.os.Looper;
import android.view.View;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/13 10:10
 *          类说明
 */
abstract class AbstractViewAware<T> implements ViewAware<T>{
    protected Reference<View> viewRef;

    public AbstractViewAware(View view) {
        if (view == null) throw new IllegalArgumentException("view must not be null");

        this.viewRef = new WeakReference<View>(view);
    }


    @Override
    public View getWrappedView() {
        return viewRef.get();
    }

    @Override
    public boolean isCollected() {
        return viewRef.get() == null;
    }

    @Override
    public int getId() {
        View view = viewRef.get();
        return view == null ? super.hashCode() : view.hashCode();
    }
@Override
    public boolean setData(T data) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        View view = viewRef.get();
        if (view != null) {
            setData(view, data);
            return true;
        }
    } else {
        L.w("Can't set a data into view. You should call AsyncDataLoader on UI thread for it.");
    }
    return false;
    }

    abstract public boolean setData(View view, T data);

}
