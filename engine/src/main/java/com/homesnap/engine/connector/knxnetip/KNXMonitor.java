package com.homesnap.engine.connector.knxnetip;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import tuwien.auto.calimero.CloseEvent;
import tuwien.auto.calimero.DataUnitBuilder;
import tuwien.auto.calimero.FrameEvent;
import tuwien.auto.calimero.cemi.CEMI;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.knxnetip.KNXnetIPConnection;
import tuwien.auto.calimero.link.KNXNetworkMonitor;
import tuwien.auto.calimero.link.KNXNetworkMonitorIP;
import tuwien.auto.calimero.link.event.LinkListener;
import tuwien.auto.calimero.link.event.MonitorFrameEvent;
import tuwien.auto.calimero.link.medium.RawFrame;
import tuwien.auto.calimero.link.medium.RawFrameBase;
import tuwien.auto.calimero.link.medium.TPSettings;

import com.homesnap.engine.connector.ConnectionListener;
import com.homesnap.engine.connector.Monitor;
import com.homesnap.engine.connector.UnknownControllerListener;
import com.homesnap.engine.controller.CommandListener;
import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.network.NetworkConfig;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class KNXMonitor implements Monitor, LinkListener {

	/** */
	private String ipAddress;
	
	/** */
	private KNXNetworkMonitor monitor;

	/** */
	private Set<Controller> controllers = new HashSet<Controller>();

	/**
	 * 
	 * @param ipAddress
	 * @param port
	 */
	KNXMonitor(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Override
	public synchronized boolean connect() {
		if (!isConnected()) {

			KNXnetIPConnectionSettings knxConnectionSettings = KNXDiscoverer.getInstance().findKNXNetIPAddresses()
					.get(NetworkConfig.getInstance().getInetAdress(ipAddress));

//			if (knxConnectionSettings == null) {
//				throw new RuntimeException("No KNXnetIP device found on the local network !");
//			}
			try {
				knxConnectionSettings = new KNXnetIPConnectionSettings(InetAddress.getByName("192.168.0.50"), InetAddress.getByName(ipAddress), KNXnetIPConnection.IP_PORT, TPSettings.TP1);
				
				InetSocketAddress localSocket = new InetSocketAddress(knxConnectionSettings.getLocalAddress(), 0);
				InetSocketAddress remoteSocket = new InetSocketAddress(knxConnectionSettings.getRemoteAddress(), knxConnectionSettings.getPort());
				monitor = new KNXNetworkMonitorIP(localSocket, remoteSocket, false, knxConnectionSettings.getSettings());
				System.out.println("Monitor connected !");
			} catch (KNXException e) {
				System.out.println("Monitor not connected !");
				e.printStackTrace();
			} catch (UnknownHostException e) {
				System.out.println("Monitor not connected !");
				e.printStackTrace();
			}
		}
		return isConnected();
	}

	@Override
	public boolean isConnected() {
		return (monitor != null) && monitor.isOpen();
	}

	@Override
	public synchronized void disconnect() {
		if (isConnected()) {
			monitor.close();
		}
	}

	@Override
	public void addControllerToMonitor(Controller controller) {
		synchronized (controllers) {
			controllers.add(controller);
		}
	}

	@Override
	public void removeControllerToMonitor(Controller controller) {
		synchronized (controllers) {
			controllers.remove(controller);
		}
	}

	@Override
	public void addConnectionListener(ConnectionListener connectionListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeConnectionListener(ConnectionListener connectionListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addControllerStatusListener(CommandListener commandListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeControllerStatusListener(CommandListener commandListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addUnknownControllerListener(UnknownControllerListener unknownControllerListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void removeUnknownControllerListener(UnknownControllerListener unknownControllerListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void indication(FrameEvent event) {
		
		CEMI frame = event.getFrame();
		if (frame instanceof MonitorFrameEvent) {
			
			System.out.println("Event received : "+ frame.toString());
			RawFrame rawFrame = ((MonitorFrameEvent) frame).getRawFrame();
			if (rawFrame != null) {
				System.out.println("Raw : "+ rawFrame.toString());
				if (rawFrame instanceof RawFrameBase) {
					RawFrameBase base = (RawFrameBase) rawFrame;
					System.out.println("Base : "+ DataUnitBuilder.decode(base.getTPDU(), base.getDestination()));
				}
			}
			
			synchronized (controllers) {
				for (Controller controller : controllers) {
//					if (who.equals(controller.getWho()) && where.equals(controller.getWhere())) {
//						known = true;
//						controller.changeState(what);
//					}
				}
			}
			
		} else {
			System.out.println("Unknow event frame received : "+ frame.toString());
		}
	}

	@Override
	public void linkClosed(CloseEvent event) {
		System.out.println("KNXMonitor received closing event : "+ event.getReason());
	}
}
