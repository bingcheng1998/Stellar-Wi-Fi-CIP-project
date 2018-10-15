package com.sansi.stellar.remote.object;

import java.nio.ByteBuffer;

/**
 * 
 * @author Chris Liu
 * @version 1.0 
 *
 */
public interface StellarObject {
	/*
	 * define the object length type in the package
	 */
	public static enum LENGTH_TYPE { 
		INT,	// 4 bytes
		BYTE,	// 1 byte
	}
	public static final int PACKAGE_MAXSIZE = 256;

	public byte[] packageToArray() throws Exception;
	public void packageToBuffer(ByteBuffer writeBuffer) throws Exception;
	public boolean unpackageFromArray(byte[] data) throws Exception;
	public boolean unpackageFromBuffer(ByteBuffer readBuffer) throws Exception;	
	public Object getObject();
}
