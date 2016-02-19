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
import com.homesnap.webserver.rest.recorder.RecorderRestAPI;
import com.homesnap.webserver.rest.recorder.utils.JSonTools;



public class RecorderGetListener extends RecorderRestListenerAbstract implements RecorderRestAPI {

	
	public static final String REST_API =	"Usage: http[s]://server:port/recorder\n" +
								"http[s]://server:port/recorder/{type}\n" +
								"http[s]://server:port/recorder/{type}/{probeId}\n" +
								"http[s]://server:port/recorder/{type}/{probeId}/{year}\n" +
								"http[s]://server:port/recorder/{type}/{probeId}/{year}/{month}\n" +
								"http[s]://server:port/recorder/{type}/{probeId}/{year}/{month}/{day}\n"
								;	

	
	public RecorderGetListener(RecorderService service, String uri, Map<String, String[]> parameters) {
		super(service, uri, parameters);
	}

	@Override
	public void onRecorder() {
		setResult(JSonTools.toJson(getService().getRecorderValueList()));
	}

	@Override
	public void onType(String type) {
		setResult(JSonTools.toJson(getService().getTypeValueList(type)));
	}

	@Override
	public void onProbe(String type, String probeId) {
		setResult(JSonTools.toJson(getService().getProbeValueList(type, probeId)));
	}

	@Override
	public void onYear(String type, String probeId, int year) {
		setResult(JSonTools.toJson(getService().getYearValueList(type, probeId, year)));
	}

	@Override
	public void onMonth(String type, String probeId, int year, int month) {
		setResult(JSonTools.toJson(getService().getMonthValueList(type, probeId, year, month)));
	}

	@Override
	public void onDay(String type, String probeId, int year, int month, int day) {
		setResult(JSonTools.toJson(getService().getDayValueList(type, probeId, year, month, day)));
	}
}
