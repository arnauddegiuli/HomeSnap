package com.homesnap.engine.connector.knxnetip;

import java.net.InetAddress;

import tuwien.auto.calimero.link.medium.KNXMediumSettings;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class KNXnetIPConnectionSettings {
	
	/** The local address which can access to the remote KNXnetIP device */
	private InetAddress localAddress;
	
	/** The remote address of the KNXnetIP device */
	private InetAddress remoteAddress;
	
	/** The remote port of the KNXnetIP device */
	private int port;
	
	/** The settings of the KNX network */
	private KNXMediumSettings settings;

	/**
	 * 
	 * @param locaAddress
	 * @param remoteAddress
	 * @param port
	 * @param settings
	 */
	KNXnetIPConnectionSettings(InetAddress locaAddress, InetAddress remoteAddress, int port, KNXMediumSettings settings) {
		this.localAddress = locaAddress;
		this.remoteAddress = remoteAddress;
		this.port = port;
		this.settings = settings;
	}
	
	/**
	 * 
	 * @return
	 */
	public InetAddress getLocalAddress() {
		return localAddress;
	}

	/**
	 * 
	 * @return
	 */
	public InetAddress getRemoteAddress() {
		return remoteAddress;
	}

	/**
	 * 
	 * @return
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 
	 * @return
	 */
	public KNXMediumSettings getSettings() {
		return settings;
	}
}
