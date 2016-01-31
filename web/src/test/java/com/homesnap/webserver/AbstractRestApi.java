package com.homesnap.webserver;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.house.Group;
import com.homesnap.engine.house.Label;
import com.homesnap.webserver.rest.house.utils.JSonTools;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;


public class AbstractRestApi {

	protected static String server = "localhost";
	protected static String port = "8080";

	protected void copyFileUsingFileChannels(File source, File dest) throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		}
		finally {
			inputChannel.close();
			outputChannel.close();
		}
	}
	
	protected JSONObject getRequestJSONObject(String urn) {

		Client client = Client.create();
		WebResource webResource = client
			.resource("http://" + server + ":" + port + urn);

		String json = null;
		try {
			ClientResponse response = webResource.accept("application/json")
					.get(ClientResponse.class);
			json = response.getEntity(String.class);
			return JSonTools.fromJson(json);
		} catch (JSONException e) {
			e.printStackTrace();
			Assert.fail("Problem with JSON [" + json + "] :" + e.getMessage());
		}
		Assert.fail("Problem when call [" + urn + "].");
		return null;
	}

	protected JSONArray getRequestJSONArray(String urn) {
		Client client = Client.create();
		WebResource webResource = client
			.resource("http://" + server + ":" + port + urn);
		String json = null;
		try {
			ClientResponse response = webResource.accept("application/json")
					.get(ClientResponse.class);
			json = response.getEntity(String.class);
			return new JSONArray(json); // TODO manage Array null
		} catch (JSONException e) {
			e.printStackTrace();
			Assert.fail("Problem with JSON [" + json + "] :" + e.getMessage());
			Assert.fail(e.getMessage());
		}
		Assert.fail("Problem when call [" + urn + "].");
		return null;
	}

	protected JSONObject postRequestJSONObject(String urn, String body, int returnCodeExpected) {
		Client client = Client.create();
		WebResource webResource = client
			.resource("http://" + server + ":" + port + urn);

		String json = null;
		try {
			ClientResponse response = webResource.accept("application/json")
					.post(ClientResponse.class, body);
			Assert.assertEquals(returnCodeExpected, response.getStatus());
			if (returnCodeExpected == HttpServletResponse.SC_NOT_IMPLEMENTED || returnCodeExpected == HttpServletResponse.SC_NOT_ACCEPTABLE|| returnCodeExpected == HttpServletResponse.SC_BAD_REQUEST) {
				return null;
			}

			json = response.getEntity(String.class);
			return JSonTools.fromJson(json);
			
		} catch (JSONException e) {
			e.printStackTrace();
			Assert.fail("Problem with JSON [" + json + "] :" + e.getMessage());
		}
		Assert.fail("Problem when call [" + urn + "].");
		return null;
	}

	protected JSONObject deleteRequestJSONObject(String urn, int returnCodeExpected) {
		Client client = Client.create();
		WebResource webResource = client
			.resource("http://" + server + ":" + port + urn);
		ClientResponse response = webResource.accept("application/json")
			.delete(ClientResponse.class);
		Assert.assertEquals(returnCodeExpected, response.getStatus());
		if (returnCodeExpected == HttpServletResponse.SC_NO_CONTENT  || returnCodeExpected == HttpServletResponse.SC_NOT_ACCEPTABLE || returnCodeExpected == HttpServletResponse.SC_NOT_IMPLEMENTED) {
			return null;
		}

		String json = null;
		try {
			json = response.getEntity(String.class);
			return JSonTools.fromJson(json);
		} catch (JSONException e) {
			e.printStackTrace();
			Assert.fail("Problem with JSON [" + json + "] :" + e.getMessage());
		}
		return null;
	}

	protected JSONObject putRequestJSONObject(String urn, String body, int returnCodeExpected) {
		Client client = Client.create();
		WebResource webResource = client
			.resource("http://" + server + ":" + port + urn);
		
		String json = null;
		try {
			ClientResponse response = webResource.accept("application/json")
					.put(ClientResponse.class, body);
			Assert.assertEquals(returnCodeExpected, response.getStatus());
			if (returnCodeExpected == HttpServletResponse.SC_NOT_IMPLEMENTED || returnCodeExpected == HttpServletResponse.SC_NOT_ACCEPTABLE || returnCodeExpected == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
				return null;
			}
			json = response.getEntity(String.class);
			return JSonTools.fromJson(json);
		} catch (JSONException e) {
			e.printStackTrace();
			Assert.fail("Problem with JSON [" + json + "] :" + e.getMessage());
		}
		Assert.fail("Problem when call [" + urn + "].");
		return null;
	}

	protected void testError(JSONObject jo, String errorClassname) {
		JSONArray errorList = jo.getJSONArray(JSonTools.ERROR);
		JSONObject error = errorList.getJSONObject(0);
		Assert.assertEquals(errorClassname, error.getString(JSonTools.ERROR_CLASSNAME));
		
	}

	protected String createJsonController(String title, String where, String who) {
		return "{" +
				Controller.JSON_TITLE + ": '" + title + "';" +
				Controller.JSON_WHERE + ": '" + where + "';" +
				Controller.JSON_WHO + ": '" + who + "'" +
				"}";
	}
	
	protected void testController(JSONObject jo, String title, String where, String who, String statusValue) {
		Assert.assertNotNull(jo);
		Assert.assertEquals(title, jo.get(Controller.JSON_TITLE));
		Assert.assertEquals(where, jo.get(Controller.JSON_WHERE));
		Assert.assertEquals(who, jo.get(Controller.JSON_WHO));
		
		JSONObject status = jo.getJSONObject(Controller.JSON_STATES);
		Assert.assertEquals(statusValue, status.get("status"/*StateName.STATUS.getName()*/));
	}

	protected String createJsonGroup(String title, String where, String controllersList) {
		return "{" + 
				Label.JSON_TITLE + ": '" + title + "'; " +
				Label.JSON_ID + ": '" + where + "';"+
				Label.JSON_CONTROLLERS + ": [" + controllersList + "]" + // Controller are not created... => normal
			"}";
	}

	protected void testGroup(JSONObject group, String where, String title, int controllersNumber) {
		Assert.assertNotNull(group);
		Assert.assertEquals(where, group.get(Group.JSON_ID));
		Assert.assertEquals(title, group.get(Group.JSON_TITLE));
// TODO		Assert.assertEquals("Une description", group.get(Group.JSON_DESCRIPTION));
		JSONArray controllers = group.getJSONArray(Group.JSON_CONTROLLERS);
		Assert.assertEquals(controllersNumber, controllers.length());
	}

	protected String createJsonLabel(String title, String where) {
		return "{" +
		Label.JSON_TITLE + ": '" + title + "';" +
		Label.JSON_ID + ": '" + where + "';" +
		"}";
	}
	
	protected void testLabel(JSONObject label, String where, String title, int controllersNumber) {
		Assert.assertEquals(where, label.get(Label.JSON_ID));
		Assert.assertEquals(title, label.get(Label.JSON_TITLE));
// TODO		Assert.assertEquals("Une description", label.get(Label.JSON_DESCRIPTION));
// TODO		Assert.assertEquals("Un icon", label.get(Label.JSON_ICON));
		JSONArray controllers = label.getJSONArray(Label.JSON_CONTROLLERS);
		Assert.assertEquals(controllersNumber, controllers.length());
	}
}
