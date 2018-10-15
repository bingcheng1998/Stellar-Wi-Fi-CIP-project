package com.sansi.stellarWiFi.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/2/18 20:16
 *          类说明
 */
public class NetUtils {
    public static boolean isNetworkAvailable(Context c) {
        boolean netSataus = false;
        ConnectivityManager conManager = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            netSataus = networkInfo.isAvailable();
        }
        return netSataus;
    }
}