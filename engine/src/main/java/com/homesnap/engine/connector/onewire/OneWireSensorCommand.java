package com.homesnap.engine.connector.onewire;

import com.dalsemi.onewire.OneWireException;
import com.dalsemi.onewire.adapter.DSPortAdapter;
import com.dalsemi.onewire.container.OneWireSensor;
import com.homesnap.engine.controller.Command;
import com.homesnap.engine.controller.what.State;
import com.homesnap.engine.controller.what.StateValue;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 * @param <S>
 */
public abstract class OneWireSensorCommand<S extends OneWireSensor> {

	private DSPortAdapter adapter;
	private Command command;
	private S sensor;
	
	/**
	 * 
	 * @param adapter
	 * @param command
	 * @param sensor
	 */
	public OneWireSensorCommand(DSPortAdapter adapter, Command command, S sensor) {
		this.adapter = adapter;
		this.command = command;
		this.sensor = sensor;
	}
	
	public void send() {
		
		try {
			adapter.beginExclusive(true);
			byte[] state = sensor.readDevice();
			
			if (command.isStatusCommand()) {
				read(command.getWhat(), sensor, state);
			} else if (command.isActionCommand()) {
				write(command.getWhat(), sensor, state);
				sensor.writeDevice(state);
			} else {
				// TODO 
			}
		} catch (OneWireException e) {
			e.printStackTrace();
		}  finally {
			adapter.endExclusive();
		}
	}

	/**
	 * 
	 * @param state
	 * @param sensor
	 * @param deviceState
	 * @return
	 * @throws OneWireException
	 */
	public abstract StateValue read(State state, S sensor, byte[] deviceState) throws OneWireException;

	/**
	 * 
	 * @param state
	 * @param sensor
	 * @param deviceState
	 * @throws OneWireException
	 */
	public abstract void write(State state, S sensor, byte[] deviceState) throws OneWireException;
}
