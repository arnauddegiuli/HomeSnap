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
import java.util.List;

import com.adgsoftware.mydomo.engine.connector.CommandListener;
import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.connector.CommandResultStatus;
import com.adgsoftware.mydomo.engine.connector.Commander;
import com.adgsoftware.mydomo.engine.connector.DefaultCommandResult;
import com.adgsoftware.mydomo.engine.house.Label;

/**
 * Controller is a generic class to simulate a controller for a device. The main
 * role of a controller is to send a command on the domotic BUS. The targeted
 * device (where) gets the command from the BUS and executes it. In our case,
 * controller send the command to the gateway (throught the {@link Commander})
 * which pushes the command on the BUS. <br>
 * A controller can be associated to different {@link Label}.
 * 
 * @param <T>
 *            Type of status supported by the controller
 */
public abstract class Controller<T extends Status> implements Serializable {

	/** serial uid */
	private static final long serialVersionUID = 1L;
	private T what; // Represent the status (on/off; open/close; ...)
	protected String where; // Represent the address of the controller
	private String title; // string representing the controller
	protected transient Commander server;
	private List<ControllerChangeListener> controllerChangeListenerList = new ArrayList<ControllerChangeListener>();
	private LabelList labelList = new LabelList(this);

	/**
	 * Return the address of the targeted device
	 * 
	 * @return address of the targeted device
	 */
	public String getWhere() {
		return where;
	}

	/**
	 * Define the address of the targeted device to control
	 * 
	 * @param newValue
	 *            address of the targeted device
	 */
	public void setWhere(String newValue) {
		this.where = newValue;
		if (newValue == null) { // Manage null value because we create some
								// controller with no address (Gateway or
								// Heating central with MyHOME Bus)
			what = null;
		} else {
			executeStatus(new StatusListener<T>() {
				@Override
				public void onStatus(T status, CommandResult result) {
					// TODO : one pb with that: if we get the value of what
					// before response we get null and nothing is done to be
					// advertise of the change when value arrive...
					what = status;
				}
			});
		}
	}

	/**
	 * Return the {@link Status} of the device
	 * 
	 * @return the {@link Status} of the device
	 */
	public T getWhat() {
		// Status is get when where is set. Probably it could be better...
		return what;
	}

	/**
	 * Define the new {@link Status} of the device.
	 * 
	 * @param newWhat
	 *            {@link Status} of the device.
	 */
	public void setWhat(final T newWhat) {
		// The command is sent to the gateway. Gateway transmits it to the
		// controller.
		// If everything is fine, Gateway provides through the monitor session
		// the new status => not need to set it here since it will be set by the
		// monitor way.
		final T oldStatus = what;
		// what = newWhat; => it will be done with changeWhat by the monitor
		// listener
		executeAction(newWhat, new CommandListener() {
			@Override
			public void onCommand(CommandResult result) {
				if (CommandResultStatus.ok.equals(result.getStatus())) {
					// Status has been changed
					// what = newWhat; => it will be done with changeWhat by the
					// monitor listener
					// notifyWhatChange(oldStatus, newWhat); call by monitor! =>
					// it will be done with changeWhat by the monitor listener
				} else {
					// Error
					// what = oldStatus; => it will be done with changeWhat by
					// the monitor listener
					notifyWhatChangeError(oldStatus, newWhat, result);
				}
			}
		});
	}

	/**
	 * Define the new {@link Status} of the device without sent the command on
	 * the bus.
	 * 
	 * @param newWhat
	 *            {@link Status} of the device.
	 */
	public void changeWhat(T newWhat) {
		this.what = newWhat;
		notifyWhatChange(this.what, newWhat);
	}

	/**
	 * Execute an action
	 * 
	 * @return result of action execution.
	 */
	protected void executeAction(T newWhat, CommandListener commandListener) {
		if (server == null || newWhat == null) {
			commandListener.onCommand(new DefaultCommandResult("",
					CommandResultStatus.nok));
		} else {
			server.sendCommand(
					server.createActionMessage(newWhat, where, getWho()),
					commandListener);
		}

	}

	/**
	 * Get the status of the controller.
	 * 
	 * @return status of the controller.
	 */
	protected void executeStatus(final StatusListener<T> statusListener) {

		if (server == null) {
			statusListener.onStatus(what, new DefaultCommandResult("",
					CommandResultStatus.nok));
		} else {
			server.sendCommand(server.createStatusMessage(where, getWho()),
					new CommandListener() {
						@Override
						public void onCommand(CommandResult result) {
							if (CommandResultStatus.ok.equals(result.getStatus())) {
								// Return the status of the controller from the server
								T status = getStatus(result.getWhat());
								statusListener.onStatus(
										status,
										result);
							} else {
								// ERROR: message not sent on Bus or error
								// return... we keep the last value
								statusListener.onStatus(what, result);
							}

						}
					});
		}
	}

	public abstract String getWho();

	public abstract T getStatus(String what);

	/**
	 * Define the gateway to connect on.
	 * 
	 * @param server
	 */
	public void setServer(Commander server) {
		this.server = server;
	}

	/**
	 * @param l
	 *            the new change listener.
	 */
	public void addControllerChangeListener(ControllerChangeListener l) {
		synchronized (controllerChangeListenerList) {
			controllerChangeListenerList.add(l);
		}
	}

	private void notifyWhatChange(Status oldStatus, Status newStatus) {
		synchronized (controllerChangeListenerList) {
			for (ControllerChangeListener listener : controllerChangeListenerList) {
				listener.onWhatChange(this, oldStatus, newStatus);
			}
		}
	}

	private void notifyWhatChangeError(Status oldStatus, Status newStatus,
			CommandResult result) {
		synchronized (controllerChangeListenerList) {
			for (ControllerChangeListener listener : controllerChangeListenerList) {
				listener.onWhatChangeError(this, oldStatus, newStatus, result);
			}
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	/**
	 * Return the list of label associated to this component.
	 * 
	 * @return list of label.
	 */
	public LabelList getLabels() {
		return labelList;
	}

}