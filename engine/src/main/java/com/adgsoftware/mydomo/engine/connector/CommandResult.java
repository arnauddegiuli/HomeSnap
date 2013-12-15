package com.adgsoftware.mydomo.engine.connector;

public interface CommandResult {
	public CommandResultStatus getStatus();
	public String getResult();
}