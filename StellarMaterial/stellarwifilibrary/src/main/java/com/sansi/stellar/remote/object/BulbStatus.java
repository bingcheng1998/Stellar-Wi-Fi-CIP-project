package com.sansi.stellar.remote.object;

import java.nio.ByteBuffer;

/**
 * 
 * @author Chris Liu
 * @version 1.0
 * 
 * Device status format
 *  +-----------+-------+-------------------------------+
 *  | Field     | Bytes | Notes                         |
 *  +-----------+-------+-------------------------------+
 *  | fault     | 1     |                               |
 *  | RGB       | 4     |                               |
 *  | CCT       | 2     |                               |
 *  | brightness| 1     |                               |
 *  | scene No. | 1     |                               |
 *  | speed     | 2     |                               |
 *  +-----------+-------+-------------------------------+
 *  
 *  Fault:
 *  	0xFF: not support failure check
 *  	0x00: no failure
 *  	others: has failure.
 */
public class BulbStatus {
	public static enum FAILURE_CODE {
		OK,				// no failure
		NOT_SUPPORT,	// not support fault diagnosis
		FAULT,			// has failures 
	};
	
	private static final int PACKAGE_SIZE = 11;

	private int rgb = 0;
	private short cct = 0;
	private byte brightness = 0;
	private short speed = 0;
	private byte scene = 0;
	private byte fault = 0x00;
	
	public BulbStatus() {
	}
	
	public BulbStatus(BulbStatus bs) {
		fault = bs.getFault();
		rgb = bs.getRGB();
		cct = bs.getCCT();
		brightness = bs.getBrightness();
		speed = bs.getSpeed();
		scene = bs.getScene();
	}
	
	public byte[] packageToArray() throws Exception {
		ByteBuffer buffer = ByteBuffer.allocate(PACKAGE_SIZE);
		packageToBuffer(buffer);
		return buffer.array();
	}
	
	public void packageToBuffer(ByteBuffer writeBuf) throws Exception {
		writeBuf.put((byte)fault);
		writeBuf.putInt(rgb);
		writeBuf.putShort((short)cct);
		writeBuf.put((byte)brightness);
		writeBuf.put((byte)scene);
		writeBuf.putShort((short)speed);
	}

	public boolean unpackageFromArray(byte[] data) throws Exception {
		if (data.length < PACKAGE_SIZE) {
			return false;
		}
		
		ByteBuffer buf = ByteBuffer.wrap(data);
		return unpackageFromBuffer(buf);
	}
	
	public boolean unpackageFromBuffer(ByteBuffer readBuf) throws Exception {
		if ((readBuf.position()+PACKAGE_SIZE) > readBuf.limit()) {
			return false;
		}
		
		fault = readBuf.get();
		rgb = readBuf.getInt();
		cct = readBuf.getShort();
		brightness = readBuf.get();
		scene = readBuf.get();
		speed = readBuf.getShort();
		return true;
	}
	
	public FAILURE_CODE getFaultCode() {
		FAILURE_CODE ret = FAILURE_CODE.FAULT;
		switch ((byte)fault) {
		case 0x00:
			ret = FAILURE_CODE.OK;
			break;
		case (byte)0xFF:
			ret = FAILURE_CODE.NOT_SUPPORT;
			break;
		}
		return ret;
	}
	
	public void setFault(byte fault) {
		this.fault = fault;
	}
	
	public byte getFault() {
		return this.fault;
	}
	
	public void setRGB(int rgb) {
		this.rgb = rgb;
	}
	
	public int getRGB() {
		return this.rgb;
	}	
	
	public void setCCT(short cct) {
		this.cct = cct;
	}
	
	public short getCCT() {
		return this.cct;
	}
	
	public void setBrightness(byte brightness) {
		this.brightness = brightness;
	}
	
	public byte getBrightness() {
		return this.brightness;
	}
	
	public void setSecne(byte scene) {
		this.scene = scene;
	}
	
	public byte getScene() {
		return this.scene;
	}
	
	public void setSepped(short speed) {
		this.speed = speed;
	}
	
	public short getSpeed() {
		return this.speed;
	}	
}
