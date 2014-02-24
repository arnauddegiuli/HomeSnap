package com.homesnap.android.util;

import java.util.List;

public class ControlException extends Exception {

	private List<Control> controlFailedList;
	
	public ControlException(List<Control> controlFailed) {
		this.controlFailedList = controlFailed;
	}

}
