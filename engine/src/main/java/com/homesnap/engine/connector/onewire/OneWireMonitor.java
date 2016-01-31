package com.homesnap.engine.connector.onewire;

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


import java.util.HashMap;
import java.util.Map;

import com.dalsemi.onewire.adapter.DSPortAdapter;
import com.dalsemi.onewire.application.monitor.DeviceMonitor;
import com.dalsemi.onewire.application.monitor.DeviceMonitorEvent;
import com.dalsemi.onewire.application.monitor.DeviceMonitorEventListener;
import com.dalsemi.onewire.application.monitor.DeviceMonitorException;
import com.dalsemi.onewire.container.OneWireContainer;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
class OneWireMonitor implements DeviceMonitorEventListener {
	
	private DSPortAdapter adapter;
	private DeviceMonitor deviceMonitor;
	private Map<String, OneWireContainer> containers = new HashMap<String, OneWireContainer>();
	
	/**
	 * 
	 * @param adapter
	 */
	public OneWireMonitor(DSPortAdapter adapter) {
		this.adapter = adapter;
	}
	
	/**
	 * 
	 * @param address
	 * @return
	 */
	public OneWireContainer getContainer(String address) {
		return containers.get(address);
	}
	
	public void start() {
		deviceMonitor = new DeviceMonitor(adapter);
		deviceMonitor.addDeviceMonitorEventListener(this);
		new Thread(deviceMonitor).start();
	}
	
	public void stop() {
		deviceMonitor.killMonitor();
		deviceMonitor = null;
		containers.clear();
	}
	
	@Override
	public void deviceArrival(DeviceMonitorEvent event) {
		synchronized (containers) {
			for (int i=0; i<event.getDeviceCount(); i++) {
				OneWireContainer owc = event.getContainerAt(i);
				containers.put(owc.getAddressAsString(), owc);
				System.out.println("device found : "+ owc.getAddressAsString());
			}
		}
	}

	@Override
	public void deviceDeparture(DeviceMonitorEvent event) {
		synchronized (containers) {
			for (int i=0; i<event.getDeviceCount(); i++) {
				containers.remove(event.getContainerAt(i).getAddressAsString());
			}
		}
	}

	@Override
	public void networkException(DeviceMonitorException exception) {
		exception.printStackTrace();
	}
}
