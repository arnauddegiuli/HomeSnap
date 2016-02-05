package com.homesnap.engine.connector.knxnetip;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2015 A. de Giuli
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
