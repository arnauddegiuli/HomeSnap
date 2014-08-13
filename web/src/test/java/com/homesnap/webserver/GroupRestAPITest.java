package com.homesnap.webserver;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


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

	// Creation group list
	// get group list
	// update group list
	// delete group list
	@Test
	public void createGroupList() {
		// Test impossible to create grouplist
		postRequestJSONObject(urn_groups,"{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	@Test
	public void getGroupList() {
		// Test to get groups list
		JSONArray groups = getRequestJSONArray(urn_groups);

		// Test groups
		testGroup1(groups.getJSONObject(0));
	}	

	@Test
	public void putGroupList() {
		// Test impossible to create grouplist
		putRequestJSONObject(urn_groups,"{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	@Test
	public void deleteGroupList() {
		// Test impossible to create grouplist
		deleteRequestJSONObject(urn_groups, HttpServletResponse.SC_NOT_IMPLEMENTED);
	}


	@Test
	public void createGroup() {
		// Test to get a specific group
		JSONObject group = postRequestJSONObject(urn_groups + "/6", createGroup6(), HttpServletResponse.SC_CREATED);
		// Test group 1
		testGroup6(group);

		group = postRequestJSONObject(urn_groups + "/group?id=7", createGroup7(), HttpServletResponse.SC_CREATED);
		// Test group 1
		testGroup7(group);

	}


	@Test
	public void getGroup() {
		// Test to get a specific group
		JSONObject group = getRequestJSONObject(urn_groups + "/1");
		// Test group 1
		testGroup1(group);

		group = getRequestJSONObject(urn_groups + "/group?id=1");
		// Test group 1
		testGroup1(group);

	}

	@Test
	public void modifyGroup() {
		// Test to get a specific group
		JSONObject group = putRequestJSONObject(urn_groups + "/6", createGroup6(), HttpServletResponse.SC_ACCEPTED);
		// Test group 1
		testGroup6(group);

		group = putRequestJSONObject(urn_groups + "/group?id=7", createGroup7(), HttpServletResponse.SC_ACCEPTED);
		// Test group 1
		testGroup7(group);

	}
	
	@Test
	public void deleteGroup() {
		// Test to get a specific group
		JSONObject group = deleteRequestJSONObject(urn_groups + "/6", HttpServletResponse.SC_OK);
		// Test group 1
		testGroup6(group);

		group = deleteRequestJSONObject(urn_groups + "/group?id=7", HttpServletResponse.SC_OK);
		// Test group 1
		testGroup7(group);

	}

	@Test
	public void onStatus() {
		
		
		// Test to get a specific group
		JSONObject group = getRequestJSONObject("/house/groups/1?param=param");
		// Test group 1
		testGroup1(group);

		group = getRequestJSONObject("/house/groups/group?id=1&param=param");
		// Test group 1
		testGroup1(group);
		
		JSONObject jo = getRequestJSONObject("/house/groups/1/controller?id=11&param=param");
		testController11(jo);

		jo = getRequestJSONObject("/house/groups/1/controller/11?param=param");
		testController11(jo);
		
		// Test to get a controller of type light!
		jo = getRequestJSONObject("/house/labels/ch1/controller?id=61&param=param");
		testController61(jo);
		
		jo = getRequestJSONObject("/house/labels/ch1/controller/61?param=param");
		testController61(jo);
		
		jo = getRequestJSONObject("/house/controllers/12?param=param");
		testController12(jo);
		
		jo = getRequestJSONObject("/house/controllers/controller?id=12&param=param");
		testController12(jo);

		jo = getRequestJSONObject("/house/controllers/controller/12?param=param");
		testController12(jo);

		putRequestJSONObject("/house/groups/1?param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		putRequestJSONObject("/house/groups/group?id=1&param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);

		postRequestJSONObject("/house/groups/1?param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		postRequestJSONObject("/house/groups/group?id=1&param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);

		deleteRequestJSONObject("/house/groups/1?param=param", HttpServletResponse.SC_NOT_IMPLEMENTED);
		deleteRequestJSONObject("/house/groups/group?id=1&param=param", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}
}
