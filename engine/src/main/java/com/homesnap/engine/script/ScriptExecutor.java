package com.homesnap.engine.script;

import com.homesnap.engine.controller.Controller;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2016 A. de Giuli
 * %%
 * This file is part of HomeSnap done by Arnaud de Giuli (arnaud.degiuli(at)free.fr)
 *     helped by Olivier Driesbach (olivier.driesbach(at)gmail.com).
 * 
 *     HomeSnap is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     HomeSnap is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with HomeSnap. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */


public class ScriptExecutor {

	public void executeScript(Script script) {
		for (Action action : script.getActionList()) {	
			changeWhat(action.getController(), action.getStatus().toString());
		}
	}
	
	
	// To use generic: only at the runtime we will know the type so this is only method to set what!
	private static void changeWhat(Controller controller, String code) {
//		TODO T status = controller.getStatus(code);
//		if (!status.equals(controller.getWhat())) { // If status has changed
//			controller.setWhat(status);
//		}
	}
}
