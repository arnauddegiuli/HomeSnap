package com.adgsoftware.mydomo.engine.connector;

/**
 * Command listener manage commandResult.
 * CommandResult is the result of a command send to gateway.
 */
public interface CommandListener {

	/**
	 * Raise each time a result from a command is received.
	 * @param commandResult
	 */
	 public void onCommand(CommandResult commandResult);
	 
}
