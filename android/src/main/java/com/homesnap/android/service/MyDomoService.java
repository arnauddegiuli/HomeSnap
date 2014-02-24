package com.homesnap.android.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import android.os.IInterface;

import com.adgsoftware.mydomo.engine.connector.ConnectionListener;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;
import com.adgsoftware.mydomo.engine.house.House;
import com.adgsoftware.mydomo.engine.house.Icon;
import com.adgsoftware.mydomo.engine.house.Label;
import com.homesnap.android.util.ControlException;

public interface MyDomoService extends IInterface {
	
	public <T extends Controller<? extends Status>> T createController(
			Class<T> clazz, String where);
	
	public void save() throws IOException;
	
	public House retrieve() throws IOException;
	
	public boolean isMonitorConnected();
	
	public boolean isCommanderConnected();
	
	public void setServerIp(String ip);
	
	public void setServerPort(int port);
	
	public void setServerPassword(int password);
	
	public void addMonitorConnectionListener(ConnectionListener listener);
	
	public void addCommanderConnectionListener(ConnectionListener listener);
	
	public void connect();
	
	public void onDestroy();
	
	public Label createLabel(String id, String title, String description,
			Icon icon, String iconPath,
			Collection<? extends Controller<? extends Status>> controllerList) throws ControlException;

	public Label modifyLabel(String oldId, String newid, String title,
			String description, Icon icon, String iconPath,
			Collection<? extends Controller<? extends Status>> controllerList)
			throws ControlException;
	
	public List<Label> getLabels();
	
	public void removeLabel(String id) throws ControlException;
}
