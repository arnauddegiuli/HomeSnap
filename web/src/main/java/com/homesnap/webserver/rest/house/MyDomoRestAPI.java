package com.homesnap.webserver.rest.house;

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




public interface MyDomoRestAPI {

	public void onHouse() throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onLabelList() throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onLabel(String labelId) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onControllerByLabel(String labelId, String where) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onGroupList() throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onGroup(String groupId) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onControllerByGroup(String groupId, String where) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
	public void onController(String where) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
//	public void onStatus(String name, String[] value) throws UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation;
}
