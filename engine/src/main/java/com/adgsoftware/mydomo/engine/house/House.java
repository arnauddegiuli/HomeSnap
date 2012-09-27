package com.adgsoftware.mydomo.engine.house;

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
