package com.adgsoftware.mydomo.server.controllermodules.light;

/*
 * #%L
 * MyDomoServerLightModule
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


import java.text.MessageFormat;

import com.adgsoftware.mydomo.engine.oldconnector.openwebnet.Command;
import com.adgsoftware.mydomo.engine.oldcontroller.light.Light;
import com.adgsoftware.mydomo.server.ControllerStateManagement;

public class LightCommand {
	
	// OSGi Shell Function
	static final String[] functions = { "on", "off" };
	
	public String on(String address) {
		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.COMMAND, new Object[] {Command.WHO_LIGHTING, Light.LightStatus.LIGHT_ON.getCode(), address} ));
	}
	
	public String off(String address) {
		return ControllerStateManagement.executeCommand(MessageFormat.format(Command.COMMAND, new Object[] {Command.WHO_LIGHTING, Light.LightStatus.LIGHT_OFF.getCode(), address} ));
	}
}
