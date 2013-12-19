package com.adgsoftware.mydomo.engine.connector.eibnetip;

import com.adgsoftware.mydomo.engine.connector.CommandListener;
import com.adgsoftware.mydomo.engine.connector.ConnectionListener;
import com.adgsoftware.mydomo.engine.connector.Monitor;
import com.adgsoftware.mydomo.engine.connector.UnknownControllerListener;
import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.Status;

/**
 * 
 * @author DRIESBACH Olivier
 */
public class KNXMonitorImpl implements Monitor {

	@Override
	public boolean connect() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
	}

	@Override
	public void addControllerToMonitor(Controller<? extends Status> controller) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeControllerToMonitor(Controller<? extends Status> controller) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addConnectionListener(ConnectionListener connectionListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeConnectionListener(ConnectionListener connectionListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addControllerStatusListener(CommandListener commandListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeControllerStatusListener(CommandListener commandListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addUnknownControllerListener(UnknownControllerListener unknownControllerListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeUnknownControllerListener(UnknownControllerListener unknownControllerListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getIp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setIp(String ip) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getPort() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setPort(int port) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getTimeout() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTimeout(int timeout) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setPasswordOpen(Integer passwordOpen) {
		// TODO Auto-generated method stub
	}
}