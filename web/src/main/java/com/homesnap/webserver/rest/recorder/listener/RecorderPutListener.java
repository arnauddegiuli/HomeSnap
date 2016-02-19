package com.homesnap.webserver.rest.recorder.listener;

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

import java.util.Map;

import com.homesnap.recorder.RecorderService;
import com.homesnap.webserver.rest.recorder.MissingParameterRestOperation;
import com.homesnap.webserver.rest.recorder.RecorderRestAPI;
import com.homesnap.webserver.rest.recorder.RestOperationException;
import com.homesnap.webserver.rest.recorder.UnsupportedRestOperation;
import com.homesnap.webserver.rest.recorder.Verb;




// Modification
public class RecorderPutListener extends RecorderRestListenerAbstract implements RecorderRestAPI {


	public RecorderPutListener(RecorderService service, String uri, Map<String, String[]> parameters, String body) {
		super(service, uri, parameters);
	}

	@Override
	public void onRecorder()
			throws UnsupportedRestOperation,
			RestOperationException,
			MissingParameterRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
	}

	@Override
	public void onType(String type)
			throws UnsupportedRestOperation,
			RestOperationException,
			MissingParameterRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
		
	}

	@Override
	public void onProbe(String type, String probeId)
			throws UnsupportedRestOperation,
			RestOperationException,
			MissingParameterRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
		
	}

	@Override
	public void onYear(String type, String probeId, int year)
			throws UnsupportedRestOperation,
			RestOperationException,
			MissingParameterRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
		
	}

	@Override
	public void onMonth(String type, String probeId, int year, int month)
			throws UnsupportedRestOperation,
			RestOperationException,
			MissingParameterRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
		
	}

	@Override
	public void onDay(String type, String probeId, int year, int month, int day)
			throws UnsupportedRestOperation,
			RestOperationException,
			MissingParameterRestOperation {
		throw new UnsupportedRestOperation(getUri(), Verb.PUT);
		
	}
}
