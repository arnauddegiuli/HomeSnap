package com.adgsoftware.mydomo.engine.oldconnector.eibnetip;

/*
 * #%L
 * MyDomoEngine
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
 * %%
 * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
 * 
 *     MyDomo is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyDomo is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


import com.adgsoftware.mydomo.engine.oldconnector.CommandListener;
import com.adgsoftware.mydomo.engine.oldconnector.ConnectionListener;
import com.adgsoftware.mydomo.engine.oldconnector.Monitor;
import com.adgsoftware.mydomo.engine.oldconnector.UnknownControllerListener;
import com.adgsoftware.mydomo.engine.oldcontroller.Controller;
import com.adgsoftware.mydomo.engine.oldcontroller.Status;

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