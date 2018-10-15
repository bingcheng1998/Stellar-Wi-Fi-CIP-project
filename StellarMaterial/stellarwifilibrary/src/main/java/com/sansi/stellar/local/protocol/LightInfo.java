package com.sansi.stellar.local.protocol;

import com.sansi.stellar.object.DeviceFilter.LIGHT_TYPES;

public class LightInfo {	
	private String ipAddr="";
	private LIGHT_TYPES deviceStyle = LIGHT_TYPES.UNKNOWN;
	private String mac="";
	private String groupName="";
	private String lightName="";
	
	public String getIpAddr() {
		return ipAddr;
	}
	
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	
	public LIGHT_TYPES getDeviceStyle() {
		return deviceStyle;
	}
	
	public void setDeviceStyle(LIGHT_TYPES deviceStyle) {
		this.deviceStyle = deviceStyle;
	}
	
	public String getMac() {
		return mac;
	}
	
	public void setMac(String mac) {
		this.mac = mac;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getLightName() {
		return lightName;
	}
	
	public void setLightName(String lightName) {
		this.lightName = lightName;
	}

}
