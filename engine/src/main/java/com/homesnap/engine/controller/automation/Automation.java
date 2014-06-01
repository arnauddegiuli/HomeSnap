package com.homesnap.engine.controller.automation;

/*
 * #%L
 * MyDomoEngine
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


import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.automation.stateValue.AutomationStatusValue;
import com.homesnap.engine.controller.what.StateName;
import com.homesnap.engine.controller.who.Who;


public class Automation extends Controller {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	@Override
	public Who getWho() {
		return Who.AUTOMATION;
	}

	public AutomationStatusValue getStatus() {
		return (AutomationStatusValue) get(StateName.STATUS);	
	}

	public void setStatus(AutomationStatusValue status) {
		set(StateName.STATUS, status);	
	}

	@Override
	protected void initStateTypes() {
		declareState(StateName.STATUS, AutomationStatusValue.class);
	}
}