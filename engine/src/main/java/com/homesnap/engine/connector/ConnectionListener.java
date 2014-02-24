package com.homesnap.engine.connector;

/*
 * #%L
 * MyDomoEngine
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
 * %%
 * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
 * 
 *     MyDomo is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     MyDomo is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

/**
 * ConnectionListener manage close and connect event.
 */
public interface ConnectionListener {

	/**
	 * Raise each time close is call on the connection.
	 */
	public void onClose();
	 
	/**
	 * Raise each time a connection to the gateway is try with the result of it,
	 * @param connectionStatus
	 */
	public void onConnect(ConnectionStatusEnum connectionStatus);
	 
}
