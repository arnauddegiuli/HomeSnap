package com.homesnap.webserver;

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
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.homesnap.engine.controller.who.Who;
import com.homesnap.engine.house.House;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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

	// Delete
	@Test
	public void test1DeleteHouse() {
		// Test impossible to create a house
		JSONObject jo = deleteRequestJSONObject(urn, HttpServletResponse.SC_NO_CONTENT);
		Assert.assertNull(jo);
		// Test to get a house with some group and label
		jo = getRequestJSONObject(urn);

		// Test labels
		JSONArray labels = jo.getJSONArray(House.JSON_LABELS);
		Assert.assertEquals(0, labels.length());
		JSONArray groups = jo.getJSONArray(House.JSON_GROUPS);
		Assert.assertEquals(0, groups.length());
	}

	// Creation
	@Test
	public void test2CreateHouse() {
		// Test impossible to create a house
		JSONObject o = postRequestJSONObject(urn, "{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
		Assert.assertEquals(null, o);
	}

	// Modification
	@Test
	public void test3PutHouse() {
		// Test impossible to create a house
		putRequestJSONObject(urn, "{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	// Get
	@Test
	public void test4GetHouse() {

		postRequestJSONObject(urn + "/labels/ch1", createLabelCh1(), HttpServletResponse.SC_CREATED);
		postRequestJSONObject(urn + "/groups/6", createGroup6(), HttpServletResponse.SC_CREATED);
		postRequestJSONObject(urn + "/labels/ch2", createLabelCh2(), HttpServletResponse.SC_CREATED);
		postRequestJSONObject(urn + "/groups/2", createGroup2(), HttpServletResponse.SC_CREATED);
		
		// Test to get a house with some group and label
		JSONObject jo = getRequestJSONObject(urn);

		// Test labels
		JSONArray labels = jo.getJSONArray(House.JSON_LABELS);
		testLabelCh1(labels.getJSONObject(0));
		testLabelCh2(labels.getJSONObject(1));

		// Test Groups
		JSONArray groups = jo.getJSONArray(House.JSON_GROUPS);
		testGroup6(groups.getJSONObject(0));
		testGroup2(groups.getJSONObject(1));
	}



	// Param
	@Test
	public void test5OnStatus() {
		postRequestJSONObject(urn + "?param=param", "{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
		
		// Test to get a specific label
		JSONObject house = getRequestJSONObject(urn + "?param=param");
		JSONArray labels = house.getJSONArray(House.JSON_LABELS);
		JSONObject label = labels.getJSONObject(0);
		// Test label Ch1
		testLabelCh1(label);
	}
	
	private String createLabelCh1() {
		return createJsonLabel("Chambre Tom", "ch1");
	}
	
	private void testLabelCh1(JSONObject label) {
		testLabel(label, "ch1", "Chambre Tom", 0);
	}
	
	private String createLabelCh2() {
		return createJsonLabel("Chambre Marius", "ch2");
	}

	private void testLabelCh2(JSONObject label) {
		testLabel(label, "ch2", "Chambre Marius", 0);
	}

	private String createGroup2() {
		return createJsonGroup("Group 2", "2", createJsonController21());
	}

	private void testGroup2(JSONObject group) {
		testGroup(group, "2", "Group 2", 0);
	}
	
	private String createGroup6() {
		return createJsonGroup("Group 6", "6", "");
	}

	private void testGroup6(JSONObject group) {
		testGroup(group, "6", "Group 6", 0);
	}

	private String createJsonController21() {
		return createJsonController("Chambre Tom", "21", Who.LIGHT.name());
	}
}
