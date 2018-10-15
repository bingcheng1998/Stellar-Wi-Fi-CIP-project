package com.sansi.stellarWiFi.api;

import java.math.BigInteger;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/3/3 13:32
 *          类说明
 */
public class MacFormatUtis {
    //16位mac地址
    public static long mac2long(String mac) {
        checkMac(mac);
        BigInteger b = new BigInteger(mac, 16);
        return b.longValue();
    }

    public static void checkMac(String mac) {
        if (mac == null || !mac.toLowerCase().matches("[0-f]{16}"))
            throw new IllegalArgumentException("mac 地址非法");
    }
}
