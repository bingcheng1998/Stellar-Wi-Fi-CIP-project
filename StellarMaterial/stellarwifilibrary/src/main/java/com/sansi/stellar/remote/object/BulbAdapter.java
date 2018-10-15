package com.sansi.stellar.remote.object;

import com.sansi.stellar.object.DeviceFilter.LIGHT_TYPES;
import com.sansi.stellar.object.Light;

import java.nio.ByteBuffer;

import coolniu.encrypt.utils.Hex;

public class BulbAdapter implements StellarObject {
	private Light light;
	
	public BulbAdapter() {
		this.light = new Light();
	}
	
	public BulbAdapter(Light l) {
		this.light = l;
	}	
	
	public boolean equals(Light b) {
		return this.light.equals(b);
	}
	
	public boolean equals(BulbAdapter b) {
		return this.light.equals((Light)b.getObject());
	}
	
	public static String toRemoteGroup(String strGroup) {
		if (strGroup.isEmpty()) {
			return "*";
		}
		return strGroup;
	}
	
	public static String toLocalGroup(String strGrp) {
		if (strGrp.equals("*")) {
			return "";
		}
		return strGrp;
	}
	
	@Override
	public Light getObject() {
		return this.light;
	}

	@Override
	public byte[] packageToArray() throws Exception {
		byte[] name = light.getName().getBytes("utf-8");
		byte[] group = light.getGroup().getBytes("utf-8");		
		ByteBuffer buffer = ByteBuffer.allocate(14+name.length+group.length);
		packageToBuffer(buffer);
		return buffer.array();
	}
	
	@Override
	public void packageToBuffer(ByteBuffer writeBuf) throws Exception {
		byte[] mac = Hex.hexStringToByteArray(light.getMac());
		byte[] name = light.getName().getBytes("utf-8");
		byte[] group = light.getGroup().getBytes("utf-8");
		
		writeBuf.put(mac);
		writeBuf.put((byte)light.getSoftVer());
		writeBuf.put((byte)light.getHdVer());
		writeBuf.putShort(typeToInt(light.getType()));
		writeBuf.put((byte)name.length);
		writeBuf.put(name);
		writeBuf.put((byte)group.length);
		writeBuf.put(group);
	}
	
	@Override
	public boolean unpackageFromArray(byte[] data) throws Exception {	
		ByteBuffer buffer = ByteBuffer.wrap(data);
		return unpackageFromBuffer(buffer);
	}
	
	@Override
	public boolean unpackageFromBuffer(ByteBuffer readBuf) throws Exception {
		if (readBuf.position() + 16 > readBuf.limit()) {
			return false;
		}
		
		byte[] mac = new byte[8];
		readBuf.get(mac);
		light.setMac(Hex.encodeHexStr(mac, false));
		
		light.setSoftVer(readBuf.get());
		light.setHdVer(readBuf.get());
		light.setType(intToType(readBuf.getShort()));
		int len = readBuf.get();
			
		if (readBuf.position()+len+2 > readBuf.limit()) {
			return false;
		}
		byte[] name = new byte[len];
		readBuf.get(name);
		light.setName(new String(name, "utf-8"));
		
		len = readBuf.get();
		if (readBuf.position()+len > readBuf.limit()) {
			return false;
		}
		
		byte[] group = new byte[len];
		readBuf.get(group);
		String strGroup = new String(group, "utf-8"); 
		light.setGroup(toLocalGroup(strGroup));
		
		return true;		
	}
	
	/**
	 * Used for single bulb property
	 * MAC(8) | SOFT_VER(1) | HARD_VER(1) | TYPE(2) | name Length(1) | name(>=1) | group(>=1) 
	 * @return
	 * @throws Exception
	 */
	public byte[] packagePropertyToArray() throws Exception {
		byte[] name = light.getName().getBytes("utf-8");		
		byte[] group = toRemoteGroup(light.getGroup()).getBytes("utf-8");
		ByteBuffer buffer = ByteBuffer.allocate(13+name.length+group.length);
		packagePropertyToBuffer(buffer);
		return buffer.array();
	}
	
	public void packagePropertyToBuffer(ByteBuffer writeBuf) throws Exception {
		byte[] mac = Hex.hexStringToByteArray(light.getMac());
		byte[] name = light.getName().getBytes("utf-8");
		byte[] group = toRemoteGroup(light.getGroup()).getBytes("utf-8");
		
		writeBuf.put(mac);
		writeBuf.put((byte)light.getSoftVer());
		writeBuf.put((byte)light.getHdVer());
		writeBuf.putShort(typeToInt(light.getType()));
		writeBuf.put((byte)name.length);
		writeBuf.put(name);
		writeBuf.put(group);
	}
	
	public static LIGHT_TYPES intToType(short nType) {
		switch (nType) {
		case 0x1001:
			return LIGHT_TYPES.RGB;
		case 0x1002:
			return LIGHT_TYPES.CCT;
		default:
			break;
		}
		return LIGHT_TYPES.UNKNOWN;
	}
	
	public static short typeToInt(LIGHT_TYPES type) {
		switch (type) {
		case RGB:
			return (short)0x1001;
		case CCT:
			return (short)0x1002;
		default:
			break;
		}
		return (short)0x00;
	}
}
