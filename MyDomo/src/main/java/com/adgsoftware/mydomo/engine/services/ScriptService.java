package com.adgsoftware.mydomo.engine.services;

import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.script.Action;

public interface ScriptService {

	public Action<? extends Status> createScript();
}
