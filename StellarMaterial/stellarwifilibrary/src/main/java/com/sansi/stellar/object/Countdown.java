package com.sansi.stellar.object;


/**
 * @author lei
 */
public class Countdown {
	
	private String mac;
	private String endTime;
	private int bright_data;
	
	public Countdown(String mac,String mEndTime,int mBright_data){
		this.mac = mac;
		this.endTime = mEndTime;
		this.bright_data = mBright_data;
	}
	

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public int getBright_data() {
		return bright_data;
	}

	public void setBright_data(int bright_data) {
		this.bright_data = bright_data;
	}

}
