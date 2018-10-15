package com.sansi.stellar.dataloader;

import android.view.View;


/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/13 10:24
 *          类说明
 */
public class ViewAwareImpl<T> extends AbstractViewAware<T> {

    public ViewAwareImpl(View view) {
        super(view);
    }

    @Override
    public boolean setData(View view, T data) {
        return false;
    }


}
