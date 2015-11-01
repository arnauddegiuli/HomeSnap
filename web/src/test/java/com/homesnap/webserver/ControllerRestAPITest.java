package com.homesnap.webserver;

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
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.homesnap.engine.controller.who.Who;
import com.homesnap.webserver.rest.house.MissingParameterRestOperation;
import com.homesnap.webserver.rest.house.RestOperationException;
import com.homesnap.webserver.rest.house.UnsupportedRestOperation;
import com.homesnap.webserver.rest.house.parser.ParseException;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
	public void test01CreateControllerByGroup() {
		// Test impossible to create an existing controller
		deleteRequestJSONObject("/house", HttpServletResponse.SC_NO_CONTENT);
		postRequestJSONObject(urn_groups + "/group?id=1", createGroup1(), HttpServletResponse.SC_CREATED);
		postRequestJSONObject(urn_groups + "/1/11", createJsonController11(), HttpServletResponse.SC_CREATED);
		postRequestJSONObject(urn_groups + "/1/12", createJsonController12(), HttpServletResponse.SC_CREATED);
		
		JSONObject jo = postRequestJSONObject(urn_groups + "/1/16", createJsonController16(), HttpServletResponse.SC_CREATED);
		testController16(jo);
		postRequestJSONObject(urn_groups + "/1/16", createJsonController16(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		
		jo = postRequestJSONObject(urn_groups + "/1/17", createJsonController17(), HttpServletResponse.SC_CREATED);
		testController17(jo);
	}

	@Test
	public void test02CreateControllerFromLabel() {
		// Test controller add in a label
		postRequestJSONObject(urn_labels + "/ch1", createJsonLabel("Chambre 1", "ch1"), HttpServletResponse.SC_CREATED);
		JSONObject jo = postRequestJSONObject(urn_labels + "/ch1/controller?id=11", createJsonController11(), HttpServletResponse.SC_CREATED);
		testController11(jo);
		
		jo = postRequestJSONObject(urn_labels + "/ch1/controller?id=17", createJsonController17(), HttpServletResponse.SC_CREATED);
		testController17(jo);
		
		jo = postRequestJSONObject(urn_labels + "/ch1/controller?id=12", createJsonController12(), HttpServletResponse.SC_CREATED);
		testController12(jo);
		
		// impossible to add again the same controller
		postRequestJSONObject(urn_labels + "/ch1/61", createJsonController61(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		
		// Test impossible to create a new controller not existing in a Group
		postRequestJSONObject(urn_labels + "/ch1/61", createJsonController61(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		postRequestJSONObject(urn_labels + "/ch1/controller?id=61", createJsonController61(), HttpServletResponse.SC_NOT_ACCEPTABLE);
	}
	
	@Test
	public void test03CreateController() {
		postRequestJSONObject("/house/controllers/12", createJsonController12(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		postRequestJSONObject("/house/controllers/controller?id=12", createJsonController12(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		postRequestJSONObject("/house/controllers/controller/12", createJsonController12(), HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	// Get
	@Test
	public void test04GetControllerFromLabel() {
		// Test to get a controller of type light!
		JSONObject jo = getRequestJSONObject(urn_labels + "/ch1/controller?id=11");
		testController11(jo);
		
		jo = getRequestJSONObject(urn_labels + "/ch1/controller/17");
		testController17(jo);
	}

	@Test
	public void test05GetControllerByGroup() {
		// Test to get a controller of type light!
		JSONObject jo = getRequestJSONObject(urn_groups + "/1/controller?id=11");
		testController11(jo);

		jo = getRequestJSONObject(urn_groups + "/1/controller/11");
		testController11(jo);
	}

	@Test
	public void test06GetController() {
		JSONObject jo = getRequestJSONObject("/house/controllers/17");
		testController17(jo);

		jo = getRequestJSONObject("/house/controllers/controller?id=17");
		testController17(jo);

		jo = getRequestJSONObject("/house/controllers/controller/17");
		testController17(jo);
	}

	// Modify
	@Test
	public void test07PutControllerFromLabel() {
		// Test controller add in a label
		JSONObject jo = putRequestJSONObject(urn_labels + "/ch1/controller?id=17", createJsonController17Bis(), HttpServletResponse.SC_OK);
		testController17Bis(jo);
		
		// impossible to add again the same controller
		putRequestJSONObject(urn_labels + "/ch1/11", createJsonController11Bis(), HttpServletResponse.SC_OK);
		
		// Test impossible to create a new controller not existing in a Group
		putRequestJSONObject(urn_labels + "/ch1/6", createJsonController6(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		putRequestJSONObject(urn_labels + "/ch1/controller?id=6", createJsonController6(), HttpServletResponse.SC_NOT_ACCEPTABLE);
	}

	@Test
	public void test08PutControllerByGroup() {
		// Test impossible to create an existing controller
		putRequestJSONObject(urn_groups + "/1/controller?id=17", createJsonController17(), HttpServletResponse.SC_OK);
		putRequestJSONObject(urn_groups + "/1/11", createJsonController11(), HttpServletResponse.SC_OK);

		JSONObject jo = putRequestJSONObject(urn_groups + "/1/16", createJsonController16(), HttpServletResponse.SC_OK);
		testController16(jo);
	}

	@Test
	public void test09PutController() {
		JSONObject jo = putRequestJSONObject("/house/controllers/17", createJsonController17(), HttpServletResponse.SC_OK);
		testController17(jo);
		jo = putRequestJSONObject("/house/controllers/controller?id=17", createJsonController17(), HttpServletResponse.SC_OK);
		testController17(jo);
		jo = putRequestJSONObject("/house/controllers/17", createJsonController17(), HttpServletResponse.SC_OK);
		testController17(jo);
	}

	// Delete
	@Test
	public void test10DeleteControllerFromLabel() throws ParseException, UnsupportedRestOperation, RestOperationException, MissingParameterRestOperation {
		// Test controller delete in a label
		JSONObject jo = deleteRequestJSONObject(urn_labels + "/ch1/controller?id=17", HttpServletResponse.SC_OK);
		testController17(jo);
		
		// impossible to delete again the same controller
		deleteRequestJSONObject(urn_labels + "/ch1/17", HttpServletResponse.SC_NOT_ACCEPTABLE);
		
		// Test impossible to delete a controller not existing in a Group
		deleteRequestJSONObject(urn_labels + "/ch1/6", HttpServletResponse.SC_NOT_ACCEPTABLE);
		deleteRequestJSONObject(urn_labels + "/ch1/controller?id=6", HttpServletResponse.SC_NOT_ACCEPTABLE);
	}

	@Test
	public void test11DeleteControllerByGroup() {
		// Test impossible to create an existing controller
		deleteRequestJSONObject(urn_groups + "/1/controller?id=17", HttpServletResponse.SC_OK);
		deleteRequestJSONObject(urn_groups + "/1/17", HttpServletResponse.SC_NOT_ACCEPTABLE);

		JSONObject jo = deleteRequestJSONObject(urn_groups + "/1/16", HttpServletResponse.SC_OK);
		testController16(jo);
		jo = getRequestJSONObject("/house/controllers/16");
		Assert.assertNull(jo);
		jo = getRequestJSONObject(urn_labels + "/ch1/16");
		Assert.assertNull(jo);

		jo = deleteRequestJSONObject(urn_groups + "/1/11", HttpServletResponse.SC_OK);
		testController11(jo);
	}

	@Test
	public void test12DeleteController() {
		deleteRequestJSONObject("/house/controllers/12", HttpServletResponse.SC_NOT_IMPLEMENTED);
		deleteRequestJSONObject("/house/controllers/controller?id=12", HttpServletResponse.SC_NOT_IMPLEMENTED);
		deleteRequestJSONObject("/house/controllers/12", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}
	
	
	@Test
	public void test13OnStatus() {

		// Test to get a controller of type light!
		JSONObject jo = getRequestJSONObject(urn_labels + "/ch1/controller?id=12&param=param");
		testController12(jo);

		jo = getRequestJSONObject(urn_labels + "/ch1/12?param=param");
		testController12(jo);

		jo = getRequestJSONObject(urn_groups + "/1/controller?id=16&param=param");
		Assert.assertNull(jo); // have been deleted

		jo = getRequestJSONObject(urn_groups + "/1/16?param=param");
		Assert.assertNull(jo); // have been deleted

		jo = getRequestJSONObject("/house/controllers/12?param=param");
		testController12(jo);
		
		jo = getRequestJSONObject("/house/controllers/controller?id=12&param=param");
		testController12(jo);

		jo = getRequestJSONObject("/house/controllers/12?param=param");
		testController12(jo);

		// Modification
		putRequestJSONObject("/house/controllers/12?status=On", createJsonController12(), HttpServletResponse.SC_OK);
		putRequestJSONObject("/house/controllers/controller?id=12&status=Off", createJsonController12(), HttpServletResponse.SC_OK);
		
		putRequestJSONObject(urn_groups + "/1/controller?id=12&param=param", createJsonController12(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		putRequestJSONObject(urn_groups + "/1/12?status=Off", createJsonController12(), HttpServletResponse.SC_OK);
		putRequestJSONObject(urn_labels + "/ch1/controller?id=12&status=Off", createJsonController12(), HttpServletResponse.SC_OK); 
		putRequestJSONObject(urn_labels + "/ch1/12?status=Off", createJsonController12(), HttpServletResponse.SC_OK);
		putRequestJSONObject("/house/controllers/12?param=param", createJsonController12(), HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		putRequestJSONObject("/house/controllers/controller?id=12&status=Off", createJsonController12(), HttpServletResponse.SC_OK);
		putRequestJSONObject("/house/controllers/12?status=Off", createJsonController12(), HttpServletResponse.SC_OK);

		// creation
		postRequestJSONObject(urn_groups + "/1/controller?id=17&status=Off", createJsonController17(), HttpServletResponse.SC_CREATED);
		postRequestJSONObject(urn_groups + "/1/12?status=Off", createJsonController12(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		postRequestJSONObject(urn_labels + "/ch1/controller?id=17&status=Off", createJsonController17(), HttpServletResponse.SC_CREATED);
		postRequestJSONObject(urn_labels + "/ch1/17?status=Off", createJsonController17(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		postRequestJSONObject("/house/17?status=Off", createJsonController17(), HttpServletResponse.SC_BAD_REQUEST);
		postRequestJSONObject("/house/controllers/controller?id=12&status=Off", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		postRequestJSONObject("/house/controllers/12?status=Off", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);

		// Deletion
		deleteRequestJSONObject(urn_groups + "/1/controller?id=11&status=Off", HttpServletResponse.SC_NOT_ACCEPTABLE);
		deleteRequestJSONObject(urn_groups + "/1/11?status=Off", HttpServletResponse.SC_NOT_ACCEPTABLE);
		deleteRequestJSONObject(urn_labels + "/ch1/controller?id=12&status=Off", HttpServletResponse.SC_OK);
		deleteRequestJSONObject(urn_labels + "/ch1/12?status=Off", HttpServletResponse.SC_NOT_ACCEPTABLE);
		deleteRequestJSONObject("/house/controllers/12?status=Off", HttpServletResponse.SC_NOT_IMPLEMENTED);
		deleteRequestJSONObject("/house/controllers/controller?id=12&status=Off", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}
	
	private String createJsonController6() {
		return createJsonController("Chambre 6", "6", Who.LIGHT.name());
	}

	private String createJsonController61() {
		return createJsonController("Chambre Tom", "61", Who.LIGHT.name());
	}

	private String createJsonController12() {
		return createJsonController("Chambre Tom", "12", Who.LIGHT.name());
	}
	
	private void testController12(JSONObject jo) {
		testController(jo, "Chambre Tom", "12", Who.LIGHT.name(), LightStatusStateValue.OFF.getValue());
	}
	
	private String createJsonController16() {
		return createJsonController("Chambre 16", "16", Who.LIGHT.name());
	}

	private void testController16(JSONObject jo) {
		testController(jo, "Chambre 16", "16", Who.LIGHT.name(), LightStatusStateValue.OFF.getValue());
	}
	
	private String createJsonController17() {
		return createJsonController("Chambre 17", "17", Who.LIGHT.name());
	}

	private void testController17(JSONObject jo) {
		testController(jo, "Chambre 17", "17", Who.LIGHT.name(), LightStatusStateValue.OFF.getValue());
	}
	
	private String createJsonController17Bis() {
		return createJsonController("Chambre 17 Bis", "17", Who.LIGHT.name());
	}

	private void testController17Bis(JSONObject jo) {
		testController(jo, "Chambre 17 Bis", "17", Who.LIGHT.name(), LightStatusStateValue.OFF.getValue());
	}
	
	private String createJsonController11() {
		return createJsonController("Chambre 11", "11", Who.LIGHT.name());
	}

	private String createJsonController11Bis() {
		return createJsonController("Chambre 11 Bis", "11", Who.LIGHT.name());
	}
	
	private void testController11(JSONObject jo) {
		testController(jo, "Chambre 11", "11", Who.LIGHT.name(), LightStatusStateValue.OFF.getValue());
	}

	private String createGroup1() {
		return createJsonGroup("Group 1", "1", "");
	}
}
