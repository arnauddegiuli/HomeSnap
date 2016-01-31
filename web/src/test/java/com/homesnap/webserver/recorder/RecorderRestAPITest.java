package com.homesnap.webserver.recorder;

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

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.homesnap.webserver.AbstractRestApi;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecorderRestAPITest extends AbstractRestApi {

	private static final String urn_recorder = "/recorder";
	@Before
	public void init() {
		try {
			copyFileUsingFileChannels(new File("Backup of house.xml"), new File("house.xml"));
		} catch (IOException e) {
			Assert.fail("Impossible to initialize file");
		}
	}

	@Test
	public void test1getRecorderList() {
		// Test to get all record!
		JSONArray result = getRequestJSONArray(urn_recorder);
		Assert.assertNotNull(result);
		Assert.assertNotEquals(0, result.length());
		JSONObject obj = result.getJSONObject(0);
		Assert.assertNotNull(obj);
	}
	
	@Test
	public void test1getRecorderTemperatureList() {
		// Test to get all record!
		JSONArray result = getRequestJSONArray(urn_recorder+ "/Temperature");
		Assert.assertNotNull(result);
		Assert.assertNotEquals(0, result.length());
		JSONObject obj = result.getJSONObject(0);
		Assert.assertNotNull(obj);
	}
	
}
