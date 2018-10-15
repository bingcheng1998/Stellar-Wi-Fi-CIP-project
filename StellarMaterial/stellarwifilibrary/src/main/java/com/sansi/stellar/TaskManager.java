package com.sansi.stellar;

import com.sansi.stellar.object.Light;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Chris Liu
 * @version 1.0 
 *
 */
public abstract class TaskManager {
	public static final String Tag = TaskManager.class.getSimpleName();
	final static Map<Short, String> cmdMap = new HashMap<Short, String>();
	static {
		cmdMap.put((short)0xF1, "<DH Key>");
		cmdMap.put((short)0x00, "<Login>");
		cmdMap.put((short)0x01, "<Logout>");
		cmdMap.put((short)0x02, "<Add User>");
		cmdMap.put((short)0x03, "<Change Pass>");
		cmdMap.put((short)0x04, "<Forgot Pass>");
		cmdMap.put((short)0x08, "<Reset Bulb>");
		cmdMap.put((short)0x10, "<Get Bulbs>");
		cmdMap.put((short)0x12, "<Add Bulb>");
		cmdMap.put((short)0x13, "<Update Bulb>");
		cmdMap.put((short)0x14, "<Del Bulb>");
		cmdMap.put((short)0x20, "<Get Status>");
		cmdMap.put((short)0x21, "<Set RGBW>");
		cmdMap.put((short)0x22, "<Set CCT>");
		cmdMap.put((short)0x23, "<Set Brightness>");
		cmdMap.put((short)0x24, "<Set Scene>");
		cmdMap.put((short)0xFF, "<NAK>");
		
		cmdMap.put((short)0x0C, "<Set Delay Time>");
		cmdMap.put((short)0x0D, "<Query Delay Time>");
	};
	
	final static ArrayList<Short> bgCmds = new ArrayList<Short>();
	static {
		bgCmds.add((short)0x20);
	}

	
	/*
	 * keep the listener to receive task events
	 */
	protected ArrayList<TaskEventListener> mListeners 
			= new ArrayList<TaskEventListener>();
	
    /**
     * 
     * @author Chris Liu
     * @version 1.0 
     *
     */
    public interface TaskEventListener {
    	/**
    	 * callback indicate connection has already been established.
    	 */
    	public void onConnected();
    	
    	/**
    	 * callback indicate connection has been closed.
    	 */
    	public void onClosed();
    	
    	/**
    	 * 
    	 * @param msg
    	 */
    	public void onTaskError(short cmd, int errCode);
    	
    	/**
    	 * 
    	 * @param msg
    	 */
    	public void onTaskSuccess(short cmd);    	
    }
    
    /**
     * 
     * @param listener
     */
    public void registerTaskListener(TaskEventListener listener) {
		if (!mListeners.contains(listener)) {
			mListeners.add(listener);
		}
    }
    
    /**
     * 
     * @param listener
     */
    public void unregisterTaskListener(TaskEventListener listener) {
		if (mListeners.contains(listener)) {
			mListeners.remove(listener);
		}
    }
    
    public static String getCmdName(short cmd) {
		String name = cmdMap.get(cmd);
		if (name == null) {
			return "<Undefined " + cmd + ">";
		}
		return name;
	}
    
    public static boolean isBackgroundCmd(short cmd) {
    	return bgCmds.contains(cmd);
    }
	
	/**
	 * Active current manager
	 * 
	 */
	public abstract void active();
	
	/**
	 * de-active current manager
	 * 
	 */
	public abstract void deactive();
	
	/**
	 * Callback when application get into pause state
	 */
	public abstract void onPause();
	
	/**
	 * Callback when application resume to activity
	 */
	public abstract void onResume();
	
	/**
	 * Callback when application finished
	 */
	public abstract void onDestroy();
	
	/**
	 * 
	 * @return check if client connection is ready
	 */
	public abstract boolean isConnectionReady();
	
	/**
	 * 
	 * @return server is ready to send data
	 */
	public abstract boolean isServerReady();
	
	/**
	 * 
	 */
	public abstract void refreshConnection();
	
	/**
	 * 
	 * @return
	 */
	public abstract boolean isHostResolved();
	
	/**
	 * 
	 * @param targets
	 */
	public abstract void setTargets(Collection<Light> targets);
	
	/**
	 * start scan device process
	 * 
	 */
	public abstract void scanDevices();
	
	/**
	 * stop and cancel current device scan process
	 */
	public abstract void stopScan();
	
	/**
	 * 
	 * @param value
	 * @throws Exception
	 */
	public abstract void setBrightness(final int value) throws Exception;
	
	/**
	 * 
	 * @param rgbw
	 * @throws Exception
	 */
	public abstract void setRGBW(final int[] rgbw) throws Exception;
	
	/**
	 * 
	 * @param value
	 * @param time
	 * @throws Exception
	 */
    public abstract void setCCT(final int value, final int time) throws Exception;
    
    /**
     * 
     * @param mode
     * @param time
     * @throws Excetion
     */
    public abstract void setScene(final int mode, final int time) throws Exception;
    
    /**
     * 
     * @param mac
     * @throws Exception
     */
    public abstract void queryStatus(Light l) throws Exception;
    
    /**
     * 
     * @param ssid
     * @param pass
     */
    public abstract void setSSID(final String ssid, final String pass);
    
    
    
    public abstract void setDelayTime(int data,int time,byte[] delay);
    

    public abstract void queryDelayTime() throws Exception;
    
    public abstract void queryDelayTimeByMac(Light light) throws Exception;
    
    /**
     * 
     * @param 
     * @throws Exception
     */
    public abstract void resetBulb() throws Exception;
    
    /**
     * Update the device properties
     * @param light
     * @param bRefresh
     * @throws Exception
     */
    public abstract void updateDevice(Light l, boolean bRefresh) throws Exception;
    
    /**
     * Remote device from current user account
     * @param strMac
     * @throws Exception
     */
    public abstract void delDevice(String strMac) throws Exception;
    
    /**
     * 
     * @param l
     * @throws Exception
     */
    public abstract void addDevice(Light l) throws Exception;
    
	/**
	 * 
	 * @param devMac
	 */
	public abstract void setOnLineState(String devMac, boolean bOnline);
	
	/**
	 * 
	 * @param group
	 */
	public abstract void delGroup(String group);
	
	/**
	 * 
	 * @param oldName
	 * @param newName
	 */
	public abstract void updateGroup(String oldName, String newName);
}
