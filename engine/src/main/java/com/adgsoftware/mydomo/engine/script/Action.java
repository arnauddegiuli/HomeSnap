package com.adgsoftware.mydomo.engine.script;

import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;

public class Action<T extends Status> {

	private T status;
	private Controller<T> controller;

	public Action(T status, Controller<T> controller) {
		this.status = status;
		this.controller = controller;
	}
	
	public Controller<T> getController() {
		return controller;
	}


	public T getStatus() {
		return status;
	}
	
	
}
