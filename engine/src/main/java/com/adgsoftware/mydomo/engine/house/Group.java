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


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import javax.xml.bind.UnmarshalException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.adgsoftware.mydomo.engine.JsonSerializable;
import com.adgsoftware.mydomo.engine.oldcontroller.Controller;
import com.adgsoftware.mydomo.engine.oldcontroller.Status;

/**
 * Group represent a physical model of controller group. 
 * It can be compare to network mask: a group of devices.
 */
public class Group
implements Serializable, JsonSerializable {

	protected static final String JSON_ID = "id";

	/** uuid */
	private static final long serialVersionUID = 1L;
	
	private List<Controller<? extends Status>> controllerList = new ArrayList<Controller<? extends Status>>();
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

	public List<Controller<? extends Status>> getControllerList() {
		return controllerList;
	}
	
	public boolean add(Controller<? extends Status> object) {
		return controllerList.add(object);
	}

	public void add(int location, Controller<? extends Status> object) {
		controllerList.add(location, object);
	}

	public boolean addAll(Collection<? extends Controller<? extends Status>> arg0) {
		return controllerList.addAll(arg0);
	}

	public boolean addAll(int arg0, Collection<? extends Controller<? extends Status>> arg1) {
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

	public Controller<?> get(int location) {
		return controllerList.get(location);
	}

	public int indexOf(Object object) {
		return controllerList.indexOf(object);
	}

	public boolean isEmpty() {
		return controllerList.isEmpty();
	}

	public Iterator<Controller<? extends Status>> iterator() {
		return controllerList.iterator();
	}

	public int lastIndexOf(Object object) {
		return controllerList.lastIndexOf(object);
	}

	public ListIterator<Controller<? extends Status>> listIterator() {
		return controllerList.listIterator();
	}

	public ListIterator<Controller<? extends Status>> listIterator(int location) {
		return controllerList.listIterator(location);
	}

	public Controller<?> remove(int location) {
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

	public Controller<?> set(int location, Controller<Status> object) {
		return controllerList.set(location, object);
	}

	public int size() {
		return controllerList.size();
	}

	public List<Controller<? extends Status>> subList(int start, int end) {
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
			.put("title", getTitle())
			.put("description", getDescription());

		JSONArray controllers = new JSONArray();
		for (Controller<? extends Status> controller : getControllerList()) {
			controllers.put(controller.toJson());
		}
		group.put("controllers", controllers);
		return group;
	}

	/**
	 * Deserialized the json. Only update data but not able to add new
	 * data. To add new child use cration api.
	 * @param jsonObject object to deseralized
	 * @return 
	 */
	@Override
	public void fromJson(JSONObject jsonObject) throws UnmarshalException {
		setTitle(jsonObject.getString("title"));
		setDescription(jsonObject.getString("description"));
		JSONArray controllers = jsonObject.getJSONArray("controllers");
		for (int i = 0; i < controllers.length(); i++) {
			JSONObject c = controllers.getJSONObject(i);
			String whereString = c.getString("where");
			for (Controller<?> controller : getControllerList()) {
				if (whereString.equals(controller.getWhere())) {
					controller.fromJson(c);
				}
			}
		}
	}
}
