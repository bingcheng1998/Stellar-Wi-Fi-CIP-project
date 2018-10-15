package com.sansi.stellarWiFi.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * @author lql E-mail: 595308107@qq.com
 * @version 0 创建时间：2017/4/14 11:19
 *          类说明
 */
public class OpenSettingsUtils {
    public static void openDetailsSettings(Activity context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }
}
