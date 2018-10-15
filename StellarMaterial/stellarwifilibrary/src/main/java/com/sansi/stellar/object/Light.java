package com.sansi.stellar.object;

import com.sansi.stellar.bean.LightInfo;
import com.sansi.stellar.object.DeviceFilter.LIGHT_TYPES;

import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;

;

/**
 * 
 * @author Chris Liu
 * @version 1.0 
 *
 */
public class Light implements Serializable{
	protected String mac_   = "0000000000000000";
	protected String name_  = "";
	protected int soft_ver_ = 0;
	protected int hd_ver_   = 0;
	protected LIGHT_TYPES type_ = LIGHT_TYPES.UNKNOWN;
	protected String group_ = "";
	protected boolean bVirtual = false;
	protected String ipAddr = "";


	private boolean allowAddRemote=true;//是否添加远程
	protected  boolean online=false;
	
	public static String addrToString(long mac_addr) {
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(mac_addr);
		return coolniu.encrypt.utils.Hex.encodeHexStr(buffer.array(), false);		
	}
	
	public static long StringToAddr(String strMac) {
		BigInteger b=new BigInteger(strMac,16);
		return b.longValue();
	}

	public Light() {
	}
	
	public Light(Light b) {
		mac_ = b.getMac();
		name_ = b.getName();
		soft_ver_ = b.getSoftVer();
		hd_ver_ = b.getHdVer();
		type_ = b.getType();
		group_ = b.getGroup();
	}

	public String getMac() {
		return mac_;
	}

	public void setMac(long mac_addr) {
		this.mac_ = Light.addrToString(mac_addr);
	}
	
	public long getMacLong() {
		return Light.StringToAddr(mac_);
	}
	
	public void setMac(String strMac) {
		setMac(Light.StringToAddr(strMac));
	}

	public String getName() {
		return name_;
	}

	public void setName(String name_) {
		this.name_ = name_;
	}

	public int getSoftVer() {
		return soft_ver_;
	}

	public void setSoftVer(int soft_ver_) {
		this.soft_ver_ = soft_ver_;
	}

	public int getHdVer() {
		return hd_ver_;
	}

	public void setHdVer(int hd_ver_) {
		this.hd_ver_ = hd_ver_;
	}

	public LIGHT_TYPES getType() {
		return type_;
	}

	public void setType(LIGHT_TYPES type_) {
		this.type_ = type_;
	}

	public String getGroup() {
		return group_;
	}

	public void setGroup(String group_) {
		this.group_ = group_;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean equals(Light b) {
		if (b == null) {
			return false;
		}
		
		if (this == b) {
			return true;
		}

		return (mac_.equalsIgnoreCase(b.getMac())) &&
			   (name_.equals(b.getName())) &&
			   (type_ == b.getType()) &&
			   (soft_ver_ == b.getSoftVer()) &&
			   (hd_ver_ == b.getHdVer()) &&
			   (group_.equals(b.getGroup()));	
	}
	
	public boolean hasChanged(Light b) {
		if (b == null) {
			return true;
		}
		
		if (this == b) {
			return false;
		}
		
		return !mac_.equalsIgnoreCase(b.getMac())
				|| !name_.equals(b.getName())
				|| type_ != b.getType()
				|| !group_.equals(b.getGroup());	
	}

	public boolean isVirtual() {
		return bVirtual;
	}

	public void setVirtual(boolean bVirtual) {
		this.bVirtual = bVirtual;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public boolean isAllowAddRemote() {
		return allowAddRemote;
	}

	public void setAllowAddRemote(boolean allowAddRemote) {
		this.allowAddRemote = allowAddRemote;
	}

	public LightInfo toLightInfo() {
		LightInfo lightInfo = new LightInfo();
		lightInfo.setMac_(this.getMac());
		lightInfo.setName_(this.getName());
		lightInfo.setType_(this.getType());
		lightInfo.setGroup_(this.getGroup());
		lightInfo.setHd_ver_(this.getHdVer());
		lightInfo.setIpAddr(this.getIpAddr());
		lightInfo.setSoft_ver_(this.getSoftVer());
		lightInfo.setAllowAddRemote(this.isAllowAddRemote());
		lightInfo.setOnline(this.isOnline());
		return lightInfo;
	}

	@Override
	public String toString() {
		return "Light{" +
				"mac_='" + mac_ + '\'' +
				", name_='" + name_ + '\'' +
				", soft_ver_=" + soft_ver_ +
				", hd_ver_=" + hd_ver_ +
				", type_=" + type_ +
				", group_='" + group_ + '\'' +
				", bVirtual=" + bVirtual +
				", ipAddr='" + ipAddr + '\'' +
				'}';
	}
}
