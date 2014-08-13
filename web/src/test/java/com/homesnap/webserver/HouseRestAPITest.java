package com.homesnap.webserver;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.homesnap.engine.house.House;


public class HouseRestAPITest extends AbstractRestApi {

	String urn = "/house";
	@Before
	public void init() {
		try {
			copyFileUsingFileChannels(new File("Backup of house.xml"), new File("house.xml"));
		} catch (IOException e) {
			Assert.fail("Impossible to initialize file");
		}
	}
	// Creation
	@Test
	public void createHouse() {
		// Test impossible to create a house
		JSONObject o = postRequestJSONObject(urn, "{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
		Assert.assertEquals(null, o);
	}

	// Modification
	@Test
	public void putHouse() {
		// Test impossible to create a house
		putRequestJSONObject(urn, "{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	// Get
	@Test
	public void getHouse() {
		// Test to get a house with some group and label
		JSONObject jo = getRequestJSONObject(urn);

		// Test labels
		JSONArray labels = jo.getJSONArray(House.JSON_LABELS);
		testLabelCh1(labels.getJSONObject(0));
		testLabelCh2(labels.getJSONObject(1));

		// Test Groups
		JSONArray groups = jo.getJSONArray(House.JSON_GROUPS);
		testGroup1(groups.getJSONObject(0));
		System.out.print(jo.toString());
	}

	// Delete
	@Test
	public void deleteHouse() {
//		// Test impossible to create a house
//		JSONObject jo = deleteRequestJSONObject(urn, HttpServletResponse.SC_NO_CONTENT);
//		Assert.assertNull(jo);
//		// Test to get a house with some group and label
//		jo = getRequestJSONObject(urn);
//
//		// Test labels
//		JSONArray labels = jo.getJSONArray(House.JSON_LABELS);
//		Assert.assertEquals(0, labels.length());
//		JSONArray groups = jo.getJSONArray(House.JSON_GROUPS);
//		Assert.assertEquals(0, groups.length());
	}

	// Param
	@Test
	public void onStatus() {
		postRequestJSONObject(urn + "?param=param", "{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
		
		// Test to get a specific label
		JSONObject house = getRequestJSONObject(urn + "?param=param");
		JSONArray labels = house.getJSONArray(House.JSON_LABELS);
		JSONObject label = labels.getJSONObject(0);
		// Test label Ch1
		testLabelCh1(label);
	}
}
