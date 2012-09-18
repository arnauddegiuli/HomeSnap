package com.adgsoftware.mydomo.engine.controller.gateway;

public class Version {

	private int version;
	private int release;
	private int build;
	
	public int getVersion() {
		return version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	public int getRelease() {
		return release;
	}
	
	public void setRelease(int release) {
		this.release = release;
	}
	
	public int getBuild() {
		return build;
	}
	
	public void setBuild(int build) {
		this.build = build;
	}
}
