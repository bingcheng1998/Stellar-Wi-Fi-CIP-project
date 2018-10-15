package com.sansi.stellarWiFi.util;

import android.text.TextUtils;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/9/1 16:13
 *          类说明
 */
public class MacFormat {

    public static String format(String mc) {
        if(TextUtils.isEmpty(mc))return null;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mc.length(); i += 2) {
            sb.append(mc.substring(i, i + 2));
            if ((i + 2) < mc.length()) {
                sb.append("-");
            }
        }
        return sb.toString().toUpperCase();
    }
}
