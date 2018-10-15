package com.sansi.stellar;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Stellar application
 * used to share the global variables and notifying listeners
 *
 * @author Chris Liu
 */
public class StellarApplication extends Application  {
    static StellarApplication app;
    /**
     * @return
     */

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static StellarApplication getApp() {
        return app;
    }
}
