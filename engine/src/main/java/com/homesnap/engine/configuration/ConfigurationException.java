package com.homesnap.engine.configuration;

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


/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class ConfigurationException extends RuntimeException {

	/** */
	private static final long serialVersionUID = -5911184766690148642L;
	
	/** The line number in the configuration file which cause the exception */
	private int lineNumer;

	/**
	 * 
	 * @param message
	 */
	public ConfigurationException(int lineNumer, String message) {
		super(message);
		this.lineNumer = lineNumer;
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public ConfigurationException(int lineNumer, String message, Throwable cause) {
		super(message, cause);
		this.lineNumer = lineNumer;
	}

	@Override
	public String getMessage() {
		return new StringBuilder("Error line ").append(lineNumer).append(": ").append(super.getMessage()).toString();
	}
}
