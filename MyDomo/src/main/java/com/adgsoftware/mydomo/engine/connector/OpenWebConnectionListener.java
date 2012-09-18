package com.adgsoftware.mydomo.engine.connector;


public interface OpenWebConnectionListener {

	 public void onClose();
	 
	 public void onConnect(ConnectionStatusEnum connectionStatus);
	 
}
