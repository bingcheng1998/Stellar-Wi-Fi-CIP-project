package com.sansi.stellarWiFi.util;

public class TimerInterval {
    final static long MIN_LIMIT = 50;
    private long mLastTime;
    private long mInterval;
    
    public TimerInterval(long interval) {
        mLastTime = System.currentTimeMillis();
        mInterval = interval < MIN_LIMIT ? MIN_LIMIT : interval;  
    }
    
    public boolean IsTimeout() {
        if ((System.currentTimeMillis() - mLastTime) >= mInterval) {
            mLastTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}
