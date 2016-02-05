package com.homesnap.engine.controller.automation;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2015 A. de Giuli
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


import java.util.ArrayList;
import java.util.List;

import com.homesnap.engine.controller.Controller;
import com.homesnap.engine.controller.what.impl.UpDownState;
import com.homesnap.engine.controller.who.Who;

public class Automation extends Controller {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	@Override
	public Who getWho() {
		return Who.AUTOMATION;
	}

	public UpDownState getStatus() {
		return (UpDownState) get(StateNameEnum.status.name());	
	}

	public void setStatus(UpDownState status) {
		set(StateNameEnum.status.name(), status);	
	}

	@Override
	public List<String> getStateList() {
		List<String> result = new ArrayList<String>();
		StateNameEnum[] list = StateNameEnum.values();
		for (int i = 0; i < list.length; i++) {
			result.add(list[i].name());
		};
		return result;
	}
	
	private enum StateNameEnum {
		status;
	}
}
