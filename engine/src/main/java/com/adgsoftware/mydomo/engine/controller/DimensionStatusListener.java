package com.adgsoftware.mydomo.engine.controller;

import com.adgsoftware.mydomo.engine.connector.CommandResult;

public interface DimensionStatusListener<T extends DimensionStatus> {

	public void onDimensionStatus(T status, CommandResult result);
}
