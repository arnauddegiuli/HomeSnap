package com.homesnap.engine.connector.onewire;

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
