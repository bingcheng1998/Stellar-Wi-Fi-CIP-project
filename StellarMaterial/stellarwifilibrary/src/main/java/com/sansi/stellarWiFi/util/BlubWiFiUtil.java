package com.sansi.stellarWiFi.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/7/10 17:15
 *          类说明
 */
public class BlubWiFiUtil {
    /**
     * 判断连接的是否是灯的WIFI
     * */
    public static boolean isBlubWifi(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        if (ssid != null) {
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
//            L.e("ssid:" + ssid);
            if (ssid.length() >= 20) {
                if (ssid.startsWith("SLCCT") || ssid.startsWith("SLRGB")) {
                    return true;
                }
            }
        }
        return false;
    }
}
