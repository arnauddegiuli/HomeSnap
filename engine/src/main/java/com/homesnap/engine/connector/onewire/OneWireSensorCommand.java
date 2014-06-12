package com.homesnap.engine.connector.onewire;

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


import com.dalsemi.onewire.OneWireException;
import com.dalsemi.onewire.adapter.DSPortAdapter;
import com.dalsemi.onewire.container.OneWireSensor;
import com.homesnap.engine.connector.Command;
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
