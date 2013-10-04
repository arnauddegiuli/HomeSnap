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

import com.adgsoftware.mydomo.engine.controller.Controller;
import com.adgsoftware.mydomo.engine.controller.LabelList;
import com.adgsoftware.mydomo.engine.controller.Status;

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
implements Serializable, List<Controller<? extends Status>> {

	/** uuid */
	private static final long serialVersionUID = 1L;
	
//	private List<Label> subLabelList = new ArrayList<Label>();
	private List<Controller<? extends Status>> controllerList = new ArrayList<Controller<? extends Status>>();
	private String title;
	private String id;
	private Icon icon;
	private String iconPath;
	
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
	
	/**
	 * 
	 * @param object
	 * @return
	 */
	public boolean add(Controller<? extends Status> object) {
		if (!controllerList.contains(object)) { // To break the loop with Controller.add
			boolean result = controllerList.add(object);
			object.getLabels().add(this);
			return result;
		} else {
			return false;
		}
	}

	public void add(int location, Controller<? extends Status> object) {
		if (!controllerList.contains(object)) {
			controllerList.add(location, object);
			object.getLabels().add(this);
		}
	}

	public boolean addAll(Collection<? extends Controller<? extends Status>> arg0) {
		for (Controller<? extends Status> controller : arg0) {
			controller.getLabels().add(this);
		}
		return controllerList.addAll(arg0);
	}

	public boolean addAll(int arg0, Collection<? extends Controller<? extends Status>> arg1) {
		for (Controller<? extends Status> controller : arg1) {
			controller.getLabels().add(this);
		}
		return controllerList.addAll(arg0, arg1);
	}

	public void clear() {
		List<Controller<? extends Status>>  backupControllerList = new ArrayList<Controller<? extends Status>>(controllerList.size());
		backupControllerList.addAll(controllerList);
		controllerList.clear();
		for (Controller<? extends Status> controller : backupControllerList) {
			controller.getLabels().remove(this);
		}
	}

	public boolean contains(Object object) {
		return controllerList.contains(object);
	}

	public boolean containsAll(Collection<?> arg0) {
		return controllerList.containsAll(arg0);
	}

	public Controller<? extends Status> get(int location) {
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

	public Controller<? extends Status> remove(int location) {
		controllerList.get(location).getLabels().remove(this);
		return controllerList.remove(location);
	}

	public boolean remove(Object object) {
		boolean result = false;
		if (controllerList.contains(object)) {
			result = controllerList.remove(object);
			((Controller<?>) object).getLabels().remove(this);
		}
		return result;
	}

	public boolean removeAll(Collection<?> arg0) {
		boolean result = true;
		for (Object object : controllerList) {
			if (object instanceof Controller<?>) {
				result &= remove(object);
			}
		}
		return result;
	}

	public boolean retainAll(Collection<?> arg0) {
		return controllerList.retainAll(arg0);
	}

	public Controller<? extends Status> set(int location, Controller<? extends Status> object) {
		controllerList.get(location).getLabels().remove(this); // Remove the link of the previous controller
		object.getLabels().add(this);
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

	public List<Controller<? extends Status>> getControllerList() {
		return controllerList;
	}

}
