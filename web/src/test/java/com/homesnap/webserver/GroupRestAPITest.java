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
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.homesnap.engine.controller.light.LightStatusStateValue;
import com.homesnap.engine.controller.who.Who;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroupRestAPITest extends AbstractRestApi {

	private static final String urn_groups = "/house/groups";

//	@Before
//	public void init() {
//		try {
//			copyFileUsingFileChannels(new File("Backup of house.xml"), new File("house.xml"));
//		} catch (IOException e) {
//			Assert.fail("Impossible to initialize file");
//		}
//	}

	@Test
	public void test0DeleteGroupList() {
		// Test impossible to create grouplist
		JSONObject jo = deleteRequestJSONObject(urn_groups, HttpServletResponse.SC_NO_CONTENT);
		Assert.assertNull(jo);

		// Test there is no more controllers
		JSONArray controllers = getRequestJSONArray(urn_groups);
		Assert.assertEquals(0, controllers.length());
	}
	
	@Test
	public void test1CreateGroupList() {
		// Test impossible to create grouplist
		postRequestJSONObject(urn_groups,"{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	@Test
	public void test2PutGroupList() {
		// Test impossible to create grouplist
		putRequestJSONObject(urn_groups,"{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}
	
	@Test
	public void test3CreateGroup() {
		// Test to get a specific group
		JSONObject group = postRequestJSONObject(urn_groups + "/6", createGroup6(), HttpServletResponse.SC_CREATED);
		// Test group 6
		testGroup6(group);

		group = postRequestJSONObject(urn_groups + "/group?id=10", createGroup10(), HttpServletResponse.SC_CREATED);
		// Test group 1
		testGroup10(group);

		group = postRequestJSONObject(urn_groups + "/group?id=1", createGroup1(), HttpServletResponse.SC_CREATED);
		// Test group 1
		testGroup1(group);
	}

	@Test
	public void test4GetGroup() {
		// Test to get a specific group
		JSONObject group = getRequestJSONObject(urn_groups + "/1");
		// Test group 1
		Assert.assertNotNull(group);
		testGroup1(group);

		group = getRequestJSONObject(urn_groups + "/group?id=10");
		// Test group 10
		Assert.assertNotNull(group);
		testGroup10(group);
	}
	@Test
	public void test5GetGroupList() {
		// Test to get groups list
		JSONArray groups = getRequestJSONArray(urn_groups);

		// Test groups
		testGroup10(groups.getJSONObject(1));
	}	
	
	@Test
	public void test6ModifyGroup() {
		// Test to get a specific group (TODO no add or remove by this methode... should be done unit by unit...)
		JSONObject group = putRequestJSONObject(urn_groups + "/1", createGroup1Bis(), HttpServletResponse.SC_OK);
		// Test group 1
		testGroup1Bis(group);

		group = putRequestJSONObject(urn_groups + "/group?id=6", createGroup6Bis(), HttpServletResponse.SC_OK);
		// Test group 1
		testGroup6Bis(group);
	}

	@Test
	public void test7OnStatus() {
		// Test to get a specific group
		JSONObject group = getRequestJSONObject("/house/groups/1?param=param");
		// Test group 1
		testGroup1Bis(group);

		group = getRequestJSONObject("/house/groups/group?id=10&param=param");
		// Test group 10
		testGroup10(group);
		
		
		putRequestJSONObject("/house/groups/1?param=param", createGroup1(), HttpServletResponse.SC_OK);
		putRequestJSONObject("/house/groups/group?id=1&param=param", createGroup1(), HttpServletResponse.SC_OK);
		
		postRequestJSONObject("/house/groups/1/21", createController21(), HttpServletResponse.SC_CREATED);
		JSONObject jo = getRequestJSONObject("/house/groups/1/controller?id=21&param=param");
		testController21(jo);

		jo = getRequestJSONObject("/house/groups/1/21?param=param");
		testController21(jo);
		

		testController21(jo);

		deleteRequestJSONObject("/house/groups/1?param=param", HttpServletResponse.SC_OK);
		deleteRequestJSONObject("/house/groups/group?id=1&param=param", HttpServletResponse.SC_NOT_ACCEPTABLE);
	}

	@Test
	public void test8DeleteGroup() {
		// Test to get a specific group
		JSONObject group = deleteRequestJSONObject(urn_groups + "/10", HttpServletResponse.SC_OK);
		// Test group 10
		testGroup10(group);

		group = deleteRequestJSONObject(urn_groups + "/group?id=6", HttpServletResponse.SC_OK);
		// Test group 10
		testGroup6Bis(group);
	}

	@Test
	public void test9DeleteGroupList() {
		// Test impossible to create grouplist
		JSONObject jo = deleteRequestJSONObject(urn_groups, HttpServletResponse.SC_NO_CONTENT);
		Assert.assertNull(jo);

		// Test there is no more controllers
		JSONArray controllers = getRequestJSONArray(urn_groups);
		Assert.assertEquals(0, controllers.length());
	}

	
	private String createController21() {
		return createJsonController("Bureau", "21", Who.LIGHT.name());
	}
	
	private void testController21(JSONObject jo) {
		testController(jo, "Bureau", "21", Who.LIGHT.name(), LightStatusStateValue.OFF.getValue());
	}
	
	private String createGroup1() {
		return createJsonGroup("Group 1", "1", "");
	}

	private void testGroup1(JSONObject group) {
		testGroup(group, "1", "Group 1", 0);
	}
	
	private String createGroup1Bis() {
		return createJsonGroup("Group Bis 1", "1", "");
	}

	private void testGroup1Bis(JSONObject group) {
		testGroup(group, "1", "Group Bis 1", 0);
	}
	
	private String createGroup6() {
		return createJsonGroup("Group 6", "6", "");
	}

	private void testGroup6(JSONObject group) {
		testGroup(group, "6", "Group 6", 0);
	}

	private String createGroup6Bis() {
		return createJsonGroup("Group 6 Bis", "6", "");
	}

	private void testGroup6Bis(JSONObject group) {
		testGroup(group, "6", "Group 6 Bis", 0);
	}
	
	private String createGroup10() {
		return createJsonGroup("Group 10", "10", "");
	}

	private void testGroup10(JSONObject group) {
		testGroup(group, "10", "Group 10", 0);
	}
}
