package com.homesnap.webserver.rest.house;

/*
 * #%L
 * HomeSnapWebServer
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
 * %%
 * This file is part of HomeSnap done by A. de Giuli (arnaud.degiuli(at)free.fr).
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

public class RestOperationException extends Exception {

	/** serial uid */
	private static final long serialVersionUID = 1L;
	private String uri;
	private Verb verb;
	
	public RestOperationException(String uri, Verb verb, String message) {
		super("[" + verb + "]-["+ uri + "]: " + message);
		this.uri = uri;
		this.verb = verb;
	}

	public String getUri() {
		return uri;
	}

	public Verb getVerb() {
		return verb;
	}
}
