package com.adgsoftware.mydomo.engine.actuator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.UnmarshalException;

import org.json.JSONObject;

import com.adgsoftware.mydomo.engine.JsonSerializable;
import com.adgsoftware.mydomo.engine.actuator.connector.Commander;
import com.adgsoftware.mydomo.engine.actuator.what.core.State;
import com.adgsoftware.mydomo.engine.actuator.what.core.StateName;
import com.adgsoftware.mydomo.engine.actuator.what.core.StateValue;
import com.adgsoftware.mydomo.engine.actuator.where.Where;
import com.adgsoftware.mydomo.engine.actuator.who.Who;
import com.adgsoftware.mydomo.engine.controller.LabelList;

public abstract class Controller implements JsonSerializable, Serializable {

	/** List of all states with their class types to prevent from set a state with an invalid value */
	private Map<StateName, Class<? extends StateValue>> stateTypes;
	/** serial uid */
	private static final long serialVersionUID = 1L;
	private boolean waitingResult = false;
	protected Where where; // Represent the address of the controller
	private String title; // string representing the controller
	private String description; // string describing the controller
	protected transient Commander server;
	private List<ControllerChangeListener> controllerChangeListenerList = new ArrayList<ControllerChangeListener>();
	private LabelList labelList = null; // new LabelList(this); TODO replugger sur les label

	/**
	 * 
	 */
	protected Controller() {
		stateTypes = getSupportedStateTypes();
	}

	/** List of all states with their values which represents the current status of a device */
	private Map<StateName, StateValue> stateList = new HashMap<StateName, StateValue>();

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
			// TODO put all status to default what = null;
		} else {
			// Récupérer les status
			for (StateName stateName : stateTypes.keySet()) {
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
			
		}
	}





//	/**
//	 * Define the new {@link Status} of the device.
//	 * 
//	 * @param newWhat
//	 *            {@link Status} of the device.
//	 */
//	public void setWhat(final T newWhat) {
//		// The command is sent to the gateway. Gateway transmits it to the
//		// controller.
//		// If everything is fine, Gateway provides through the monitor session
//		// the new status => not need to set it here since it will be set by the
//		// monitor way.
//		final T oldStatus = what;
//		// what = newWhat; => it will be done with changeWhat by the monitor
//		// listener
//		executeAction(newWhat, new CommandListener() {
//			@Override
//			public void onCommand(CommandResult result) {
//				if (CommandResultStatus.ok.equals(result.getStatus())) {
//					// Status has been changed
//					// what = newWhat; => it will be done with changeWhat by the
//					// monitor listener
//					// notifyWhatChange(oldStatus, newWhat); call by monitor! =>
//					// it will be done with changeWhat by the monitor listener
//				} else {
//					// Error
//					// what = oldStatus; => it will be done with changeWhat by
//					// the monitor listener
//					notifyWhatChangeError(oldStatus, newWhat, result);
//				}
//			}
//		});
//	}
//
//	
//	/**
//	 * Define the new {@link Status} of the device without sent the command on
//	 * the bus.
//	 * 
//	 * @param newWhat
//	 *            {@link Status} of the device.
//	 */
//	public void changeWhat(T newWhat) {
//		this.what = newWhat;
//		notifyWhatChange(this.what, newWhat);
//	}

	/**
	 * Execute an action
	 * 
	 * @return result of action execution.
	 */
	protected void executeAction(final State what, final CommandListener commandListener) {
		if (server == null || what == null) {
			commandListener.onCommand(new DefaultCommandResult("",
					CommandResultStatus.nok));
		} else {
			waitingResult = true;
			server.sendCommand(
					server.createActionMessage(what, where, getWho()),
					new CommandListener() {
						@Override
						public void onCommand(CommandResult commandResult) {
							waitingResult = false;
							commandListener.onCommand(commandResult);
						}
					}
			);
		}
	}
// TOD ODELETE			server.createDimensionActionMessage(getWhere(), getWho(), dimensionStatus)
	/**
	 * Get the status of the controller.
	 * 
	 * @return status of the controller.
	 */
	protected void executeStatus(final StateName stateName, final StatusListener statusListener) {

		if (server == null) {
			statusListener.onStatus(new State(stateName, null), new DefaultCommandResult("",
					CommandResultStatus.nok));
		} else {
			waitingResult = true;
			server.sendCommand(server.createStatusMessage(stateName, where, getWho()),
					new CommandListener() {
						@Override
						public void onCommand(CommandResult result) {
							waitingResult = false;
							if (CommandResultStatus.ok.equals(result.getStatus())) {
								// Return the status of the controller from the server
								State status = result.getWhat();
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
	protected StateValue get(String state) {
		return stateList.get(state);
	}
	
	/**
	 * Return the value of a state of the status.
	 * If the state is not yet defined into the status, the default value is returned.
	 * @param state The state name
	 * @param defaultValue The default value
	 * @return The current value of the state into the current status or <code>defaultValue</code>
	 */
	protected StateValue get(String state, StateValue defaultValue) {
		
		StateValue result = get(state);
		return result == null ? defaultValue : result;
	}

	/**
	 * Create/update a value of the status.
	 * @param state The state name
	 * @param value The value of the state to set into the status
	 */
	protected void set(StateName state, StateValue value) {
		if (checkStateValue(state, value)) {
			stateList.put(state, value);
		} else {
			// TODO Create a StateValueException
			throw new IllegalArgumentException("Unable to set ["+ state +"] state to "+ value.getValue()
					+", the state value must be an instance of "+ stateTypes.get(state).getName());
		}
	}

	/**
	 * 
	 * @param state
	 */
	protected void remove(String state) {
		if (canRemove(state)) {
			stateList.remove(state);
		} else {
			// TODO NonRemoveableStateException
		}
	}

	/**
	 * Return all states with their values of the status.
	 * @return an unmodifiable set of state/value
	 */
	public Set<Entry<StateName, StateValue>> entrySet() {
		return Collections.unmodifiableSet(stateList.entrySet());
	}
	

	
	/**
	 * Return the states types in order to prevent from a wrong assigment when
	 * the {@link #set(String, StateValue)} method is called.
	 * @return A map of all the states types supported by this controller, where
	 * the key is the state name and the value associated is a {@link StateValue} class
	 */
	protected abstract Map<StateName, Class<? extends StateValue>> getSupportedStateTypes();

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
				states.put(entry.getKey().name(), entry.getValue().toJson());
			}
		}
		controllerJson.put("states", states);
		return controllerJson;
	}

	@Override
	public void fromJson(JSONObject jsonObject) throws UnmarshalException {
		setTitle(jsonObject.getString("title"));
		setDescription(jsonObject.getString("description"));
		JSONObject states = jsonObject.getJSONObject("states");
		for (String key : states.keySet()) {
			// TODO manage type statesList.put(key, states.get(key));
			
		} 
	}
}