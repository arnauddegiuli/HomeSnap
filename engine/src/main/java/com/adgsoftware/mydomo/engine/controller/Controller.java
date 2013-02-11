package com.adgsoftware.mydomo.engine.controller;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.adgsoftware.mydomo.engine.Command;
import com.adgsoftware.mydomo.engine.connector.CommandListener;
import com.adgsoftware.mydomo.engine.connector.CommandResult;
import com.adgsoftware.mydomo.engine.connector.CommandResultStatus;
import com.adgsoftware.mydomo.engine.connector.Commander;

/**
 * Controller represents a controller for a device.
 * <br>
 * A controller can be associated to different label.
 *
 * @param <T>
 */
public abstract class Controller<T extends Status>
implements Serializable {

	/** serial uid */
	private static final long serialVersionUID = 1L;
	private T what;
	protected String where;
	private String title;
	protected transient Commander server;
    private List<ControllerChangeListener> controllerChangeListenerList = new ArrayList<ControllerChangeListener>();
    private LabelList labelList = new LabelList(this);
    
    /**
     * Return the address of the device
     * @return address of the device
     */
	public String getWhere() {
		return where;
	}
	
	/**
	 * Define the address of the device to control
	 * @param newValue address of the device
	 */
	public void setWhere(String newValue) {
		this.where = newValue;
		if (newValue == null) { // Manage null value because we create the controller with no address
			what = null;
		} else {
			executeStatus(new StatusListener<T>() {
				@Override
				public void onStatus(T status, CommandResult result) {
					what = status;
				}
			});
		}
	}
	
	/**
	 * Return the {@link Status} of the device
	 * @return the {@link Status} of the device 
	 */
	public T getWhat() {
		return what;
	}
	
	/**
	 * Define the new {@link Status} of the device.
	 * @param newWhat {@link Status} of the device.
	 */
	public void setWhat(final T newWhat) {
		final T oldStatus = what; // FIXME: see if we just lunch the command and monitor change the state, or if we do the two here (send command AND change state...)
		what = newWhat;
		executeAction( new CommandListener() {
			@Override
			public void onCommand(CommandResult result) {
				if (CommandResultStatus.ok.equals(result.status)) {
					// Status has been changed
					notifyWhatChange(what, newWhat);
				} else {
					// Error
					what = oldStatus;
					notifyWhatChangeError(what, newWhat, result);
				}
			}
		});
		
	}
	
	/**
	 * Define the new {@link Status} of the device
	 * without sent the command on the bus.
	 * @param newWhat {@link Status} of the device.
	 */
	public void changeWhat(T newWhat) {
		notifyWhatChange(this.what, newWhat);
		this.what = newWhat;
	}
	
	/**
	 * Create the open message for action.
	 * @return open message.
	 */
	protected String createActionMessage() {
		if (where == null) {
			throw new IllegalArgumentException("Controller must contain an address with where");
		}
		return MessageFormat.format(Command.COMMAND, new Object[] {getWho(), getWhat().getCode(), where}); 
	}
	
	/**
	 * Create the open message for status.
	 * @return open message.
	 */
	protected String createStatusMessage() {
		if (where == null) {
			throw new IllegalArgumentException("Controller must contain an address with where");
		}
		return MessageFormat.format(Command.STATUS, new Object[] {getWho(), where}); 
	}

	/**
	 * Execute an action
	 * @return result of action execution.
	 */
	protected void executeAction(CommandListener commandListener) {
		if (server == null || getWhat() == null) {
			commandListener.onCommand(new CommandResult("", CommandResultStatus.nok));
		} else {
			server.sendCommand(createActionMessage(), commandListener);
		}

	}
	
	/**
	 * Get the status of the controller.
	 * @return status of the controller.
	 */
	protected void executeStatus(final StatusListener<T> statusListener) {
		
		if (server == null) {
			statusListener.onStatus(what, new CommandResult("", CommandResultStatus.nok));
		} else {
			server.sendCommand(createStatusMessage(), new CommandListener() {
				@Override
				public void onCommand(CommandResult result) {
					if (CommandResultStatus.ok.equals(result.status)) {
						// Return the status of the controller from the server
						statusListener.onStatus(getStatus(Command.getWhatFromCommand(result.commandResult)), result);
					} else {
						// ERROR: message not sent on Bus or error return... we keep the last value
						statusListener.onStatus(what, result);
					}
					
				}
			});
		}
		
		
		
		
		
		
	}
		
	protected abstract String getWho();
	
	public abstract T getStatus(String what);
	
	/**
	 * Define the gateway to connect on.
	 * @param server
	 */
	public void setServer(Commander server) {	
		this.server= server ;
	}
	
    /** @param l the new change listener. */
    public void addControllerChangeListener(ControllerChangeListener l) {
    	controllerChangeListenerList.add(l);
    }
	
    private void notifyWhatChange(Status oldStatus, Status newStatus) {
    	for (ControllerChangeListener listener : controllerChangeListenerList) {
    		listener.onWhatChange(this, oldStatus, newStatus); 
		}
    }
    
    private void notifyWhatChangeError(Status oldStatus, Status newStatus, CommandResult result) {
    	for (ControllerChangeListener listener : controllerChangeListenerList) {
    		listener.onWhatChangeError(this, oldStatus, newStatus, result); 
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
	 * @return list of label.
	 */
	public LabelList getLabels(){
		return labelList;
	}

}