package com.sansi.stellar.object;

import android.graphics.Color;

import java.nio.ByteBuffer;

/**
 * Hold the light status
 * @author Chris Liu
 * @version 1.0 
 *
 */
public class LightStatus {
	private int rgbw = 0;
	private int cct  = 6500;
	private int brightness = 50;
	private int scene = 10;
	private int rate = 0;
	private byte failure = 0x00;
	private String name;

	public LightStatus() {		
	}
	
	public LightStatus(byte[] content) {
		if (content.length >= 11) {
			ByteBuffer buf = ByteBuffer.wrap(content);
			failure = buf.get();
			rgbw = buf.getInt();
			cct = (int)(buf.getShort() & 0xFFFF);
			brightness = (int)(buf.get() & 0xFF);
			scene = (int)(buf.get() & 0xFF);
			rate = (int)(buf.getShort() & 0xFFFF);
		}
	}
	
	public int getRGBW() {
		return rgbw;
	}
	
	public void setRGBW(int rgbw) {
		this.rgbw = rgbw;
	}	

	public void setRBGW(int[] rgbw) {
		this.rgbw = this.ColorArray2Int(rgbw);
	}	
	
	public int[] getRGBWColors() {
		return this.Int2ColorArray(this.rgbw);
	}
	
	public int getCCT() {
		return cct;
	}
	
	public void setCCT(int cct) {
		this.cct = cct;
	}
	
	public void setCCT(int[] cct) {
		this.cct = this.ColorArray2Int(cct);
	}
	
	public int getBrightness() {
		return brightness;
	}
	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}
	
	public int getScene() {
		return scene;
	}
	public void setScene(int scene) {
		this.scene = scene;
	}
	
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public byte getFailure() {
		return failure;
	}
	public void setFailure(byte failure) {
		this.failure = failure;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int toColor(int alpha) {
		int argb = Color.argb(alpha, (rgbw >> 24) & 0xFF, (rgbw >> 16) & 0xFF, (rgbw >> 8) & 0xFF);
		return argb;
	}
	
	public int ColorArray2Int(int[] colors) {
		return ((colors[0] & 0xFF) << 24)
				+ ((colors[1] & 0xFF) << 16)
				+ ((colors[2] & 0xFF) << 8)
				+ (colors[3] & 0xFF);
	}
	
	public int[] Int2ColorArray(int value) {
		int[] colors = new int[4];
		colors[0] = (value >> 24) & 0xFF;
		colors[1] = (value >> 16) & 0xFF;
		colors[2] = (value >> 8) & 0xFF;
		colors[3] = value & 0xFF;
		return colors;
	}
	
}
