package com.adgsoftware.mydomo.engine.controller;

import com.adgsoftware.mydomo.engine.connector.CommandResult;

public interface StatusListener<T extends Status> {

	public void onStatus(T status, CommandResult result);
}
