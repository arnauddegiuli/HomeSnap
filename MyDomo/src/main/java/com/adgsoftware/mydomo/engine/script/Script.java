package com.adgsoftware.mydomo.engine.script;

import java.util.ArrayList;
import java.util.List;

import com.adgsoftware.mydomo.engine.controller.Status;

public class Script {

	private List<Action<? extends Status>> actionList = new ArrayList<Action<? extends Status>>();
	
	public List<Action<? extends Status>> getActionList() {
		return actionList;
	}
}
