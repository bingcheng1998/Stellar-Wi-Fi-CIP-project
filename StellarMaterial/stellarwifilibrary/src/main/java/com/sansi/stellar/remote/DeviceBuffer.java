package com.sansi.stellar.remote;

import com.sansi.stellar.object.Light;
import com.sansi.stellar.remote.object.BulbAdapter;
import com.sansi.stellar.remote.object.ObjectInputBuffer;
import com.sansi.stellar.remote.object.ObjectOutputBuffer;
import com.sansi.stellar.remote.object.StellarObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Chris Liu
 * @version 1.0 
 *
 */
public class DeviceBuffer {
	private static final int BULB_PACKAGE_SIZE = 2048;	// predefined bulb package size
	private static final int BULB_SIZE = 128;			// default bulb number to allocate buffer.
	private ObjectOutputBuffer dev_outBuf;
	private ObjectInputBuffer dev_inBuf;
	
	/**
	 * keep the scanned device list within scan process
	 * key: device MAC addr
	 * value: Light object
	 */
	private Map<String, Light> mScannedDevices;

	public DeviceBuffer() {
		dev_inBuf = new ObjectInputBuffer(
				BULB_PACKAGE_SIZE, 
				StellarObject.LENGTH_TYPE.INT);
		dev_outBuf = new ObjectOutputBuffer(
		        BULB_SIZE,
		        StellarObject.PACKAGE_MAXSIZE,
		        BULB_PACKAGE_SIZE,
		        StellarObject.LENGTH_TYPE.INT);
		mScannedDevices = new HashMap<String, Light>();
	}
	
    /**
     * reset the bulb out buffer;
     */
    public void preparePackage(Collection<Light> devices) throws Exception {
    	Collection<StellarObject> list = new ArrayList<StellarObject>();
    	for (Light l: devices) {
    		list.add(new BulbAdapter(l));
    	}
    	dev_outBuf.setObjects(list);
    }
    
    /**
     * get the bulb out buffer next package data
     */
    public byte[] getNextPackage() {
    	if (dev_outBuf.hasRemaining()) {
    		return dev_outBuf.getNextPackage();
    	}
    	return new byte[0];
    }
    
    /**
     * 
     * @return
     */
    public int getPackageOffset() {
    	return dev_outBuf.getOffset();
    }
    
    /**
     * reset the bulb out buffer to origin position
     */
    public void resetPackage() {
    	dev_outBuf.reset();
    }
    
    /**
     * check if package out process finished.
     */
    public boolean package_finished() {
    	return !dev_outBuf.hasRemaining();
    }
    
    /**
     * reset the input buffer
     */
    public void resetScan() {
    	dev_inBuf.reset();
    	mScannedDevices.clear();
    }
    
    /**
     * accept data to input buffer and parse to object
     */
    public void onScanData(byte[] data) throws Exception {
    	dev_inBuf.appendPackage(data);
    	Collection<StellarObject> list = new ArrayList<StellarObject>();
    	dev_inBuf.decodeToObjects(list, BulbAdapter.class);
    	this.appendDevices(list);
    }
    
    /**
     * check if scan process finished.
     */
    public boolean scan_finished() {
    	return !dev_inBuf.hasRemaining();
    }
    
    /**
     * get the next scan offset value from server.
     */
    public int get_scan_offset() {
    	return dev_inBuf.getOffset();
    }
    
    /**
     * 
     * @param devices
     */
    private void appendDevices(Collection<StellarObject> devices) {
    	for (StellarObject o: devices) {
    		Light l = (Light)(o.getObject());
    		if (!mScannedDevices.containsKey(l.getMac())) {
    			mScannedDevices.put(l.getMac(), l);
    		}
    	}
    }
    
    /**
     * 
     * @return
     */
    public Map<String, Light> getAvailableDevices() {
    	return mScannedDevices;
    }
}
