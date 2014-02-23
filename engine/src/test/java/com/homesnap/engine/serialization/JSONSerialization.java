//package com.homesnap.engine.serialization;
//
///*
// * #%L
// * MyDomoEngine
// * %%
// * Copyright (C) 2011 - 2014 A. de Giuli
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
//import org.junit.Assert;
//import org.junit.Test;
//
//import com.adgsoftware.mydomo.engine.connector.openwebnet.light.LightStatus;
//import com.adgsoftware.mydomo.engine.controller.light.Light;
//
//
///**
// * 
// * @author DRIESBACH Olivier
// * @version 1.0
// * @since 1.0
// */
//public class JSONSerialization extends SerializerTestUnit {
//	
//	@Test
//	public void testLightSerialization() {
//		
//		Light light = new JSONLight();
//		light.setName("Light 1");
//		light.setDescription("The serializable light !");
//		light.setStatus(LightStatus.ON);
//		light.setLevel(35);
//		serialize(light);
//		
//		Light newLight = (Light) deserialize();
//		Assert.assertEquals(light.toString(), newLight.toString());
//	}
//}
