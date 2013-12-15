package com.adgsoftware.mydomo.engine.controller;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

import com.adgsoftware.mydomo.engine.Log;
import com.adgsoftware.mydomo.engine.Log.Session;
import com.adgsoftware.mydomo.engine.connector.CommandListener;
import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.connector.CommandResultStatus;
import com.adgsoftware.mydomo.engine.connector.DefaultCommandResult;
import com.adgsoftware.mydomo.engine.connector.openwebnet.Command;
import com.adgsoftware.mydomo.engine.connector.openwebnet.parser.ParseException;

public abstract class ControllerDimension<T extends Status> extends Controller<T> 
implements Serializable {

	/** serial uid */
	private static final long serialVersionUID = 1L;
	private Log log = new Log();
	private List<ControllerDimensionChangeListener> controllerDimensionChangeListenerList = new ArrayList<ControllerDimensionChangeListener>();
	protected Hashtable<String, DimensionStatus> list = new Hashtable<String, DimensionStatus>(); // Cache of dimension status => avoid to call the bus each time
	private boolean waitingResult = false;

	/**
	 * Return true if controller is waiting information from gateway
	 * @return
	 */
	public boolean isWaitingResult() {
		return waitingResult;
	}

	/**
	 * Define the address of the device to control
	 * @param newValue address of the device
	 */
	@Override
	public void setWhere(String newValue) {
		// It is override since for dimension we dont manage a specific status but some
		this.where = newValue;
	}
	

	protected void executeAction(DimensionStatus dimensionStatus, final CommandListener commandListener) {
		if (server == null) {
			commandListener.onCommand( new DefaultCommandResult("", CommandResultStatus.nok));
		} else {
			waitingResult = true;
			server.sendCommand(server.createDimensionActionMessage(getWhere(), getWho(), dimensionStatus), new CommandListener() {

				@Override
				public void onCommand(CommandResult commandResult) {
					waitingResult = false;
					commandListener.onCommand(commandResult);
				}
			});
		}
	}

	protected <D extends DimensionStatus> void executeStatus(final D dimensionStatus, final DimensionStatusListener<D> listener) {
		if (server == null) {
			listener.onDimensionStatus(null, new DefaultCommandResult("", CommandResultStatus.nok));
		} else {
			waitingResult = true;
			server.sendCommand(server.createDimensionStatusMessage(getWhere(), getWho(), dimensionStatus), new CommandListener() {
				@Override
				public void onCommand(CommandResult result) {
					waitingResult = false;
					if (CommandResultStatus.ok.equals(result.getStatus())) {
						List<DimensionValue> dimensionListResult = null;
						try {
							Command.getCommandAnalyser(result.getResult()).getDimensionListFromCommand(); // TODO move from here to connector
						} catch (ParseException e) {
							log.log(Session.Command, Level.SEVERE, "Unknown response [" + result.getResult() + "]. Command result ignored.");
						}
						dimensionStatus.setValueList(dimensionListResult);
						listener.onDimensionStatus(dimensionStatus, result); 
					} else {
						listener.onDimensionStatus(null, result);
					}		
				}
			});
		}
	}

	/**
	 * @param l
	 *            the new change listener.
	 */
	public void addControllerDimensionChangeListener(
			ControllerDimensionChangeListener l) {
		controllerDimensionChangeListenerList.add(l);
	}

	protected void notifyDimensionChange(DimensionStatus newStatus) {
		for (ControllerDimensionChangeListener listener : controllerDimensionChangeListenerList) {
			listener.onDimensionChange(this, newStatus);
		}
	}

	protected void notifyDimensionChangeError(DimensionStatus newStatus,
			CommandResult result) {
		for (ControllerDimensionChangeListener listener : controllerDimensionChangeListenerList) {
			listener.onDimensionChangeError(this, newStatus, result);
		}
	}

	/**
	 * Get the {@link DimensionStatus} of the device by sending the command on
	 * the bus.
	 * 
	 * @param clazz
	 *            class of the dimensionStatus of the device.
	 * @return the dimension status
	 */
	protected <D extends DimensionStatus> void getDimensionStatus(Class<D> clazz, DimensionStatusListener<D> listener) {
		try {
			D result = clazz.newInstance();
			executeStatus(result, listener);
		} catch (InstantiationException e) {
			e.printStackTrace();
			listener.onDimensionStatus(null, new DefaultCommandResult(null, CommandResultStatus.error));
		} catch (IllegalAccessException e) {
			e.printStackTrace(); // FIXME log and not stacktrace
			listener.onDimensionStatus(null, new DefaultCommandResult(null, CommandResultStatus.error));
		}
	}

	/**
	 * Define the {@link Status} of the device.
	 * @param what {@link Status} of the device.
	 */
	protected void setDimensionStatus(final DimensionStatus dimensionStatus) {
		executeAction(dimensionStatus, new CommandListener() {
			@Override
			public void onCommand(CommandResult result) {
				if (CommandResultStatus.ok.equals(result.getStatus())) {
					list.put(dimensionStatus.getCode(), dimensionStatus); // I do it here and not in monitor because too difficult to recreate the good dimension later....
				//	notifyDimensionChange(dimensionStatus); // done when come back from monitor
				} else {
					// Error...
					notifyDimensionChangeError(dimensionStatus, result);
				}
			}
		});
	}

	/**
	 * Define the new {@link DimensionStatus} of the device
	 * without sent the command on the bus.
	 * @param newWhat {@link DimensionStatus} of the device.
	 */
	public void changeDimensionStatus(DimensionStatus dimensionStatus) {
		if (dimensionStatus != null) {
			list.put(dimensionStatus.getCode(), dimensionStatus); // First because sometime unlock thread in listener...
			notifyDimensionChange(dimensionStatus);
		}
	}

	/**
	 * Get the {@link DimensionStatus} of the device
	 * without sent the command on the bus.
	 * @param code code of the dimensionStatus of the device.
	 * @return the dimension status
	 */
	public DimensionStatus getDimensionStatusFromCache(String code) {
		return list.get(code);
	}
}
