package com.homesnap.webserver;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.homesnap.webserver.rest.MissingParameterRestOperation;
import com.homesnap.webserver.rest.RestOperationException;
import com.homesnap.webserver.rest.UnsupportedRestOperation;
import com.homesnap.webserver.rest.parser.ParseException;


public class ControllerRestAPITest extends AbstractRestApi {

	private static final String urn_groups = "/house/groups";
	private static final String urn_labels = "/house/labels";
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
	public void createControllerFromLabel() {
		// Test controller add in a label
		JSONObject jo = postRequestJSONObject(urn_labels + "/ch1/controller?id=61", createJsonController61(), HttpServletResponse.SC_CREATED);
		testController61(jo);
		
		// impossible to add again the same controller
		postRequestJSONObject(urn_labels + "/ch1/controller/61", createJsonController61(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		
		// Test impossible to create a new controller not existing in a Group
		postRequestJSONObject(urn_labels + "/ch1/controller/6", createJsonController6(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		postRequestJSONObject(urn_labels + "/ch1/controller?id=6", createJsonController6(), HttpServletResponse.SC_NOT_ACCEPTABLE);
	}

	@Test
	public void createControllerByGroup() {
		// Test impossible to create an existing controller
		postRequestJSONObject(urn_groups + "/1/controller?id=11", createJsonController11(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		postRequestJSONObject(urn_groups + "/1/controller/11", createJsonController11(), HttpServletResponse.SC_NOT_ACCEPTABLE);
	
		JSONObject jo = postRequestJSONObject(urn_groups + "/1/controller/16", createJsonController16(), HttpServletResponse.SC_CREATED);
		testController16(jo);
	
		jo = postRequestJSONObject(urn_groups + "/1/controller/17", createJsonController17(), HttpServletResponse.SC_CREATED);
		testController17(jo);
	}
	
	@Test
	public void createController() {
		postRequestJSONObject("/house/controllers/12", createJsonController16(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		postRequestJSONObject("/house/controllers/controller?id=12", createJsonController16(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		postRequestJSONObject("/house/controllers/controller/12", createJsonController16(), HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	// Get
	@Test
	public void getControllerFromLabel() {
		// Test to get a controller of type light!
		JSONObject jo = getRequestJSONObject(urn_labels + "/ch1/controller?id=61");
		testController61(jo);
		
		jo = getRequestJSONObject(urn_labels + "/ch1/controller/61");
		testController61(jo);
	}

	@Test
	public void getControllerByGroup() {
		// Test to get a controller of type light!
		JSONObject jo = getRequestJSONObject(urn_groups + "/1/controller?id=11");
		testController11(jo);

		jo = getRequestJSONObject(urn_groups + "/1/controller/11");
		testController11(jo);
	}

	@Test
	public void getController() {
		JSONObject jo = getRequestJSONObject("/house/controllers/12");
		testController12(jo);

		jo = getRequestJSONObject("/house/controllers/controller?id=12");
		testController12(jo);

		jo = getRequestJSONObject("/house/controllers/controller/12");
		testController12(jo);
	}

	// Modify
	@Test
	public void putControllerFromLabel() {
		// Test controller add in a label
		JSONObject jo = putRequestJSONObject(urn_labels + "/ch1/controller?id=61", createJsonController61(), HttpServletResponse.SC_ACCEPTED);
		testController61(jo);
		
		// impossible to add again the same controller
		putRequestJSONObject(urn_labels + "/ch1/controller/61", createJsonController61(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		
		// Test impossible to create a new controller not existing in a Group
		putRequestJSONObject(urn_labels + "/ch1/controller/6", createJsonController6(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		putRequestJSONObject(urn_labels + "/ch1/controller?id=6", createJsonController6(), HttpServletResponse.SC_NOT_ACCEPTABLE);
	}

	@Test
	public void putControllerByGroup() {
		// Test impossible to create an existing controller
		putRequestJSONObject(urn_groups + "/1/controller?id=11", createJsonController11(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		putRequestJSONObject(urn_groups + "/1/controller/11", createJsonController11(), HttpServletResponse.SC_NOT_ACCEPTABLE);

		JSONObject jo = putRequestJSONObject(urn_groups + "/1/controller/16", createJsonController16(), HttpServletResponse.SC_ACCEPTED);
		testController16(jo);

		jo = putRequestJSONObject(urn_groups + "/1/controller/17", createJsonController17(), HttpServletResponse.SC_ACCEPTED);
		testController17(jo);
	}

	@Test
	public void putController() {
		putRequestJSONObject("/house/controllers/12", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		putRequestJSONObject("/house/controllers/controller?id=12", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		putRequestJSONObject("/house/controllers/controller/12", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	// Delete
	@Test
	public void deleteControllerFromLabel() throws ParseException, UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation {
		// Test controller add in a label
		JSONObject jo = deleteRequestJSONObject(urn_labels + "/ch1/controller?id=61", HttpServletResponse.SC_OK);
		testController61(jo);
		
		// impossible to add again the same controller
		deleteRequestJSONObject(urn_labels + "/ch1/controller/61", HttpServletResponse.SC_NOT_ACCEPTABLE);
		
		// Test impossible to create a new controller not existing in a Group
		deleteRequestJSONObject(urn_labels + "/ch1/controller/6", HttpServletResponse.SC_NOT_ACCEPTABLE);
		deleteRequestJSONObject(urn_labels + "/ch1/controller?id=6", HttpServletResponse.SC_NOT_ACCEPTABLE);
	}

	@Test
	public void deleteControllerByGroup() {
		// Test impossible to create an existing controller
		deleteRequestJSONObject(urn_groups + "/1/controller?id=11", HttpServletResponse.SC_NOT_ACCEPTABLE);
		deleteRequestJSONObject(urn_groups + "/1/controller/11", HttpServletResponse.SC_NOT_ACCEPTABLE);

		JSONObject jo = deleteRequestJSONObject(urn_groups + "/1/controller/16", HttpServletResponse.SC_OK);
		testController16(jo);

		jo = deleteRequestJSONObject(urn_groups + "/1/controller/17", HttpServletResponse.SC_OK);
		testController17(jo);
	}

	@Test
	public void deleteController() {
		deleteRequestJSONObject("/house/controllers/12", HttpServletResponse.SC_NOT_IMPLEMENTED);
		deleteRequestJSONObject("/house/controllers/controller?id=12", HttpServletResponse.SC_NOT_IMPLEMENTED);
		deleteRequestJSONObject("/house/controllers/controller/12", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}
	
	
	@Test
	public void onStatus() {

		// Test to get a controller of type light!
		JSONObject jo = getRequestJSONObject(urn_labels + "/ch1/controller?id=61&param=param");
		testController61(jo);

		jo = getRequestJSONObject(urn_labels + "/ch1/controller/61?param=param");
		testController61(jo);

		jo = getRequestJSONObject(urn_groups + "/1/controller?id=11&param=param");
		testController11(jo);

		jo = getRequestJSONObject(urn_groups + "/1/controller/11?param=param");
		testController11(jo);

		jo = getRequestJSONObject("/house/controllers/12?param=param");
		testController12(jo);
		
		jo = getRequestJSONObject("/house/controllers/controller?id=12&param=param");
		testController12(jo);

		jo = getRequestJSONObject("/house/controllers/controller/12?param=param");
		testController12(jo);

		// Modification
		putRequestJSONObject("/house/controllers/12", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		putRequestJSONObject("/house/controllers/controller?id=12", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		putRequestJSONObject("/house/controllers/controller/12", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		
		putRequestJSONObject(urn_groups + "/1/controller?id=11&param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		putRequestJSONObject(urn_groups + "/1/controller/11?param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		putRequestJSONObject(urn_labels + "/ch1/controller?id=61&param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		putRequestJSONObject(urn_labels + "/ch1/controller/61?param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		putRequestJSONObject("/house/controllers/12?param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		putRequestJSONObject("/house/controllers/controller?id=12&param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		putRequestJSONObject("/house/controllers/controller/12?param=param", createJsonController17(), HttpServletResponse.SC_OK);

		// creation
		postRequestJSONObject(urn_groups + "/1/controller?id=11&param=param", createJsonController17(), HttpServletResponse.SC_OK);
		postRequestJSONObject(urn_groups + "/1/controller/11?param=param", createJsonController17(), HttpServletResponse.SC_OK);
		postRequestJSONObject(urn_labels + "/ch1/controller?id=61&param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		postRequestJSONObject(urn_labels + "/ch1/controller/61?param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		postRequestJSONObject("/house/controllers/12?param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		postRequestJSONObject("/house/controllers/controller?id=12&param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		postRequestJSONObject("/house/controllers/controller/12?param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);

		// Deletion
		deleteRequestJSONObject(urn_groups + "/1/controller?id=11&param=param", HttpServletResponse.SC_OK);
		deleteRequestJSONObject(urn_groups + "/1/controller/11?param=param", HttpServletResponse.SC_OK);
		deleteRequestJSONObject(urn_labels + "/ch1/controller?id=61&param=param", HttpServletResponse.SC_OK);
		deleteRequestJSONObject(urn_labels + "/ch1/controller/61?param=param", HttpServletResponse.SC_OK);
		deleteRequestJSONObject("/house/controllers/12?param=param", HttpServletResponse.SC_OK);
		deleteRequestJSONObject("/house/controllers/controller?id=12&param=param", HttpServletResponse.SC_OK);
		deleteRequestJSONObject("/house/controllers/controller/12?param=param", HttpServletResponse.SC_OK);
	}
}
