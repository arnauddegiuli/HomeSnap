package com.homesnap.webserver;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.xml.sax.SAXException;

import com.homesnap.webserver.utils.JSonTools;
import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;


public class AbstractRestApi {

	private static String server = "localhost";
	private static String port = "8080";

	protected JSONObject requestJSONObject(String urn) {
		WebConversation wc = new WebConversation();
		WebRequest req = new GetMethodWebRequest("http://" + server + ":" + port + urn);

		String json = null;
		try {
			WebResponse resp = wc.getResponse(req);
			if (resp.getContentType().equals("application/json")) 
			{
				json = resp.getText();
				return JSonTools.fromJson(json);
			} else {
				Assert.fail("No JSON return...");
			}
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
			Assert.fail("Problem with JSON [" + json + "] :" + e.getMessage());
		}
		Assert.fail("Problem when call [" + urn + "].");
		return null;
	}

	protected JSONArray requestJSONArray(String urn) {
		WebConversation wc = new WebConversation();
		WebRequest req = new GetMethodWebRequest("http://" + server + ":" + port + urn);
		String json = null;
		try {
			WebResponse resp = wc.getResponse(req);
			if (resp.getContentType().equals("application/json")) 
			{
				json = resp.getText();
				return new JSONArray(json); // TODO manage Array null
			} else {
				Assert.fail("No JSON return...");
			}
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		} catch (JSONException e) {
			e.printStackTrace();
			Assert.fail("Problem with JSON [" + json + "] :" + e.getMessage());
			Assert.fail(e.getMessage());
		}
		Assert.fail("Problem when call [" + urn + "].");
		return null;
	}
}
