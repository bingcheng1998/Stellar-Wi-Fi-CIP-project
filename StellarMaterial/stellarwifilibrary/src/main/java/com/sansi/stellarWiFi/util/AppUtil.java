package com.sansi.stellarWiFi.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

public class AppUtil {
	private final static String VERSION_KEY = "version.key";

	public static  void appLaunch(Context context, AppLaunch launch) {
		PackageInfo info = getPackageInfo(context);
		int currentVersion = info.versionCode;
		SharedPreferences prefs = getAppInfoSp(context);
		int lastVersion = prefs.getInt(VERSION_KEY, 0);
		if (currentVersion > lastVersion) {
			launch.firstLaunch();
			prefs.edit().putInt(VERSION_KEY, currentVersion).commit();
		} else {
			launch.launch();
		}
	}
	private static SharedPreferences getAppInfoSp(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs;
	}
	public static  PackageInfo getPackageInfo(Context context) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}
	public static boolean appFirstLaunch(Context context) {
		PackageInfo info = getPackageInfo(context);
		int currentVersion = info.versionCode;
		SharedPreferences prefs = getAppInfoSp(context);
		int lastVersion = prefs.getInt(VERSION_KEY, 0);
		if (currentVersion > lastVersion) {
			prefs.edit().putInt(VERSION_KEY, currentVersion).commit();
			return true;
		} else {
			return false;
		}
	}
}
