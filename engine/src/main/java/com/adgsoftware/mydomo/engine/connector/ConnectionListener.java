package com.adgsoftware.mydomo.engine.connector;

/**
 * ConnectionListener manage close and connect event.
 */
public interface ConnectionListener {

	/**
	 * Raise each time close is call on the connection.
	 */
	public void onClose();
	 
	/**
	 * Raise each time a connection to the gateway is try with the result of it,
	 * @param connectionStatus
	 */
	public void onConnect(ConnectionStatusEnum connectionStatus);
	 
}
