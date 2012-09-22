package com.adgsoftware.mydomo.engine.connector;



public class CommandResult {
	public String commandResult;
	public CommandResultStatus status;
	
	public CommandResult(String commandResult, CommandResultStatus status) {
		this.commandResult = commandResult;
		this.status = status;
	}
	
}
