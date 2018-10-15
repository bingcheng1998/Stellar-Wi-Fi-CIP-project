package com.sansi.stellar.object;

public class Group {
	private String name;
	private String desc;
	
	public Group() {
		this.name = "";
		this.desc = "Ungrouped";
	}
	
	public Group(String name) {
		this.name = name;
		this.desc = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
