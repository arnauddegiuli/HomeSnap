//package com.adgsoftware.mydomo.engine.test.model;
//
///*
// * #%L
// * MyDomoEngine
// * %%
// * Copyright (C) 2011 - 2013 A. de Giuli
// * %%
// * This file is part of MyDomo done by A. de Giuli (arnaud.degiuli(at)free.fr).
// * 
// *     MyDomo is free software: you can redistribute it and/or modify
// *     it under the terms of the GNU General Public License as published by
// *     the Free Software Foundation, either version 3 of the License, or
// *     (at your option) any later version.
// * 
// *     MyDomo is distributed in the hope that it will be useful,
// *     but WITHOUT ANY WARRANTY; without even the implied warranty of
// *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// *     GNU General Public License for more details.
// * 
// *     You should have received a copy of the GNU General Public License
// *     along with MyDomo.  If not, see <http://www.gnu.org/licenses/>.
// * #L%
// */
//
//
//import java.io.IOException;
//
//import javax.xml.bind.UnmarshalException;
//
//import org.json.JSONObject;
//import org.junit.Assert;
//import org.junit.Test;
//
//import com.adgsoftware.mydomo.engine.house.House;
//import com.adgsoftware.mydomo.engine.oldcontroller.automation.Automation;
//import com.adgsoftware.mydomo.engine.oldcontroller.automation.Automation.AutomationStatus;
//import com.adgsoftware.mydomo.engine.oldcontroller.light.Light;
//import com.adgsoftware.mydomo.engine.oldcontroller.light.Light.LightStatus;
//import com.adgsoftware.mydomo.engine.services.ControllerService;
//import com.adgsoftware.mydomo.engine.services.PersistenceService;
//import com.adgsoftware.mydomo.engine.services.impl.OpenWebNetControllerService;
//import com.adgsoftware.mydomo.engine.services.impl.PersistenceServiceImpl;
//
//public class JsonSerializeTest {
//
//	private ControllerService controllerService = new OpenWebNetControllerService("localhost", 1234, 12345);
//	private PersistenceService persistence = new PersistenceServiceImpl(controllerService);
//	
//	@Test
//	public void lightJsonTest() throws UnmarshalException {
//		Light l = controllerService.createController(Light.class, "11");
//		l.setTitle("light title");
//		l.setDescription("light description");
//		l.setWhat(LightStatus.LIGHT_ON);
//		
//		Light lDest = controllerService.createController(Light.class, "11");
//		JSONObject o = l.toJson();
//		System.out.println("Light: [" + o.toString() + "]");
//		lDest.fromJson(o);
//		Assert.assertEquals(l.getWho(), lDest.getWho());
//		Assert.assertEquals(l.getTitle(), lDest.getTitle());
//		Assert.assertEquals(l.getWhere(), lDest.getWhere());
//		Assert.assertEquals(l.getWhat(), lDest.getWhat());
//	}
//
//	@Test
//	public void automationJsonTest() {
//		
//		Automation l = controllerService.createController(Automation.class, "16");
//		l.setTitle("automation title");
//		l.setDescription("automation description");
//		l.setWhat(AutomationStatus.AUTOMATION_UP);
//		
//		Automation lDest = controllerService.createController(Automation.class, "16");
//		JSONObject o = l.toJson();
//		System.out.println("Automation: [" + o.toString() + "]");
//		lDest.fromJson(o);
//		Assert.assertEquals(l.getWho(), lDest.getWho());
//		Assert.assertEquals(l.getTitle(), lDest.getTitle());
//		Assert.assertEquals(l.getWhere(), lDest.getWhere());
//		Assert.assertEquals(l.getWhat(), lDest.getWhat());
//	}
//
//	@Test
//	public void houseJsonTest() throws IOException, UnmarshalException {
//		House house = persistence.retrieve(Thread.currentThread().getContextClassLoader().getResourceAsStream("testHouse.xml"));
//		JSONObject o = house.toJson();
//		System.out.println(o.toString());
//		JSONObject controllerUn = o.getJSONArray("groups").getJSONArray(0).getJSONObject(0);
//		Assert.assertEquals("Dressing", controllerUn.getString("title"));
//		Assert.assertEquals("Dressing description", controllerUn.getString("description"));
//		Assert.assertEquals("11", controllerUn.getString("where"));
//		controllerUn.put("title", "New Dressing");
//		controllerUn.put("description", "New Dressing Description");
//		house.fromJson(o);
//	}
//}
