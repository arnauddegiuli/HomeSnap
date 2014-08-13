package com.homesnap.webserver;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


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
	public void createLabelList() {
		// Test impossible to create label list
		postRequestJSONObject(urn_labels,"{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	@Test
	public void getLabelList() {
		// Test to get labels list
		JSONArray labels = getRequestJSONArray(urn_labels);

		// Test labels
		testLabelCh1(labels.getJSONObject(0));
		testLabelCh2(labels.getJSONObject(1));
	}

	@Test
	public void modifyLabelList() {
		// Test impossible to modify label list
		putRequestJSONObject(urn_labels,"{}", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}

	@Test
	public void deleteLabelList() {
		// Test impossible to create label list
		JSONObject result = deleteRequestJSONObject(urn_labels, HttpServletResponse.SC_ACCEPTED);
		Assert.assertNull(result);
	}

	@Test
	public void createLabel() {
		// Test new label creation
		JSONObject label = postRequestJSONObject(urn_labels + "/ch6", createJsonLabelCh6(), HttpServletResponse.SC_CREATED);
		testLabelCh6(label);
		
		label = postRequestJSONObject(urn_labels + "/label?id=ch7", createJsonLabelCh7(), HttpServletResponse.SC_CREATED);
		testLabelCh7(label);
	}

	@Test
	public void getLabel() {
		// Test to get a specific label
		JSONObject label = getRequestJSONObject(urn_labels + "/ch1");
		// Test label Ch1
		testLabelCh1(label);

		label = getRequestJSONObject(urn_labels + "/label?id=ch1");
		// Test label Ch1
		testLabelCh1(label);
	}

	@Test
	public void updateLabel() {
		// Test new label creation
		JSONObject label = putRequestJSONObject(urn_labels + "/ch6", createJsonLabelCh6(), HttpServletResponse.SC_OK);
		testLabelCh6(label);
		
		label = putRequestJSONObject(urn_labels + "/label?id=ch7", createJsonLabelCh7(), HttpServletResponse.SC_OK);
		testLabelCh7(label);
	}

	@Test
	public void deleteLabel() {
		// Test new label creation
		JSONObject label = deleteRequestJSONObject(urn_labels + "/ch6", HttpServletResponse.SC_OK);
		testLabelCh6(label);
		
		label = deleteRequestJSONObject(urn_labels + "/label?id=ch7", HttpServletResponse.SC_OK);
		testLabelCh7(label);
	}

	@Test
	public void onStatus() {
		
		// Test to get a specific label
		JSONObject label = getRequestJSONObject("/house/labels/ch1?param=param");
		// Test label Ch1
		testLabelCh1(label);

		label = getRequestJSONObject("/house/labels/label?id=ch1&param=param");
		// Test label Ch1
		testLabelCh1(label);
		
		putRequestJSONObject("/house/labels/ch1?param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		putRequestJSONObject("/house/labels/label?id=ch1&param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		
		postRequestJSONObject("/house/labels/ch1?param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		postRequestJSONObject("/house/labels/label?id=ch1&param=param", createJsonController17(), HttpServletResponse.SC_NOT_IMPLEMENTED);
		
		deleteRequestJSONObject("/house/labels/ch1?param=param", HttpServletResponse.SC_NOT_IMPLEMENTED);
		deleteRequestJSONObject("/house/labels/label?id=ch1&param=param", HttpServletResponse.SC_NOT_IMPLEMENTED);
	}
}
