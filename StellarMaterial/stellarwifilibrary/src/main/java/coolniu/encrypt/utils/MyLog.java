package coolniu.encrypt.utils;

import android.util.Log;

/**
 * log tool
 * @author lei
 * 
 */

public class MyLog {

	private static boolean isShow = true;
	
	public static void i(String TAG, String msg){
		if(isShow){
			Log.i(TAG, msg);
		}
	}
	
}
