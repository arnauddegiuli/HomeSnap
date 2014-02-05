package com.homesnap.engine.serialization;

import org.junit.Assert;
import org.junit.Test;

import com.homesnap.engine.controller.light.LightController;
import com.homesnap.engine.controller.light.LightStatus;

/**
 * 
 * @author DRIESBACH Olivier
 * @version 1.0
 * @since 1.0
 */
public class JSONSerialization extends SerializerTestUnit {
	
	@Test
	public void testLightSerialization() {
		
		LightController light = new JSONLight("FF056x14");
		light.setName("Light 1");
		light.setDescription("The serializable light !");
		light.setStatus(LightStatus.ON);
		light.setLevel(35);
		serialize(light);
		
		LightController newLight = (LightController) deserialize();
		Assert.assertEquals(light.toString(), newLight.toString());
	}
}
