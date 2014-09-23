package com.homesnap.engine.controller;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.json.JSONObject;

import com.homesnap.engine.JsonSerializable;
import com.homesnap.engine.connector.Command;
import com.homesnap.engine.connector.Command.Type;
import com.homesnap.engine.connector.CommandListener;
import com.homesnap.engine.connector.CommandResult;
import com.homesnap.engine.connector.CommandResultStatus;
import com.homesnap.engine.connector.Commander;
import com.homesnap.engine.connector.DefaultCommandResult;
import com.homesnap.engine.controller.types.DateTimeType;
import com.homesnap.engine.controller.types.DateType;
import com.homesnap.engine.controller.types.LabelType;
import com.homesnap.engine.controller.types.ListOfValuesType;
import com.homesnap.engine.controller.types.NumberType;
import com.homesnap.engine.controller.types.PercentageType;
import com.homesnap.engine.controller.types.RGBType;
import com.homesnap.engine.controller.types.TimeType;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.StateDefinition;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.what.StateValue;
import com.homesnap.engine.controller.what.StateValueType;
import com.homesnap.engine.controller.what.Status;
import com.homesnap.engine.controller.where.Where;
import com.homesnap.engine.controller.who.Who;

public abstract class Controller implements JsonSerializable, Serializable {

	/** serial uid */
	private static final long serialVersionUID = 1L;
	
	public static final String STATES_EXTENSION = ".states";
	
	private boolean waitingResult = false;
	protected Where where; // Represent the address of the controller
	private String title; // string representing the controller
	private String description; // string describing the controller
	protected transient Commander server;
	private List<ControllerChangeListener> controllerChangeListenerList = new ArrayList<ControllerChangeListener>();
	private LabelList labelList = new LabelList(this);
	
	/** List of all state names with their current values of the controller. This map repesents the complete status of the controller. */
	private Map<StateName, StateValue> stateList = new HashMap<StateName, StateValue>();
	/** List of all states names with their class value type. This map is used to prevent from set a state with an invalid value. */
	private Map<StateName, StateValueType> stateTypes;
	/** Cache of all controller classes with their state types */
	private static Map<Class<?>, Map<StateName, StateValueType>> classTypes = new Hashtable<Class<?>, Map<StateName, StateValueType>>();

	public static final String JSON_TITLE = "title";
	public static final String JSON_STATES = "states";
	public static final String JSON_WHERE = "where";
	public static final String JSON_WHO = "who";
	public static final String JSON_DESCRIPTION = "description";
	
	/**
	 * Constructor.
	 */
	protected Controller() {
		initStateTypes(getClass(), new Properties());
	}
	
	/**
	 * Initializes the state names with their class types in order to prevent from a wrong assigment when the {@link #set(StateName, StateValue)} method is called.
	 */
	private void initStateTypes(Class<?> clazz, Properties stateNames) {
		// Check if the controller class is known
		stateTypes = classTypes.get(clazz);
		if (stateTypes == null) {
			
			stateTypes = new HashMap<StateName, StateValueType>();
			Class<?> superClass = clazz.getSuperclass();
			while (!Controller.class.equals(superClass)) {
				initStateTypes(superClass, stateNames);
				break;
			}
			// Search the ".states" resource which defines the state types of the controller
			String pkgName = clazz.getPackage().getName().replace('.', '/');
			URL url = clazz.getClassLoader().getResource(pkgName +"/"+ clazz.getSimpleName() +".states");
			if (url == null) {
				throw new RuntimeException("Unable to find states definition file for "+ clazz.getName());
			}
			// Load the definition file
			StateDefinition stateDefinition = new StateDefinition();
			try {
				stateDefinition.load(url.openStream());
			} catch (IOException e) {
				throw new RuntimeException("Unable to load states definition file for "+ getClass().getName(), e);
			}
			// Load each key/value pair (key=state name, value=state class name)
			for (Entry<String, String> states : stateDefinition.getSectionProperties(StateDefinition.CONTROLLER_SECTION).entrySet()) {
				
				String name = (String) states.getKey();
				StateName stateName = initStateName(name); // The state name read
				if (stateName == null) {
					throw new ControllerStateException("State name "+ name +" is not a valid state name for class "+ getClass().getName());
				}
				
				String type = (String) states.getValue();
				StateValueType stateType = null;
				Throwable cause = null;
				try {
					stateType = parseStateValueType(name, type); // Determine the class type used to store the value of the state name
				} catch (UnknowStateValueTypeException e) {
					cause = e;
					stateType = initStateType(stateName, type); // User defined types
				}
				if (stateType == null) {
					throw new ControllerStateException("State type "+ type +" is not valid for controller class "+ getClass().getName(), cause);
				}
				stateTypes.put(stateName, stateType);
			}
			classTypes.put(clazz, stateTypes);
		}
	}
	
	/**
	 * Indicates if the controller is waiting information from the gateway.
	 * 
	 * @return <code>true</code> if controller is waiting information and <code>false</code> otherwise
	 */
	public boolean isWaitingResult() {
		return waitingResult;
	}

	/**
	 * Returns the address of the targeted device
	 * 
	 * @return the address of the targeted device
	 */
	public Where getWhere() {
		return where;
	}

	/**
	 * Defines the address of the targeted device to control
	 * 
	 * @param newValue the address of the targeted device to control
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
	 * Executes an action. The result is wait by a command listener.
	 * 
	 * @param what The state to update
	 * @param commandListener The listener which will wait for action result
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
	 * Executes a read operation on a state name of the controller.
	 * The result is wait by a status listener.
	 * 
	 * @param stateName The state name to read
	 * @param statusListener The listener which will wait for result
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
	 * @param listener
	 *            the new change listener.
	 */
	public void addControllerChangeListener(ControllerChangeListener listener) {
		synchronized (controllerChangeListenerList) {
			controllerChangeListenerList.add(listener);
		}
	}

	private void notifyStateChange(State oldStatus, State newStatus) {
		synchronized (controllerChangeListenerList) {
			for (ControllerChangeListener listener : controllerChangeListenerList) {
				listener.onStateChange(this, oldStatus, newStatus);
			}
		}
	}

	private void notifyStateChangeError(State oldStatus, State newStatus, CommandResult result) {
		synchronized (controllerChangeListenerList) {
			for (ControllerChangeListener listener : controllerChangeListenerList) {
				listener.onStateChangeError(this, oldStatus, newStatus, result);
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
	 * 
	 * @return description of the controller
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Define the description of the controller.
	 * 
	 * @param description of the controller
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Returns the current value of a state name.
	 * 
	 * @param state The state name
	 * @return The current value of a state name
	 */
	protected StateValue get(StateName stateName) {
		StateValue value = stateList.get(stateName);
		if (value == null) {
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
		return value;
	}

	/**
	 * Returns the current value of a state name and a default one if this state name has not been set.
	 * 
	 * @param stateName The state name to read
	 * @param defaultValue The default value
	 * @return the current value of the state name if exists and the default otherwise
	 */
	protected StateValue get(StateName stateName, StateValue defaultValue) {
		StateValue result = get(stateName);
		return result == null ? defaultValue : result;
	}

	/**
	 * Create/update a value of the status.
	 * @param stateName The state name
	 * @param stateValue The value of the state to set into the status
	 */
	protected void set(StateName stateName, StateValue stateValue) {
		if (stateName == null || stateName.getName() == null) {
			throw new NullPointerException("Could not set null state name.");
		}
		StateValueType stateType = stateTypes.get(stateName);
		try {
			stateType.setValue(stateValue);
		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to set "+ stateName.getName() +": "+ e.getMessage());
		}

		// The command is sent to the gateway. Gateway transmits it to the
		// actuator.
		// If everything is fine, Gateway provides through the monitor session
		// the new status => not need to set it here since it will be set by the
		// monitor way.
		final State oldStatus = new State(stateName, get(stateName));
		final State newStatus = new State(stateName, stateValue);
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
					notifyStateChangeError(oldStatus, newStatus, result);
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
		notifyStateChange(oldWhat, newWhat);
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 * @return
	 * @throws UnknowStateValueTypeException 
	 */
	public StateValueType parseStateValueType(String name, String value) throws UnknowStateValueTypeException {
		if (value == null || value.length() == 0) {
			// TODO ConfigurationException
			throw new NullPointerException("Value is empty for state name "+ name);
		}
		int length = value.length();
		char firstChar = value.charAt(0);
		if (firstChar == '#' && length > 2) { // Class type
			if (length > 2) {
				String keyword = value.substring(1);
//				firstChar = value.charAt(2);
				if ("Date".equals(keyword)) {
					return new DateType();
				}
				else if ("Datetime".equals(keyword)) {
					return new DateTimeType();
				}
				else if ("Label".equals(keyword)) {
					return new LabelType();
				}
				else if ("Number".equals(keyword)) {
					return new NumberType();
				}
				else if ("Percentage".equals(keyword)) {
					return new PercentageType();
				}
				else if ("RGBType".equals(keyword)) {
					return new RGBType();
				}
				else if ("Time".equals(keyword)) {
					return new TimeType();
				}
				throw new UnknowStateValueTypeException("Unknown state value type "+ keyword +" for state name "+ name);
			}
			// TODO ConfigurationException
			throw new NullPointerException("Missing value after class definition character '#' for state name "+ name);
		}
		else if (firstChar == '{') { // List of values
			if (length > 2 && (value.charAt(length-1) == '}')) {
				String values = value.substring(1, length-1);
				return new ListOfValuesType(values.split(","));
			}
			// TODO ConfigurationException
			throw new NullPointerException("Missing end character '}' of list of values for state name "+ name);
		}
		else if (firstChar == '[') { // MinMaxType
			if (length > 2 && (value.charAt(length-1) == ']')) {
				
			}
			// TODO ConfigurationException
			throw new NullPointerException("Missing end character ']' of number interval for state name "+ name);
		}
		throw new IllegalStateException("Unsupported value "+ value +" for state name "+ name);
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
		if (! stateList.isEmpty()) {
			for (Entry<StateName, StateValue> entry : stateList.entrySet()) {
				StateValue sv = entry.getValue();
				states.put(entry.getKey().getName(), sv == null ? null : sv.getValue());
			}
		}
		controllerJson.put(JSON_STATES, states);
		return controllerJson;
	}

	@Override
	public void fromJson(JSONObject jsonObject) {
		setTitle(jsonObject.getString(JSON_TITLE));
		setDescription(jsonObject.getString(JSON_DESCRIPTION));

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
	
	/**
	 * Create the {@link StateName} instance corresponding to the key name of one state managed by this controller.
	 * @param name The name of the state
	 * @return
	 */
	protected abstract StateName initStateName(String name);
	
	/**
	 * Create the {@link StateValueType} instance corresponding to a state name managed by this controller.
	 * @param stateName The state name
	 * @param value The string representation of the state value type red in the configuration file of this controller class.
	 * @return
	 */
	protected StateValueType initStateType(StateName stateName, String value) {
		return null;
	}
}
