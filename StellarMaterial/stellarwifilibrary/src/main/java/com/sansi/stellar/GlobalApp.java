package com.sansi.stellar;

import android.util.Pair;

import com.sansi.stellar.object.Countdown;
import com.sansi.stellar.object.Light;

import java.util.Collection;

public interface GlobalApp {
	
	/**
     * @return
     */
	public Pair<String,String> loadSsidInfo();
	
	/**
     * 
     * @param ssidName
     * @param ssidPassword
     */
	public void saveSsidInfo(String ssidName, String ssidPassword);

	/**
     *
     * @param ssidName
     * @param ssidPassword
     */
	public void clearSsidInfo();


    /**
     *
     * @return
     */
	public Pair<String, String> loadLoginInfo();

    /**
     *
     * @param strUserName
     * @param strPassword
     */
    public void saveLoginInfo(String strUserName, String strPassword);


    public void clearLoginInfo();


    public void saveCountdown(String mac, String endTime, int bright);
    
    public Countdown loadCountdown(String mac);
    
    public void clearCountdown(String mac);
    
    
    /**
     * 
     * @return
     */
    public boolean isNetworkAvailable();
    
    /**
     * 
     * @return
     */
    public Collection<Light> getLocalToRemoteDevices();

}
