package com.homesnap.webserver;
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
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.house.Group;
import com.homesnap.engine.house.Label;
import com.homesnap.webserver.utils.JSonTools;
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
			if (returnCodeExpected == HttpServletResponse.SC_NOT_IMPLEMENTED) {
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
		if (returnCodeExpected == HttpServletResponse.SC_NO_CONTENT) {
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
			if (returnCodeExpected == HttpServletResponse.SC_NOT_IMPLEMENTED) {
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

	protected void testController11(JSONObject jo) {
		Assert.assertEquals("Dressing", jo.get(Controller.JSON_TITLE));
		Assert.assertEquals("11", jo.get(Controller.JSON_WHERE));
		Assert.assertEquals("LIGHT", jo.get(Controller.JSON_WHO)); // TODO replace string light by Who.LIGHT.name()
		
		JSONObject status = jo.getJSONObject(Controller.JSON_STATES);
		Assert.assertEquals("LIGHT_OFF", status.get(StateName.STATUS.getName()));
	}

	protected void testController12(JSONObject jo) {
		Assert.assertEquals("Suite Parentale", jo.get(Controller.JSON_TITLE));
		Assert.assertEquals("12", jo.get(Controller.JSON_WHERE));
		Assert.assertEquals("LIGHT", jo.get(Controller.JSON_WHO)); // TODO replace string light by Who.LIGHT.name()
		
		JSONObject status = jo.getJSONObject(Controller.JSON_STATES);
		Assert.assertEquals("LIGHT_OFF", status.get(StateName.STATUS.getName()));
	}

	/**
	 * Test that the json object represent the Tom chamber from the house.xml
	 * data sample.
	 * @param jo
	 */
	protected void testController61(JSONObject jo) {
		Assert.assertEquals("Chambre Tom", jo.get(Controller.JSON_TITLE));
		Assert.assertEquals("61", jo.get(Controller.JSON_WHERE));
		Assert.assertEquals("LIGHT", jo.get(Controller.JSON_WHO)); // TODO replace string light by Who.LIGHT.name()
		
		JSONObject status = jo.getJSONObject(Controller.JSON_STATES);
		Assert.assertEquals("LIGHT_OFF", status.get(StateName.STATUS.getName()));
	}

	protected void testController63(JSONObject jo) {
		Assert.assertEquals("Chambre Marius", jo.get(Controller.JSON_TITLE));
		Assert.assertEquals("63", jo.get(Controller.JSON_WHERE));
		Assert.assertEquals("LIGHT", jo.get(Controller.JSON_WHO)); // TODO replace string light by Who.LIGHT.name()
		
		JSONObject status = jo.getJSONObject(Controller.JSON_STATES);
		Assert.assertEquals("LIGHT_OFF", status.get(StateName.STATUS.getName()));
	}

	protected void testLabelCh1(JSONObject label) {
		Assert.assertEquals("ch1", label.get(Label.JSON_ID));
		Assert.assertEquals("Chambre Tom", label.get(Label.JSON_TITLE));
// TODO		Assert.assertEquals("Une description", label.get(Label.JSON_DESCRIPTION));
// TODO		Assert.assertEquals("Un icon", label.get(Label.JSON_ICON));
		JSONArray controllers = label.getJSONArray(Label.JSON_CONTROLLERS);
		testController61(controllers.getJSONObject(0));
		
	}
	
	protected void testLabelCh2(JSONObject label) {
		Assert.assertEquals("ch2", label.get(Label.JSON_ID));
		Assert.assertEquals("Chambre Marius", label.get(Label.JSON_TITLE));
// TODO		Assert.assertEquals("Une description", label.get(Label.JSON_DESCRIPTION));
// TODO		Assert.assertEquals("Un icon", label.get(Label.JSON_ICON));
		JSONArray controllers = label.getJSONArray(Label.JSON_CONTROLLERS);
		testController63(controllers.getJSONObject(0));
		
	}

	protected void testGroup1(JSONObject group) {
		Assert.assertEquals("1", group.get(Group.JSON_ID));
		Assert.assertEquals("Group 1", group.get(Group.JSON_TITLE));
// TODO		Assert.assertEquals("Une description", group.get(Group.JSON_DESCRIPTION));
		JSONArray controllers = group.getJSONArray(Group.JSON_CONTROLLERS);
		testController11(controllers.getJSONObject(0));
		testController12(controllers.getJSONObject(1));
		
	}
	
	protected String createJsonController11() {
		return "{" +
			Controller.JSON_TITLE + ": 'Chambre 11';" +
			Controller.JSON_WHERE + ":11;" +
			Controller.JSON_WHO   + ":'LIGHT'" + // TODO replace string light by Who.LIGHT.name()
			"}";
	}

	protected String createJsonController6() {
		return "{" +
			Controller.JSON_TITLE + ": 'Chambre 6';" +
			Controller.JSON_WHERE + ":6;" +
			Controller.JSON_WHO   + ":'LIGHT'" + // TODO replace string light by Who.LIGHT.name()
			"}";
	}

	protected String createJsonController16() {
		return "{" +
			Controller.JSON_TITLE + ": 'Chambre 16';" +
			Controller.JSON_WHERE + ":16;" +
			Controller.JSON_WHO   + ":'LIGHT'" + // TODO replace string light by Who.LIGHT.name()
			"}";
	}

	/**
	 * Test that the json object represent the Tom chamber from the house.xml
	 * data sample.
	 * @param jo
	 */
	protected void testController16(JSONObject jo) {
		Assert.assertEquals("Chambre 16", jo.get(Controller.JSON_TITLE));
		Assert.assertEquals("16", jo.get(Controller.JSON_WHERE));
		Assert.assertEquals("LIGHT", jo.get(Controller.JSON_WHO)); // TODO replace string light by Who.LIGHT.name()
		
		JSONObject status = jo.getJSONObject(Controller.JSON_STATES);
		Assert.assertEquals("LIGHT_OFF", status.get(StateName.STATUS.getName()));
	}
	
	protected String createJsonController17() {
		return "{" +
			Controller.JSON_TITLE + ": 'Chambre 17';" +
			Controller.JSON_WHERE + ":17;" +
			Controller.JSON_WHO   + ":'LIGHT'" + // TODO replace string light by Who.LIGHT.name()
			"}";
	}

	/**
	 * Test that the json object represent the Tom chamber from the house.xml
	 * data sample.
	 * @param jo
	 */
	protected void testController17(JSONObject jo) {
		Assert.assertEquals("Chambre 17", jo.get(Controller.JSON_TITLE));
		Assert.assertEquals("17", jo.get(Controller.JSON_WHERE));
		Assert.assertEquals("LIGHT", jo.get(Controller.JSON_WHO)); // TODO replace string light by Who.LIGHT.name()
		
		JSONObject status = jo.getJSONObject(Controller.JSON_STATES);
		Assert.assertEquals("LIGHT_OFF", status.get(StateName.STATUS.getName()));
	}

	protected String createJsonController61() {
		return "{" +
			Controller.JSON_TITLE + ": 'Chambre Tom';" +
			Controller.JSON_WHERE + ":61;" +
			Controller.JSON_WHO   + ":'LIGHT'" + // TODO replace string light by Who.LIGHT.name()
			"}";
	}


	protected String createJsonLabelCh6() {
		return "{" + 
				Label.JSON_TITLE + ": 'Chambre 6'; " +
				Label.JSON_ID + ": 'ch6';"+
				Label.JSON_CONTROLLERS + ": [" + createJsonController61() + "]" +
			"}";
	}

	protected void testLabelCh6(JSONObject label) {
		Assert.assertEquals("ch6", label.get(Label.JSON_ID));
		Assert.assertEquals("Chambre 6", label.get(Label.JSON_TITLE));
// TODO		Assert.assertEquals("Une description", label.get(Label.JSON_DESCRIPTION));
// TODO		Assert.assertEquals("Un icon", label.get(Label.JSON_ICON));
		JSONArray controllers = label.getJSONArray(Label.JSON_CONTROLLERS);
		testController61(controllers.getJSONObject(0));
	}

	protected String createJsonLabelCh7() {
		return "{" + 
				Label.JSON_TITLE + ": 'Chambre 7'; " +
				Label.JSON_ID + ": 'ch7';"+
				Label.JSON_CONTROLLERS + ": [" + createJsonController61() + "]" +
			"}";
	}

	protected void testLabelCh7(JSONObject label) {
		Assert.assertEquals("ch7", label.get(Label.JSON_ID));
		Assert.assertEquals("Chambre 7", label.get(Label.JSON_TITLE));
// TODO		Assert.assertEquals("Une description", label.get(Label.JSON_DESCRIPTION));
// TODO		Assert.assertEquals("Un icon", label.get(Label.JSON_ICON));
		JSONArray controllers = label.getJSONArray(Label.JSON_CONTROLLERS);
		testController61(controllers.getJSONObject(0));
	}

	protected String createGroup6() {
		return "{" + 
				Label.JSON_TITLE + ": 'Group 6'; " +
				Label.JSON_ID + ": '6';"+
				Label.JSON_CONTROLLERS + ": [" + createJsonController61() + "]" + // Controller are not created... => normal
			"}";
	}

	protected void testGroup6(JSONObject group) {
		Assert.assertEquals("6", group.get(Group.JSON_ID));
		Assert.assertEquals("Group 6", group.get(Group.JSON_TITLE));
// TODO		Assert.assertEquals("Une description", group.get(Group.JSON_DESCRIPTION));
		JSONArray controllers = group.getJSONArray(Group.JSON_CONTROLLERS);
		Assert.assertEquals(0, controllers.length());
	}

	protected String createGroup7() {
		return "{" + 
				Label.JSON_TITLE + ": 'Group 7'; " +
				Label.JSON_ID + ": '7';"+
				Label.JSON_CONTROLLERS + ": []" +
			"}";
	}

	protected void testGroup7(JSONObject group) {
		Assert.assertEquals("7", group.get(Group.JSON_ID));
		Assert.assertEquals("Group 7", group.get(Group.JSON_TITLE));
// TODO		Assert.assertEquals("Une description", group.get(Group.JSON_DESCRIPTION));
		JSONArray controllers = group.getJSONArray(Group.JSON_CONTROLLERS);
		Assert.assertEquals(0, controllers.length());
	}
}
