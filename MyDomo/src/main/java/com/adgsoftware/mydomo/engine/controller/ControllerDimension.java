package com.adgsoftware.mydomo.engine.controller;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.connector.CommandResultStatus;

public abstract class ControllerDimension<T extends Status> extends Controller<T> 
implements Serializable {

	/** serial uid */
	private static final long serialVersionUID = 1L;
	private List<ControllerDimensionChangeListener> controllerDimensionChangeListenerList = new ArrayList<ControllerDimensionChangeListener>();
	protected Hashtable<String, DimensionStatus> list = new Hashtable<String, DimensionStatus>(); // Cache of dimension status => avoid to call the bus each time

	
	protected String createDimensionActionMessage(DimensionStatus dimensionStatus) {
		if (where == null) {
			throw new IllegalArgumentException("Controller must contain a where.");
		}
		
		StringBuilder sb = new StringBuilder();
		for (DimensionValue dimension : dimensionStatus.getValueList()) {
			sb.append(dimension.getValue());
			sb.append(Command.DIMENSION_SEPARATOR);
		}
		sb.setLength(sb.length()-1);
		
		return MessageFormat.format(Command.DIMENSION_COMMAND, new Object[] {getWho(), getWhere(), dimensionStatus.getCode(), sb.toString()}); 
	}
	
	protected String createDimensionStatusMessage(DimensionStatus dimension) {
		if (getWhere() == null) {
			throw new IllegalArgumentException("Controller must contain a where.");
		}
		return MessageFormat.format(Command.DIMENSION_STATUS, new Object[] {getWho(), getWhere(), dimension.getCode()}); 
	}
	
	protected CommandResult executeAction(DimensionStatus dimensionStatus) {
		CommandResult result;
		if (server == null) {
			result = new CommandResult("", CommandResultStatus.nok);
		} else {
			result = server.sendCommand(createDimensionActionMessage(dimensionStatus));
		}
		return result;
	}
		
	protected <D extends DimensionStatus> D executeStatus(D dimensionStatus) {
		
		CommandResult result;
		if (server == null) {
			result = new CommandResult("", CommandResultStatus.nok);
		} else {
			result = server.sendCommand(createDimensionStatusMessage(dimensionStatus));
		}
		
		if (CommandResultStatus.ok.equals(result.status)) {
			List<DimensionValue> dimensionListResult = Command.getDimensionListFromCommand(result.commandResult);
			dimensionStatus.setValueList(dimensionListResult);
			return dimensionStatus;
		} else {
			return null;
		}
	}
	
    protected void notifyDimensionChange(DimensionStatus newStatus) {
    	for (ControllerDimensionChangeListener listener : controllerDimensionChangeListenerList) {
    		listener.onDimensionChange(this, newStatus); 
		}
    }
    
    protected void notifyDimensionChangeError(DimensionStatus newStatus, CommandResult result) {
    	for (ControllerDimensionChangeListener listener : controllerDimensionChangeListenerList) {
    		listener.onDimensionChangeError(this, newStatus, result); 
		}
    }
    
    /**
	 * Get the {@link DimensionStatus} of the device
	 * by sending the command on the bus.
	 * @param clazz class of the dimensionStatus of the device.
	 * @return the dimension status
	 */
	protected <D extends DimensionStatus> D  getDimensionStatus(Class<D> clazz) {
		try {
			D result = clazz.newInstance();
			return executeStatus(result);
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Define the {@link Status} of the device.
	 * @param what {@link Status} of the device.
	 */
	protected void setDimensionStatus(DimensionStatus dimensionStatus) {
		CommandResult result = executeAction(dimensionStatus);
		if (CommandResultStatus.ok.equals(result.status)) {
			notifyDimensionChange(dimensionStatus);
			list.put(dimensionStatus.getCode(), dimensionStatus);
		} else {
			// Error...
			notifyDimensionChangeError(dimensionStatus, result);
		}
	}
	
	/**
	 * Define the new {@link DimensionStatus} of the device
	 * without sent the command on the bus.
	 * @param newWhat {@link DimensionStatus} of the device.
	 */
	public void changeDimensionStatus(DimensionStatus dimensionStatus) {
		if (dimensionStatus != null) {
			notifyDimensionChange(dimensionStatus);
			list.put(dimensionStatus.getCode(), dimensionStatus);
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
