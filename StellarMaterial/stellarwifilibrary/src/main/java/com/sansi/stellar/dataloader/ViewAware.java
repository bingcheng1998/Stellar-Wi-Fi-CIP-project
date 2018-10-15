package com.sansi.stellar.dataloader;

import android.view.View;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/13 10:08
 *          类说明
 */
public interface ViewAware<T> {
    View getWrappedView();
    boolean isCollected();
    int getId();
    boolean setData(T data);
}
