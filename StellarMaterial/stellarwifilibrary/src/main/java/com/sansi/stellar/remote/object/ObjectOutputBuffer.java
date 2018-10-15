package com.sansi.stellar.remote.object;

import java.nio.ByteBuffer;
import java.util.Collection;

/**
 * 
 * @author Chris Liu
 * @version 1.0
 * 
 * It should be a temporary object to maintain the package output buffer data
 */
public class ObjectOutputBuffer {
	private int initNums;
	private ByteBuffer buffer;
	private int offset;
	private int packageSize;
	private int objSize;
	private StellarObject.LENGTH_TYPE len_type;
	private int len_size;	
	
	/*
	 * Generate the buffer data
	 * @param initNums: device list
	 * @param size: package size
	 */
	public ObjectOutputBuffer(int initNums, int objSize, int packageSize, 
			                  StellarObject.LENGTH_TYPE len_type) {
		this.initNums = initNums;
		this.packageSize = packageSize;
		this.objSize = objSize;
		this.len_type = len_type;
		switch(len_type) {
		case INT:
			len_size = 4;
			break;
		case BYTE:
			len_size = 1;
			if (this.initNums > 255) this.initNums = 255; 
			break;
		}		
		buffer = ByteBuffer.allocate(initNums*objSize+len_size);
		buffer.limit(0);
		init();
	}
	
	private void init() {
		this.offset = 0;		
	}
	
	/*
	 * Push the device list info into buffer.
	 * It will clear the old data.
	 */
	public void setObjects(Collection<StellarObject> objs) throws Exception {
		int accept_size = objs.size();
		if (objs.size() > this.initNums) {
			if (this.len_type == StellarObject.LENGTH_TYPE.BYTE 
				&& objs.size() > 255) {
				this.initNums = 255;
				accept_size = 255;
			} else {
				this.initNums = objs.size();
			}			
			// delete the old buffer and assign a new larger buffer
			buffer = null;
			buffer = ByteBuffer.allocate(initNums*objSize+len_size);			
		} else {
			buffer.clear();
		}
		
		switch(this.len_type) {
		case BYTE:
			buffer.put((byte)accept_size);
			break;
		case INT:
			buffer.putInt(accept_size);
			break;
		}
		
		int idx = 0;
		for (StellarObject o : objs) {
			++idx;
			if (idx > this.initNums) {
				break;
			}
			o.packageToBuffer(buffer);
		}
		// change to read mode
		buffer.flip();
	}
	
	/*
	 * Get the next package from buffer
	 */
	public byte[] getNextPackage() {
		if (buffer == null || !buffer.hasRemaining()) {
			return new byte[0];
		}	
	
		byte[] data = new byte[buffer.remaining() > this.packageSize ? packageSize : buffer.remaining()];
		buffer.get(data);
		offset++;
		return data;
	}
	
	/*
	 * Check if there are any data not read.
	 */
	public boolean hasRemaining() {
		return (buffer != null) && buffer.hasRemaining();
	}
	
	public int getOffset() {
		return this.offset;
	}
	
	/*
	 * reset to origin status
	 */
	public void reset() {
		buffer.position(0);
		init();		
	}
}
