package com.sansi.stellar.remote.object;

import java.nio.ByteBuffer;

/**
 * 
 * @author Chris Liu
 * @version 1.0
 * 
 *  Schedule list format:   
 *  +-------------------+--------+------------------------------------+
 *  | Field             | Bytes  | Notes                              |
 *  +-------------------+--------+------------------------------------+
 *  | schedule count    | 1      |                                    |
 *  | hour              | 1      | 0-23                               |
 *  | minute            | 1      | 0-59                               |
 *  | second            | 1      | 0-59                               |
 *  | week              | 1      | bit0-Sun, ......, bit6 - Sat       |
 *  | type              | 1      | 0: RGB(4)+brightness(1)            |
 *  |                   |        | 1: CCT(2)+brightness(1)+Reserve(2) |
 *  |                   |        | 2: Scene(1)+speed(2)+Reserve(2)    |
 *  +-------------------+--------+------------------------------------+
 * 
 */
public class CycleSchedule implements StellarObject {
	public static final int PACKAGE_SIZE = 10;
	
	private int hour;
	private int minute;
	private int second;
	private byte week;
	private byte type;
	private byte[] values;
	
	public CycleSchedule() {
		this.hour = 0; 
		this.minute = 0;
		this.second = 0;
		this.setWeek((byte)0x00);
		this.type = 0;
		this.values = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00};
	}
	
	public CycleSchedule(CycleSchedule cs) {
		this.hour  = cs.getHour();
		this.minute = cs.getMinute();
		this.second = cs.getSecond();
		this.setWeek(cs.getWeek());
	}
	
	public boolean equals(CycleSchedule cs) {
		if (cs == null) {
			return false;
		}
		
		if (this == cs) {
			return true;
		}
		
		if ((this.week != cs.getWeek()) ||
		    (this.hour != cs.getHour()) ||
		    (this.minute != cs.getMinute()) ||
		    (this.second != cs.getSecond()) ){
			return false;
		}
		
		if (this.type != cs.getType()) {
			return false;
		}
		
		if (this.values.length != cs.getValues().length) {
			return false;
		}
		
		byte[] v2 = cs.getValues();
		for (int i=0; i<this.values.length; i++) {
			if (this.values[i] != v2[i]) {
				return false;
			}
		}
		return true;		
	}

	@Override
	public byte[] packageToArray() throws Exception {
		ByteBuffer buffer = ByteBuffer.allocate(PACKAGE_SIZE);
		packageToBuffer(buffer);
		return (byte[])buffer.flip().array();
	}

	@Override
	public void packageToBuffer(ByteBuffer writeBuf) throws Exception {
		writeBuf.put((byte)this.hour);
		writeBuf.put((byte)this.minute);
		writeBuf.put((byte)this.second);
		writeBuf.put(this.week);
		writeBuf.put((byte)this.type);
		writeBuf.put(this.values);
	}

	@Override
	public boolean unpackageFromArray(byte[] data) throws Exception {
		ByteBuffer buffer = ByteBuffer.wrap(data);
		return unpackageFromBuffer(buffer);
	}

	@Override
	public boolean unpackageFromBuffer(ByteBuffer readBuf) throws Exception {
		if (readBuf.position()+PACKAGE_SIZE > readBuf.limit()) {
			return false;
		}
		
		this.hour = readBuf.get();
		this.minute = readBuf.get();
		this.second = readBuf.get();
		this.week = readBuf.get();
		this.type = readBuf.get();
		this.values = new byte[5];
		readBuf.get(this.values);
		return true;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte[] getValues() {
		return values;
	}

	public void setValues(byte[] values) {
		this.values = values;
	}

	public byte getWeek() {
		return week;
	}

	public void setWeek(byte week) {
		this.week = week;
	}

	@Override
	public CycleSchedule getObject() {
		return this;
	}
}
