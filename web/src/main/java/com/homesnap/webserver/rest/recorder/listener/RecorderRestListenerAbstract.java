package com.homesnap.webserver.rest.recorder.listener;

/*
 * #%L
 * HomeSnapWebServer
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
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

import java.util.Map;

import com.homesnap.recorder.RecorderService;

public class RecorderRestListenerAbstract {

	private RecorderService service;
	
	private String result = "";
	private String uri;
	private Map<String, String[]> parameters;

	public RecorderRestListenerAbstract(RecorderService service, String uri, Map<String, String[]> parameters) {
		super();
		this.service = service;
		this.uri = uri;
		this.parameters = parameters;
	}

	protected RecorderService getService() {
		return service;
	}
	
	public String getUri() {
		return uri;
	}

	public Map<String, String[]> getParameters() {
		return parameters;
	}

	public String getResult() {
		return result;
	}
	
	protected void setResult(String result) {
		this.result = result;
	}
}
