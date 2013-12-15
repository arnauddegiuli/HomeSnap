package com.adgsoftware.mydomo.engine.connector;

public class DefaultCommandResult implements CommandResult {
	private String commandResult;
	private CommandResultStatus status;
	
	public DefaultCommandResult(String commandResult, CommandResultStatus status) {
		this.commandResult = commandResult;
		this.status = status;
	}

	@Override
	public CommandResultStatus getStatus() {
		return status;
	}

	@Override
	public String getResult() {
		return commandResult;
	}
}
