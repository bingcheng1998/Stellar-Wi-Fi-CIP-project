package com.sansi.stellar;

public class DeviceItem {
	public static enum ITEMTYPE { GROUP, BULB };
	
	private String key;
	private String description;
	private ITEMTYPE type;
	
	public DeviceItem(String tag, String name, ITEMTYPE type) {
		this.setKey(tag);
		this.setDescription(name);
		this.setType(type);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String tag) {
		this.key = tag;
	}

	public ITEMTYPE getType() {
		return type;
	}

	public void setType(ITEMTYPE type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String name) {
		this.description = name;
	}	
}
