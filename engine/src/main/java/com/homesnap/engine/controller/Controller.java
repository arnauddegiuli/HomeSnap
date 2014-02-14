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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.adgsoftware.mydomo.engine.oldconnector.CommandListener;
import com.adgsoftware.mydomo.engine.oldconnector.CommandResult;
import com.homesnap.engine.controller.state.State;
import com.homesnap.engine.controller.state.StateName;
import com.homesnap.engine.controller.state.StateValue;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public abstract class Controller/*<T extends ControllerType>*/ implements Serializable {
	
	/** */
	private static final long serialVersionUID = 1L;

	/** The name the controller */
	private String name;
	
	/** A short description of the controller */
	private String description;
	
	/** The physical address of the controller of the target protocol */
	private String address;
	
	/** The type of the controller */
	private ControllerType type;
//	private T type;
	
	/** List of all state names with their type class of value to prevent from set a state with an invalid value */
	private Map<StateName, Class<? extends StateValue>> stateTypes = new HashMap<StateName, Class<? extends StateValue>>();
	
	/** List of all state names with their values which represents the current status of a device */
	private Map<StateName, StateValue> stateList = new HashMap<StateName, StateValue>(10);
	
	/** Controller of the device */
	private transient Commander commander;
	
	/**
	 * Construct the device
	 */
	protected Controller(String address) {
		this.address = address;
		initStateTypes();
	}
	
	/**
	 * 
	 * @return
	 */
	
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * 
	 * @param address
	 */
	protected void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * 
	 * @return
	 */
	public ControllerType getType() {
		return type;
	}

	/**
	 * Return the value of a state of the status.
	 * @param state The state name
	 * @return The current value of the state into the current status.
	 */
	public StateValue get(StateName state) {
		return stateList.get(state);
	}
	
	/**
	 * Return the value of a state of the status.
	 * If the state is not yet defined into the status, the default value is returned.
	 * @param state The state name
	 * @param defaultValue The default value
	 * @return The current value of the state into the current status or <code>defaultValue</code>
	 */
	public StateValue get(StateName state, StateValue defaultValue) {
		StateValue result = get(state);
		if (result == null) {
			result = defaultValue; // TODO A warning could be log here to prevent from returning an invalid StateValue to the caller if the state key is known into the types of this controller
		}
		return result;
	}
	
	/**
	 * 
	 * @return The string representation of all states with their values from the current status of the controller
	 */
	public String getStatesToString() {
		
		StringBuilder result = new StringBuilder("{");
		if (! stateList.isEmpty()) {
			result.append("\n");
			for (Entry<StateName, StateValue> entry : stateList.entrySet()) {
				result.append(entry.getKey().getName()).append("=").append(entry.getValue().getValue()).append("\n");
			}
		}
		return result.append("}").toString();
	}
	
	/**
	 * Set a state of the status.
	 * @param state The state name
	 * @param value The value of the state to set into the status
	 */
	public void set(final StateName state, final StateValue value) {
		
		if (checkStateValue(state, value)) {
			if (commander == null) {
				stateList.put(state, value);
			} else {
				executeLater(
						createCommand(new State(state, value), new CommandListener() {
							@Override
							public void onCommand(CommandResult commandResult) {
								switch (commandResult.getStatus()) {
								case ok: {
									stateList.put(state, value);
									System.out.println("device updated "+ commandResult.getResult());
									break;
								}
								default: {
									System.out.println("device not updated "+ commandResult.getResult());
								}
								}
							}
						})
				);
			}
		} else {
			// TODO Create a StateValueException
			throw new IllegalArgumentException("Unable to set ["+ state +"] state to "+ value.getValue()
					+", the state value must be an instance of "+ stateTypes.get(state).getName());
		}
	}
	
	/**
	 * Remove a state from the status.
	 * @param state
	 */
	public void remove(String state) {
		if (canRemove(state)) {
			stateList.remove(state);
		} else {
			// TODO NonRemoveableStateException
		}
	}

	/**
	 * 
	 * @param controller
	 */
	public void setController(Commander controller) {
		this.commander = controller;
	}
	
	@Override
	public String toString() {
		return new StringBuilder().append(description).append(" ").append(getStatesToString()).toString();
	}

	/**
	 * Initialises the state names withtypes in order to prevent from a wrong assigment when the {@link #set(String, StateValue)} method is called.
	 * @return A map of all the states types supported by this controller, where the key is the state name and the value associated is a {@link StateValue} class
	 */
	protected abstract void initStateTypes();
	
	/**
	 * Declares one state that the controller instance manages.
	 * This method must be called during the {@link #initStateTypes()} phase
	 * @param state The state name
	 * @param stateClass The {@link StateValue} class of that state
	 */
	protected void declareState(StateName state, Class<? extends StateValue> stateClass) {
		stateTypes.put(state, stateClass);
	}
	
	/**
	 * 
	 * @return
	 */
	protected Map<StateName,  StateValue> getStates() {
		return Collections.unmodifiableMap(stateList);
	}

	/**
	 * 
	 * @param state
	 * @return
	 */
	private boolean canRemove(String state) {
		return true; // TODO Some states like "status" sould not be deletable. May be we have to add a "deleteble" flag into the stateTypes map.
	}
	
	/**
	 * Check that a class value is compatible with a state.
	 * For state names which are not defined by this controller, the result is always <true>.
	 * @param state The state name to check
	 * @param value The value of the state
	 * @return <code>true</code> if the instance of the state value's class is assignable from the one which is defined by this controller and <code>false</code> otherwise
	 * @throws NullPointerException if the state is null
	 * @throws NullPointerException if the value is null
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
	
	/**
	 * 
	 * @param command
	 */
	private void executeLater(Runnable command) {
		new Thread(command).start();
	}

	/**
	 * 
	 * @param state
	 * @param commandListener
	 * @return
	 */
	private Runnable createCommand(final State state, final CommandListener commandListener) {
		return new Runnable() {
			@Override
			public void run() {
				commander.sendCommand(new Command(commander.getName(), getAddress(), state), commandListener);
			}
		};
	}
}
