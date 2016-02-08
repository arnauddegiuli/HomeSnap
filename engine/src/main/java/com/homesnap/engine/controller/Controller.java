package com.homesnap.engine.controller;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
 * %%
 * This file is part of HomeSnap done by Arnaud de Giuli (arnaud.degiuli(at)free.fr)
 *     helped by Olivier Driesbach (olivier.driesbach(at)gmail.com).
 * 
 *     HomeSnap is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     HomeSnap is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with HomeSnap. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import com.homesnap.engine.JsonSerializable;
import com.homesnap.engine.connector.Command;
import com.homesnap.engine.connector.Command.Type;
import com.homesnap.engine.connector.CommandListener;
import com.homesnap.engine.connector.CommandResult;
import com.homesnap.engine.connector.CommandResultStatus;
import com.homesnap.engine.connector.Commander;
import com.homesnap.engine.connector.DefaultCommandResult;
import com.homesnap.engine.connector.Monitor;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.What;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

/**
 * Controller is the abstract class for all device supporter by SnapHome.
 * Each sub class represent a device. Controller contains the main logic to manage status.
 *
 */
public abstract class Controller implements JsonSerializable, Serializable {

	/** serial uid */
	private static final long serialVersionUID = 1L;
	
	private boolean waitingResult = false;
	protected Where where; // Represent the address of the controller
	private String title; // string representing the controller
	private String description; // string describing the controller
	protected transient Commander server;
	private List<ControllerChangeListener> controllerChangeListenerList = new ArrayList<ControllerChangeListener>();
	private LabelList labelList = new LabelList(this);
	/** List of all state names with their current values of the controller. This map repesents the complete status of the controller. */
	private Map<String, State<?>> whatList = new HashMap<String, State<?>>();


	public static final String JSON_TITLE = "title";
	public static final String JSON_STATES = "states";
	public static final String JSON_WHERE = "where";
	public static final String JSON_WHO = "who";
	public static final String JSON_DESCRIPTION = "description";
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	/**
	 * Return the description of the controller
	 * @return description of the controller
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Define the description of the controller.
	 * @param description of the controller
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Return the list of label associated to this component.
	 * @return list of label.
	 */
	public LabelList getLabels() {
		return labelList;
	}

	public abstract Who getWho();

	/**
	 * Returns the address of the targeted device
	 * @return the address of the targeted device
	 */
	public Where getWhere() {
		return where;
	}

	/**
	 * Defines the address of the targeted device to control
	 * @param newValue the address of the targeted device to control
	 */
	public void setWhere(Where newValue) {
		this.whatList.clear();
		this.where = newValue;
		// Request all status
		for (String state : getStateList()) {
			get(state);
		}
	}

	public abstract List<String> getStateList();
	
	public Map<String, State<?>> getStateMap() {
		return whatList;
	}
	
	/**
	 * If current value of a state name is known, provide it directly 
	 * without request the server. Else (if value
	 * is null) request the server and return null without waiting: to get
	 * the value use a StatusListener, or wait a bit and call get again.
	 * Normally the value is never null since we request the value when you set the where
	 * and later the {@link Monitor} refresh the value when domotic server push an event.
	 * 
	 * @param name The state name
	 * @return The current value of a state name or null if a request to the server
	 * is done (when current value is null).
	 */
	protected State<?> get(final String name) {
		if (name == null) {
			throw new NullPointerException("Could not get null state name.");
		}
		
		State<?> value = whatList.get(name);
		if (value == null) {
			executeStatus(name, new StatusListener() {
				@Override
				public void onStatus(What what, CommandResult result) {
					// The major pb with that: if we get the value of what
					// before initial response (done when setWhere call get the first time)
					// we get null and nothing is done to be
					// advertise of the change when value arrive...
					if (what != null)  { // what can be null since sometime only acknowledgement is return...
						whatList.put(what.getName(), what.getValue());
					}
				}
			});	
		}
		return value;
	}
	
	/**
	 * Update a value of the status.
	 * @param name The state name to update
	 * @param state The new value of the state name
	 */
	protected void set(String name, State<?> state) {

		if (state == null || name == null) {
			throw new NullPointerException("Could not set null state name.");
		}
		
		// The command is sent to the gateway. Gateway transmits it to the actuator.
		// If everything is fine, Gateway provides through the monitor session
		// the new status => not need to set it here since it will be set by the
		// monitor way.

		final What oldWhat = new What(name, whatList.get(name));
		final What newWhat= new What(name, state);
		// what = newWhat; => it will be done with changeWhat by the monitor listener
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
					notifyStateChangeError(oldWhat, newWhat, result);
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
	public void receivedAction(What newWhat) {
		State<?> oldState = whatList.get(newWhat.getName());
		whatList.put(newWhat.getName(), newWhat.getValue());
		notifyStateChange(new What(newWhat.getName(),oldState), newWhat);
	}

	
	/**
	 * Define the gateway to connect on.
	 * @param server
	 */
	public void setServer(Commander server) {
		this.server = server;
	}
	
	
	
	/**
	 * Executes an action. The result is wait by a command listener.
	 * 
	 * @param what The state to update
	 * @param commandListener The listener which will wait for action result
	 */
	protected void executeAction(final What what, final CommandListener commandListener) {
		if (server == null || what == null || what.getName() == null) {
			commandListener.onCommand(new DefaultCommandResult("",
					CommandResultStatus.nok));
		} else {
			waitingResult = true;
			server.sendCommand(
					new Command(getWho(), what, where, Type.ACTION, this),
					new CommandListener() {
						@Override
						public void onCommand(CommandResult commandResult) {
							// TODO tester que le type du status match avec ceux supporté!
							waitingResult = false;
							commandListener.onCommand(commandResult);
						}
					}
			);
		}
	}

	/**
	 * Executes a read operation on a state name of the controller.
	 * The result is wait by a status listener.
	 * 
	 * @param stateName The state name to read
	 * @param statusListener The listener which will wait for result
	 */
	protected void executeStatus(final String name, final StatusListener statusListener) {
		if (server == null || name == null) {
			statusListener.onStatus(new What(name, null), new DefaultCommandResult("",
					CommandResultStatus.nok));
		} else {
			waitingResult = true;
			server.sendCommand(
				new Command(getWho(), new What(name, null), where, Type.STATUS, this),
				new CommandListener() {
					@Override
					public void onCommand(CommandResult result) {
						waitingResult = false;
						if (CommandResultStatus.ok.equals(result.getStatus())) {
							// Return the status of the controller from the server
							statusListener.onStatus(
									result.getWhat(name),
									result);
						} else {
							// ERROR: message not sent on Bus or error
							// return... we keep the last value
							statusListener.onStatus(new What(name, null), result);
						}
					}
				});
		}
	}

	/**
	 * @param listener
	 *            the new change listener.
	 */
	public void addControllerChangeListener(ControllerChangeListener listener) {
		synchronized (controllerChangeListenerList) {
			controllerChangeListenerList.add(listener);
		}
	}

	/**
	 * @param listener
	 *            the change listener to remove.
	 */
	public void removeControllerChangeListener(ControllerChangeListener listener) {
		synchronized (controllerChangeListenerList) {
			controllerChangeListenerList.remove(listener);
		}
	}
	
	private void notifyStateChange(What oldWhat, What newWhat) {
		synchronized (controllerChangeListenerList) {
			for (ControllerChangeListener listener : controllerChangeListenerList) {
				listener.onStateChange(this,oldWhat , newWhat);
			}
		}
	}

	private void notifyStateChangeError(What oldStatus, What newStatus, CommandResult result) {
		synchronized (controllerChangeListenerList) {
			for (ControllerChangeListener listener : controllerChangeListenerList) {
				listener.onStateChangeError(this, oldStatus, newStatus, result);
			}
		}
	}

	/**
	 * Indicates if the controller is waiting information from the gateway.
	 * @return <code>true</code> if controller is waiting information and <code>false</code> otherwise
	 */
	public boolean isWaitingResult() {
		return waitingResult;
	}
	
	@Override
	public String toString() {
		return toJson().toString();
	}

	@Override
	public JSONObject toJson() {
		JSONObject controllerJson = new JSONObject();
		controllerJson.put(JSON_WHO, getWho())
				 .put(JSON_TITLE, getTitle())
				 .put(JSON_DESCRIPTION, getDescription());
		if (getWhere() != null) {
			controllerJson.put(JSON_WHERE, getWhere().getTo());
		}
		JSONObject states = new JSONObject();
		if (! whatList.isEmpty()) {
			for (Entry<String, State<?>> entry : whatList.entrySet()) {
				State<?> state = entry.getValue();
				String name = entry.getKey();
				states.put(name, state == null ? null : state.toString());
			}
		}
		controllerJson.put(JSON_STATES, states);
		return controllerJson;
	}

	@Override
	public void fromJson(JSONObject jsonObject) {
		setTitle(jsonObject.getString(JSON_TITLE));
		try {
			setDescription(jsonObject.getString(JSON_DESCRIPTION));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String to = jsonObject.getString(JSON_WHERE);
		if (!"null".equals(String.valueOf(to))) {
			setWhere(new Where(to, to));
		}

//		JSONObject states = jsonObject.getJSONObject(JSON_STATES);
//		for (final String name: states.keySet()) {
//			String value = states.getString(name);
//			StateName sname = new StateName() {
//				
//				@Override
//				public String getName() {
//					return name;
//				}
//			};
//			StateValue svalue; Les valeurs sont en lecture seul???
//			try {
//				svalue = stateTypes.get(sname).newInstance();
//				svalue.setValue(value);
//				stateList.put(sname, svalue);
//			} catch (InstantiationException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
	}
}
