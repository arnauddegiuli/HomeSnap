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
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.homesnap.engine.JsonSerializable;
import com.homesnap.engine.Log;
import com.homesnap.engine.Log.Session;
import com.homesnap.engine.controller.Controller;

/**
 * Group represent a physical model of controller group. 
 * It can be compare to network mask: a group of devices.
 */
public class Group
implements Serializable, JsonSerializable {

	public static final String JSON_CONTROLLERS = "controllers";
	public static final String JSON_DESCRIPTION = "description";
	public static final String JSON_TITLE = "title";
	public static final String JSON_ID = "id";

	private static final Log log = new Log();

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	private List<Controller> controllerList = new ArrayList<Controller>();
	private String title;
	private String description;
	private String id;
	
	public String getId() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Controller> getControllerList() {
		return controllerList;
	}
	
	public boolean add(Controller object) {
		return controllerList.add(object);
	}

	public void add(int location, Controller object) {
		controllerList.add(location, object);
	}

	public boolean addAll(Collection<? extends Controller> arg0) {
		return controllerList.addAll(arg0);
	}

	public boolean addAll(int arg0, Collection<? extends Controller> arg1) {
		return controllerList.addAll(arg0, arg1);
	}

	public void clear() {
		controllerList.clear();
	}

	public boolean contains(Object object) {
		return controllerList.contains(object);
	}

	public boolean containsAll(Collection<?> arg0) {
		return controllerList.containsAll(arg0);
	}

	public Controller get(int location) {
		return controllerList.get(location);
	}

	public int indexOf(Object object) {
		return controllerList.indexOf(object);
	}

	public boolean isEmpty() {
		return controllerList.isEmpty();
	}

	public Iterator<Controller> iterator() {
		return controllerList.iterator();
	}

	public int lastIndexOf(Object object) {
		return controllerList.lastIndexOf(object);
	}

	public ListIterator<Controller> listIterator() {
		return controllerList.listIterator();
	}

	public ListIterator<Controller> listIterator(int location) {
		return controllerList.listIterator(location);
	}

	public Controller remove(int location) {
		return controllerList.remove(location);
	}

	public boolean remove(Object object) {
		return controllerList.remove(object);
	}

	public boolean removeAll(Collection<?> arg0) {
		return controllerList.removeAll(arg0);
	}

	public boolean retainAll(Collection<?> arg0) {
		return controllerList.retainAll(arg0);
	}

	public Controller set(int location, Controller object) {
		return controllerList.set(location, object);
	}

	public int size() {
		return controllerList.size();
	}

	public List<Controller> subList(int start, int end) {
		return controllerList.subList(start, end);
	}

	public Object[] toArray() {
		return controllerList.toArray();
	}

	public <T> T[] toArray(T[] array) {
		return controllerList.toArray(array);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Group other = (Group) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public JSONObject toJson() {
		JSONObject group = new JSONObject();
		group.put(JSON_ID, getId())
			.put(JSON_TITLE, getTitle())
			.put(JSON_DESCRIPTION, getDescription());

		JSONArray controllers = new JSONArray();
		for (Controller controller : getControllerList()) {
			controllers.put(controller.toJson());
		}
		group.put(JSON_CONTROLLERS, controllers);
		return group;
	}

	/**
	 * Deserialized the json. Only update data but not able to add new
	 * data. To add new child use cration api.
	 * @param jsonObject object to deseralized
	 * @return 
	 */
	@Override
	public void fromJson(JSONObject jsonObject) {
		try {
			setTitle(jsonObject.getString(JSON_TITLE));
		} catch (JSONException e) {
			log.finest(Session.Server, "No title for group [" + id + "] in json string.");
		}
		try {
			setDescription(jsonObject.getString(JSON_DESCRIPTION));
		} catch (JSONException e) {
			log.finest(Session.Server, "No description for group [" + id + "] in json string.");
		}
		try {
			JSONArray controllers = jsonObject.getJSONArray(JSON_CONTROLLERS);

			// Update controller already existing in group
			for (int i = 0; i < controllers.length(); i++) {
				JSONObject c = controllers.getJSONObject(i);
				String whereString = c.getString(Controller.JSON_WHERE);
				for (Controller controller : getControllerList()) {
					if (whereString.equals(controller.getWhere())) {
						controller.fromJson(c);
						break;
					}
				}
			}
		} catch (JSONException e) {
			log.finest(Session.Server, "No controllers for group [" + id + "] in json string.");
		}
		// TODO In group it is not possible to remove??? => ben si...
	}
}
