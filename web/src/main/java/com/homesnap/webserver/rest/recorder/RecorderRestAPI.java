package com.homesnap.webserver.rest.recorder;

/*
 * #%L
 * HomeSnapWebServer
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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




public interface RecorderRestAPI {

	public void onRecorder() throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onType(String type) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onProbe(String type, String probeId) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onYear(String type, String probeId, int year) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onMonth(String type, String probeId, int year, int month) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onDay(String type, String probeId, int year, int month, int day) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
}
