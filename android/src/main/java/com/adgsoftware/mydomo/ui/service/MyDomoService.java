package com.adgsoftware.mydomo.ui.service;

import java.io.IOException;

import android.os.IInterface;

import com.adgsoftware.mydomo.engine.connector.OpenWebConnectionListener;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.house.House;

public interface MyDomoService extends IInterface {
	
	public <T extends Controller<? extends Status>> T createController(
			Class<T> clazz, String where);
	
	public void save(House house) throws IOException;
	
	public House retrieve() throws IOException;
	
	public boolean isMonitorConnected();
	
	public boolean isCommanderConnected();
	
	public void setServerIp(String ip);
	
	public void setServerPort(int port);
	
	public void addMonitorConnectionListener(OpenWebConnectionListener listener);
	
	public void addCommanderConnectionListener(OpenWebConnectionListener listener);
}
