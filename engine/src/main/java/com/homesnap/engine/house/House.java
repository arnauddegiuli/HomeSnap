package com.homesnap.engine.house;

/*
 * #%L
 * HomeSnapEngine
 * %%
 * Copyright (C) 2011 - 2014 A. de Giuli
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.UnmarshalException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.homesnap.engine.JsonSerializable;

public class House implements Serializable, JsonSerializable {

	private static final String JSON_TITLE = "title";
	private static final String JSON_ID = "id";
	private static final String JSON_LABELS = "labels";
	private static final String JSON_GROUPS = "groups";
	/** serial uid */
	private static final long serialVersionUID = 1L;
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
			labels = new LabelList(this);
		}
		return labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = new LabelList(this);
		this.labels.addAll(labels);
	}

	/**
	 * Serialize in json
	 */
	@Override
	public JSONObject toJson() {

		JSONObject house = new JSONObject();
		house.put(JSON_ID, "house")
			.put(JSON_TITLE, "House");

		JSONArray labels = new JSONArray();
		for (Label label : getLabels()) {
			labels.put(label.toJson());
		}
		house.put(JSON_LABELS, labels);

		JSONArray groups = new JSONArray();
		for (Group group : getGroups()) {
			groups.put(group.toJson());
		}
		house.put(JSON_GROUPS, groups);

		return house;
	}

	/**
	 * Deserialized json data. This is only able to update data
	 * but not to create new. To create new child you must use creation
	 * API.
	 */
	@Override
	public void fromJson(JSONObject house) throws UnmarshalException {
		JSONArray groups = house.getJSONArray(JSON_GROUPS);
		JSONArray labels = house.getJSONArray(JSON_LABELS);
		for (int i = 0; i < groups.length(); i++) {
			JSONObject o = groups.getJSONObject(i);
			String id = o.getString(Group.JSON_ID);
			for (Group group : getGroups()) {
				if (id.equals(group.getId())) {
					group.fromJson(o);
				}
			}
		}
		for (int i = 0; i < labels.length(); i++) {
			JSONObject o = labels.getJSONObject(i);
			String id = o.getString(Label.JSON_ID);
			for (Label label : getLabels()) {
				if (id.equals(label.getId())) {
					label.fromJson(o);
				}
			}
		}
	}

	/**
	 * List of Label. Manage the link with house.
	 */
	private class LabelList extends ArrayList<Label> {

		/** serial UID*/
		private static final long serialVersionUID = 1L;

		private House house;
		
		protected LabelList(House house) {
			this.house = house;
		}

		@Override
		public boolean add(Label e) {
			e.setHouse(house);
			return super.add(e);
		}

		@Override
		public void add(int index, Label e) {
			e.setHouse(house);
			super.add(index, e);
		}

		@Override
		public Label remove(int index) {
			Label e = super.remove(index);
			e.setHouse(null);
			return e;
		}

		@Override
		public boolean remove(Object o) {
			boolean result = super.remove(o);
			if (result && o instanceof Label) {
				((Label) o).setHouse(null);
			}
			return result;
		}

		@Override
		public void clear() {
			for (Label e : this) {
				e.setHouse(null);
			}
			super.clear();
		}

		@Override
		public boolean addAll(Collection<? extends Label> c) {
			for (Label e : c) {
				e.setHouse(house);
			}
			return super.addAll(c);
		}

		@Override
		public boolean addAll(int index, Collection<? extends Label> c) {
			for (Label e : c) {
				e.setHouse(house);
			}
			return super.addAll(index, c);
		}

		@Override
		protected void removeRange(int fromIndex, int toIndex) {
			for(int i = fromIndex; i < toIndex; i++) {
				this.get(i).setHouse(null);
			}
			super.removeRange(fromIndex, toIndex);
		}
	}
}
