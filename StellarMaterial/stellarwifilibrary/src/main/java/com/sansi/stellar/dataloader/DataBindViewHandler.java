package com.sansi.stellar.dataloader;

import android.view.View;

public interface DataBindViewHandler<T> {
    void onBindView(T data, View view);
}