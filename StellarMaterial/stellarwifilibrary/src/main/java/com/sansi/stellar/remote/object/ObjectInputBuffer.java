package com.sansi.stellar.remote.object;

import java.nio.ByteBuffer;
import java.util.Collection;

/**
 * 
 * @author Chris Liu
 * @version 1.0
 * 
 * It should be a temporary object to 
 * maintain the packaged bulb data from server
 * The basic work-flow is:
 * 1. get offset
 * 2. send command to server to get the package data
 * 3. waiting for server response
 * 4. appendPackge
 * 5. decode data to bulb object
 * 6. get next offset
 * We suppose buffer will be decoded after each append action,
 * there only keep the 2 times of package size in buffer.
 */

public class ObjectInputBuffer {	
	private ByteBuffer buffer;
	private int packageSize;
	private int offset;
	private boolean bRemaining;
	private int obj_nums;
	private int len_size;
	private StellarObject.LENGTH_TYPE len_type;
	
	/*
	 * @param packageSize
	 * 			predefined package group size (2048)
	 * 			0 - 2047 means last package group 
	 */
	public ObjectInputBuffer(int packageSize, StellarObject.LENGTH_TYPE len_type) {
		this.packageSize = packageSize;
		this.offset = 0;
		this.bRemaining = true;
		this.obj_nums = 0;
		this.len_type = len_type;
		switch(len_type) {
		case INT:
			len_size = 4;
			break;
		case BYTE:
			len_size = 1;
			break;
		}
		buffer = ByteBuffer.allocate(packageSize*2);
	}
	
	private void init() {
		this.offset = 0;
		this.bRemaining = true;
		this.obj_nums = 0;
	}
	
	/*
	 * append data to current buffer
	 */
	public void appendPackage(byte[] data) {
		bRemaining = (data.length == this.packageSize);
		if (offset == 0) {
			// 1st group package includes total object numbers
			ByteBuffer wrapped = ByteBuffer.allocate(len_size);
			wrapped.put(data, 0, len_size);
			wrapped.flip();
			switch(this.len_type) {
			case INT:
				obj_nums = wrapped.getInt();
				break;
			case BYTE:
				obj_nums = (int)wrapped.get() & 0xFF;
				break;
			}
			buffer.put(data, len_size, data.length-len_size);
		} else {
			buffer.put(data);
		}
		this.offset++;
	}
	
	/*
	 * check if all data has been already pushed to the buffer
	 */
	public boolean hasRemaining() {
		return this.bRemaining;
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	public int getNums() {		
		return this.obj_nums;
	}
	
	/*
	 * Decode the current buffer to bulb object. 
	 */
	public void decodeToObjects(Collection<StellarObject> objs, Class<?> theClass) throws Exception {
		// set to read mode
		buffer.flip();
		while (buffer.hasRemaining()) {
			buffer.mark();
			StellarObject o = (StellarObject)theClass.newInstance();
			if (o.unpackageFromBuffer(buffer)) {
				objs.add(o);
			} else {
				buffer.reset();
				break;
			}
		}
		
		// remove parsed bulb data from buffer 
		// and change to write mode
		buffer.compact();
	}
	
	/*
	 * clear buffer data and set to initialize status
	 */
	public void reset() {
		buffer.clear();
		init();
	}
}
