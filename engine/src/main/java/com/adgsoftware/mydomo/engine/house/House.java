package com.adgsoftware.mydomo.engine.house;

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

import java.util.ArrayList;
import java.util.List;

public class House {

	private List<Group> groups; // Physical model
	private List<Label> labels; // Logical model
	

	public List<Group> getGroups() {
		if (groups == null) {
			groups = new ArrayList<Group>();
		}
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public List<Label> getLabels() {
		if (labels == null) {
			labels = new ArrayList<Label>();
		}
		return labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}	
}
