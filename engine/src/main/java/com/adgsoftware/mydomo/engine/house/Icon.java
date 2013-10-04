package com.adgsoftware.mydomo.engine.house;

public enum Icon {

	chamber("icn_categories"),
	garage(""),
	house(""), 
	room(""), 
	kitchen(""),
	bathroom(""),
	showroom(""),
	movie(""),
	tv(""),
	light(""),
	heating(""),
	livingroom(""),
	outlet(""),
	automation(""),
	cellar(""),
	storeroom(""),
	door(""),
	window(""),
	fireplace(""),
	garden(""),
	outdoor("");

	private String className;
	
	private Icon(String className)  {
		this.className = className;
	}
	
	public String getClassName() {
		return className;
	}
}
