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


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LabelRestAPITest extends AbstractRestApi {

	private static final String urn_labels = "/house/labels";
	@Before
	public void init() {
		try {
			copyFileUsingFileChannels(new File("Backup of house.xml"), new File("house.xml"));
		} catch (IOException e) {
			Assert.fail("Impossible to initialize file");
		}
	}

	@Test
	public void test1DeleteLabelList() {
		// Test impossible to create label list
		JSONObject result = deleteRequestJSONObject(urn_labels, HttpServletResponse.SC_NO_CONTENT);
		Assert.assertNull(result);
	}
	
	@Test
	public void test2CreateLabelList() {
		// Test impossible to create label list
		postRequestJSONObject(urn_labels,"{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	@Test
	public void test3CreateLabel() {
		// Test new label creation
		JSONObject label = postRequestJSONObject(urn_labels + "/ch6", createJsonLabelCh6(), HttpServletResponse.SC_CREATED);
		testLabelCh6(label);
		
		label = postRequestJSONObject(urn_labels + "/label?id=ch7", createJsonLabelCh7(), HttpServletResponse.SC_CREATED);
		testLabelCh7(label);
	}
	
	@Test
	public void test4GetLabelList() {
		// Test to get labels list
		JSONArray labels = getRequestJSONArray(urn_labels);

		// Test labels
		testLabelCh6(labels.getJSONObject(0));
		testLabelCh7(labels.getJSONObject(1));
	}

	@Test
	public void test5ModifyLabelList() {
		// Test impossible to modify label list
		putRequestJSONObject(urn_labels,"{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	@Test
	public void test6GetLabel() {
		// Test to get a specific label
		JSONObject label = getRequestJSONObject(urn_labels + "/ch6");
		// Test label Ch1
		testLabelCh6(label);

		label = getRequestJSONObject(urn_labels + "/label?id=ch7");
		// Test label Ch1
		testLabelCh7(label);
	}

	@Test
	public void test7UpdateLabel() {
		// Test new label creation
		JSONObject label = putRequestJSONObject(urn_labels + "/ch6", createJsonLabelCh6Bis(), HttpServletResponse.SC_OK);
		testLabelCh6Bis(label);
		
		label = putRequestJSONObject(urn_labels + "/label?id=ch7", createJsonLabelCh7Bis(), HttpServletResponse.SC_OK);
		testLabelCh7Bis(label);
	}

	@Test
	public void test8OnStatus() {
		
		// Test to get a specific label
		JSONObject label = getRequestJSONObject("/house/labels/ch6?param=param");
		// Test label Ch1
		testLabelCh6Bis(label);

		label = getRequestJSONObject("/house/labels/label?id=ch7&param=param");
		// Test label Ch1
		testLabelCh7Bis(label);
		
		putRequestJSONObject("/house/labels/ch1?param=param", createJsonController17(), HttpServletResponse.SC_NOT_ACCEPTABLE); // Ch1 not existing!
		putRequestJSONObject("/house/labels/label?id=ch1&param=param", createJsonController17(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		
		postRequestJSONObject("/house/labels/ch7?param=param", createJsonLabelCh7(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		postRequestJSONObject("/house/labels/label?id=ch7&param=param", createJsonLabelCh7(), HttpServletResponse.SC_NOT_ACCEPTABLE);
		
		deleteRequestJSONObject("/house/labels/ch7?param=param", HttpServletResponse.SC_OK);
		deleteRequestJSONObject("/house/labels/label?id=ch7&param=param", HttpServletResponse.SC_NOT_ACCEPTABLE);
	}
	
	@Test
	public void test9DeleteLabel() {
		// Test new label creation
		JSONObject label = deleteRequestJSONObject(urn_labels + "/ch6", HttpServletResponse.SC_OK);
		testLabelCh6Bis(label);
		
		label = deleteRequestJSONObject(urn_labels + "/label?id=ch7", HttpServletResponse.SC_NOT_ACCEPTABLE);
		Assert.assertNull(label);
	}


	
	private String createJsonLabelCh6() {
		return createJsonLabel("Chambre 6", "ch6");
	}

	private void testLabelCh6(JSONObject label) {
		testLabel(label, "ch6", "Chambre 6", 0);
	}

	private String createJsonLabelCh6Bis() {
		return createJsonLabel("Chambre 6 Bis", "ch6");
	}

	private void testLabelCh6Bis(JSONObject label) {
		testLabel(label, "ch6", "Chambre 6 Bis", 0);
	}
	
	private String createJsonLabelCh7() {
		return createJsonLabel("Chambre 7", "ch7");
	}

	private void testLabelCh7(JSONObject label) {
		testLabel(label, "ch7", "Chambre 7", 0);
	}
	
	private String createJsonLabelCh7Bis() {
		return createJsonLabel("Chambre 7 Bis", "ch7");
	}

	private void testLabelCh7Bis(JSONObject label) {
		testLabel(label, "ch7", "Chambre 7 Bis", 0);
	}
	
	private String createJsonController17() {
		return createJsonController("Test", "17", Who.LIGHT.name());
	}
}
