package com.sansi.stellarWiFi.util;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/29 10:32
 *          类说明
 */
public interface MusicPlayChangeListener {

    void onStart();
    void onFftDataCapture(byte[] fft);
    void onStop();

    void onPause();

    void onNext();

    void onPre();

}
