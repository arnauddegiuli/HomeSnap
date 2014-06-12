package com.homesnap.engine.controller.what.impl;

import com.homesnap.engine.controller.what.StateValue;

public class VersionValue implements StateValue {

	private int version, release, build;
	
	public VersionValue (int version, int release, int build) {
		this.version = version;
		this.release = release;
		this.build = build;
	}
	
	public int getVersion() {
		return version;
	}

	public int getRelease() {
		return release;
	}

	public int getBuild() {
		return build;
	}

	@Override
	public String getValue() {
		return "".concat(String.valueOf(version))
				.concat(".").concat(String.valueOf(release))
				.concat(".").concat(String.valueOf(build));
	}

}
