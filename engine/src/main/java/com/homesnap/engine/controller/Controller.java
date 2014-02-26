package com.homesnap.engine.controller;

/*
 * #%L
 * MyDomoEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.UnmarshalException;

import org.json.JSONObject;

import com.homesnap.engine.JsonSerializable;
import com.homesnap.engine.connector.CommandResult;
import com.homesnap.engine.connector.CommandResultStatus;
import com.homesnap.engine.connector.Commander;
import com.homesnap.engine.connector.DefaultCommandResult;
import com.homesnap.engine.controller.Command.Type;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

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
	/** List of all states with their class types to prevent from set a state with an invalid value */
	private Map<StateName, Class<? extends StateValue>> stateTypes;
	/** List of all states with their values which represents the current status of a device */
	private Map<StateName, StateValue> stateList = new HashMap<StateName, StateValue>();

	/**
	 * Constructor.
	 */
	protected Controller() {
		initStateTypes();
	}
	
	/**
	 * Initialises the state names withtypes in order to prevent from a wrong assigment when the {@link #set(String, StateValue)} method is called.
	 * @return A map of all the states types supported by this controller, where the key is the state name and the value associated is a {@link StateValue} class
	 */
	protected abstract void initStateTypes();
	
	/**
	 * Declares one state that the controller instance manages.
	 * This method must be called during the {@link #initStateTypes()} phase
	 * All declared states will prevent from a wrong assigment when the {@link #set(String, StateValue)} method is called.
	 * @param state The state name
	 * @param stateClass The {@link StateValue} class of that state
	 */
	protected void declareState(StateName state, Class<? extends StateValue> stateClass) {
		stateTypes.put(state, stateClass);
	}
	
	/**
	 * Return true if controller is waiting information from gateway
	 * @return
	 */
	public boolean isWaitingResult() {
		return waitingResult;
	}

	/**
	 * Return the address of the targeted device
	 * 
	 * @return address of the targeted device
	 */
	public Where getWhere() {
		return where;
	}

	/**
	 * Define the address of the targeted device to control
	 * 
	 * @param newValue
	 *            address of the targeted device
	 */
	public void setWhere(Where newValue) {
		this.where = newValue;
		if (newValue == null) { // Manage null value because we create some
								// controller with no address (Gateway or
								// Heating central with MyHOME Bus)
			for (StateName stateName : stateTypes.keySet()) {
				stateList.put(stateName, null);
			}
		} else {
			// Récupérer les status
			for (StateName stateName : stateTypes.keySet()) {
				get(stateName);
			}
			
		}
	}

	/**
	 * Execute an action
	 * 
	 * @return result of action execution.
	 */
	protected void executeAction(final State what, final CommandListener commandListener) {
		if (server == null || what == null || what.getName() == null) {
			commandListener.onCommand(new DefaultCommandResult("",
					CommandResultStatus.nok));
		} else {
			waitingResult = true;
			server.sendCommand(
					new Command(getWho(), what, where, Type.ACTION),
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
	 * Get the status of the controller.
	 * 
	 * @return status of the controller.
	 */
	protected void executeStatus(final StateName stateName, final StatusListener statusListener) {
		if (server == null || stateName == null) {
			statusListener.onStatus(new State(stateName, null), new DefaultCommandResult("",
					CommandResultStatus.nok));
		} else {
			waitingResult = true;
			server.sendCommand(
				new Command(getWho(), new State(stateName, null), where, Type.STATUS),
				new CommandListener() {
					@Override
					public void onCommand(CommandResult result) {
						waitingResult = false;
						// TODO tester que le type du status match avec ceux supporté!
						if (CommandResultStatus.ok.equals(result.getStatus())) {
							// Return the status of the controller from the server
							State status = result.getWhat(stateName);
							statusListener.onStatus(
									status,
									result);
						} else {
							// ERROR: message not sent on Bus or error
							// return... we keep the last value
							
							statusListener.onStatus(new State(stateName, stateList.get(stateName)), result);
						}
					}
				});
		}
	}

	public abstract Who getWho();

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

	private void notifyWhatChange(State oldStatus, State newStatus) {
		synchronized (controllerChangeListenerList) {
			for (ControllerChangeListener listener : controllerChangeListenerList) {
				listener.onWhatChange(this, oldStatus, newStatus);
			}
		}
	}

	private void notifyWhatChangeError(State oldStatus, State newStatus,
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

//	/**
//	 * Get the {@link DimensionStatus} of the device by sending the command on
//	 * the bus.
//	 * 
//	 * @param clazzkey)(
//	 *            class of the dimensionStatus of the device.
//	 * @return the dimension status
//	 */
//	protected <D extends DimensionStatus> void getDimensionStatus(Class<D> clazz, DimensionStatusListener<D> listener) {
//		try {
//			D result = clazz.newInstance();
//			executeStatus(result, listener);
//		} catch (InstantiationException e) {
//			e.printStackTrace();
//			listener.onDimensionStatus(null, new DefaultCommandResult(null, CommandResultStatus.error));
//		} catch (IllegalAccessException e) {
//			e.printStackTrace(); // FIXME log and not stacktrace
//			listener.onDimensionStatus(null, new DefaultCommandResult(null, CommandResultStatus.error));
//		}
//	}
//
//	/**
//	 * Define the {@link Status} of the device.
//	 * @param what {@link Status} of the device.
//	 */
//	protected void setDimensionStatus(final DimensionStatus dimensionStatus) {
//		executeAction(dimensionStatus, new CommandListener() {
//			@Override
//			public void onCommand(CommandResult result) {
//				if (CommandResultStatus.ok.equals(result.getStatus())) {
//					list.put(dimensionStatus.getCode(), dimensionStatus); // I do it here and not in monitor because too difficult to recreate the good dimension later....
//				//	notifyDimensionChange(dimensionStatus); // done when come back from monitor
//				} else {
//					// Error...
//					notifyDimensionChangeError(dimensionStatus, result);
//				}
//			}
//		});
//	}
//
//	/**
//	 * Define the new {@link DimensionStatus} of the device
//	 * without sent the command on the bus.
//	 * @param newWhat {@link DimensionStatus} of the device.
//	 */
//	public void changeDimensionStatus(DimensionStatus dimensionStatus) {
//		if (dimensionStatus != null) {
//			list.put(dimensionStatus.getCode(), dimensionStatus); // First because sometime unlock thread in listener...
//			notifyDimensionChange(dimensionStatus);
//		}
//	}
//
//	/**
//	 * Get the {@link DimensionStatus} of the device
//	 * without sent the command on the bus.
//	 * @param code code of the dimensionStatus of the device.
//	 * @return the dimension status
//	 */
//	public DimensionStatus getDimensionStatusFromCache(String code) {
//		return list.get(code);
//	}

	/**
	 * Return the value of a state of the status.
	 * @param state The state name
	 * @return The current value of the state into the current status.
	 */
	protected StateValue get(StateName stateName) {
		StateValue val = stateList.get(stateName);
		if (val == null) {
			executeStatus(stateName, new StatusListener() {
				@Override
				public void onStatus(State status, CommandResult result) {
					// TODO : one pb with that: if we get the value of what
					// before response we get null and nothing is done to be
					// advertise of the change when value arrive...
					stateList.put(status.getName(), status.getValue());
				}
			});	
		}
		return val;
	}

	/**
	 * Return the value of a state of the status.
	 * If the state is not yet defined into the status, the default value is returned.
	 * @param state The state name
	 * @param defaultValue The default value
	 * @return The current value of the state into the current status or <code>defaultValue</code>
	 */
	protected StateValue get(StateName state, StateValue defaultValue) {
		StateValue result = get(state);
		return result == null ? defaultValue : result;
	}

	/**
	 * Create/update a value of the status.
	 * @param state The state name
	 * @param value The value of the state to set into the status
	 */
	protected void set(StateName state, StateValue value) {
		if (!checkStateValue(state, value)) {
			// TODO Create a StateValueException
			throw new IllegalArgumentException("Unable to set ["+ state.getName() +"] state to "+ value.getValue()
					+", the state value must be an instance of "+ stateTypes.get(state).getName());
		}

		// The command is sent to the gateway. Gateway transmits it to the
		// actuator.
		// If everything is fine, Gateway provides through the monitor session
		// the new status => not need to set it here since it will be set by the
		// monitor way.
		final State oldStatus = new State(state, get(state));
		final State newStatus = new State(state, value);
		// what = newWhat; => it will be done with changeWhat by the monitor
		// listener
		executeAction(newStatus, new CommandListener() {
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
					notifyWhatChangeError(oldStatus, newStatus, result);
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
	public void changeState(State newWhat) {
		State oldWhat = new State(newWhat.getName(), stateList.get(newWhat.getName()));
		stateList.put(newWhat.getName(), newWhat.getValue());
		notifyWhatChange(oldWhat, newWhat);
	}

	/**
	 * Check that a class value is compatible with a state.
	 * For state names which are not defined by this controller, the result is always <true>.
	 * @param state The state name to check
	 * @param value The value of the state
	 * @return <code>true</code> if the instance of the state value's class is assignable from the one which is defined by this controller and <code>false</code> otherwise
	 * @throws NullPointerException if the state and/or the value is null
	 */
	private boolean checkStateValue(StateName state, StateValue value) {
		if (state == null) {
			throw new NullPointerException("Could not set null state.");
		}
		if (value == null) {
			throw new NullPointerException("Could not set null value to a state.");
		}
		
		Class<? extends StateValue> stateValueClass = stateTypes.get(state);
		if (stateValueClass == null) { // The state is not defined by this controller, consider this is a user defined state
			return true;
		}
		return value.getClass().isAssignableFrom(stateValueClass); 
	}

	@Override
	public String toString() {
		return toJson().toString();
	}

	@Override
	public JSONObject toJson() {
		JSONObject controllerJson = new JSONObject();
		controllerJson.put("who", getWho())
				 .put("title", getTitle())
				 .put("description", getDescription());
		JSONObject states = new JSONObject();
		if (! stateList.isEmpty()) {
			for (Entry<StateName, StateValue> entry : stateList.entrySet()) {
				states.put(entry.getKey().getName(), entry.getValue().getValue());
			}
		}
		controllerJson.put("states", states);
		return controllerJson;
	}

	@Override
	public void fromJson(JSONObject jsonObject) throws UnmarshalException {
		setTitle(jsonObject.getString("title"));
		setDescription(jsonObject.getString("description"));
//		JSONObject states = jsonObject.getJSONObject("states");
//		for (String key : states.keySet()) {
//			// TODO manage type statesList.put(key, states.get(key));
//			
//		} 
	}
}
