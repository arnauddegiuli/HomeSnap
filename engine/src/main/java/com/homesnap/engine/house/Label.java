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
 * Label contains controller. It is a logical list of controller.
 * The link is bi-directionnal between controller and label.
 * <br>
 * It should too contain sub label but not supported for now.
 * 
 * @see {@link LabelList}.
 *
 */
public class Label  
implements Serializable, JsonSerializable, List<Controller> {

	public static final String JSON_CONTROLLERS = "controllers";
	public static final String JSON_ICON = "icon";
	public static final String JSON_DESCRIPTION = "description";
	public static final String JSON_TITLE = "title";
	public static final String JSON_ID = "id";

	private static final Log log = new Log();

	/** uuid */
	private static final long serialVersionUID = 1L;
	
//	private List<Label> subLabelList = new ArrayList<Label>();
	private List<Controller> controllerList = new ArrayList<Controller>();
	private String title;
	private String description;
	private String id;
	private Icon icon;
	private String iconPath;
	private House house;
	
	public String getId() {
		if (id == null) {
			id = UUID.randomUUID().toString();
		}
		return id;
	}
	
	public void setId(String id) {
		this.id= id;
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

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	protected void setHouse(House house) {
		this.house = house;
	}

	/**
	 * 
	 * @param object
	 * @return
	 */
	public boolean add(Controller object) {
		if (!controllerList.contains(object)) { // To break the loop with Controller.add
			boolean result = controllerList.add(object);
			object.getLabels().add(this);
			return result;
		} else {
			return false;
		}
	}

	public void add(int location, Controller object) {
		if (!controllerList.contains(object)) {
			controllerList.add(location, object);
			object.getLabels().add(this);
		}
	}

	public boolean addAll(Collection<? extends Controller> arg0) {
		for (Controller controller : arg0) {
			controller.getLabels().add(this);
		}
		return controllerList.addAll(arg0);
	}

	public boolean addAll(int arg0, Collection<? extends Controller> arg1) {
		for (Controller controller : arg1) {
			controller.getLabels().add(this);
		}
		return controllerList.addAll(arg0, arg1);
	}

	public void clear() {
		List<Controller>  backupControllerList = new ArrayList<Controller>(controllerList.size());
		backupControllerList.addAll(controllerList);
		controllerList.clear();
		for (Controller controller : backupControllerList) {
			controller.getLabels().remove(this);
		}
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
		controllerList.get(location).getLabels().remove(this);
		return controllerList.remove(location);
	}

	public boolean remove(Object object) {
		boolean result = false;
		if (controllerList.contains(object)) {
			result = controllerList.remove(object);
			((Controller) object).getLabels().remove(this);
		}
		return result;
	}

	public boolean removeAll(Collection<?> arg0) {
		boolean result = true;
		for (Object object : controllerList) {
			if (object instanceof Controller) {
				result &= remove(object);
			}
		}
		return result;
	}

	public boolean retainAll(Collection<?> arg0) {
		return controllerList.retainAll(arg0);
	}

	public Controller set(int location, Controller object) {
		controllerList.get(location).getLabels().remove(this); // Remove the link of the previous controller
		object.getLabels().add(this);
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

//	public List<Label> getSubLabelList() {
//		return subLabelList;
//	}
//
//	public void setSubLabelList(List<Label> subLabelList) {
//		this.subLabelList = subLabelList;
//	}

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
		Label other = (Label) obj;
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
	public String toString() {
		return getTitle();
	}

	public List<Controller> getControllerList() {
		return controllerList;
	}

	@Override
	public JSONObject toJson() {
		JSONObject label = new JSONObject();
		label.put(JSON_ID, getId());
		label.put(JSON_TITLE, getTitle());
		label.put(JSON_DESCRIPTION, getDescription());
		String icon = getIcon() != null ? getIcon().getClassName() : getIconPath();
		label.put(JSON_ICON, icon);
		JSONArray controllers = new JSONArray();

		for (Controller controller : getControllerList()) {
			controllers.put(controller.toJson());
		}
		label.put(JSON_CONTROLLERS, controllers); // TODO ici on doit passer par l'id et modifier le javascript
		return label;
	}

	@Override
	public void fromJson(JSONObject jsonObject) {
		setId(jsonObject.getString(JSON_ID));
		try {
			setTitle(jsonObject.getString(JSON_TITLE));
		} catch (JSONException e) {
			log.finest(Session.Server, "No title for label [" + id + "] in json string.");
		}
		try {
			setDescription(jsonObject.getString(JSON_DESCRIPTION));
		} catch (JSONException e) {
			log.finest(Session.Server, "No description for label [" + id + "] in json string.");
		}
		try {
			String icon = jsonObject.getString(JSON_ICON);
			try {
				setIcon((Icon) Class.forName(icon).newInstance());
			} catch (Exception e) {
				setIconPath(icon);
			}
		} catch (JSONException e) {
			log.finest(Session.Server, "No icon for label [" + id + "] in json string.");
		}
		try {
			JSONArray controllers = jsonObject.getJSONArray(JSON_CONTROLLERS);
			for (int i = 0; i < controllers.length(); i++) {
				String where = controllers.getString(i);
				boolean found = false;
				for (Controller controller : getControllerList()) {
					if(where.equals(controller.getWhere())) {
						found = true;
					}
				}
				// If controller not found update the label with the controller
				if (!found) {
					if (house != null) {
						for (Group group : house.getGroups()) {
							for (Controller controller : group.getControllerList()) {
								if (where.equals(controller.getWhere())) {
									add(controller);
									break;
								}
							}
						}
					}
				}
			}
			
			// Remove controller from label
			for (Controller controller : getControllerList()) {
				boolean found = false;
				for (int i = 0; i < controllers.length(); i++) {
					String where = controllers.getString(i);
					if(where.equals(controller.getWhere())) {
						found = true;
					}
				}
	
				if (!found) {
					getControllerList().remove(controller);
				}
			}
		} catch (JSONException e) {
			log.finest(Session.Server, "No controllers for label [" + id + "] in json string.");
		}
	}
}
