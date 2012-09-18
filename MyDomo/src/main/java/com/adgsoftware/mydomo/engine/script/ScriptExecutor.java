package com.adgsoftware.mydomo.engine.script;

import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;

public class ScriptExecutor {

	public void executeScript(Script script) {
		for (Action<? extends Status> action : script.getActionList()) {	
			changeWhat(action.getController(), action.getStatus().getCode());
		}
	}
	
	
	// To use generic: only at the runtime we will know the type so this is only method to set what!
	private static <T extends Status> void changeWhat(Controller<T> controller, String code) {
		T status = controller.getStatus(code);
		if (!status.equals(controller.getWhat())) { // If status has changed
			controller.setWhat(status);
		}
	}
}
