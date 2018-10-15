package com.sansi.stellar.remote.object;

import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * 
 * @author Chris Liu
 * @version 1.0
 * 
 *  Schedule list format: (Used UTC time, not local time.)  
 *  +-------------------+--------+------------------------------------+
 *  | Field             | Bytes  | Notes                              |
 *  +-------------------+--------+------------------------------------+
 *  | year              | 2      |                                    |
 *  | month             | 1      | 1-12                               |
 *  | day               | 1      | 1-31                               |
 *  | hour              | 1      | 0-23                               |
 *  | minute            | 1      | 0-59                               |
 *  | second            | 1      | 0-59                               |
 *  | type              | 1      | 0: RGB(4)+brightness(1)            |
 *  |                   |        | 1: CCT(2)+brightness(1)+Reserve(2) |
 *  |                   |        | 2: Scene(1)+speed(2)+Reserve(2)    |
 *  +-------------------+--------+------------------------------------+
 * 
 */
public class TimeSchedule implements StellarObject {
	public static final int PACKAGE_SIZE = 13;
	private static final String TIME_ZONE = "UTC";
	
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minute;
	private int second;
	private byte type;
	private byte[] values;
	
	public TimeSchedule() {
		TimeZone tz = TimeZone.getTimeZone(TIME_ZONE);
		Calendar cd = Calendar.getInstance(tz);
		this.year = cd.get(Calendar.YEAR);
		this.month = cd.get(Calendar.MONTH)-Calendar.JANUARY+1;
		this.day = cd.get(Calendar.DATE);
		this.hour = cd.get(Calendar.HOUR) + 
				(cd.get(Calendar.AM_PM) == Calendar.AM ? 0 : 12);
		this.minute = cd.get(Calendar.MINUTE);
		this.second = cd.get(Calendar.SECOND);
		this.type = 0;
		this.values = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00};
	}
	
	public TimeSchedule(TimeSchedule t) {
		this.year  = t.getYear();
		this.month = t.getMonth();
		this.day   = t.getDay();
		this.hour  = t.getHour();
		this.minute = t.getMinute();
		this.second = t.getSecond();
	}
	
	public boolean equals(TimeSchedule t) {
		if (t == null) {
			return false;
		}
		
		if (this == t) {
			return true;
		}
		
		if ((this.year != t.getYear()) ||
		    (this.month != t.getMonth()) ||
		    (this.day != t.getDay()) ||
		    (this.hour != t.getHour()) ||
		    (this.minute != t.getMinute()) ||
		    (this.second != t.getSecond()) ){
			return false;
		}
		
		if (this.type != t.getType()) {
			return false;
		}
		
		if (this.values.length != t.getValues().length) {
			return false;
		}
		
		byte[] v2 = t.getValues();
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
		writeBuf.putShort((short)this.year);
		writeBuf.put((byte)this.month);
		writeBuf.put((byte)this.day);
		writeBuf.put((byte)this.hour);
		writeBuf.put((byte)this.minute);
		writeBuf.put((byte)this.second);
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
		
		this.year = readBuf.getShort();
		this.month = readBuf.get();
		this.day = readBuf.get();
		this.hour = readBuf.get();
		this.minute = readBuf.get();
		this.second = readBuf.get();
		this.type = readBuf.get();
		this.values = new byte[5];
		readBuf.get(this.values);
		return true;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
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

	@Override
	public TimeSchedule getObject() {
		return this;
	}
}
