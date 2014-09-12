package com.homesnap.webserver;
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


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroupRestAPITest extends AbstractRestApi {

	private static final String urn_groups = "/house/groups";

	@Before
	public void init() {
		try {
			copyFileUsingFileChannels(new File("Backup of house.xml"), new File("house.xml"));
		} catch (IOException e) {
			Assert.fail("Impossible to initialize file");
		}
	}

	@Test
	public void aCreateGroupList() {
		// Test impossible to create grouplist
		postRequestJSONObject(urn_groups,"{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	@Test
	public void bPutGroupList() {
		// Test impossible to create grouplist
		putRequestJSONObject(urn_groups,"{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	@Test
	public void cgetGroupList() {
		// Test to get groups list
		JSONArray groups = getRequestJSONArray(urn_groups);

		// Test groups
		testGroup2(groups.getJSONObject(1));
	}	
	
	@Test
	public void fModifyGroup() {
		// Test to get a specific group (TODO no add or remove by this methode... should be done unit by unit...)
		JSONObject group = putRequestJSONObject(urn_groups + "/4", createGroup4(), HttpServletResponse.SC_OK);
		// Test group 1
		testGroup4(group);

		group = putRequestJSONObject(urn_groups + "/group?id=5", createGroup5(), HttpServletResponse.SC_OK);
		// Test group 1
		testGroup5(group);
	}






	@Test
	public void fOnStatus() {
		// Test to get a specific group
		JSONObject group = getRequestJSONObject("/house/groups/2?param=param");
		// Test group 1
		testGroup2(group);

		group = getRequestJSONObject("/house/groups/group?id=2&param=param");
		// Test group 1
		testGroup2(group);
		
		JSONObject jo = getRequestJSONObject("/house/groups/2/controller?id=21&param=param");
		testController21(jo);

		jo = getRequestJSONObject("/house/groups/2/controller/21?param=param");
		testController21(jo);
		
		// Test to get a controller of type light!
		jo = getRequestJSONObject("/house/labels/ch1/controller?id=61&param=param");
		testController61(jo);
		
		jo = getRequestJSONObject("/house/labels/ch1/controller/61?param=param");
		testController61(jo);
		
		jo = getRequestJSONObject("/house/controllers/21?param=param");
		testController21(jo);
		
		jo = getRequestJSONObject("/house/controllers/controller?id=21&param=param");
		testController21(jo);

		jo = getRequestJSONObject("/house/controllers/controller/21?param=param");
		testController21(jo);

		putRequestJSONObject("/house/groups/1?param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		putRequestJSONObject("/house/groups/group?id=1&param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);

		postRequestJSONObject("/house/groups/1?param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		postRequestJSONObject("/house/groups/group?id=1&param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);

		deleteRequestJSONObject("/house/groups/1?param=param", HttpServletResponse.SC_NOT_IMPLEMENTED);
		deleteRequestJSONObject("/house/groups/group?id=1&param=param", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	@Test
	public void uDeleteGroup() {
		// Test to get a specific group
		JSONObject group = deleteRequestJSONObject(urn_groups + "/6", HttpServletResponse.SC_OK);
		// Test group 1
		testGroup6(group);

		group = deleteRequestJSONObject(urn_groups + "/group?id=7", HttpServletResponse.SC_OK);
		// Test group 1
		testGroup7(group);
	}

	@Test
	public void vDeleteGroupList() {
		// Test impossible to create grouplist
		JSONObject jo = deleteRequestJSONObject(urn_groups, HttpServletResponse.SC_NO_CONTENT);
		Assert.assertNull(jo);

		// Test there is no more controllers
		JSONArray controllers = getRequestJSONArray(urn_groups);
		Assert.assertEquals(0, controllers.length());
	}

	@Test
	public void wcreateGroup() {
		// Test to get a specific group
		JSONObject group = postRequestJSONObject(urn_groups + "/6", createGroup6(), HttpServletResponse.SC_CREATED);
		// Test group 1
		testGroup6(group);

		group = postRequestJSONObject(urn_groups + "/group?id=10", createGroup10(), HttpServletResponse.SC_CREATED);
		// Test group 1
		testGroup10(group);

		group = postRequestJSONObject(urn_groups + "/group?id=1", createGroup1(), HttpServletResponse.SC_CREATED);
		// Test group 1
		testGroup1(group);
	}

	@Test
	public void zgetGroup() {
		// Test to get a specific group
		JSONObject group = getRequestJSONObject(urn_groups + "/1");
		// Test group 1
		Assert.assertNotNull(group);
		testGroup1(group);

		group = getRequestJSONObject(urn_groups + "/group?id=1");
		// Test group 1
		Assert.assertNotNull(group);
		testGroup1(group);
	}

}
