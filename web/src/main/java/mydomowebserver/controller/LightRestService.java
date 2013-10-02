package mydomowebserver.controller;

import com.adgsoftware.mydomo.engine.controller.light.Light;
import com.adgsoftware.mydomo.engine.controller.light.Light.LightStatus;

/*
 * #%L
 * MyDomoWebServer
 * %%
 * Copyright (C) 2011 - 2013 A. de Giuli
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



public interface LightRestService {

	/**
	 * Update the device with the status
	 * @param adress device address
	 * @param status the status
	 * @return the device
	 */
	public Light putCommand(String address, LightStatus status); // Update the address with the status
	
	/**
	 * Return the device
	 * @param address the device's address
	 * @return
	 */
	public Light getStatus(String address); // Get the light
	
	/**
	 * Update or Create the device
	 * @param address the device's address
	 * @param label the title of the device
	 * @return the device
	 */
	public Light putLight(String address, String label); // Create or update the light
	
	public boolean delLight(String address); // Delete the light

}
