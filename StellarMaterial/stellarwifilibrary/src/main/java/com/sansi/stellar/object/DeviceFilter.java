package com.sansi.stellar.object;

import java.util.HashMap;

/**
 * 
 * @author Chris liu
 * This class verify the device by pre_defined name
 */
public class DeviceFilter {
	public enum LIGHT_TYPES { UNKNOWN, RGB, CCT } 
	private static HashMap<Short, LIGHT_TYPES> nameFilters = new HashMap<Short, LIGHT_TYPES>();
	
	static {
		nameFilters.put((short)0x1001, LIGHT_TYPES.RGB);
		nameFilters.put((short)0x1002, LIGHT_TYPES.CCT);		
	}
	
    public static LIGHT_TYPES lookup(short key) {
        LIGHT_TYPES type = nameFilters.get(key);
	    return type == null ? LIGHT_TYPES.UNKNOWN : type;
	}
}
